package org.knoldus
package utils

class Validator {

  /*
  * ^[A-Z][a-z] checks for name starting with capital letter and having only alphabetic characters
  * */
  def nameValidator(name: String): Either[Boolean, String] = {
    val nameCondition = "^[A-Z][a-z]+$".r
    if (nameCondition matches name) {
      Left(true)
    } else {
      Right(s"First name and last name must have only alphabets which starts with capital.")
    }
  }

  /*
    * ([\w\.!#$%&*+/=?^_{|}~-]+) Matches one or more word characters (\w), periods (\.), or special characters (![#$%&*+/=?^_{|}~-]).
    *   - This represents the local part of the email address before the '@' symbol.
    *
    * @ Matches the '@' symbol.
    *
    * ([\w]+) Matches one or more word characters (\w). This represents the domain name part of the email address.
    *
    * ([.]{1}[\w]+) Matches a period (\.), followed by one or more word characters (\w).
    *   - This represents the domain extension (e.g., .com, .org) part of the email address.
    *
    * ([.]{1}[\w]+)+ Matches one or more occurrences of the domain extension pattern mentioned in the previous block.
    */
  def emailValidator(email: String): Either[Boolean, String] = {
    val emailCondition = """([\w\.!#$%&*+/=?^_`{|}~-]+)@([\w]+)([\.]{1}[\w]+)+""".r
    if (emailCondition matches email) {
      Left(true)
    } else {
      Right("Invalid email format.")
    }
  }

  /*
  * (?=.*[A-Z]) checks for at least one uppercase letter.
  * (?=.*[a-z]) checks for at least one lowercase letter.
  * (?=.*\d) checks for at least one digit.
  * (?=.*[!@#$%^&*()_+]) checks for at least one special character. Feel free to modify this character class with your desired special characters.
  * [A-Za-z\d!@#$%^&*()_+] matches any uppercase letter, lowercase letter, digit, or special character.
  * {8,} ensures that the password is at least 8 characters long.
  */
  def passwordValidator(password: String): Either[Boolean, String] = {
    val passwordCondition = """^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$""".r
    if (passwordCondition matches password) {
      Left(true)
    } else {
      Right("Invalid password format.")
    }
  }
}
