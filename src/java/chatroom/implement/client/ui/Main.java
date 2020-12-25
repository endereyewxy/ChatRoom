package chatroom.implement.client.ui;

import chatroom.implement.client.Client;
import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main implements Initializable {
    @FXML
    private ListView<User> lstUser;
    @FXML
    private Menu           mnuChat;
    @FXML
    private Label          lblChat;
    @FXML
    private WebView        webChat;
    @FXML
    private TextField      txtChat;
    private Integer        usrChat = null;
    private Integer        selChat = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstUser.setCellFactory(userListView -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(user == null ? "" : user.getName());
            }
        });
        lstUser.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            final User selected = lstUser.getSelectionModel().getSelectedItem();
            actionSwitchChat(selected == null ? null : selected.getUuid(), selChat);
        });

        webChat.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED)
                webChat.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);");
        });
    }

    public void updateUserList(Integer sel, User[] userArray) {
        if (Objects.equals(sel, selChat)) {
            lstUser.getItems().clear();
            lstUser.getItems().addAll(userArray);
        }
    }

    public void updateChatList(Chat[] chats) {
        mnuChat.getItems().removeIf(item -> item instanceof RadioMenuItem);
        for (final Chat chat : chats)
            insertChatIntoMenu(chat);
    }

    public void insertChatIntoMenu(Chat chat) {
        final MenuItem item = new RadioMenuItem(chat.getName());
        item.setOnAction(event -> actionSwitchChat(usrChat, chat.getUuid()));
        item.setId(String.valueOf(chat.getUuid()));
        mnuChat.getItems().add(item);
    }

    public void removeChatFromMenu(Chat chat) {
        mnuChat.getItems()
               .removeIf(item -> item instanceof RadioMenuItem && item.getId().equals(String.valueOf(chat.getUuid())));
    }

    public void actionSwitchChat(Integer usr, Integer sel) {
        if (Objects.equals(usr, usrChat) && Objects.equals(sel, selChat))
            return;
        Platform.runLater(() -> {
            if (!Objects.equals(sel, selChat)) {
                if (sel == null)
                    updateUserList(null, Client.getInstance().getUsers().values().toArray(new User[0]));
                else
                    Client.getInstance().doWithSocket(socket -> socket.requestChatInfo(sel));
            }
            if (sel != null) {
                lblChat.setText(Client.getInstance().getMyChats().get(sel).getName());
            } else {
                lblChat.setText("");
            }
            usrChat = usr;
            selChat = sel;
        });
    }

    public void actionInitChat() {
        final ArrayList<User> list = new ArrayList<>(lstUser.getSelectionModel().getSelectedItems());
        list.removeIf(user -> user.getUuid() == Client.getInstance().getMyself());

        if (list.size() == 1) {
            UI.error("请选择至少一个初始会话成员");
            return;
        }

        Client.getInstance().doWithSocket(socket -> socket.requestInitChat(
                UI.input("请输入会话名称："),
                list.stream().map(User::getUuid).toArray(Integer[]::new)));
    }

    public void actionQuitChat() {
        if (selChat != null)
            Client.getInstance().doWithSocket(socket -> socket.requestQuitChat(selChat, Client.getInstance().getMyself()));
    }

    public void actionExitChat() {
        actionSwitchChat(usrChat, null);
    }
}
