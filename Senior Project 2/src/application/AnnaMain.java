package application;
	
import java.sql.Connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

//test view for my own controllers

public class AnnaMain extends Application {
	
	public static Connection con;

	
	public void start(Stage primaryStage) {

		try {
			 con = DBConfig.getConnection();
			
			
			BorderPane root = FXMLLoader.load(getClass().getResource("/view/UserView.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
