package org.branlewalk.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.branlewalk.dao.UserDAO;
import org.branlewalk.dao.UserDaoImpl;
import org.branlewalk.dto.UserDTO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private Text loginMessage;
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    private final UserDAO userDao;
    private ResourceBundle languageBundle;
    static String username;

    public LoginController() throws SQLException {
        userDao = new UserDaoImpl(Main.connection());
        // Using bundle/resources to change languages based on system's Locale
        languageBundle = ResourceBundle.getBundle("language", Locale.getDefault());
    }

    @FXML
    void handleSignInButtonAction(ActionEvent actionEvent) throws IOException {
        try {
            UserDTO userDTO = userDao.find(user.getText());
            if(userDTO == null) {
                loginMessage.setText(user.getText() + languageBundle.getString("not_found"));
            } else {
                if(!userDTO.getPassword().equals(password.getText())) {
                    loginMessage.setText(languageBundle.getString("wrong_pw")+ user.getText());
                } else {
                    username = userDTO.getName();
                    logUserLogin();
                    Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                    Stage dashboard = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    dashboard.setTitle("Appoint - Dashboard");
                    dashboard.setScene(new Scene(root,800,750));
                    dashboard.show();
                }
            }
        } catch (SQLException e) {
            loginMessage.setText(languageBundle.getString("sql_error") + user.getText() + " " + e.getMessage());
        }
    }

    private void logUserLogin() throws IOException {
        File file = new File("userlog.txt");
        FileWriter fw = new FileWriter(file, true);
        try(BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("User: %s logged in @ %s ", username, LocalDateTime.now()));
            bw.newLine();
        }
    }

    @FXML
    void handleCreateAccountButtonAction(ActionEvent actionEvent) {
        try {
            UserDTO userDTO = userDao.find(user.getText());
            if(userDTO != null) {
                loginMessage.setText(user.getText() + languageBundle.getString("exists"));
            } else {
                userDao.create(user.getText(), password.getText());
                loginMessage.setText(languageBundle.getString("success"));

            }
        } catch (SQLException e) {
            loginMessage.setText(languageBundle.getString("sql_error") + user.getText() + " " + e.getMessage());
        }
    }

}
