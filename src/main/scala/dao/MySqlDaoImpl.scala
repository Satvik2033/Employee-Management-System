package org.knoldus
package dao

import model.{User, UserType}

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MySqlDaoImpl(url: String, username: String, password: String) extends UserDao {

  private def getConnection(): Connection = {
    DriverManager.getConnection(url, username, password)
  }

  // Helper method to close resources
  private def closeResources(connection: Connection, statement: PreparedStatement, resultSet: ResultSet): Unit = {
    if (resultSet != null) resultSet.close()
    if (statement != null) statement.close()
    if (connection != null) connection.close()
  }

  private def userTypeToString(userTypeString: String): UserType = {
    userTypeString match {
      case "Admin" => UserType.Admin
      case "Customer" => UserType.Employee
      case _ => throw new IllegalArgumentException(s"Invalid user type: $userTypeString")
    }
  }

  /**
   * Creates a new user.
   *
   * @param user The user to be created.
   * @return A Future that resolves to either the created user or an error message.
   */
  override def createUser(newUser: User): Future[Either[User, String]] = Future {
    val connection = getConnection()

    val emailExistsQuery = "SELECT id FROM Users WHERE email = ?"
    val insertUserQuery = s"INSERT INTO Users (id, firstName, lastName, password, email, userType) VALUES (?,?,?,?,?,?)"
    var result: PreparedStatement = null
    result = connection.prepareStatement(emailExistsQuery)
    result.setString(1, newUser.email)
    val resultSet = result.executeQuery()
    if (resultSet.next()) {
      closeResources(connection, result, resultSet)
      Right("User Already Exists")
    } else {
      // Insert new user
      val userId = UUID.randomUUID().toString
      result = connection.prepareStatement(insertUserQuery)
      result.setObject(1, userId)
      result.setString(2, newUser.firstName)
      result.setString(3, newUser.lastName)
      result.setString(5, newUser.email)
      result.setString(4, newUser.password)
      result.setString(6, newUser.userType.toString)
      result.executeUpdate()
      closeResources(connection, result, resultSet)
      Left(newUser)
    }
  }

  /**
   * Retrieves a user by their user ID.
   *
   * @param userID The ID of the user to retrieve.
   * @return A Future that resolves to either the user with the specified ID or an error message.
   */
  override def getUserById(userID: String): Future[Either[User, String]] = Future {
    val connection = getConnection()

    val getUserQuery = "SELECT * FROM Users WHERE id = ?"
    val result = connection.prepareStatement(getUserQuery)
    result.setObject(1, userID)
    val resultSet = result.executeQuery()

    if (resultSet.next()) {
      val user = User(
        resultSet.getString("id"),
        resultSet.getString("firstName"),
        resultSet.getString("lastName"),
        resultSet.getString("email"),
        resultSet.getString("password"),
        userTypeToString(resultSet.getString("userType"))
      )
      closeResources(connection, result, resultSet)
      Left(user)
    } else {
      closeResources(connection, result, resultSet)
      Right("User not found")
    }
  }

  /**
   * Retrieves all users.
   *
   * @return A Future that resolves to either a list of all users or an error message.
   */
  override def getAllUser: Future[Either[List[User], String]] = Future {
    val connection = getConnection()

    val getAllUsersQuery = "SELECT * FROM Users"
    val result = connection.prepareStatement(getAllUsersQuery)
    val resultSet = result.executeQuery()

    var users: List[User] = List.empty
    while (resultSet.next()) {
      val user = User(
        resultSet.getString("id"),
        resultSet.getString("firstName"),
        resultSet.getString("lastName"),
        resultSet.getString("password"),
        resultSet.getString("email"),
        userTypeToString(resultSet.getString("userType"))
      )
      users = user :: users
    }

    closeResources(connection, result, resultSet)
    Left(users.reverse)
  }

  /**
   * Updates a user.
   *
   * @param user The updated user information.
   * @return A Future that resolves to either the updated user or an error message.
   */
  override def updateUser( user: User): Future[Either[User, String]] = Future {
    val connection = getConnection()

    val updateUserQuery = "UPDATE Users SET firstName = ?, lastName = ?, email = ?, password = ? WHERE id = ?"
    val result = connection.prepareStatement(updateUserQuery)
    result.setString(1, user.firstName)
    result.setString(2, user.lastName)
    result.setString(3, user.email)
    result.setString(4, user.password)
    result.setObject(5, user.userId)
    val affectedRows = result.executeUpdate()

    closeResources(connection, result, null)

    if (affectedRows > 0) {
      Left(user)
    } else {
      Right("Failed to update user")
    }
  }

  /**
   * Deletes a user.
   *
   * @param userID The ID of the user to delete.
   * @return A Future that resolves to either the deleted user or an error message.
   */
  override def delete(userID: String): Future[String] = Future {
    val connection = getConnection()

    val deleteUserQuery = "DELETE FROM Users WHERE id = ?"
    val result = connection.prepareStatement(deleteUserQuery)
    result.setObject(1, userID)
    val affectedRows = result.executeUpdate()

    closeResources(connection, result, null)

    if (affectedRows > 0) {
      "user deleted successfully"
    } else {
      "Failed to delete user"
    }
  }
}
