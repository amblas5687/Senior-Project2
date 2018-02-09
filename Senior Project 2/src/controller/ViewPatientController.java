package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ViewPatientController {

    @FXML
    private TextField fnameTF;
    @FXML
    private TextField lnameTF;
    @FXML
    private DatePicker DOBPicker;
    @FXML
    private PasswordField doctorTF;
    @FXML
    private Button submitBTN;
    @FXML
    private Button editBTN;
    @FXML
    private Button cancelBTN;
    @FXML
    private ComboBox<String> stageBox;
    @FXML
    private DatePicker diagnosesPicker;
    @FXML
    private TextField cargiverTF;
   
    
    public void initialize(){
    	
    	//add combo box selections
    	stageBox.getItems().addAll("Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5", "Stage 6", "Stage 7");
    	stageBox.setDisable(true);
    }
    
    
    @FXML
    void edit(ActionEvent event) {
    	fnameTF.setEditable(true);;
    	lnameTF.setEditable(true);
    	DOBPicker.setEditable(true);
    	doctorTF.setEditable(true);
    	stageBox.setDisable(false);
    	diagnosesPicker.setDisable(false);
    	cargiverTF.setEditable(true);;
    }

    @FXML
    void returnMain(ActionEvent event) {

    }

    @FXML
    void submit(ActionEvent event) {

    }

}
