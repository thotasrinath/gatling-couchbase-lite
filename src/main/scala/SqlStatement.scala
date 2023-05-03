package io.github.gatling.couchbase

import protocol.CouchLiteProtocol

import com.couchbase.lite.Query
import com.typesafe.scalalogging.StrictLogging
import io.gatling.commons.validation.{Failure, Success, Validation}
import io.gatling.core.session.{Expression, Session}

import java.util


abstract class SqlStatement() extends StrictLogging {
  def apply(session: Session, sqlProtocol: CouchLiteProtocol): (Session, Validation[util.HashMap[String,Object]]) = null
}

class SimpleSqlStatement(statement: Expression[util.HashMap[String,Object]]) extends SqlStatement {
  override def apply(session: Session, sqlProtocol: CouchLiteProtocol): (Session, Validation[util.HashMap[String,Object]]) = {
    statement(session) match {
      case Success(stmt) =>
        //logger.debug(s"STMT: $stmt")
        (session, Success(stmt))
      case Failure(t) => (session, Failure(t))
    }
  }

}
