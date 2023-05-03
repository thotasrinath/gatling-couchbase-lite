package io.github.gatling.couchbase
package couchlite_explore

import com.fasterxml.jackson.databind.ObjectMapper

import java.io.File
import java.util

object Test {

  def main(args: Array[String]): Unit = {
    val query = new ObjectMapper().readValue(new File("src/main/resources/scratch-100.json"),classOf[util.HashMap[String,Object]])


    println(query.get("meta"))
  }

}
