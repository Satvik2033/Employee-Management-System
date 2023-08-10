package org.knoldus
package db

import model.User

import java.util.UUID

class UserDB {
  // The map used as a database to store user objects with their UUID as the key
  var users: Map[UUID, User] = Map.empty
}
