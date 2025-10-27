package app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            var url = getClass().getResource("/ui/main_view.fxml");
            System.out.println("FXML URL: " + url);
            if (url == null) throw new IllegalStateException("main_view.fxml not found on classpath");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main_view.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 700);
            stage.setTitle("Invexus DMS");
            stage.setScene(scene);
            stage.show();
        }
        catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
