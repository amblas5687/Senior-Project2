package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import application.AnnaMain;
import application.DBConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.MedModel;

public class ArchivedMedsController {

    @FXML
    private TableView<MedModel> archiveTable;
    @FXML
    private TableColumn<MedModel, String> mName;
    @FXML
    private TableColumn<MedModel, String> mDosage;
    @FXML
    private TableColumn<MedModel, String> mDate;
    @FXML
    private TableColumn<MedModel, String> mDoc;
    @FXML
    private TableColumn<MedModel, String> archiveDate;
    @FXML
    private TableColumn<MedModel, String> archiveReason;
    
    ObservableList<MedModel> archivedMeds = FXCollections.observableArrayList();
    
    @FXML
	private Button btnDetails;
    
    @FXML
    private Button btnSearch;
    
    @FXML
    private TextField searchTF;
	
    @FXML
    private ToggleButton currMed;

    @FXML
    private ToggleButton archMed;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private DatePicker DRPicker1;
    
    @FXML
    private DatePicker DRPicker2;
    
    @FXML
    private Label drpLabel;
    
	@FXML
    private ComboBox<String> searchOptions;
	
	@FXML
	private AnchorPane content_view;

	private URL toPane;
	private AnchorPane temp;
	
	private ToggleGroup state = new ToggleGroup();
	
	
	Connection conn = AnnaMain.con;
	
	public void initialize(){
		
		searchOptions.getItems().addAll("Name", "Date", "Date Range");
		
		//add to toggle group
		currMed.setToggleGroup(state);
		archMed.setToggleGroup(state);
		
		archMed.setSelected(true);
		
		//grabs all the archived meds
		grabMeds();
		
	}
	
    void setMeds(ObservableList<MedModel> queryList) {
    	
    	archivedMeds = queryList;
    	
    	mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
    	mDosage.setCellValueFactory(cellData -> cellData.getValue().getMedDosage());
    	mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
    	mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
    	archiveDate.setCellValueFactory(cellData -> cellData.getValue().getArchiveDate());
    	archiveReason.setCellValueFactory(cellData -> cellData.getValue().getArchiveReason());
    	
    	archiveTable.setItems(archivedMeds);
    	
    }
    
    
void grabMeds() {
    	
    	try {
		    	String medQ = "SELECT * FROM archivedMeds WHERE patientCode = ?";
		    	PreparedStatement arcMedsPS = conn.prepareStatement(medQ);
		    	arcMedsPS.setString(1, LoginController.currentPatientID);
		    	ResultSet rs = arcMedsPS.executeQuery();
		    	
		    	
		    	MedModel tempMed;
		    	String patientCode;
		    	String medName;
		    	String medDose;
		    	String doc;
		    	String purpose;
		    	String medDate;
		    	String medDetails;
		    	String archiveDate;
		    	String archiveReason;
		    	
		    	
		    while(rs.next())
		    	{
		    		patientCode = rs.getString("patientCode");
		    		medName = rs.getString("medName");
		    		medDate = rs.getString("prescribDate");
		    		medDetails = rs.getString("medDescript");
		    		doc = rs.getString("prescribDoc");
		    		medDose = rs.getString("medDosage");
		    		purpose = rs.getString("purpPresrcipt");
		    		archiveDate = rs.getString("dateArchived");
		    		archiveReason = rs.getString("archiveReason");
		    		
		    		tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDetails, null, null, archiveDate, archiveReason);
		    			    		
		    		archivedMeds.add(tempMed);	
		    		System.out.println("grabbing med-archive... " + tempMed);
		    	}
	    	}
    	catch (SQLException e) {
    		DBConfig.displayException(e);	
    		System.out.println("failed grab");
    	}
    	
    	
    	mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
    	mDosage.setCellValueFactory(cellData -> cellData.getValue().getMedDosage());
    	mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
    	mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
    	archiveDate.setCellValueFactory(cellData -> cellData.getValue().getArchiveDate());
    	archiveReason.setCellValueFactory(cellData -> cellData.getValue().getArchiveReason());
    	
