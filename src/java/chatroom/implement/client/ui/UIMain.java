package chatroom.implement.client.ui;

import chatroom.protocols.IClientSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class UIMain implements Initializable {
    private IClientSocket socket;
    private int           myUuid;

    @FXML
    private WebView        chat;
    @FXML
    private TextArea       text;
    @FXML
    private ListView<User> userView;
    @FXML
    private ListView<Chat> chatView;

    private final HashMap<Integer, User> userMap = new HashMap<>();

    private final HashMap<Integer, LinkedList<Pair<String, String>>> history = new HashMap<>();

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

        userMap.clear();
        for (final User user : users)
            userMap.put(user.getUuid(), user);
    }

    public void setChatList(Chat[] chats) {
        final ObservableList<Chat> list = chatView.getItems();
        list.clear();
        list.addAll(chats);

        for (final Chat chat : chats)
            history.putIfAbsent(chat.getUuid(), new LinkedList<>());

        final Set<Integer> visited = Arrays.stream(chats).map(Chat::getUuid).collect(Collectors.toSet());
        for (final int key : history.keySet()) {
            if (!visited.contains(key))
                history.remove(key);
        }
    }

    public void addHistory(int userUuid, int chatUuid, String text) {
        history.get(chatUuid).add(new Pair<>(userMap.get(userUuid).getUsername(), text));

        final Chat chatObj = chatView.getSelectionModel().getSelectedItem();
        if (chatObj != null && chatObj.getUuid() == chatUuid)
            chatChanged(null);
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
        chat.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED)
                chat.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);");
        });
    }

    @SuppressWarnings("unused")
    public void createChat(ActionEvent actionEvent) {
        final ArrayList<User> list = new ArrayList<>(userView.getSelectionModel().getSelectedItems());
        list.removeIf(user -> user.getUuid() == myUuid);

        if (list.isEmpty())
            return;

        final TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("新建会话名称");
        inputDialog.show();

        try {
            socket.requestCreateChat(inputDialog.getContentText(), list.toArray(new User[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void sendMessage(ActionEvent actionEvent) {
        try {
            final Chat chat = chatView.getSelectionModel().getSelectedItem();
            if (chat != null && !text.getText().isEmpty())
                socket.requestSendMessage(chat.getUuid(), text.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void chatChanged(MouseEvent mouseEvent) {
        final Chat chatObj = chatView.getSelectionModel().getSelectedItem();
        if (chatObj != null) {
            final StringBuilder builder = new StringBuilder();
            for (final Pair<String, String> line : history.get(chatObj.getUuid()))
                builder.append("<p>")
                       .append(line.getKey())
                       .append("</p><p>")
                       .append(line.getValue())
                       .append("<p>");
            chat.getEngine().loadContent(builder.toString());
        }
    }

    @SuppressWarnings("unused")
    public void quitChat(ActionEvent actionEvent) {
        try {
            final Chat chatObj = chatView.getSelectionModel().getSelectedItem();
            if (chatObj != null)
                socket.requestQuitChat(myUuid, chatObj.getUuid());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void joinChat(ActionEvent actionEvent) {
        try {
            final Chat chatObj = chatView.getSelectionModel().getSelectedItem();
            if (chatObj != null)
                socket.requestJoinChat(myUuid, chatObj.getUuid());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
