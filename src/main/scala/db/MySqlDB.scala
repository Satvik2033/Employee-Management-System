package org.knoldus
package db

import dao.UserDao
import model.{User, UserType}

import scalafx.Includes.integer2IntegerBinding

import java.sql.{Connection, PreparedStatement, ResultSet}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MySqlDB(connection: Connection) extends UserDao {

  def userTypeToString(stringUserType: String): UserType = {
    stringUserType match {
      case "Admin" => UserType.Admin
      case "Customer" => UserType.Employee
      case _ => throw new IllegalArgumentException(s"Invalid user type: $stringUserType")
    }
  }

  override def createUser(newUser: User): Future[Either[User, String]] = {
    val statement = connection.prepareStatement("INSERT INTO Users (id, firstName, lastName, email, password, userType) VALUES (?, ?, ?, ?, ?, ?)")
    statement.setObject(1, newUser.userId)
    statement.setString(2, newUser.firstName)
    statement.setString(3, newUser.lastName)
    statement.setString(4, newUser.email)
    statement.setString(5, newUser.password)
    statement.setObject(5, newUser.userType)

    executeUpdateStatement(statement)
      .map(_ => Left(newUser))
      .recover {
        case ex: Exception => Right(ex.getMessage)
      }
  }

  override def getUserById(userID: String): Future[Either[User, String]] = {
    val statement = connection.prepareStatement("SELECT id, firstName, lastName, email, password, userType FROM Users WHERE id = ?")
    statement.setObject(1, userID)

    executeQueryStatement(statement)
      .flatMap { resultSet =>
        if (resultSet.next()) {
          val userTypeString = resultSet.getString("userType")
          val userType = userTypeToString(userTypeString)
          val user = User(
            resultSet.getString("id"),
            resultSet.getString("firstName"),
            resultSet.getString("lastName"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            userType
          )
          Future.successful(Left(user))
        } else {
          Future.successful(Right("User does not exist"))
        }
      }
      .recover {
        case ex: Exception => Right(ex.getMessage)
      }
  }

  override def getAllUser: Future[Either[List[User], String]] = {
    val statement = connection.prepareStatement("SELECT id, firstName, lastName, email, password FROM Users")

    executeQueryStatement(statement)
      .map { resultSet =>
        val users = new scala.collection.mutable.ListBuffer[User]()
        while (resultSet.next()) {
          val userTypeString = resultSet.getString("userType")
          val userType = userTypeToString(userTypeString)
          val user = User(
            resultSet.getString("id"),
            resultSet.getString("firstName"),
            resultSet.getString("lastName"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            userType
          )
          users += user
        }
        if (users.isEmpty)
          Right("No users found")
        else
          Left(users.toList)
      }
      .recover {
        case ex: Exception => Right(ex.getMessage)
      }
  }

  override def updateUser( user: User): Future[Either[User, String]] = {
    val statement = connection.prepareStatement("UPDATE Users SET firstName = ?, lastName = ?, email = ?, password = ? WHERE id = ?")
    statement.setString(1, user.firstName)
    statement.setString(2, user.lastName)
    statement.setString(3, user.email)
    statement.setString(4, user.password)
    statement.setObject(5, user.userId)

    executeUpdateStatement(statement)
      .flatMap { _ =>
        getUserById(user.userId)
      }
      .recover {
        case ex: Exception => Right(ex.getMessage)
      }
  }

  override def delete(userID: String): Future[String] = {
    val statement = connection.prepareStatement("DELETE FROM Users WHERE id = ?")
    statement.setString(1, userID)
    statement.executeUpdate()
    Future.successful("User Deleted Successfully")
  }
    .recover {
      case ex: Exception => ex.getMessage
    }

  def getUserByEmailAndPassword(email: String, password: String): Future[Either[User, String]] = {
    val statement = connection.prepareStatement("SELECT id, firstName, lastName, email, password, userType FROM Users WHERE email = ? AND password = ?")
    statement.setString(1, email)
    statement.setString(2, password)

    executeQueryStatement(statement).flatMap { resultSet =>
      if (resultSet.next()) {
        val userTypeString = resultSet.getString("userType")
        val userType = userTypeToString(userTypeString)
        val user = User(
          resultSet.getString("id"),
          resultSet.getString("firstName"),
          resultSet.getString("lastName"),
          resultSet.getString("email"),
          resultSet.getString("password"),
          userType
        )
        Future.successful(Left(user))
      } else {
        Future.successful(Right("User not found"))
      }
    }.recover {
      case ex: Exception => Right(ex.getMessage)
    }
  }

  private def executeUpdateStatement(statement: PreparedStatement): Future[Unit] = Future {
    statement.executeUpdate()
    statement.close()
  }

  private def executeQueryStatement(statement: PreparedStatement): Future[ResultSet] = Future {
    statement.executeQuery()
  }
}