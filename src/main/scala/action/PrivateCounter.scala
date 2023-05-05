package io.github.gatling.couchbase
package action

import java.util.concurrent.atomic.AtomicLong

object PrivateCounter {

  private val counter = new AtomicLong()

  def getCount(): Long = counter.getAndAdd(1)

}
