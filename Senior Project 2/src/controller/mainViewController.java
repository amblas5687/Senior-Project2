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
	 
	 @FXML
	  private void selectMenuOption(ActionEvent event) {
		 try{
			 URL toPane;
			 AnchorPane temp;
			 char btn = event.getSource().toString().charAt(16);
			 System.out.println(btn);
		     
		     switch(btn)
		     {
		     	case 'M': //My Meds
		     		toPane = getClass().getResource("/view/ViewMedInfo.fxml"); 
		     		temp = FXMLLoader.load(toPane);
		     		content_view.getChildren().setAll(temp);
		     		//FXMLDocumentController.rootP.setStyle("-fx-background-color:#00FF00");
		     		break;
		        case 'P': //My Patients
		        	toPane = getClass().getResource("/view/ViewPatientInfo.fxml"); 
		     		temp = FXMLLoader.load(toPane);
		     		content_view.getChildren().setAll(temp);
		        	//FXMLDocumentController.rootP.setStyle("-fx-background-color:#0000FF");
		            break;
		        case 'I': //My Info
		        	toPane = getClass().getResource("/view/ViewUserInfo.fxml"); 
		     		temp = FXMLLoader.load(toPane);
		     		content_view.getChildren().setAll(temp);
		        	//FXMLDocumentController.rootP.setStyle("-fx-background-color:#FF0000");
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
