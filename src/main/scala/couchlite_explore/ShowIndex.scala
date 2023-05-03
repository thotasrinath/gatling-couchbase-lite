package io.github.gatling.couchbase
package couchlite_explore

import com.couchbase.lite.{CouchbaseLite, Database, ValueIndexConfiguration}

object ShowIndex {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

    val coll = database.getCollection("myCol")

    coll.getIndexes.forEach(println)

    database.close()
  }

}
