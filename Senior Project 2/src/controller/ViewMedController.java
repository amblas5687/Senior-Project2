package controller;


import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ViewMedController {

	@FXML
	Button add;

	@FXML
	AnchorPane content_view;

	
	 private URL toPane;
	 private AnchorPane temp;
	 
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
