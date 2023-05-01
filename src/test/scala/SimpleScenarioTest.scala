package io.github.gatling.couchbase

import action.CouchLiteActionBuilder
import protocol.CouchLiteProtocolBuilder

import com.couchbase.lite.{CouchbaseLite, Database, MutableDocument}
import io.gatling.core.Predef._
import io.github.gatling.couchbase.Predef._

class SimpleScenarioTest extends Simulation {

  CouchbaseLite.init()

  val conn = new Database("mydb")
  conn.createCollection("myCol")

  val sqlConfig = sql.connection(conn)

  val query = "{ \"type\" : \"hotel\", \"name\" : \"new hotel\" }"


  def scn =
    scenario("test").repeat(100) {
      exec(sql("csd").insertQuery(query))

    }

  setUp(scn.inject(atOnceUsers(1)))
    .protocols(sqlConfig)
}
