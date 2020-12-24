package chatroom.implement.client.ui;

import chatroom.protocols.IClientSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class UIClient extends Application {
    private static IClientSocket socket;

    @SuppressWarnings("FieldCanBeLocal")
    private static Parent initWindow;
    private static Parent mainWindow;

    @SuppressWarnings("FieldCanBeLocal")
    private static UIInit initController;
    private static UIMain mainController;

    private static Stage primaryStage;

    public static void launch(IClientSocket socket) {
        UIClient.socket = socket;
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            final FXMLLoader initLoader = new FXMLLoader(getClass().getResource("/ui/Init.fxml"));
            final FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/ui/Main.fxml"));

            initWindow = initLoader.load();
            mainWindow = mainLoader.load();

            initController = initLoader.getController();
            initController.setSocket(socket);
            mainController = mainLoader.getController();
            mainController.setSocket(socket);

            primaryStage = stage;
            stage.setTitle("登录");
            stage.setScene(new Scene(initWindow));
            stage.setOnCloseRequest(windowEvent -> {
                try {
                    socket.closeSocket();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void replyUserList(User[] users) {
        Platform.runLater(() -> mainController.setUserList(users));
    }

    public static void replyChatList(Chat[] chats) {
        Platform.runLater(() -> mainController.setChatList(chats));
    }

    @SuppressWarnings("unused")
    public static void replyChatMemberList(User[] users) {
    }

    public static void notifySignInSucceeded(int userUuid) {
        if (userUuid > 0) {
            mainController.setMyUuid(userUuid);
            try {
                socket.acquireUserList();
                socket.acquireChatList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                primaryStage.setTitle("多人聊天系统");
                primaryStage.setScene(new Scene(mainWindow));
                primaryStage.show();
            });
        } else {
            Platform.runLater(() -> {
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setContentText("登录失败");
                alert.showAndWait();
            });
        }
    }

    public static void notifyChatJoinRequest(int userUuid, int chatUuid) {
        Platform.runLater(() -> mainController.notifyChatJoinRequest(userUuid, chatUuid));
    }

    public static void notifyMessageReceived(int userUuid, int chatUuid, String text) {
        Platform.runLater(() -> mainController.addHistory(userUuid, chatUuid, text));
    }
}
