package chatroom.implement.client.ui;

import chatroom.protocols.IClientSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UIMain implements Initializable {
    private IClientSocket socket;

    int  myUuid;
    User myUser;

    @FXML
    private WebView        chat;
    @FXML
    private ListView<User> userView;
    @FXML
    private ListView<Chat> chatView;

    @FXML
    private Button btnCreateChat;

    ObservableList<User> taskCreateChatMembers = null;

    public void setSocket(IClientSocket client) {
        this.socket = client;
    }

    public void setMyUuid(int myUuid) {
        this.myUuid = myUuid;
    }

    public void setUserList(User[] users) {
        final ObservableList<User> list = userView.getItems();
        list.clear();
        list.addAll(users);

        for (final User user : users) {
            if (myUuid == user.getUuid())
                myUser = user;
        }
    }

    public void setChatList(Chat[] chats) {
        final ObservableList<Chat> list = chatView.getItems();
        list.clear();
        list.addAll(chats);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userView.setCellFactory(userListView -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(user == null ? "" : user.getUsername());
            }
        });
        userView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        chatView.setCellFactory(chatListView -> new ListCell<Chat>() {
            @Override
            protected void updateItem(Chat chat, boolean empty) {
                super.updateItem(chat, empty);
                setText(chat == null ? "" : chat.getName());
            }
        });
    }

    @SuppressWarnings("unused")
    public void createChat(ActionEvent actionEvent) {
        final ObservableList<User> list = userView.getSelectionModel().getSelectedItems();
        list.removeIf(user -> user.getUuid() == myUuid);

        if (list.size() < 1)
            return;

        final String name;
        if (list.size() > 1) {
            final TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("新建群组名称");
            inputDialog.show();
            name = inputDialog.getContentText();
        } else {
            name = myUser.getUsername() + " - " + list.get(0).getUsername();
        }

        btnCreateChat.setText("正在创建……");
        btnCreateChat.setDisable(true);
        try {
            socket.requestCreateChat(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
