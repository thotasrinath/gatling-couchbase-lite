package io.github.gatling.couchbase

import protocol.CouchLiteProtocol

import com.couchbase.lite.Query
import com.typesafe.scalalogging.StrictLogging
import io.gatling.commons.validation.{Failure, Success, Validation}
import io.gatling.core.session.{Expression, Session}

import java.util


case class SqlStatementRequest(stmtType: String, stmt: String = "", obj: Any = None)


abstract class SqlStatement() extends StrictLogging {
  def apply(session: Session, sqlProtocol: CouchLiteProtocol): (Session, Validation[SqlStatementRequest]) = null
}

class SimpleSqlStatement(statement: Expression[SqlStatementRequest]) extends SqlStatement {
  override def apply(session: Session, sqlProtocol: CouchLiteProtocol): (Session, Validation[SqlStatementRequest]) = {
    statement(session) match {
      case Success(stmt) =>
        //logger.debug(s"STMT: $stmt")
        (session, Success(stmt))
      case Failure(t) => (session, Failure(t))
    }
  }
}


