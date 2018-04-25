package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.MedModel;

public class CurMedDetailsController {

    @FXML
    private AnchorPane content_view;

    @FXML
    private TextField medName;

    @FXML
    private TextField medDosage;

    @FXML
    private TextArea medDescript;

    @FXML
    private TextField prescribDoc;

    @FXML
    private TextField purpOfPrescript;

    @FXML
    private DatePicker DOPPicker;

    @FXML
    private DatePicker LUPicker;

    @FXML
    private Button btnOK;
    
    @FXML
    private TextField addByTF;

    @FXML
    private TextField editByTF;
    
	private MedModel medDetails;


    
    public void initialize(){
    	
		System.out.println("*******VIEW CURRENT DETAILS*******");
    	
    	CurrentMedsController test = new CurrentMedsController();
		medDetails = test.getDetail();
		//System.out.println("SETTING MED DETAILS FOR... " + medDetails);
		getDetails();	        
    }
    
    
    private void getDetails()
    {
    	medName.setText(medDetails.getMedName().get());
    	
    	//concatenate dose and type
    	String dosage = medDetails.getMedDosage().get() + " " + medDetails.getDoseType().get();
    	
		medDosage.setText(dosage);
    	medDescript.setText(medDetails.getDetails().get());
    	prescribDoc.setText(medDetails.getDoc().get());
    	purpOfPrescript.setText(medDetails.getPurpose().get());
    	
    	//setting the datepicker DOP
    	String prescribDateString = medDetails.getDate().get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	LocalDate prescribDate = LocalDate.parse(prescribDateString, formatter);
    	DOPPicker.setValue(prescribDate);
    	
    	//setting the datepicker DOP
    	String updateDateString = medDetails.getDateUpdated().get();
    	if (updateDateString !=null)
    	{
	    	LocalDate updateDate = LocalDate.parse(updateDateString, formatter);
	    	LUPicker.setValue(updateDate);
    	}
    	addByTF.setText(medDetails.getAddedBy().get());
    	editByTF.setText(medDetails.getUpdatedBy().get());
    	
    }
    
    @FXML
    void stopViewing(ActionEvent event) {
    	
    	System.out.println("CLOSING CURRENT DETAIL WINDOW...");
    	
    	Stage detailStage = (Stage)btnOK.getScene().getWindow();
        // do what you have to do
    	detailStage.close();

    }

}
