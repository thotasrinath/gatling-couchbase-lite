package io.github.gatling.couchbase
package couchlite_explore

import com.couchbase.lite.{CouchbaseLite, Database, IndexBuilder, QueryBuilder, ValueIndexConfiguration}

object CreateIndex {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

    val coll = database.createCollection("myCol")

//    coll.createIndex("dateFieldIndex", new ValueIndexConfiguration("datefield"))
//    coll.createIndex("firstLevelSequence", new ValueIndexConfiguration("meta.firstLevelSequence"))
//     coll.createIndex("secondLevelSequence", new ValueIndexConfiguration("trade.party.[0].secondLevelSequence"))

    database.close()

  }

}
