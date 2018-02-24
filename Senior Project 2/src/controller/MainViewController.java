package controller;


import java.net.URL;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class mainViewController {
    
	 @FXML
	  private JFXButton btnMed;
	 @FXML
	  private JFXButton btnPat;
	 @FXML
	  private JFXButton btnInfo;
	 @FXML
	  private JFXButton btnExit;
	 @FXML
	 private AnchorPane content;
	 @FXML
	 private AnchorPane content_view = new AnchorPane();
	 
	 private URL toPane;
	 private AnchorPane temp;
	 
	 public void initialize(){
		 try {
			 
			 toPane = getClass().getResource("/view/NewMedView.fxml"); 
	  		 temp = FXMLLoader.load(toPane);
			 content_view.getChildren().setAll(temp);
			 
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		 
	 }
	    
	 @FXML
	  private void selectMenuOption(ActionEvent event) {
		 
		 try{
			 char btn = event.getSource().toString().charAt(16);
			 System.out.println(btn);
			 String btn2 = event.getSource().toString();
			 System.out.println(btn2);
			 
			 btnMed.getStyleClass().remove("activeButton");
			 btnPat.getStyleClass().remove("activeButton");
			 btnInfo.getStyleClass().remove("activeButton");
		     
		     switch(btn)
		     {
		     	case 'M': //My Meds
		     		toPane = getClass().getResource("/view/ViewMedInfo.fxml"); 
		     		temp = FXMLLoader.load(toPane);
		     		content_view.getChildren().setAll(temp);
		     		btnMed.getStyleClass().add("activeButton");
		     		break;
		        case 'P': //My Patients
		        	toPane = getClass().getResource("/view/ViewPatientInfo.fxml"); 
		     		temp = FXMLLoader.load(toPane);
		     		content_view.getChildren().setAll(temp);
		     		btnPat.getStyleClass().add("activeButton");
		        	break;
		        case 'I': //My Info
		        	toPane = getClass().getResource("/view/ViewUserInfo.fxml"); 
		     		temp = FXMLLoader.load(toPane);
		     		content_view.getChildren().setAll(temp);
		     		btnInfo.getStyleClass().add("activeButton");
		     		break;
		     }
		  
		 } catch (Exception e){
			 e.printStackTrace();
		 }
		 
	  }

	  @FXML
	   private void logout(ActionEvent event) {
		  System.exit(0);
	   }
	    
}
