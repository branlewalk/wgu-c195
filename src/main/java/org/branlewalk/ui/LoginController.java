package org.branlewalk.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    private ResourceBundle bundles = ResourceBundle.getBundle("language", Locale.getDefault());

    public LoginController() throws SQLException {
        String db = "U05vOU";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U05vOU";
        String pass = "53688621490";
        Connection connection = DriverManager.getConnection(url, user, pass);
        userDao = new UserDaoImpl(connection);
    }

    @FXML
    void handleSignInButtonAction(ActionEvent actionEvent) throws IOException {
        try {
            UserDTO userDTO = userDao.find(user.getText());
            if(userDTO == null) {
                loginMessage.setText(user.getText() + bundles.getString("key1"));
            } else {
                if(!userDTO.getPassword().equals(password.getText())) {
                    loginMessage.setText(bundles.getString("key2")+ user.getText());
                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                    Stage dashboard = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    dashboard.setTitle("Appoint - Dashboard");
                    dashboard.setScene(new Scene(root,800,750));
                    dashboard.show();
                }
            }
        } catch (SQLException e) {
            loginMessage.setText(bundles.getString("key3") + user.getText() + " " + e.getMessage());
        }
    }

    @FXML
    void handleCreateAccountButtonAction(ActionEvent actionEvent) {
        try {
            UserDTO userDTO = userDao.find(user.getText());
            if(userDTO != null) {
                loginMessage.setText(user.getText() + bundles.getString("key4"));
            } else {
                userDao.create(user.getText(), password.getText(), user.getText(), new Date(System.currentTimeMillis()));
                loginMessage.setText(bundles.getString("key5"));
            }
        } catch (SQLException e) {
            loginMessage.setText(bundles.getString("key3") + user.getText() + " " + e.getMessage());
        }
    }

}
