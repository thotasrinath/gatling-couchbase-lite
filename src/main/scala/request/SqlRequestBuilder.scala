package io.github.gatling.couchbase
package request

import action.CouchLiteActionBuilder

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.protocol.Protocols
import io.gatling.core.session.Expression


case class SqlRequestBuilderBase(tag: String) {

  def insertQuery(query: Expression[String]) : SqlRequestBuilder =
    SqlRequestBuilder(SqlAttributes(tag, new SimpleSqlStatement(query)))

  def update(query: Expression[String], protocols : Protocols) : SqlRequestBuilder =
    SqlRequestBuilder(SqlAttributes(tag, new SimpleSqlStatement(query)))
}

case class SqlAttributes(tag: String, statement: SqlStatement)


case class SqlRequestBuilder(attr: SqlAttributes) {
  def build(): ActionBuilder = new CouchLiteActionBuilder(attr)
  /**
   * See GatlingCql for example of implementing something like the following
   * Adds checks on the response
   *
   */
  //  def check(checks: CqlCheck*): SqlRequestBuilder = SqlRequestBuilder(attr.copy(checks = attr.checks ::: checks.toList))
}