    	archiveTable.setItems(archivedMeds);

    	
    }
    
	@FXML
	private void currentMed(ActionEvent event) {
		 
		 try {
			//Replace table_med's present display with the view of current med table
			 toPane = getClass().getResource("/view/CurrentMedsView.fxml"); 
	  		 temp = FXMLLoader.load(toPane);
			 content_view.getChildren().setAll(temp);
			 
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		 
	}
	
	//activates when the archived toggle button is selected
	@FXML
	private void archivedMed(ActionEvent event) {
		
		 archMed.setSelected(true);

		 try {
			//Replace table_med's present display with the view of archived med table
			 toPane = getClass().getResource("/view/ArchiveMedsView.fxml");
	  		 temp = FXMLLoader.load(toPane);
			 content_view.getChildren().setAll(temp);
			 
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		 
	}
	
    @FXML
    void clickMed(MouseEvent event) {
    	
    	MedModel selectedMed = archiveTable.getSelectionModel().getSelectedItem();
    	System.out.println(selectedMed);   
    	
    	if(selectedMed != null) {
	    	btnDetails.setDisable(false);
    	}
    }
	
	@FXML
	private void viewDetails(ActionEvent event) {
		
	}
	

    @FXML
    void options(ActionEvent event) {
    	String option = searchOptions.getValue();
    	if(option == "Date") {
    		searchTF.setVisible(false);
    		DRPicker1.setVisible(false);
    		DRPicker2.setVisible(false);
    		drpLabel.setVisible(false);
    		datePicker.setVisible(true);
    	} else if(option == "Date Range"){
    		searchTF.setVisible(false);
    		datePicker.setVisible(false);
    		DRPicker1.setVisible(true);
    		DRPicker2.setVisible(true);
    		drpLabel.setVisible(true);
    	} else {
    		DRPicker1.setVisible(false);
    		DRPicker2.setVisible(false);
    		drpLabel.setVisible(false);
    		datePicker.setVisible(false);
    		searchTF.setVisible(true);
    	}
    }
    
	@FXML
	private void searchMed(ActionEvent event) {
		
		String option = searchOptions.getValue();
		
    	if(option == "Name") {
    		
    	} else if(option == "Date") {
    		optionDate();
    	} else if(option == "Date Range") {
    		optionDateRange();
    	}
    	
    }
	
	void optionDate() {
		LocalDate dp = datePicker.getValue();
		Date date = java.sql.Date.valueOf(dp);
		
		String query = "SELECT * FROM archivedMeds WHERE prescribDate = ?";
		
    	try {
    		
    		PreparedStatement ps = conn.prepareStatement(query);
    		ps.setDate(1, date);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		archiveTable.getItems().clear();
    		
    		while(rs.next()) {
    			
    			MedModel tempMed;
    	    	String patientCode;
    	    	String medName;
    	    	String medDose;
    	    	String doc;
    	    	String purpose;
    	    	String medDate;
    	    	String medDetails;
    	    	String archiveDate;
    	    	String archiveReason;

    			patientCode = rs.getString("patientCode");
	    		medName = rs.getString("medName");
	    		medDate = rs.getString("prescribDate");
	    		medDetails = rs.getString("medDescript");
	    		doc = rs.getString("prescribDoc");
	    		medDose = rs.getString("medDosage");
	    		purpose = rs.getString("purpPresrcipt");
	    		archiveDate = rs.getString("dateArchived");
	    		archiveReason = rs.getString("archiveReason");
	    		
	    		tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDetails, null, null, archiveDate, archiveReason);
	    			    		
	    		archivedMeds.add(tempMed);	
    			
    		}
    		
    	} catch (SQLException ex) {
    		DBConfig.displayException(ex);
    	}

		mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
	   	mDosage.setCellValueFactory(cellData -> cellData.getValue().getMedDosage());
	   	mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
	   	mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
	   	archiveDate.setCellValueFactory(cellData -> cellData.getValue().getArchiveDate());
	    archiveReason.setCellValueFactory(cellData -> cellData.getValue().getArchiveReason());
    		
	    archiveTable.setItems(archivedMeds);
	}
	
	void optionDateRange() {
		
		LocalDate drp1 = DRPicker1.getValue();
		LocalDate drp2 = DRPicker2.getValue();
		Date date1 = java.sql.Date.valueOf(drp1);
		Date date2 = java.sql.Date.valueOf(drp2);
		
		String query = "SELECT * FROM archivedMeds WHERE prescribDate >= '" + date1 + "' AND prescribDate <= '" + date2 + "';";
		
    	try {
    		
    		PreparedStatement ps = conn.prepareStatement(query);  		
    		ResultSet rs = ps.executeQuery();
    		
    		archiveTable.getItems().clear();
    		
    		while(rs.next()) {

    			MedModel tempMed;
    	    	String patientCode;
    	    	String medName;
    	    	String medDose;
    	    	String doc;
    	    	String purpose;
    	    	String medDate;
    	    	String medDetails;
    	    	String archiveDate;
    	    	String archiveReason;

    			patientCode = rs.getString("patientCode");
	    		medName = rs.getString("medName");
	    		medDate = rs.getString("prescribDate");
	    		medDetails = rs.getString("medDescript");
	    		doc = rs.getString("prescribDoc");
	    		medDose = rs.getString("medDosage");
	    		purpose = rs.getString("purpPresrcipt");
	    		archiveDate = rs.getString("dateArchived");
	    		archiveReason = rs.getString("archiveReason");
	    		
	    		tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDetails, null, null, archiveDate, archiveReason);
	    			    		
	    		archivedMeds.add(tempMed);	
	    		
    		}
    		
    	} catch (SQLException ex) {
    		DBConfig.displayException(ex);
    	}
    	
		mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
	   	mDosage.setCellValueFactory(cellData -> cellData.getValue().getMedDosage());
	   	mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
	   	mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
	   	archiveDate.setCellValueFactory(cellData -> cellData.getValue().getArchiveDate());
	    archiveReason.setCellValueFactory(cellData -> cellData.getValue().getArchiveReason());
    		
	    archiveTable.setItems(archivedMeds);
	}

}
