package chatroom.implement.client.ui;

import chatroom.implement.client.Client;
import chatroom.util.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;

public class UI extends Application {
    private Parent initWindow;
    private Parent mainWindow;

    private Init initController;
    private Main mainController;

    private Stage stage;

    public static void initializeGUI() {
        launch();
    }

    public Init getInitController() {
        return initController;
    }

    public Main getMainController() {
        return mainController;
    }

    @Override
    public void start(Stage primaryStage) {
        Client.getInstance().bind(this);
        try {
            final FXMLLoader initLoader = new FXMLLoader(getClass().getResource("/ui/Init.fxml"));
            final FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/ui/Main.fxml"));

            initWindow = initLoader.load();
            mainWindow = mainLoader.load();

            initController = initLoader.getController();
            mainController = mainLoader.getController();

            stage = primaryStage;
            stage.setOnCloseRequest(windowEvent -> Client.getInstance().getSocket().stop());
            switchToInit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.client("Cannot load UI materials, please check your programme integrity");
            Log.failed();
        }
    }

    public void switchToInit() {
        stage.setTitle("登录");
        stage.setScene(new Scene(initWindow));
        stage.show();
    }

    public void switchToMain() {
        stage.setScene(new Scene(mainWindow));
        stage.show();
    }

    public static String input(String msg) {
        final TextInputDialog input = new TextInputDialog();
        input.setHeaderText("");
        input.setContentText(msg);
        input.showAndWait();
        return input.getResult();
    }

    public static boolean confirm(String msg) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("");
        alert.setContentText(msg);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    public static void error(String msg) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void inform(String msg) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
