package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.MedModel;

public class CurrentMedsController {
	
	//code for testing
	Connection conn = AnnaMain.con;


    @FXML
    private TableView<MedModel> medicationTable;
    @FXML
    private TableColumn<MedModel, String> mName;
    @FXML
    private TableColumn<MedModel, String> mDosage;
    @FXML
    private TableColumn<MedModel, String> mDate;
    @FXML
    private TableColumn<MedModel, String> mDoc;
    
    ObservableList<MedModel> patientMeds = FXCollections.observableArrayList();
    
    @FXML
	private Button btnAdd;
	
	@FXML
	private Button btnDetails;
	
	@FXML
	private Button btnArchive;
	
	@FXML
	private Button btnEdit;

    @FXML
    private Button btnSearch;
    
    @FXML
    private TextField searchTF;
	
    @FXML
    private ToggleButton currMed;

    @FXML
    private ToggleButton archMed;
    
    @FXML
    private DatePicker DRPicker;
    
	@FXML
    private ComboBox<String> searchOptions;
	@FXML
	private AnchorPane content_view;

	private URL toPane;
	private AnchorPane temp;
	
	private ToggleGroup state = new ToggleGroup();
    
    public void initialize(){
    	grabMeds();
    	
    	searchOptions.getItems().addAll("Name", "Date", "Date Range");
    	
    	//add to toggle group
		currMed.setToggleGroup(state);
		archMed.setToggleGroup(state);
		 
		currMed.setSelected(true);
		
    	//adds listener to table
    	/*medicationTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MedModel>() {
    	    @Override
    	    public void changed(ObservableValue<? extends MedModel> observable,
    	    		MedModel oldValue, MedModel newValue) {

    	        System.out.println("ListView Selection Changed (newValue: " + newValue.getMedName() + ")\n");
    	    }
    	});*/
    }
    
    @FXML
    void clickMed(MouseEvent event) {
    	
    	MedModel selectedMed = medicationTable.getSelectionModel().getSelectedItem();
    	System.out.println("selected" + selectedMed);   

    	System.out.println(selectedMed);   
    	
    	if(selectedMed != null) {
	    	btnDetails.setDisable(false);
	    	btnArchive.setDisable(false);
	    	btnEdit.setDisable(false);
    	}
    }
    
    void grabMeds() {
    	
    	try {
		    	String medQ = "SELECT * FROM currentMeds WHERE patientCode = ?";
		    	PreparedStatement curMedPS = conn.prepareStatement(medQ);
		    	curMedPS.setString(1, MainViewController.currentPatientID);
		    	ResultSet rs = curMedPS.executeQuery();
		    	
		    	
		    	MedModel tempMed;
		    	String patientCode;
		    	String medName;
		    	String medDate;
		    	String medDetails;
		    	String doc;
		    	String medDose;
		    	String purpose;
		    	String dateAdded;
		    	
		    	
		    	
		    while(rs.next())
		    	{
		    		patientCode = rs.getString("patientCode");
		    		medName = rs.getString("medName");
		    		medDate = rs.getString("prescribDate");
		    		medDetails = rs.getString("medDescript");
		    		doc = rs.getString("prescribDoc");
		    		//System.out.println(doc);
		    		medDose = rs.getString("medDosage");
		    		purpose = rs.getString("purpPresrcipt");
		    		dateAdded = rs.getString("dateAdded");
		    		
		    		
		    		tempMed = new MedModel(patientCode, medName, medDate, doc, medDetails, medDose, purpose, dateAdded);
		    		patientMeds.add(tempMed);	
		    		System.out.println("grabbing med... " + tempMed);
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
    	//listView.setItems(patientMeds);
    	medicationTable.setItems(patientMeds);

    	
    }
    
	@FXML
	private void currentMed(ActionEvent event) {
		
		 currMed.setSelected(true);
		
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
	private void searchMed(ActionEvent event) {

    }

	
	@FXML
	private void addMed(ActionEvent event) {
		
		try {
			//Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/NewMedView.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@FXML
	private void viewDetails(ActionEvent event) {
		
	}
	
	//activates when the archive button is selected
	@FXML
	private void archiveMed(ActionEvent event) {
		
		//get selected med
		MedModel moveMed = medicationTable.getSelectionModel().getSelectedItem();
		System.out.println("Med to move..." + moveMed);
		
		
		try {
				
				//********************************
				//MOVE MED TO ARCHIVE TABLE
				//********************************

				//grab values to insert into archive
				String patientCode = moveMed.getPatientCode().get();
				String medName = moveMed.getMedName().get();
		    	String medDosage = moveMed.getMedDosage().get();
		    	String medDescript = moveMed.getDetails().get();
		    	String prescribDoc = moveMed.getDoc().get();
		    	String purpPresrcipt = moveMed.getPurpose().get();
		    	String prescribDate = moveMed.getDate().get();
		    	String dateArchived = java.time.LocalDate.now().toString();  
		    	String archiveReason="get from textfield popup";
				
		    	
		    	
				String moveMedQ = "INSERT INTO archivedMeds(patientCode, medName, medDosage, medDescript, "
						+ "prescribDoc, purpPresrcipt, prescribDate, dateArchived, archiveReason) "
						+ "VALUES (?,?,?,?,?,?,?,?,?)";
		    	
				PreparedStatement moveMedPS = conn.prepareStatement(moveMedQ);
		    	
		    	//set strings
		    	moveMedPS.setString(1, patientCode);
		    	moveMedPS.setString(2, medName);
		    	moveMedPS.setString(3, medDosage);
		    	moveMedPS.setString(4, medDescript);
		    	moveMedPS.setString(5, prescribDoc);
		    	moveMedPS.setString(6, purpPresrcipt);
		    	moveMedPS.setString(7, prescribDate);
		    	moveMedPS.setString(8, dateArchived);
		    	moveMedPS.setString(9, archiveReason);
		    	
		    	moveMedPS.execute();
		    	System.out.println("Medication moved!");
		    	
		    	
		    	//************************************
				//DELETE MED AFTER MOVING FROM CURRENT
				//************************************
		    	String deleteMedQ = "DELETE FROM currentMeds WHERE patientCode = ? AND medName = ? AND prescribDate = ? AND dateAdded = ?";
				PreparedStatement deleteMedPS = conn.prepareStatement(deleteMedQ);
				
				deleteMedPS.setString(1, patientCode);
				deleteMedPS.setString(2, medName);
				deleteMedPS.setString(3, prescribDate);
				deleteMedPS.setString(4, moveMed.getDateAdded().get());
		    	
				deleteMedPS.execute();
				System.out.println("Medication deleted!");
				
				
				//reload current med page
				patientMeds.clear();
				grabMeds();
		    	
		    	
		    	
		}catch (SQLException e) {
    		
    		DBConfig.displayException(e);
    		System.out.println("Failed move of medication information.");
    	}
		
	}
	
	
	@FXML
	private void editMed(ActionEvent event) {
		
		try {
    	
			//Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/EditCurrentMeds.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		
	}  

}
