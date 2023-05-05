package io.github.gatling.couchbase

import Predef._
import protocol.CouchLiteProtocolBuilder

import com.couchbase.lite.{CouchbaseLite, Database}
import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef._

import java.io.File
import java.util
import scala.concurrent.duration.DurationInt

class SimpleScenarioTest extends Simulation {

  val data = new ObjectMapper().readValue(new File("src/main/resources/scratch-25.json"),classOf[util.HashMap[String,Object]])

  CouchbaseLite.init()

  var dataRef:Option[Database] = None
  def getDb(): CouchLiteProtocolBuilder = {
    val conn = new Database("mydb11")
    conn.createCollection("myCol")

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
    scenario("test").repeat(2000) {
      exec(sql("csd").insertQuery(SqlStatementRequest("insert",obj = data)))
    }

  setUp(scn.inject(atOnceUsers(100)))
    .protocols(getDb())
}
