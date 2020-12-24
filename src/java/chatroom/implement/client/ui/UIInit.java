package chatroom.implement.client.ui;

import chatroom.protocols.IClientSocket;
import chatroom.util.MD5;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UIInit {
    private IClientSocket socket;

    @FXML
    private TextField     username;
    @FXML
    private PasswordField password;

    public void setSocket(IClientSocket client) {
        this.socket = client;
    }

    @SuppressWarnings("unused")
    public void signIn(ActionEvent actionEvent) throws IOException {
        socket.requestSignIn(username.getText(), MD5.md5(password.getText()));
    }
}
