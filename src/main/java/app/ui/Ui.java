
package app.ui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Window;

public final class Ui {
    private Ui() {}

    /** Show an error alert from any thread. Owner will be the focused window if available. */
    public static void error(String header, String message) {
        runAlert(Alert.AlertType.ERROR, "Error", header, message);
    }

    public static boolean confirm(Window owner, String title, String header, String message) {
        final boolean[] yes = {false};
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            if (owner != null) a.initOwner(owner);
            a.setTitle(title);
            a.setHeaderText(header);
            a.setContentText(message != null && !message.isBlank() ? message : "(no details)");
            yes[0] = a.showAndWait().filter(btn -> btn.getButtonData().isDefaultButton()).isPresent();
        });
        return yes[0];
    }

    private static void runAlert(Alert.AlertType type, String title, String header, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(type);
            // Try to attach to the currently focused window (if any)
            Window owner = null;
            try {
                owner = Window.getWindows().stream().filter(Window::isFocused).findFirst().orElse(null);
            } catch (Exception ignored) {}
            if (owner != null) a.initOwner(owner);

            a.setTitle(title);
            a.setHeaderText(header);
            a.setContentText(message != null && !message.isBlank() ? message : "(no details)");
            a.showAndWait();
        });
    }
}
