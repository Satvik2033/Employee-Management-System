package org.knoldus
package model

/**
 * Represents a user in the system.
 *
 * @param userId    The unique identifier for the user.
 * @param firstName The first name of the user.
 * @param lastName  The last name of the user.
 * @param email     The email address of the user.
 * @param password  The password of the user.
 * @param userType  The type of the user, either Admin or Customer.
 */
case class User(
                 userId: String,
                 firstName: String,
                 lastName: String,
                 email: String,
                 password: String,
                 userType: UserType
               )
