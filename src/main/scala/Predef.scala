package io.github.gatling.couchbase

import protocol.{CouchLiteProtocol, CouchLiteProtocolBuilder}
import request.{SqlRequestBuilder, SqlRequestBuilderBase}

import io.gatling.core.action.builder.ActionBuilder

object Predef { // someday maybe - implement trait for checks; re: approach from CQL Extension

  val sql: CouchLiteProtocolBuilder.type = CouchLiteProtocolBuilder

  implicit def sqlBuilderToProtocol(builder: CouchLiteProtocolBuilder): CouchLiteProtocol = builder.build()
  implicit def sqlBuilderActionBuilder(builder: SqlRequestBuilder): ActionBuilder = builder.build()

  def sql(tag: String) = SqlRequestBuilderBase(tag)
}
