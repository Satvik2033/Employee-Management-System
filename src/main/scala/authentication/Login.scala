package org.knoldus
package authentication

import db.MySqlDB
import model.User

import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Login(mySqlDB: MySqlDB) {
  private val logger = Logger(getClass)

  /**
   * Retrieves a user from the user database based on the provided email and password.
   *
   * @param email    The email of the user.
   * @param password The password of the user.
   * @return A Future containing either the user object if found or an error message if not found.
   */
  def getUserByEmailAndPassword(email: String, password: String): Future[Either[User, String]] = {
    mySqlDB.getUserByEmailAndPassword(email, password).map {
      case Left(user) =>
        logger.info(s"Login successful!!! User is ${user.userType}")
        Left(user)
      case Right(errorMessage) =>
        Right(s"Login unsuccessful!!! $errorMessage")
    }
  }
}
