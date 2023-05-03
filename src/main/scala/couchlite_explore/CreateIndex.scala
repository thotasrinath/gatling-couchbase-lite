package io.github.gatling.couchbase
package couchlite_explore

import com.couchbase.lite.{CouchbaseLite, Database, ValueIndexConfiguration}

object CreateIndex {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

    val coll = database.createCollection("myCol")

    coll.createIndex("dateFieldIndex", new ValueIndexConfiguration("datefield"))
    coll.createIndex("sequenceIndex", new ValueIndexConfiguration("sequence"))

    database.close()
  }

}
