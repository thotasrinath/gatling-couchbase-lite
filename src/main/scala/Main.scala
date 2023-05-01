package io.github.gatling.couchbase

import com.couchbase.lite.{CouchbaseLite, DataSource, Database, MutableDocument, QueryBuilder, SelectResult}

object Main {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

    val coll = database.getCollection("myCol")

    val query = QueryBuilder.select(SelectResult.all).from(DataSource.collection(coll))


    val rs = query.execute()

    //println(rs.allResults().size())

    rs.allResults().forEach(x => println(x.toJSON))



/*    database.createQuery("").execute()

    QueryBuilder.createQuery("insert i",database)

    println("Database created")

    val collection =  database.createCollection("myCol","myScope")

    collection.save(new MutableDocument().setString("1","One"))*/
  }

}
