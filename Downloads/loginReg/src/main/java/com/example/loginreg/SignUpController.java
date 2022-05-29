package com.example.loginreg;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button button_signup;

    @FXML
    private  Button button_log_in;

    @FXML
    private RadioButton rb_yes;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;

    @Override
    public void initialze(URL location, ResourceBundle resources){
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_yes.setToggleGroup(toggleGroup);

        rb_yes.setSelected(true);

        button_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    DBUtils.signupUser(event, tf_username.getText(), tf_password.getText(), toggleName);
                }else{
                    System.out.println("Please fill in all the information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the information to sign up!");
                    alert.show();
                }
            }
        });

        button_log_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(event, "sample.fxml", "Log in!", null);

            }
        });
    }
}
