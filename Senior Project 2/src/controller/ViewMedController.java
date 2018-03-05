package controller;


import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class ViewMedController {

	@FXML
	private Button add;
	
    @FXML
    private ToggleButton currMed;

    @FXML
    private ToggleButton archMed;

	@FXML
	private AnchorPane table_med = new AnchorPane();

	@FXML
	private AnchorPane content_view;

	private URL toPane;
	private AnchorPane temp;
	
	private ToggleGroup state = new ToggleGroup();
	
	public void initialize(){
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
		 
	}
	
	@FXML
	private void currentMed(ActionEvent event) {
		
	}
	
	@FXML
	private void archivedMed(ActionEvent event) {
		
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
}
