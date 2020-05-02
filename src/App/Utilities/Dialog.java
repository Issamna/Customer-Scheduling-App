package App.Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Dialog class to facilitate alerts through out the program
 */
public class Dialog {

    /**
     * Error or information alert
     * @param type alert type
     * @param title
     * @param content
     */
    public static void dialog(String type, String title,  String content){
        Alert alert;
        if ("ERROR".equals(type.toUpperCase())) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Confirmation dialog
     * @param title
     * @param content
     * @return user confirmation, true or false
     */
    public static boolean confirmationDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }
}
