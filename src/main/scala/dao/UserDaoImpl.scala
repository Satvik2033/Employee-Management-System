//package org.knoldus
//package dao
//
//import db.UserDB
//import model.User
//import model.UserType.Admin
//import utils.Validator
//
//import com.typesafe.scalalogging.Logger
//
//import java.util.UUID
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class UserDaoImpl(userDB: UserDB) extends UserDao {
//  val logger: Logger = Logger(getClass)
//  private val validator = new Validator
//
//  // Create a new user
//  override def createUser(newUser: User): Future[Either[User, String]] = Future {
//    if (userDB.users.contains(newUser.userId)) {
//      Right("User already exists")
//    } else {
//      userDB.users += (newUser.userId -> newUser)
//      logger.info(s"User created successfully $newUser")
//      Left(newUser)
//    }
//  }
//
//  // Get user by ID
//  override def getUserById(userID: UUID): Future[Either[User, String]] = Future {
//    if (userDB.users.contains(userID)) {
//      logger.info(s"User found ${userDB.users(userID)}")
//      Left(userDB.users(userID))
//    } else {
//      val userNotExist: String = "User does not exist"
//      logger.info(s"$userNotExist")
//      Right(userNotExist)
//    }
//  }
//
//  // Get all users
//  override def getAllUser: Future[Either[List[User], String]] = Future {
//    if (userDB.users.isEmpty) {
//      Right("No users found")
//    } else {
//      logger.info(s"List of all users ${userDB.users.values.toList}")
//      Left(userDB.users.values.toList)
//    }
//  }
//
//  // Update user information
//  override def updateUser(updater: User, user: User): Future[Either[User, String]] = Future {
//    if (userDB.users.contains(user.userId)) {
//      if (updater.userType == Admin) {
//        if (validator.nameValidator(user.firstName).isLeft) {
//          if (validator.nameValidator(user.lastName).isLeft) {
//            if (validator.emailValidator(user.email).isLeft) {
//              if (validator.passwordValidator(user.password).isLeft) {
//                val updatedUser = userDB.users(user.userId).copy(
//                  firstName = user.firstName,
//                  lastName = user.lastName,
//                  email = user.email,
//                  password = user.password
//                )
//                userDB.users += (user.userId -> updatedUser)
//                logger.info(s"User updated successfully with values: $updatedUser")
//                Left(updatedUser)
//              } else {
//                val invalidPassword: String = "Update Unsuccessful!!! Invalid password format"
//                logger.info(s"$invalidPassword")
//                Right(invalidPassword)
//              }
//            } else {
//              val invalidEmail: String ="Update Unsuccessful!!! Invalid email format"
//              logger.info(s"$invalidEmail")
//              Right(invalidEmail)
//            }
//          } else {
//            val invalidLastName: String = "Update Unsuccessful!!! Invalid last name format"
//            logger.info(s"$invalidLastName")
//            Right(invalidLastName)
//          }
//        } else {
//          val invalidFirstname: String = "Update Unsuccessful!!! Invalid first name format"
//          logger.info(s"$invalidFirstname")
//          Right(invalidFirstname)
//        }
//      } else {
//        Right("Access denied. Only admin can update users.")
//      }
//    } else {
//      Right("Update unsuccessful as user not found")
//    }
//  }
//
//  // Delete a user
//  override def delete(user: User, userID: UUID): Future[Either[User, String]] = Future {
//    if (userDB.users.contains(userID)) {
//      if (user.userType == Admin) {
//        val deletedUser = userDB.users(userID)
//        userDB.users -= userID
//        logger.info(s"Deleted successfully $deletedUser")
//        Left(deletedUser)
//      } else {
//        Right("Access denied. Only admin can delete users.")
//      }
//    } else {
//      Right("Deletion unsuccessful as user not found")
//    }
//  }
//}
