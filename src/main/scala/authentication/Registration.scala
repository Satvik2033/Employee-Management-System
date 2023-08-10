package org.knoldus
package authentication

import dao.MySqlDaoImpl
import model.User
import utils.Validator

import scala.concurrent.Future

class Registration(userDaoImpl: MySqlDaoImpl) {
  private val validator = new Validator

  def registration(user: User): Future[Either[User, String]] = {
    if (validator.nameValidator(user.firstName).isLeft)  {
      if (validator.nameValidator(user.lastName).isLeft) {
        if (validator.emailValidator(user.email).isLeft) {
          if (validator.passwordValidator(user.password).isLeft)  {
            userDaoImpl.createUser(user)
          } else {
            Future.successful(Right("Invalid password format"))
          }
        } else {
          Future.successful(Right("Invalid email format"))
        }
      } else {
        Future.successful(Right("Invalid last name format"))
      }
    } else {
      Future.successful(Right("Invalid first name format"))
    }
  }
}
