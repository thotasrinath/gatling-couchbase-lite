package io.github.gatling.couchbase
package couchlite_explore

import com.couchbase.lite.{CouchbaseLite, DataSource, Database, QueryBuilder, SelectResult, ValueIndexConfiguration}

object CountQuery {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

    val query = QueryBuilder.createQuery("select count(sequence) from myCol",database)

    val rs = query.execute()

    rs.allResults().forEach(x => println(x.toJSON))

    database.close()
  }

}
