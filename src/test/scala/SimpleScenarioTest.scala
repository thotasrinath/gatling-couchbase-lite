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

  val data = new ObjectMapper().readValue(new File("src/main/resources/scratch-100.json"),classOf[util.HashMap[String,Object]])

  CouchbaseLite.init()

  var dataRef:Option[Database] = None
  def getDb(): CouchLiteProtocolBuilder = {
    val conn = new Database("mydb")
    //conn.createCollection("myCol")

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
    scenario("test").asLongAsDuring(session => true, 5.minutes, "counter", false) {
      exec(sql("csd").insertQuery(data))
    }

  setUp(scn.inject(atOnceUsers(10)))
    .protocols(getDb())
}
