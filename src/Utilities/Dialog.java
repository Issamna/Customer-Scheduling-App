package Utilities;

import javafx.scene.control.Alert;

public class Dialog {
    //information dialog
    public static void dialog(String type, String title,  String content){
        Alert alert;
        switch (type.toUpperCase()){
            case "ERROR":
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            default:
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
