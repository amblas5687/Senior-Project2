package controller;

import java.net.URL;
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
    private DatePicker DRPicker;
    
	@FXML
    private ComboBox<String> searchOptions;
	
	@FXML
	private AnchorPane content_view;

	private URL toPane;
	private AnchorPane temp;
	
	private ToggleGroup state = new ToggleGroup();
	
	
	public void initialize(){
		
		searchOptions.getItems().addAll("Name", "Date", "Date Range");
		
		//add to toggle group
		currMed.setToggleGroup(state);
		archMed.setToggleGroup(state);
		
		archMed.setSelected(true);
		
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
	private void viewDetails(ActionEvent event) {
		
	}
	
	
	@FXML
	private void searchMed(ActionEvent event) {

    }

	

}
