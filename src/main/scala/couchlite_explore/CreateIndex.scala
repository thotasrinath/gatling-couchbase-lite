package io.github.gatling.couchbase
package couchlite_explore

import com.couchbase.lite.{CouchbaseLite, Database, QueryBuilder, ValueIndexConfiguration}

object CreateIndex {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

    val coll = database.createCollection("myCol")

    coll.createIndex("dateFieldIndex", new ValueIndexConfiguration("datefield"))
    coll.createIndex("firstLevelSequence", new ValueIndexConfiguration("meta.firstLevelSequence"))
    coll.createIndex("secondLevelSequence", new ValueIndexConfiguration("trade.party.secondLevelSequence"))


    database.close()

  }

}
