package app.ui;

/**
 * Small bootstrap class used to launch the JavaFX {@link MainApp} from environments
 * that require a plain {@code public static void main} entry point.
 */
public class Launcher {

    /**
     * Creates a new {@code Launcher} instance.
     * <p>
     * Instances of this class are not normally needed; the {@code main}
     * method is used as the entry point.
     */
    public Launcher() {
        // no-op
    }

    /**
     * Delegates startup to {@link MainApp#main(String[])}.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        MainApp.main(args);
    }
}
