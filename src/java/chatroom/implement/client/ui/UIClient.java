package chatroom.implement.client.ui;

import chatroom.protocols.IClient;
import chatroom.protocols.IClientSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UIClient extends Application {
    private static IClient       client;
    private static IClientSocket socket;

    private Parent initWindow;
    private Parent mainWindow;

    private UIInit initController;
    private UIMain mainController;

    public static void launch(IClient client, IClientSocket socket) {
        UIClient.client = client;
        UIClient.socket = socket;
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            final FXMLLoader initLoader = new FXMLLoader(getClass().getResource("/ui/Init.fxml"));

            initWindow = initLoader.load();

            initController = initLoader.getController();
            initController.setSocket(socket);

            stage.setTitle("登录");
            stage.setScene(new Scene(initWindow));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
