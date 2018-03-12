package controller;


import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class ViewMedController {

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
	private AnchorPane table_med = new AnchorPane();

	@FXML
	private AnchorPane content_view;

	private URL toPane;
	private AnchorPane temp;
	
	private ToggleGroup state = new ToggleGroup();
	
	public void initialize(){
		
		searchOptions.getItems().addAll("Name", "Date", "Date Range");
		
		 try {
			 
			 toPane = getClass().getResource("/view/CurrentMedsView.fxml"); 
	  		 temp = FXMLLoader.load(toPane);
			 table_med.getChildren().setAll(temp);
			 
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	 
		 //add to toggle group
		 currMed.setToggleGroup(state);
		 archMed.setToggleGroup(state);
		 
		 currMed.setSelected(true);
		 btnArchive.setDisable(false);
		
	}
	
	@FXML
	private void currentMed(ActionEvent event) {
		
		 currMed.setSelected(true);
		
		 btnAdd.setVisible(true);
		 btnArchive.setVisible(true);
		 btnEdit.setVisible(true);
		 
		 try {
			//Replace table_med's present display with the view of current med table
			 toPane = getClass().getResource("/view/CurrentMedsView.fxml"); 
	  		 temp = FXMLLoader.load(toPane);
			 table_med.getChildren().setAll(temp);
			 
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		 
	}
	
	//activates when the archived toggle button is selected
	@FXML
	private void archivedMed(ActionEvent event) {
		
		 archMed.setSelected(true);
		
		 btnAdd.setVisible(false);
		 btnArchive.setVisible(false);
		 btnEdit.setVisible(false);
		 
		 try {
			//Replace table_med's present display with the view of archived med table
			 toPane = getClass().getResource("/view/CurrentMedsView.fxml");//Change to archived med table when it is created
	  		 temp = FXMLLoader.load(toPane);
			 table_med.getChildren().setAll(temp);
			 
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
		CurrentMedsController tempMedController = new CurrentMedsController();
		tempMedController.archiveMed();
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
