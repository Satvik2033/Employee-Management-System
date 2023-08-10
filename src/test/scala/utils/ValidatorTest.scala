package org.knoldus
package utils

import org.scalatest.funsuite.AnyFunSuite


class ValidatorTest extends AnyFunSuite {
  val validatorObject = new Validator


  test("if name has valid format it should return true") {
    val validNameFormat = "Satvik"
    assert(validatorObject.nameValidator(validNameFormat).isLeft)
  }

  test("name validator should return 'First name and last name must have only alphabets which starts with capital.' if format is invalid") {
    val invalidNameFormat = "satvik"
    assert(validatorObject.nameValidator(invalidNameFormat).right.get == "First name and last name must have only alphabets which starts with capital.")
  }

  test("if email has valid format it should return true") {
    val validEmailFormat = "satvik@gmail.com"
    assert(validatorObject.emailValidator(validEmailFormat).isLeft)
  }

  test("emailValidator should return 'Invalid email format.', if email has invalid format") {
    val invalidEmailFormat = "satvik.com"
    assert(validatorObject.emailValidator(invalidEmailFormat).right.get == "Invalid email format.")

  }

  test("if password has valid format it should return true") {
    val validPasswordFormat = "Password@123"
    assert(validatorObject.passwordValidator(validPasswordFormat).isLeft)
  }

  test("passwordValidator should return 'Invalid password format.', if password ha invalid format") {
    val invalidPasswordFormat = "Password123"
    assert(validatorObject.passwordValidator(invalidPasswordFormat).right.get == "Invalid password format.")
  }

}
