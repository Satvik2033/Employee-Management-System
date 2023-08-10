package org.knoldus
package dao

import model.User

import scala.concurrent.Future

/**
 * A trait representing the data access operations for the User entity.
 */
trait UserDao {
  /**
   * Creates a new user.
   *
   * @param user The user to be created.
   * @return A Future that resolves to either the created user or an error message.
   */
  def createUser(user: User): Future[Either[User, String]]

  /**
   * Retrieves a user by their user ID.
   *
   * @param userID The ID of the user to retrieve.
   * @return A Future that resolves to either the user with the specified ID or an error message.
   */
  def getUserById(userID: String): Future[Either[User, String]]

  /**
   * Retrieves all users.
   *
   * @return A Future that resolves to either a list of all users or an error message.
   */
  def getAllUser: Future[Either[List[User], String]]

  /**
   * Updates a user.
   *
   * @param user The updated user information.
   * @return A Future that resolves to either the updated user or an error message.
   */
  def updateUser(user: User): Future[Either[User, String]]

  /**
   * Deletes a user.
   *
   * @param userID The ID of the user to delete.
   * @return A Future that resolves to either the deleted user or an error message.
   */
  def delete(userID: String): Future[String]
}
