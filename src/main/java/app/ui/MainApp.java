package app.ui;

import app.repository.ARepoMySQL;
import app.repository.AssetRepository;
import app.service.AssetManager;
import app.config.DB;                  // whatever class returns your DataSource


import app.ui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Application entry point. Boots the JavaFX stage and loads the main view.
 */
public class MainApp extends Application {

    /**
     * Creates a new JavaFX application instance for Invexus DMS.
     * <p>
     * The JavaFX runtime constructs this class when the application starts.
     */
    public MainApp() {
        // no-op
    }

    /**
     * Starts the primary stage and shows the main inventory screen.
     *
     * @param stage primary JavaFX stage provided by the runtime.
     * @throws Exception if the FXML cannot be loaded from the classpath
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Build the real, DB-backed pipeline once
        AssetRepository repo = new ARepoMySQL(DB.dataSource());
        AssetManager manager = new AssetManager(repo);

        FXMLLoader fx = new FXMLLoader(MainApp.class.getResource("/ui/main_view.fxml"));

        // Give JavaFX a way to construct controllers that need ctor args
        fx.setControllerFactory(type -> {
            if (type == MainController.class) return new MainController();
            try { return type.getDeclaredConstructor().newInstance(); }
            catch (Exception e) { throw new RuntimeException(e); }
        });

        Parent root = fx.load();
        stage.setTitle("Invexus DMS");
        stage.setScene(new Scene(root));
        stage.show();
    }


    /**
     * Launch the app
     * @param args this won't be used at this time
     */
    public static void main(String[] args) {
        launch(args);
    }
}
