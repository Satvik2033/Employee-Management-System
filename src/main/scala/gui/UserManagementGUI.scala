package org.knoldus
package gui

import authentication.{Login, Registration}
import dao.MySqlDaoImpl
import db.Connection.{password, url, username}
import db.{Connection, MySqlDB}
import model.UserType.{Admin, Employee}
import model.{User, UserType}
import services.Service

import javafx.stage.Screen
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{JFXApp, Platform}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.effect.BlendMode
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.{Font, FontWeight, Text, TextAlignment}

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object UserManagementGUI extends JFXApp {

  private val userDatabaseObject = new MySqlDB(Connection.connection)
  private val mySqlImplementationObject = new MySqlDaoImpl(url, username, password)
  private val userServiceObject = new Service(mySqlImplementationObject)
  private val register = new Registration(mySqlImplementationObject)
  private val login = new Login(userDatabaseObject)

  val backgroundImagePath = "finger-pressing-social-network-button-touch-screen.jpg"
  val backgroundImage: Image = new Image(backgroundImagePath)
  val backgroundSize: BackgroundSize = new BackgroundSize(BackgroundSize.Auto, BackgroundSize.Auto, false, false, true, true)
  val backgroundPosition: BackgroundPosition = BackgroundPosition.Center
  val backgroundRepeat: BackgroundRepeat = BackgroundRepeat.NoRepeat
  val blendedBackgroundImage = new BackgroundImage(
    backgroundImage,
    backgroundRepeat,
    backgroundRepeat,
    backgroundPosition,
    backgroundSize
  )

  val blendEffect = new scalafx.scene.effect.Blend()
  blendEffect.mode = BlendMode.MULTIPLY
  blendEffect.opacity = 0.7 // Adjust the opacity value to control the level of fading

  val background: Background = new Background(Array(blendedBackgroundImage))
  // Set up the primary stage
  stage = new PrimaryStage {
    title = "Employee Management System"
    scene = createSceneUser()
  }

  // Function to create the initial scene
  private def createSceneUser(): Scene = {
    // Create UI components
    val titleText = new Text("Employee Management System") {
      font = Font.font("Verdana", FontWeight.Bold, 20)
    }

    val loginButton = new Button("Login")
    val registerButton = new Button("Register")

    val buttonBox = new HBox(10, loginButton, registerButton)
    buttonBox.padding = Insets(10)
    buttonBox.alignment = Pos.Center

    // Register event handlers
    loginButton.onAction = _ => showLoginDialog()
    registerButton.onAction = _ => showRegistrationDialog()

    // Create the root layout
    val root = new VBox(20, titleText, buttonBox) {
      padding = Insets(25)
      alignment = Pos.Center
    }
    root.setBackground(background)
    val screen = Screen.getPrimary

    // Set the scene dimensions to fullscreen
    val sceneWidth = screen.getBounds.getWidth
    val sceneHeight = screen.getBounds.getHeight

    new Scene(root, sceneWidth, sceneHeight)

  }

  // Function to show the login dialog
  private def showLoginDialog(): Unit = {
    val emailField = new TextField()
    val passwordField = new PasswordField()

    val formGrid = new GridPane {
      hgap = 15
      vgap = 15
      padding = Insets(10)

      add(new Text("Email:"), 1, 0)
      add(emailField, 1, 1)

      add(new Text("Password:"), 1, 2)
      add(passwordField, 1, 3)
      alignment = Pos.Center
    }

    val loginButton = new Button("Login")
    val backButton = new Button("Back")

    val buttonBox = new HBox(10, loginButton, backButton)
    buttonBox.padding = Insets(10)
    buttonBox.alignment = Pos.Center // Center align the buttons

    val root = new VBox(20, formGrid, buttonBox)
    root.padding = Insets(25)
    root.alignment = Pos.Center // Center align the entire root VBox
    root.setBackground(background)
    val loginScene = new Scene(root, 600, 400)

    stage.setScene(loginScene)
    // Event handler for login button
    loginButton.onAction = _ => {
      val email = emailField.text()
      val password = passwordField.text()

      val result: Future[Either[User, String]] = login.getUserByEmailAndPassword(email, password)

      result onComplete {
        case Success(Left(user)) =>
          Platform.runLater {
            if (user.userType == Admin) {
              adminMenu()
            } else {
              employeeMenu()
            }
          }
        case Success(Right(errorMessage)) =>
          Platform.runLater {
            val alert = new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Login Failed"
              headerText = "Login failed"
              contentText = errorMessage
            }
            alert.showAndWait()
          }

        case Failure(exception) =>
          Platform.runLater {
            val alert = new Alert(AlertType.Error) {
              title = "Login Failed"
              headerText = "Error occurred during login"
              contentText = s"Error: ${exception.getMessage}"
            }
            alert.showAndWait()
          }
      }
    }

    // Event handler for back button
    backButton.onAction = _ => stage.setScene(createSceneUser())

  }

  // Function to show the registration dialog
  private def showRegistrationDialog(): Unit = {

    val firstNameField = new TextField()
    val lastNameField = new TextField()
    val emailField = new TextField()
    val passwordField = new PasswordField()
    val confirmPasswordField = new PasswordField()

    val userTypeToggleGroup = new ToggleGroup
    val adminRadioButton = new RadioButton("Admin")
    adminRadioButton.setToggleGroup(userTypeToggleGroup)
    adminRadioButton.setSelected(true)
    val employeeRadioButton = new RadioButton("Employee")
    employeeRadioButton.setToggleGroup(userTypeToggleGroup)

    val formGrid = new GridPane {
      hgap = 15
      vgap = 15
      padding = Insets(10)

      add(new Text("First Name:"), 1, 2)
      add(firstNameField, 1, 3)

      add(new Text("Last Name:"), 1, 4)
      add(lastNameField, 1, 5)

      add(new Text("Email:"), 1, 6)
      add(emailField, 1, 7)

      add(new Text("Password:"), 1, 8)
      add(passwordField, 1, 9)

      add(new Text("Confirm Password:"), 1, 10)
      add(confirmPasswordField, 1, 11)

      add(new Text("User Type:"), 1, 12)
      add(adminRadioButton, 1, 13)
      add(employeeRadioButton, 1, 14)

      alignment = Pos.Center
    }

    val registerButton = new Button("Register")
    val backButton = new Button("Back")

    val buttonBox = new HBox(10, registerButton, backButton)
    buttonBox.padding = Insets(10)
    buttonBox.alignment = Pos.Center // Center align the buttons

    val root = new VBox(20, formGrid, buttonBox)
    root.padding = Insets(25)
    root.alignment = Pos.Center // Center align the entire root VBox
    root.setBackground(background)
    val registrationScene = new Scene(root, 600, 400)

    stage.setScene(registrationScene)
    // Event handler for register button
    registerButton.onAction = _ => {
      val firstName = firstNameField.text()
      val lastName = lastNameField.text()
      val email = emailField.text()
      val password = passwordField.text()
      val confirmPassword = confirmPasswordField.text()
      val userTypeOption = if (adminRadioButton.isSelected) Admin else Employee

      if (password == confirmPassword) {
        val user = User(UUID.randomUUID().toString, firstName, lastName, email, password, userTypeOption)
        val result = register.registration(user)

        result onComplete {
          case Success(_) =>
            Platform.runLater {
              val alert = new Alert(AlertType.Information) {
                initOwner(stage)
                title = "Registration Successful"
                headerText = "Registration successful"
                contentText = "User registered successfully"
              }
              alert.showAndWait()
            }

          case Failure(exception) =>
            Platform.runLater {
              val alert = new Alert(AlertType.Error) {
                title = "Registration Failed"
                headerText = "Error occurred during registration"
                contentText = s"Error: ${exception.getMessage}"
              }
              alert.showAndWait()
            }
        }
      } else {
        Platform.runLater {
          val alert = new Alert(AlertType.Error) {
            title = "Registration Failed"
            headerText = "Password mismatch"
            contentText = "Please make sure the passwords match."
          }
          alert.showAndWait()
        }
      }
    }

    // Event handler for back button
    backButton.onAction = _ => stage.setScene(createSceneUser())
  }

  // Function to display the admin menu
  private def adminMenu(): Unit = {
    val fetchUserByIdButton = new Button("Fetch User by ID")
    val getAllUsersButton = new Button("Fetch All Users")
    val deleteUserByIdButton = new Button("Delete User by ID")
    val updateUserByIdButton = new Button("Update User by ID")
    val logoutButton = new Button("Logout")

    fetchUserByIdButton.prefWidth = 200 // Increase button width
    getAllUsersButton.prefWidth = 200
    deleteUserByIdButton.prefWidth = 200
    updateUserByIdButton.prefWidth = 200
    logoutButton.prefWidth = 200

    val buttonBox = new VBox(10, fetchUserByIdButton, getAllUsersButton, updateUserByIdButton, deleteUserByIdButton, logoutButton)
    buttonBox.padding = Insets(10)
    buttonBox.alignment = Pos.Center

    val root = new VBox(20, buttonBox)
    root.padding = Insets(25)
    root.alignment = Pos.Center
    root.setBackground(background)

    val adminMenuScene = new Scene(root, 800, 600) // Set scene dimensions

    stage.setScene(adminMenuScene)

    // Event handler for fetchUserByIdButton
    fetchUserByIdButton.onAction = _ => {
        val idDialog = new TextInputDialog() {
          initOwner(stage)
          title = "Fetch User by ID"
          headerText = "Enter the User ID:"
        }
        val idResult = idDialog.showAndWait()

        idResult match {
          case Some(userID) =>
            val result = userServiceObject.getUserById(userID)
            result onComplete {
              case Success(user) =>
                Platform.runLater {
                  val alert = new Alert(AlertType.Information) {
                    initOwner(stage)
                    title = "User Details"
                    headerText = "User Details"
                    contentText = s"User ID: ${user.left.get.userId}\nFirst Name: ${user.left.get.firstName}\nLast Name: ${user.left.get.lastName}\nEmail: ${user.left.get.email}\nUser Type: ${user.left.get.userType}"
                  }
                  alert.showAndWait()
                }
              case Failure(exception) =>
                Platform.runLater {
                  val alert = new Alert(AlertType.Error) {
                    initOwner(stage)
                    title = "Error"
                    headerText = "Error"
                    contentText = exception.getMessage
                  }
                  alert.showAndWait()
                }
            }
          case None =>
          // No input provided
        }
      }

    // Event handler for getAllUsersButton
    getAllUsersButton.onAction = _ => {
      val result = userServiceObject.getAllUser
      result onComplete {
        case Success(users) =>
          Platform.runLater {
            val alert = new Alert(AlertType.Information) {
              initOwner(stage)
              title = "Fetch All Users"
              headerText = "All Users"
              contentText = users.left.get.mkString("\n")
            }
            val dialogPane = alert.getDialogPane
            dialogPane.setPrefWidth(800)
            dialogPane.setPrefHeight(600)
            alert.showAndWait()
          }
        case Failure(exception) =>
          Platform.runLater {
            val alert = new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Error"
              headerText = "Error"
              contentText = exception.getMessage
            }
            alert.showAndWait()
          }
      }
    }
    updateUserByIdButton.onAction = _ => {

      // Create UI components
      val idField = new TextField()
      val firstNameField = new TextField()
      val lastNameField = new TextField()
      val emailField = new TextField()
      val passwordField = new PasswordField()
      val userTypeGroup = new ToggleGroup()
      val adminRadioButton = new RadioButton("Admin")
      val employeeRadioButton = new RadioButton("Employee")
      adminRadioButton.toggleGroup = userTypeGroup
      employeeRadioButton.toggleGroup = userTypeGroup

      adminRadioButton.disable = true
      employeeRadioButton.disable = true

      // Initially deselect the user type buttons
      userTypeGroup.selectToggle(null)

      val titleText = new Text("Update User") {
        font = Font.font("Verdana", FontWeight.Bold, 20)
        textAlignment = TextAlignment.Center
      }

      val formGrid = new GridPane {
        hgap = 15
        vgap = 15
        padding = Insets(10)
        alignment = Pos.Center

        val userTypeLabel = new Text("User Type:")
        GridPane.setConstraints(userTypeLabel, 0, 4)

        val userTypeBox = new HBox(10, adminRadioButton, employeeRadioButton)
        GridPane.setConstraints(userTypeBox, 1, 4)

        add(new Text("UserId: "), 0, 0)
        add(idField, 1, 0)
        add(new Text("First Name:"), 0, 1)
        add(firstNameField, 1, 1)
        add(new Text("Last Name:"), 0, 2)
        add(lastNameField, 1, 2)
        add(new Text("Email:"), 0, 3)
        add(emailField, 1, 3)
        add(new Text("Password:"), 0, 4)
        add(passwordField, 1, 4)
        add(userTypeLabel, 0, 5)
        add(userTypeBox, 1, 5)
      }

      def stringToUserType(userTypeString: String): UserType = {
        userTypeString match {
          case "Admin" => UserType.Admin
          case "Employee" => UserType.Employee
          case _ => throw new IllegalArgumentException(s"Invalid user type: $userTypeString")
        }
      }

      val updateButton = new Button("Update")
      val backButton = new Button("Back")
      backButton.onAction = _ => {
        stage.setScene(adminMenuScene)
      }
      updateButton.onAction = _ => {
        val id = idField.text()
        val firstName = firstNameField.text()
        val lastName = lastNameField.text()
        val email = emailField.text()
        val password = passwordField.text()
        val userType = if (adminRadioButton.isSelected) "Admin" else "Employee"

        if (id.nonEmpty && firstName.nonEmpty && lastName.nonEmpty && email.nonEmpty && password.nonEmpty) {
          val updatedUser = User(id, firstName, lastName, email, password, stringToUserType(userType))

          userServiceObject.updateUser(updatedUser).onComplete {
            case Success(_) =>
              Platform.runLater {
                val successAlert = new Alert(AlertType.Information) {
                  initOwner(stage)
                  title = "Update"
                  headerText = "Updation Status"
                  contentText = s"User updated successfully with details:\n\n$updatedUser"
                }
                successAlert.showAndWait()
                idField.clear()
                firstNameField.clear()
                lastNameField.clear()
                emailField.clear()
                passwordField.clear()
                userTypeGroup.selectToggle(null)
              }
            case Failure(exception) =>
              Platform.runLater {
                val failureAlert = new Alert(AlertType.Error) {
                  initOwner(stage)
                  title = "Updation Error"
                  headerText = "Update Failed"
                  contentText = exception.getMessage
                }
                failureAlert.showAndWait()
              }
          }
        } else {
          val errorAlert = new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Invalid Input"
            headerText = "Invalid Input"
            contentText = "Please provide all the details."
          }
          errorAlert.showAndWait()
        }
      }



      val buttonBox = new HBox(10, updateButton, backButton)
      buttonBox.alignment = Pos.Center

      val backgroundImage = new Image("finger-pressing-social-network-button-touch-screen.jpg")
      val backgroundImageView = new ImageView(backgroundImage) {
        fitWidth = stage.width.value
        fitHeight = stage.height.value
        preserveRatio = true
      }

      val backgroundSize = new BackgroundSize(BackgroundSize.Auto, BackgroundSize.Auto, false, false, true, true)
      val backgroundPic = new Background(Array(new BackgroundImage(backgroundImage, BackgroundRepeat.NoRepeat, BackgroundRepeat.NoRepeat, BackgroundPosition.Center, backgroundSize)))
      val sceneLayout = new VBox(20, titleText, formGrid, buttonBox) {
        alignment = Pos.Center
        background = backgroundPic

      }

      val scene = new Scene(sceneLayout, 400, 400)
      stage.setScene(scene)
    }



    // Event handler for deleteUserByIdButton
    deleteUserByIdButton.onAction = _ => {
      val idDialog = new TextInputDialog() {
        initOwner(stage)
        title = "Delete User by ID"
        headerText = "Enter the User ID:"
      }
      val idResult = idDialog.showAndWait()

      idResult match {
        case Some(userID) =>
          val result = userServiceObject.delete(userID)
          result onComplete {
            case Success(_) =>
              Platform.runLater {
                val alert = new Alert(AlertType.Information) {
                  initOwner(stage)
                  title = "User Deleted"
                  headerText = "User Deleted"
                  contentText = "User deleted successfully"
                }
                alert.showAndWait()
              }
            case Failure(exception) =>
              Platform.runLater {
                val alert = new Alert(AlertType.Error) {
                  initOwner(stage)
                  title = "Error"
                  headerText = "Error"
                  contentText = exception.getMessage
                }
                alert.showAndWait()
              }
          }
        case None =>
        // No input provided
      }
    }

    // Event handler for back button
    logoutButton.onAction = _ => stage.setScene(createSceneUser())
  }

  // Function to display the employee menu
  private def employeeMenu(): Unit = {
    val fetchUserByIdButton = new Button("Fetch User by ID")
    val getAllUsersButton = new Button("Fetch All Users")
    val logoutButton = new Button("Logout")

    fetchUserByIdButton.prefWidth = 200 // Increase button width
    getAllUsersButton.prefWidth = 200
    logoutButton.prefWidth = 200

    val buttonBox = new VBox(10, fetchUserByIdButton, getAllUsersButton, logoutButton)
    buttonBox.padding = Insets(10)
    buttonBox.alignment = Pos.Center

    val root = new VBox(20, buttonBox)
    root.padding = Insets(25)
    root.alignment = Pos.Center
    root.setBackground(background)

    val employeeMenuScene = new Scene(root, 400, 200)

    stage.setScene(employeeMenuScene)

    // Event handler for fetchUserByIdButton
    fetchUserByIdButton.onAction = _ => {
      val idDialog = new TextInputDialog() {
        initOwner(stage)
        title = "Fetch User by ID"
        headerText = "Enter the User ID:"
      }
      val idResult = idDialog.showAndWait()

      idResult match {
        case Some(userID) =>
          val result = userServiceObject.getUserById(userID)
          result onComplete {
            case Success(user) =>
              Platform.runLater {
                val alert = new Alert(AlertType.Information) {
                  initOwner(stage)
                  title = "User Details"
                  headerText = "User Details"
                  contentText = s"User ID: ${user.left.get.userId}\nFirst Name: ${user.left.get.firstName}\nLast Name: ${user.left.get.lastName}\nEmail: ${user.left.get.email}\nUser Type: ${user.left.get.userType}"
                }
                alert.showAndWait()
              }
            case Failure(exception) =>
              Platform.runLater {
                val alert = new Alert(AlertType.Error) {
                  initOwner(stage)
                  title = "Error"
                  headerText = "Error"
                  contentText = exception.getMessage
                }
                alert.showAndWait()
              }
          }
        case None =>
        // No input provided
      }
    }

    // Event handler for getAllUsersButton
    getAllUsersButton.onAction = _ => {
      val result = userServiceObject.getAllUser
      result onComplete {
        case Success(users) =>
          Platform.runLater {
            val alert = new Alert(AlertType.Information) {
              initOwner(stage)
              title = "Fetch All Users"
              headerText = "All Users"
              contentText = users.left.get.mkString("\n")
            }
            val dialogPane = alert.getDialogPane
            dialogPane.setPrefWidth(800)
            dialogPane.setPrefHeight(600)
            alert.showAndWait()
          }
        case Failure(exception) =>
          Platform.runLater {
            val alert = new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Error"
              headerText = "Error"
              contentText = exception.getMessage
            }
            alert.showAndWait()
          }
      }
    }

    // Event handler for back button
    logoutButton.onAction = _ => stage.setScene(createSceneUser())
  }
}
