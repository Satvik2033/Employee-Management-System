//package org.knoldus
//package authentication
//
//import dao.UserDaoImpl
//import db.UserDB
//import model.User
//import model.UserType.Admin
//
//import org.scalatest.funsuite.AnyFunSuite
//
//import java.util.UUID
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.util.{Failure, Success}
//
//class RegistrationTest extends AnyFunSuite {
//  private val userDBObject = new UserDB
//  val userDaoImpl = new UserDaoImpl(userDBObject)
//  private val registrationObj = new Registration(userDaoImpl)
//
//  test("test for successful registration") {
//    val userOne = User(UUID.randomUUID(), "Satvik", "Gupta", "satvik.knoldus@gmail.com", "Password@123", Admin)
//    val userOneRegistration = registrationObj.registration(userOne)
//    userOneRegistration.onComplete {
//      case Success(value) => assert(value.left.get == userOne)
//      case Failure(exception) => fail(s"Login failed: ${exception.getMessage}")
//    }
//  }
//
//  test("test for invalid first name format in registration") {
//    val userOne = User(UUID.randomUUID(), "satvik", "Gupta", "satvik.knoldus@gmail.com", "Password@123", Admin)
//    val userOneRegistration = registrationObj.registration(userOne)
//    userOneRegistration.onComplete {
//      case Success(value) => assert(value.right.get == "Invalid first name format")
//      case Failure(exception) => fail(s"Login failed: ${exception.getMessage}")
//    }
//  }
//
//  test("test for invalid last name format in registration") {
//    val userOne = User(UUID.randomUUID(), "Satvik", "gupta", "satvik.knoldus@gmail.com", "Password@123", Admin)
//    val userOneRegistration = registrationObj.registration(userOne)
//    userOneRegistration.onComplete {
//      case Success(value) => assert(value === "Invalid last name format")
//      case Failure(exception) => fail(s"Login failed: ${exception.getMessage}")
//    }
//  }
//
//  test("test for invalid email format in registration") {
//    val userOne = User(UUID.randomUUID(), "Satvik", "Gupta", "satvik.knoldusgmail.com", "Password@123", Admin)
//    val userOneRegistration = registrationObj.registration(userOne)
//    userOneRegistration.onComplete {
//      case Success(value) => assert(value.right.get == "Invalid email format")
//      case Failure(exception) => fail(s"Login failed: ${exception.getMessage}")
//    }
//  }
//
//  test("test for invalid password format in registration") {
//    val userOne = User(UUID.randomUUID(), "Satvik", "Gupta", "satvik.knoldus@gmail.com", "Password123", Admin)
//    val userOneRegistration = registrationObj.registration(userOne)
//    userOneRegistration.onComplete {
//      case Success(value) => assert(value.right.get == "Invalid password format")
//      case Failure(exception) => fail(s"Login failed: ${exception.getMessage}")
//    }
//  }
//}
