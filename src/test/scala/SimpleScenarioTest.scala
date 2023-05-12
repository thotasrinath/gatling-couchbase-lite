package io.github.gatling.couchbase

import Predef._
import protocol.CouchLiteProtocolBuilder

import com.couchbase.lite.{CouchbaseLite, DataSource, Database, Query, QueryBuilder, SelectResult}
import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef._

import java.io.File
import java.util
import scala.concurrent.duration.DurationInt

class SimpleScenarioTest extends Simulation {
  CouchbaseLite.init()
  //val data = new ObjectMapper().readValue(new File("src/main/resources/scratch-25.json"), classOf[util.HashMap[String, Object]])
  val conn = new Database("mydb")
  //val coll = conn.getCollection("myCol")
  val query = "select * from myCol where any v in trade.party satisfies v.secondLevelSequence between $lower and $higher end limit 20"

  var dataRef: Option[Database] = None

  def getDb(): CouchLiteProtocolBuilder = {

    dataRef = Some(conn)
    sql.connection(conn)
  }

  before {
    println("Before Called")
  }

  after {
    println("After Called")
    dataRef.map(k => k.close())
  }

  def scn =
    scenario("test").asLongAsDuring(session => true, 10.minutes, "counter", false) {
      exec(sql("csd").searchQuery(SqlStatementRequest("select", stmt = query)))
    }

  setUp(scn.inject(atOnceUsers(30)))
    .protocols(getDb())
}
