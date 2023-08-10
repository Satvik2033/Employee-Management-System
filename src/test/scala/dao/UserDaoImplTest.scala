//package org.knoldus
//package dao
//
//import db.UserDB
//import model.UserType.{Admin, Employee}
//import model._
//
//import org.scalatest.concurrent.ScalaFutures.whenReady
//import org.scalatest.funsuite.AnyFunSuite
//
//import java.util.UUID
//
//class UserDaoImplTest extends AnyFunSuite {
//  val userOne: User = User(UUID.randomUUID(), "Satvik", "Gupta", "satvik@gmail.com", "Password@123", Admin)
//  val userTwo: User = User(UUID.randomUUID(), "Vanshika", "Srivastava", "vanshika@gmail.com", "Password@123", Employee)
//  private val userDBObj = new UserDB
//  private val userDaoImpl = new UserDaoImpl(userDBObj)
//
//  test("getAllUser should return 'No users found' if the database is empty") {
//    val result = userDaoImpl.getAllUser
//    whenReady(result) { response =>
//      assert(response.right.get == "No users found")
//    }
//  }
//
//  test("createUser should add a new user to the database") {
//    val result = userDaoImpl.createUser(userOne)
//    whenReady(result) { _ =>
//      assert(userDBObj.users.contains(userOne.userId))
//      assert(userDBObj.users(userOne.userId) == userOne)
//    }
//  }
//
//  test("createUser should return 'User already exists' if user with the same ID already exists") {
//    val existingUser = userOne
//    userDBObj.users += (existingUser.userId -> existingUser)
//    val result = userDaoImpl.createUser(existingUser)
//    whenReady(result) { response =>
//      assert(response.right.get == "User already exists")
//    }
//  }
//
//  test("getUserById should retrieve a user by ID") {
//    val user = userOne
//    userDBObj.users += (user.userId -> user)
//    val result = userDaoImpl.getUserById(user.userId)
//    whenReady(result) { response =>
//      assert(response.left.get == user)
//    }
//  }
//
//  test("getUserById should return 'User does not exist' if user with the given ID does not exist") {
//    val nonExistingUserID = UUID.randomUUID()
//    val result = userDaoImpl.getUserById(nonExistingUserID)
//    whenReady(result) { response =>
//      assert(response.right.get == "User does not exist")
//    }
//  }
//
//  test("getAllUser should retrieve all users from the database") {
//    userDBObj.users += (userOne.userId -> userOne)
//    userDBObj.users += (userTwo.userId -> userTwo)
//    val result = userDaoImpl.getAllUser
//    whenReady(result) { response =>
//      assert(response.left.get == List(userOne, userTwo))
//    }
//  }
//
//  test("updateUser should update user information if the updater is an admin and provided information is valid") {
//    val user = userOne.copy(lastName = "Singh")
//    userDBObj.users += (user.userId -> user)
//    val updatedUser = user.copy(lastName = "Gupta")
//    val result = userDaoImpl.updateUser(user, updatedUser)
//    whenReady(result) { response =>
//      assert(userDBObj.users(user.userId) == updatedUser)
//      assert(response.left.get == updatedUser)
//    }
//  }
//
//  test("updateUser should return 'Access denied. Only admin can update users.' if the updater is not an admin") {
//    val user = userTwo
//    userDBObj.users += (user.userId -> user)
//    val updatedUser = user.copy(lastName = "Srivastava")
//    val result = userDaoImpl.updateUser(user, updatedUser)
//    whenReady(result) { response =>
//      assert(response.right.get == "Access denied. Only admin can update users.")
//    }
//  }
//
//  test("updateUser should return 'Update Unsuccessful!!! Invalid password format' if the provided password is invalid") {
//    val user = userOne
//    userDBObj.users += (user.userId -> user)
//    val updatedUser = user.copy(password = "123")
//    val result = userDaoImpl.updateUser(user, updatedUser)
//    whenReady(result) { response =>
//      assert(response.right.get == "Update Unsuccessful!!! Invalid password format")
//    }
//  }
//
//  test("updateUser should return 'Update Unsuccessful!!! Invalid email format' if the provided email is invalid") {
//    val user = userOne
//    userDBObj.users += (user.userId -> user)
//    val updatedUser = user.copy(email = "invalidemail.com")
//    val result = userDaoImpl.updateUser(user, updatedUser)
//    whenReady(result) { response =>
//      assert(response.right.get == "Update Unsuccessful!!! Invalid email format")
//    }
//  }
//
//  test("updateUser should return 'Update Unsuccessful!!! Invalid last name format' if the provided last name is invalid") {
//    val user = userOne
//    userDBObj.users += (user.userId -> user)
//    val updatedUser = user.copy(lastName = "gupta")
//    val result = userDaoImpl.updateUser(user, updatedUser)
//    whenReady(result) { response =>
//      assert(response.right.get == "Update Unsuccessful!!! Invalid last name format")
//    }
//  }
//
//  test("updateUser should return 'Update Unsuccessful!!! Invalid first name format' if the provided first name is invalid") {
//    val user = userOne
//    userDBObj.users += (user.userId -> user)
//    val updatedUser = user.copy(firstName = "satvik")
//    val result = userDaoImpl.updateUser(user, updatedUser)
//    whenReady(result) { response =>
//      assert(response.right.get == "Update Unsuccessful!!! Invalid first name format")
//    }
//  }
//
//  test("updateUser should return 'Update unsuccessful as user not found' if the user to be updated does not exist") {
//    val nonExistingUser = User(UUID.randomUUID(), "Pradyuman", "Singh", "pradyuman@example.com", "Password@123", Admin)
//    val result = userDaoImpl.updateUser(nonExistingUser, nonExistingUser)
//    whenReady(result) { response =>
//      assert(response.right.get == "Update unsuccessful as user not found")
//    }
//  }
//
//  test("delete should delete a user if the user is an admin") {
//    val user = userOne
//    userDBObj.users += (user.userId -> user)
//    userDaoImpl.delete(user, user.userId)
//      assert(!userDBObj.users.values.exists(user => false))
//    }
//
//  test("delete should return 'Access denied. Only admin can delete users.' if the user is not an admin") {
//    val user = userTwo
//    userDBObj.users += (user.userId -> user)
//    val result = userDaoImpl.delete(user, user.userId)
//    whenReady(result) { response =>
//      assert(response.right.get == "Access denied. Only admin can delete users.")
//    }
//  }
//
//  test("delete should return 'Deletion unsuccessful as user not found' if the user to be deleted does not exist") {
//    val nonExistingUserID = UUID.randomUUID()
//    val result = userDaoImpl.delete(userOne, nonExistingUserID)
//    whenReady(result) { response =>
//      assert(response.right.get == "Deletion unsuccessful as user not found")
//    }
//  }
//}
