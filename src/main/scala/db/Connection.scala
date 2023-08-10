package org.knoldus
package db

import com.typesafe.config.ConfigFactory

import java.sql.{Connection, DriverManager}

object Connection {

  Class.forName("com.mysql.cj.jdbc.Driver")
  private val config = ConfigFactory.load()
  val url: String = config.getString("database.url")
  val username: String = config.getString("database.username")
  val password: String = config.getString("database.password")
  val connection: Connection = DriverManager.getConnection(url, username, password)

}
