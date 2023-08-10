//package org.knoldus
//package bootstrap
//
//import authentication.{Login, Registration}
//import dao.UserDaoImpl
//import db.UserDB
//import model.User
//import model.UserType.{Admin, Customer}
//import services.Service
//
//import com.typesafe.scalalogging.Logger
//
//import java.util.UUID
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.util.{Failure, Success}
//
//object Main extends App {
//  private val logger = Logger(getClass)
//
//  // Create instances of required classes
//  private val userDBObject = new UserDB
//  val userDaoImpl = new UserDaoImpl(userDBObject)
//  private val serviceObj = new Service(userDaoImpl)
//  private val registrationObj = new Registration(userDaoImpl)
//  private val loginObj = new Login(userDBObject)
//
//  // Creating userOne with invalid credentials
//  private val invalidUserOne = User(UUID.randomUUID(),"satvik","Gupta","satvik@email.com","Password@123",Admin)
//  private val invalidFirstUser = registrationObj.registration(invalidUserOne)
//  invalidFirstUser.onComplete {
//    case Success(value) => logger.info(s"$value")
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Creating userOne with valid credentials
//  private val validUserOne = User(UUID.randomUUID(), "Satvik", "Gupta", "satvik@email.com", "Password@123", Admin)
//  private val validFirstUser = registrationObj.registration(validUserOne)
//  validFirstUser.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Creating validUserTwo with valid credentials
//  private val validUserTwo = User(UUID.randomUUID(), "Vanshika", "Srivastava", "vanshika@email.com", "Password#123", Customer)
//  private val secondUser = registrationObj.registration(validUserTwo)
//  secondUser.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Login user one with valid credentials
//  private val loginUserOne = loginObj.getUserByEmailAndPassword(validUserOne.email, validUserOne.password)
//  loginUserOne.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Login user two with valid credentials
//  private val loginUserTwo = loginObj.getUserByEmailAndPassword(validUserTwo.email, validUserTwo.password)
//  loginUserTwo.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Get all users
//  private val serviceGetAllUser = serviceObj.getAllUser
//  serviceGetAllUser.onComplete {
//    case Success(user) => user
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Get user by ID
//  private val userById = serviceObj.getUserById(validUserTwo.userId)
//  userById.onComplete{
//    case Success(user) => user
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Updating user by Admin
//  private val updateUserByAdmin = serviceObj.updateUser(validUserOne, user = validUserTwo.copy(password = "Password#456"))
//  updateUserByAdmin.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Updating user by admin with incorrect password format
//  private val invalidPasswordUpdateByAdmin = serviceObj.updateUser(validUserOne, user = validUserTwo.copy(password = "Password56"))
//  invalidPasswordUpdateByAdmin.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Updating user by admin with incorrect email format
//  private val invalidEmailUpdateByAdmin = serviceObj.updateUser(validUserOne, user = validUserTwo.copy(email = "vanshikagmail.com"))
//  invalidEmailUpdateByAdmin.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Updating user by admin with incorrect firstName format
//  private val invalidFirstNameUpdateByAdmin = serviceObj.updateUser(validUserOne, user = validUserTwo.copy(firstName = "Vanshika123"))
//  invalidFirstNameUpdateByAdmin.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Updating user by admin with incorrect lastName format
//  private val invalidLastNameUpdateByAdmin = serviceObj.updateUser(validUserOne, user = validUserTwo.copy(lastName = "srivastava"))
//  invalidLastNameUpdateByAdmin.onComplete {
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Updating user by Customer
//  private val updateUserByCustomer = serviceObj.updateUser(validUserTwo
//    , user = validUserTwo.copy(password = "Password#435"))
//  updateUserByCustomer.onComplete {
//    case Success(value) => logger.info(s"$value")
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
////
//  // Deleting user by Admin
//  private val deleteUserByAdmin = serviceObj.delete(validUserOne,validUserTwo.userId)
//  deleteUserByAdmin.onComplete{
//    case Success(value) => value
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//
//  // Get all users after deletion
//  private val getAllAfterDeletion = serviceObj.getAllUser
//  getAllAfterDeletion.onComplete {
//    case Success(user) => user
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Get user by ID after deletion
//  private val userByIdAfterDeletion = serviceObj.getUserById(validUserTwo.userId)
//  userByIdAfterDeletion.onComplete {
//    case Success(user) => user
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Updating user by after deletion
//  private val updateUserByAdminAfterDeletion = serviceObj.updateUser(validUserOne,user = validUserTwo.copy(password = "Password#435"))
//  updateUserByAdminAfterDeletion.onComplete {
//    case Success(value) => logger.info(s"$value")
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//
//  // Deleting user by Customer
//  private val deleteUserByCustomer = serviceObj.delete(validUserTwo,validUserOne.userId)
//  deleteUserByCustomer.onComplete {
//    case Success(value) => logger.info(s"$value")
//    case Failure(exception) => logger.info(exception.getMessage)
//  }
//}
