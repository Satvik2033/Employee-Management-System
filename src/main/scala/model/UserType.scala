package org.knoldus
package model

// Represents the different types of users
sealed trait UserType

object UserType {
  case object Employee extends UserType

  case object Admin extends UserType
}