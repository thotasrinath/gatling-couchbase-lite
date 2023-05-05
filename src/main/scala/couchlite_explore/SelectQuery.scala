package io.github.gatling.couchbase
package couchlite_explore

import com.couchbase.lite.{CouchbaseLite, DataSource, Database, Expression, Meta, MetaExpression, Parameters, QueryBuilder, SelectResult}
import net.sf.saxon.expr.flwor.WhereClause

object SelectQuery {

  def main(args: Array[String]): Unit = {
    println("Hello World")
    CouchbaseLite.init()
    println("CBL Initialized")

    val database = new Database("mydb")

  /*  val query = QueryBuilder.select(SelectResult.all()).from(DataSource.collection(database.getCollection("myCol")))
      .where(Meta.id.equalTo(Expression.value("1")))*/

    val query = QueryBuilder.createQuery("select * from myCol where META().id= $pk",database)
    println(query.explain())

    query.setParameters(new Parameters().setString("pk","1"))

    val rs = query.execute()

    rs.allResults().forEach(x => println(x.toJSON))

    database.close()
  }

}
