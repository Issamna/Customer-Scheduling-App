package App.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Appointment {
    public void onCustSearch(ActionEvent event) {
    }

    public void onCusClear(ActionEvent event) {
    }

    public void onSave(ActionEvent event) {
        returnMain(event);
    }

    public void onCancel(ActionEvent event) {
        returnMain(event);
    }

    private void returnMain(ActionEvent event){
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/App/View/MainScreen.fxml"));
            stage.setTitle("Customer Schedule");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
