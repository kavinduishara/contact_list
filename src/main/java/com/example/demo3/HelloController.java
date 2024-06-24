package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController implements Initializable {
    @FXML
    protected ListView<String> contacts;
    protected List<String> list;
    @FXML
    protected TextField searchbar;

    @FXML
    protected void gotoaddcontact(ActionEvent event) throws IOException {
        FXMLLoader root = new FXMLLoader(HelloApplication.class.getResource("add_contact.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 660, 480);
        scene.getStylesheets().add(getClass().getResource("/com/example/demo3/add.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void edit(ActionEvent event) throws IOException {
        try {
            if (list==null){
                showMessage("select value to edit");
                return;
            }
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("add_contact.fxml"));
            Parent root = loader.load();
            Addcontacts addcontacts = loader.getController();
            String[] details = list.get(0).split(" ");
            addcontacts.set(details[0], details[1], details[2], details[3]);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 660, 480);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/com/example/demo3/add.css").toExternalForm());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void delete() {

        if(list!=null){
            list.forEach(line->{
                Pattern p=Pattern.compile("\\d{10}");
                Matcher m=p.matcher(line);
                if (m.find()){
                    Contactfile.deleteNode(m.group());
                    contacts.getItems().remove(line);
                }
            });
        }
    }
/*    private List<String> formatter(){
        List<String> list1=new ArrayList<>();
        Contactfile.read().forEach((key,value)->list1.add(key));
        int maxNameLength = list1.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        // StringBuilder to store the formatted output
        StringBuilder output = new StringBuilder();

        // Append header
        output.append(String.format("%-" + maxNameLength + "s\tPhone Number%n", "Name"));
        output.append("=".repeat(maxNameLength + 13)).append("\n");

        Contactfile.read().forEach((key,value)->{
            output.append(String.format("%-" + maxNameLength + "s\t%s%n", key, value));
            list1.add(String.valueOf(output));
        });
        return list1;
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AtomicReference<Map<String, String>> map= new AtomicReference<>(Contactfile.read());
        map.get().forEach((name, phone) -> contacts.getItems().add(name+" "+phone));
        contacts.getSelectionModel().selectedItemProperty().addListener((observableValue, string, t1) -> {
            list=contacts.getSelectionModel().getSelectedItems();
        });
        searchbar.textProperty().addListener((observableValue, string, t1) -> {
            contacts.getItems().clear();
            Pattern p=Pattern.compile(searchbar.getText());

            map.set(Contactfile.read());
            map.get().forEach((name, phone) -> {
                Matcher m=p.matcher(name+" "+phone);
                if(m.find()){
                    contacts.getItems().add(name+" "+phone);
                }
            });
        } );
    }
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}