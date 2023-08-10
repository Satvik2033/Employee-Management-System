//package org.knoldus
//package authentication
//
//import db.UserDB
//import model.UserType.Admin
//
//import org.scalatest.funsuite.AnyFunSuite
//
//import java.util.UUID
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.util.{Failure, Success}
//
//class LoginTest extends AnyFunSuite {
//  val userDaoImpl = new UserDaoImpl(userDBObject)
//  private val userDBObject = new UserDB
//  private val registrationObj = new Registration(userDaoImpl)
//  private val loginObj = new Login(userDBObject)
//
//  private val userOne = User(UUID.randomUUID(), "Satvik", "Gupta", "satvik.knoldus@gmail.com", "Password@123", Admin)
//  private val validFirstUser = registrationObj.registration(userOne)
//  validFirstUser.onComplete {
//    case Success(value) => value
//    case Failure(exception) => exception.getMessage
//  }
//  test("user exists") {
//    val loginUserOne = loginObj.getUserByEmailAndPassword(userOne.email, userOne.password)
//    loginUserOne.onComplete {
//      case Success(value) => assert(value.left.get == userOne)
//      case Failure(exception) => fail(s"Login failed: ${exception.getMessage}")
//    }
//  }
//
//  test("Invalid login") {
//    val userTwo = User(UUID.randomUUID(), "Vanshika", "Srivastava", "vanshika.knoldus@gmail.com", "Password@123", Admin)
//    val invalidLogin = loginObj.getUserByEmailAndPassword(userTwo.email, userTwo.password)
//
//    invalidLogin.onComplete {
//      case Success(value) => assert(value.right.get == "Login unsuccessful!!! User not found")
//      case Failure(exception) => fail(s"Unexpected failure: ${exception.getMessage}")
//    }
//  }
//}
