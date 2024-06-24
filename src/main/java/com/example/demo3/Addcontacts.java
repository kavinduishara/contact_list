package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Addcontacts implements Initializable {

    @FXML
    protected TextField lname;

    @FXML
    protected TextField fname;

    @FXML
    protected TextField mname;

    @FXML
    protected TextField phonenumber;
    protected String oldnumber = "";

    @FXML
    protected Button editbtn;
    @FXML
    protected Button addbtn;
    @FXML
    protected void set(String fName, String mName, String lName, String pnumber) {
        fname.setText(fName);
        mname.setText(mName);
        lname.setText(lName);
        phonenumber.setText(pnumber);
        oldnumber = pnumber;
    }

    @FXML
    protected void editer() {
        String first = fname.getText();
        String middle = mname.getText();
        String last = lname.getText();
        String number = phonenumber.getText();

        Pattern p = Pattern.compile("\\d{10}");
        Matcher m = p.matcher(number);

        if (m.matches()) {
            Contactfile.editNode(oldnumber, first, middle, last, number);
            showMessage("Contact edited");
        } else {
            showMessage("The number should contain 10 digits only");
        }
    }

    @FXML
    protected void mainscene(ActionEvent event) throws IOException {
        FXMLLoader root = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 660, 480);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/com/example/demo3/hello.css").toExternalForm());
        stage.show();
    }

    @FXML
    protected void addcontact() {
        if(!oldnumber.isEmpty()){
            editer();
            return;
        }
        String first = fname.getText();
        String middle = mname.getText();
        String last = lname.getText();
        String number = phonenumber.getText();

        Pattern p = Pattern.compile("\\d{10}");
        Matcher m = p.matcher(number);

        if (Contactfile.read().containsValue(number)) {
            showMessage("The number exists in the list");
        } else if (Contactfile.read().containsKey(first + " " + middle + " " + last)) {
            showMessage("The name exists in the list");
        } else if (m.matches()) {
            Contactfile.write(first, middle, last, number);
            showMessage("Contact saved");
        } else {
            showMessage("The number should contain 10 digits only");
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
