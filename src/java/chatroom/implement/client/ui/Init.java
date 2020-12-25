package chatroom.implement.client.ui;

import chatroom.implement.client.Client;
import chatroom.util.MD5;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Init {
    @FXML
    private TextField     txtUsername;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private void actionSignIn() {
        final String name = txtUsername.getText();
        final String pass = txtPassword.getText();
        if (!name.isEmpty())
            Client.getInstance().doWithSocket(socket -> socket.requestSignIn(name, MD5.md5(pass)));
    }
}
