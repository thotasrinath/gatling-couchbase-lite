package io.github.gatling.couchbase
package couchlite_explore

import com.couchbase.lite.{CouchbaseLite, DataSource, Database, DatabaseConfiguration, Expression, LogFileConfiguration, Meta, MetaExpression, Parameters, QueryBuilder, SelectResult}
import net.sf.saxon.expr.flwor.WhereClause

object SelectQuery {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

    val log = new LogFileConfiguration("./logs")

   /* val query = QueryBuilder.select(SelectResult.all()).from(DataSource.collection(database.getCollection("myCol"))).limit(Expression.value(100))
*/

    val query = QueryBuilder.createQuery("select * from myCol where meta.firstLevelSequence between $lower and $higher limit 3",database)
    println(query.explain())

    query.setParameters(new Parameters().setInt("lower",1000).setInt("higher",100000))

    val rs = query.execute()

    if(rs.next() !=null){
      println("Not Workde")
    }

    database.close()
  }

}
