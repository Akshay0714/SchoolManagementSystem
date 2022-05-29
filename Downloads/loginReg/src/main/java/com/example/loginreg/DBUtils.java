package com.example.loginreg;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Predicate;

public class DBUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username){
        Parent root = null;
        if (username != null){
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);
            } catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(Title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signup (ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-loginreg", "root", "toor");
            psCheckUserExists = connection.prepareStatement("Select * from users WHERE username = ? ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()){
                System.out.println("User already Exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot have this username");
                alert.show();
            }else{
                psInsert = connection.prepareStatement("INSERT into users (username, password, fav_uni VALUES (? ? ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, fav_uni);
                psInsert.executeUpdate();

                changeScene(event, "logged-in.fxml", "welcome", username);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null){
                    try {
                        resultSet.close();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                if (psCheckUserExists != null){
                    try{
                        psCheckUserExists.close();
                    } catch (SQLException e) {
                         e.printStackTrace();
                    }
                }
            }
        }
    }

    public  static  void logInUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-loginreg", "root", "toor");
            preparedStatement = connection.prepareStatement("SELECT password, fav_uni from users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()){
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedChannel = resultSet.getString("fav_uni");
                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "logged-in.fxml", "welcome", username);
                    } else {
                        System.out.println("Passwords did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLExecution e){
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try{
                    resultSet.close();
                } catch  (SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e){
                     e.printStackTrace();
                }
            }
        }



    }
}
