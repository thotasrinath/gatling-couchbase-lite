package io.github.gatling.couchbase
package action

import protocol.CouchLiteProtocol
import request.SqlAttributes

import com.couchbase.lite.{MutableDocument, Query}
import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.Clock
import io.gatling.commons.validation.{Failure, Success, Validation}
import io.gatling.core.CoreComponents
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.protocol.ProtocolComponentsRegistry
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import io.gatling.core.structure.ScenarioContext
import io.gatling.core.util.NameGen

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.{LocalDate, ZoneId}
import java.time.temporal.ChronoUnit.DAYS
import java.util
import java.util.Date
import scala.util.Random

class CouchLiteActionBuilder(attr: SqlAttributes) extends ActionBuilder with NameGen {
  private def components(protocolComponentsRegistry: ProtocolComponentsRegistry) =
    protocolComponentsRegistry.components(CouchLiteProtocol.CouchLiteProtocolKey)

  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx._
    val sqlComponents = components(protocolComponentsRegistry)
    new CouchLiteAction(attr, sqlComponents.couchLiteProtocol, ctx.throttled, ctx.coreComponents, next)

  }
}

class CouchLiteAction(val attr: SqlAttributes, protocol: CouchLiteProtocol, val throttled: Boolean, val coreComponents: CoreComponents, val nextAction: Action) extends ExitableAction {
  val defaultZoneId: ZoneId = ZoneId.systemDefault
  def randomDate(from: LocalDate, to: LocalDate): LocalDate = {
    val diff = DAYS.between(from, to)
    val random = new Random(System.nanoTime) // You may want a different seed
    from.plusDays(random.nextInt(diff.toInt))
  }
  override protected def execute(session: Session): Unit = {
    val (newSession, psExpr: Validation[util.HashMap[String,Object]]) = attr.statement(session, protocol)

    psExpr match {
      case Success(stmt) =>
        if (throttled) {
          coreComponents.throttler.get.throttle(name, () => {
            executeStatement(stmt, newSession)
          })
        } else {
          executeStatement(stmt, newSession)
        }
      case Failure(err) =>
        statsEngine.logResponse(session.scenario, session.groups, name, System.currentTimeMillis(), System.currentTimeMillis(), KO, None, Some("Error setting up statement: " + err))
        next ! newSession.markAsFailed
    }


    def executeStatement(stmt: util.HashMap[String,Object], newSession: Session) = {
      val start = System.currentTimeMillis()

      //logger.debug("Type of counter "+  session.attributes("counter").getClass)

      val docId = s"${session.userId}-${session.attributes("counter")}"
      //logger.debug(s"docId is : $docId")
      val future = Future {

        val date = Date.from(randomDate( LocalDate.of(1970, 1, 1), LocalDate.of(2021, 1, 1)).atStartOfDay(defaultZoneId).toInstant)
        protocol.database.getCollection("myCol").save(new MutableDocument(docId,stmt)
          .setDate("datefield",date)
          .setLong("sequence",Random.nextLong(Long.MaxValue))
         )
      }

      future onComplete {
        case scala.util.Success(result) =>
          val requestEndDate = System.currentTimeMillis()
          statsEngine.logResponse(
            session.scenario, session.groups,
            name,
            start, requestEndDate,
            OK,
            None,
            None
          )
          next ! session.markAsSucceeded
        case scala.util.Failure(t) =>
          val requestEndDate = System.currentTimeMillis()
          statsEngine.logResponse(
            session.scenario, session.groups,
            name,
            start, requestEndDate,
            KO,
            None,
            Some(t.getMessage)
          )
          next ! session.markAsFailed
      }
    }

  }


  override def statsEngine: StatsEngine = coreComponents.statsEngine

  override def clock: Clock = coreComponents.clock

  override def next: Action = nextAction

  override def name: String = "Name"
}
