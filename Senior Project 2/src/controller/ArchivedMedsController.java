package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    
    

}
