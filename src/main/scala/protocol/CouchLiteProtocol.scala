package io.github.gatling.couchbase
package protocol

import com.couchbase.lite.Database
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolComponents, ProtocolKey}
import io.gatling.core.session.Session
import io.gatling.core.session.Session.{Identity, NothingOnExit}

case class CouchLiteProtocol(database: Database) extends Protocol {
  type Components = CouchLiteComponents
}

object CouchLiteProtocol {

  val CouchLiteProtocolKey = new ProtocolKey[CouchLiteProtocol, CouchLiteComponents] {

    type Protocol = CouchLiteProtocol
    type Components = CouchLiteComponents

    override def protocolClass: Class[io.gatling.core.protocol.Protocol] = classOf[CouchLiteProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    override def defaultProtocolValue(configuration: GatlingConfiguration): CouchLiteProtocol = throw new IllegalStateException("Can't provide a default value for SqlProtocol")

    override def newComponents(coreComponents: CoreComponents): CouchLiteProtocol => CouchLiteComponents = {
      couchLiteProtocol => CouchLiteComponents(couchLiteProtocol)
    }
  }

}

case class CouchLiteComponents(couchLiteProtocol: CouchLiteProtocol) extends ProtocolComponents {
  def onStart: Session => Session = Identity

  def onExit: Session => Unit = NothingOnExit
}

case class CouchLiteProtocolBuilder(database: Database = null) {
  private var fileName: String = _

  def fileName(fileName: String): CouchLiteProtocolBuilder = {
    this.fileName = fileName
    this
  }

  def build(): CouchLiteProtocol = {
    var db: Database = null
    if (database != null) {
      db = database
    } else {
      println("Creating Database")
      db = new Database(fileName)
    }
    CouchLiteProtocol(database)
  }
}

object CouchLiteProtocolBuilder {
  def connection(database: Database) = CouchLiteProtocolBuilder(database)
}