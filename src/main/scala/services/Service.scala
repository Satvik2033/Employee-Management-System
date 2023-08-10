package org.knoldus
package services

import dao.UserDao
import model.User

import scala.concurrent.Future

class Service(userDaoImpl: UserDao) {
  /**
   * Creates a new user.
   *
   * @param user The user object containing the user's information.
   * @return A Future containing either the created user object if successful or an error message if unsuccessful.
   */
  def createUser(user: User): Future[Either[User, String]] = userDaoImpl.createUser(user)

  /**
   * Retrieves a user by their user ID.
   *
   * @param userID The ID of the user to retrieve.
   * @return A Future containing either the retrieved user object if successful or an error message if unsuccessful.
   */
  def getUserById(userID: String): Future[Either[User, String]] = userDaoImpl.getUserById(userID)

  /**
   * Retrieves all users.
   *
   * @return A Future containing either a list of all user objects if successful or an error message if unsuccessful.
   */
  def getAllUser: Future[Either[List[User], String]] = userDaoImpl.getAllUser

  /**
   * Updates a user's information.
   *
   * @param updater The user performing the update (e.g., an admin user).
   * @param user    The user object containing the updated information.
   * @return A Future containing either the updated user object if successful or an error message if unsuccessful.
   */
  def updateUser(user: User): Future[Either[User, String]] = userDaoImpl.updateUser(user)

  /**
   * Deletes a user.
   *
   * @param userID The ID of the user to delete.
   * @return A Future containing either the deleted user object if successful or an error message if unsuccessful.
   */
  def delete(userID: String): Future[String] = userDaoImpl.delete(userID)
}
