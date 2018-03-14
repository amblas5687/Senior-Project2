package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import application.DBConfig;
import application.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.MedModel;

public class CurrentMedsController {
	

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
    
	//static variable for editing meds
	private static MedModel curEditMed;
	
	//static variable for viewing details
	private static MedModel curDetailMed;

	
	
    public void initialize(){
    	
		System.out.println("*******CURRENT MED*******");
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
    	System.out.println("SELECTED CURRENT MED..." + selectedMed);   
    	
    	if(selectedMed != null) {
	    	btnDetails.setDisable(false);
	    	btnArchive.setDisable(false);
	    	btnEdit.setDisable(false);
    	}
    }
    
    void grabMeds() {
    	
    	Connection connection = null;
    	PreparedStatement curMedPS = null;
    	ResultSet rs = null;
    	
    	try {
    		
				connection = DataSource.getInstance().getConnection();

		    	String medQ = "SELECT * FROM currentMeds WHERE patientCode = ?";
		    	curMedPS = connection.prepareStatement(medQ);
		    	curMedPS.setString(1, LoginController.currentPatientID);
		    	rs = curMedPS.executeQuery();
		    	
		    	
		    	MedModel tempMed;
		    	String patientCode;
		    	String medName;
		    	String medDate;
		    	String medDetails;
		    	String doc;
		    	String medDose;
		    	String purpose;
		    	String dateAdded;
		    	String medID;
		    	String dateUpdated;
		    	
		    	
		    	
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
		    		medID = rs.getString("medID");
		    		dateUpdated = rs.getString("dateUpdated");
		    		
		    		
		    		tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDetails, dateAdded, medID, null, null, dateUpdated);
		    		patientMeds.add(tempMed);	
		    		System.out.println("GRABBING MEDS FROM CURRENT..." + tempMed);
		    	}
	    	}
    	catch (SQLException e) {
    		DBConfig.displayException(e);	
    		System.out.println("FAILED GRAB CURRENT");
    	}catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally {
			if(connection!=null)
			{
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(curMedPS!=null)
			{
				try {
					curMedPS.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(rs!=null)
			{
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
		
		
		Connection connection = null;
    	PreparedStatement namePS = null;
    	ResultSet rs = null;
		
		//clear the list for search
		patientMeds.clear();
		
		String option = searchOptions.getValue();
		System.out.println("SEARCHING MED CURRENT BY..." + option);
		
		//try database connection first
		try
		{
			connection = DataSource.getInstance().getConnection();

			
			//if they do not select a search value, just grab all again
			if(option == null){
				
				grabMeds();
				
			}
			
			//search by name
			else if(option.equals("Name"))
			{
					String medNameSearch = searchTF.getText();
	
			    	String nameQ = "SELECT * FROM currentMeds WHERE medName = ? AND patientCode = ?";
			    	namePS = connection.prepareStatement(nameQ);
			    	namePS.setString(1, medNameSearch);
			    	namePS.setString(2, LoginController.currentPatientID);
			    	rs = namePS.executeQuery();
			
		    	
			    	//create model
			    	MedModel tempMed;
			    	String patientCode;
			    	String medName;
			    	String medDate;
			    	String medDetails;
			    	String doc;
			    	String medDose;
			    	String purpose;
			    	String dateAdded;
			    	String medID;
			    	String dateUpdated;
			    	
				    	
				    //set values from search
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
				    		medID = rs.getString("medID");
				    		dateUpdated = rs.getString("dateUpdated");
				    		
				    		tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDetails, dateAdded, medID, null, null, dateUpdated);
				    		
				    		//add to list
				    		patientMeds.add(tempMed);	
				    		System.out.println("RESULT FROM NAME SEARCH CURRENT... " + tempMed);
				    	}
			
			
				
				//set table values
				mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
				mDosage.setCellValueFactory(cellData -> cellData.getValue().getMedDosage());
				mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
				mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
				//listView.setItems(patientMeds);
				medicationTable.setItems(patientMeds);
				
								
				//clear the search values
				searchOptions.setValue(null);
				searchTF.setText(null);
			}//end search by name
			
		}catch (SQLException e) {
			DBConfig.displayException(e);	
			System.out.println("FAILED SEARCH CURRENT");
			}catch (Exception e)
			{
	    		e.printStackTrace();
	    	}
			//close the connections
	    	finally {
				if(connection!=null)
				{
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(namePS!=null)
				{
					try {
						namePS.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(rs!=null)
				{
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}//end finally

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
	private void viewDetails(ActionEvent event) throws IOException {
		
		//get the med to edit and set it into a static variable
		curDetailMed = medicationTable.getSelectionModel().getSelectedItem();
		
        Stage detailStage = new Stage();
        Parent detailRoot = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/ViewDetailsCurrentMeds.fxml")); // FXML for second stage
        Scene detailScene = new Scene(detailRoot);
        detailStage.setScene(detailScene);
        detailStage.initModality(Modality.APPLICATION_MODAL);
		 detailStage.show();		
	}
	
	@FXML
    void stopViewing(ActionEvent event) {

    }

	
	//activates when the archive button is selected
	@FXML
	private void archiveMed(ActionEvent event) {
		
		//get selected med
		MedModel moveMed = medicationTable.getSelectionModel().getSelectedItem();
		System.out.println("MED TO MOVE CURRENT..." + moveMed);
		
		Connection connection = null;
    	PreparedStatement moveMedPS = null;
    	PreparedStatement deleteMedPS = null;
    	ResultSet rs = null;
		
		try {
			
				connection = DataSource.getInstance().getConnection();

			
				
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
		    	
		    	String medID = moveMed.getMedID().get();
				
		    	
		    	
				String moveMedQ = "INSERT INTO archivedMeds(patientCode, medName, medDosage, medDescript, "
						+ "prescribDoc, purpPresrcipt, prescribDate, dateArchived, archiveReason) "
						+ "VALUES (?,?,?,?,?,?,?,?,?)";
		    	
				moveMedPS = connection.prepareStatement(moveMedQ);
		    	
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
		    	System.out.println("MEDICATION MOVED CURRENT");
		    	
		    	
		    	//************************************
				//DELETE MED AFTER MOVING FROM CURRENT
				//************************************
		    	String deleteMedQ = "DELETE FROM currentMeds WHERE patientCode = ? AND medID = ?";
				deleteMedPS = connection.prepareStatement(deleteMedQ);
				
				deleteMedPS.setString(1, patientCode);
				deleteMedPS.setString(2, medID);
		    	
				deleteMedPS.execute();
				System.out.println("MEDICATION DELETED CURRENT");
				
				
				//reload current med page
				patientMeds.clear();
				grabMeds();
		    	
		    	
		    	
		}catch (SQLException e) {
    		
    		DBConfig.displayException(e);
    		System.out.println("FAILED ARCHIVCE");
    	}catch (Exception e)
    	{
    		e.printStackTrace();
    	}
		//close connections
    	finally {
			if(connection!=null)
			{
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(moveMedPS!=null)
			{
				try {
					moveMedPS.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(deleteMedPS!=null)
			{
				try {
					deleteMedPS.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(rs!=null)
			{
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	@FXML
	private void editMed(ActionEvent event) {
		
		try {
    	
			//Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/EditCurrentMeds.fxml");
			
			//get the med to edit and set it into a static variable
			curEditMed = medicationTable.getSelectionModel().getSelectedItem();
			
			temp = FXMLLoader.load(toPane);
			
			
			content_view.getChildren().setAll(temp);
            
			
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		
		
	}
	
	public MedModel getEdit()
	{
		return curEditMed;
	}
	
	public MedModel getDetail()
	{
		return curDetailMed;
	}

}
