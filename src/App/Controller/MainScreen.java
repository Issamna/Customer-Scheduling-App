package App.Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
4/27/2020
*/


import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.stage.Stage;



public class MainScreen {
    private Stage stage;
    private Parent scene;

    public void onCustSearch(ActionEvent event) {
    }

    public void onCusClear(ActionEvent event) {
    }

    public void onCustNew(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/Customer.fxml"));
            stage.setTitle("Customer Schedule | Add New Customer");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void onCustEdit(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/Customer.fxml"));
            stage.setTitle("Customer Schedule | Edit Customer");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void onCustDelete(ActionEvent event) {
    }

    public void onApptSearch(ActionEvent event) {
    }

    public void onApptClear(ActionEvent event) {
    }

    public void onApptNew(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/Appointment.fxml"));
            stage.setTitle("Customer Schedule | Add New Appointment");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void onApptEdit(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/Appointment.fxml"));
            stage.setTitle("Customer Schedule | Edit Appointment");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void onApptDelete(ActionEvent event) {
    }


    public void onShowReport(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/Report.fxml"));
            stage.setTitle("Customer Schedule | Add New Report");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void onApptAll(ActionEvent event) {
    }
    public void onApptWeek(ActionEvent event) {
    }

    public void onApptMonth(ActionEvent event) {
    }


    public void onExit(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

}
