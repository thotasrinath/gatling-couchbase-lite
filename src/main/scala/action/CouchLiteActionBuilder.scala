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


  override protected def execute(session: Session): Unit = {
    val (newSession, psExpr: Validation[String]) = attr.statement(session, protocol)

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


    def executeStatement(stmt: String, newSession: Session) = {
      val start = System.currentTimeMillis()
      val future = Future {
        protocol.database.getCollection("myCol").save(new MutableDocument().setJSON(stmt))
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
