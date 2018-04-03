package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.DBConfig;
import application.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
	private DatePicker datePicker;

	@FXML
	private DatePicker DRPicker1;

	@FXML
	private DatePicker DRPicker2;

	@FXML
	private Label drpLabel;

	@FXML
	private Label lblSearch;

	@FXML
	private ComboBox<String> searchOptions;

	@FXML
	private AnchorPane content_view;

	private boolean flag;

	private URL toPane;
	private AnchorPane temp;

	private ToggleGroup state = new ToggleGroup();

	// static variable for editing meds
	private static MedModel curEditMed;

	// static variable for viewing details
	private static MedModel curDetailMed;

	public void initialize() {

		System.out.println("*******CURRENT MED*******");

		// bind columns
		mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
		mDosage.setCellValueFactory(cellData -> cellData.getValue().getTypeAndAmount());
		mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
		mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
		medicationTable.setItems(patientMeds);

		// load the meds
		grabMeds();

		searchOptions.getItems().addAll("Name", "Date", "Date Range");

		// add to toggle group
		currMed.setToggleGroup(state);
		archMed.setToggleGroup(state);

		currMed.setSelected(true);

		// adds listener to table
		/*
		 * medicationTable.getSelectionModel().selectedItemProperty().addListener(new
		 * ChangeListener<MedModel>() {
		 * 
		 * @Override public void changed(ObservableValue<? extends MedModel> observable,
		 * MedModel oldValue, MedModel newValue) {
		 * 
		 * System.out.println("ListView Selection Changed (newValue: " +
		 * newValue.getMedName() + ")\n"); } });
		 */
	}

	@FXML
	void clickMed(MouseEvent event) {

		MedModel selectedMed = medicationTable.getSelectionModel().getSelectedItem();
		System.out.println("SELECTED CURRENT MED..." + selectedMed);

		if (selectedMed != null) {
			btnDetails.setDisable(false);
			btnArchive.setDisable(false);
			btnEdit.setDisable(false);
		}
	}

	void grabMeds() {

		System.out.println("GRABBING ALL MEDS FROM CURRENT...");

		// default clear table
		medicationTable.getItems().clear();

		Connection connection = null;
		PreparedStatement curMedPS = null;
		ResultSet rs = null;

		try {

			connection = DataSource.getInstance().getConnection();

			String medQ = "SELECT * FROM currentMeds WHERE patientCode = ? ORDER BY prescribDate DESC";
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
			String medDoseType;
			String purpose;
			String dateAdded;
			String addedBy;
			String medID;
			String dateUpdated;
			String updatedBy;

			while (rs.next()) {
				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				// System.out.println(doc);
				medDose = rs.getString("medDosage");
				medDoseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				dateAdded = rs.getString("dateAdded");
				addedBy = rs.getString("addedBy");
				medID = rs.getString("medID");
				dateUpdated = rs.getString("dateUpdated");
				updatedBy = rs.getString("updatedBy");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDoseType, medDetails,
						dateAdded, addedBy, medID, null, null, dateUpdated, updatedBy);
				patientMeds.add(tempMed);
				System.out.println("MED..." + tempMed);
			}
		} catch (SQLException e) {
			DBConfig.displayException(e);
			System.out.println("FAILED GRAB CURRENT");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (curMedPS != null) {
				try {
					curMedPS.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// medicationTable.setItems(patientMeds);

	}

	@FXML
	private void currentMed(ActionEvent event) {

		currMed.setSelected(true);

		try {
			// Replace table_med's present display with the view of current med table
			toPane = getClass().getResource("/view/CurrentMedsView.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// activates when the archived toggle button is selected
	@FXML
	private void archivedMed(ActionEvent event) {

		archMed.setSelected(true);

		try {
			// Replace table_med's present display with the view of archived med table
			toPane = getClass().getResource("/view/ArchiveMedsView.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	void options(ActionEvent event) {

		String option = searchOptions.getValue();

		if (option == "Date") {
			searchTF.setVisible(false);
			DRPicker1.setVisible(false);
			DRPicker2.setVisible(false);
			drpLabel.setVisible(false);

			datePicker.setVisible(true);
		} else if (option == "Date Range") {
			searchTF.setVisible(false);
			datePicker.setVisible(false);

			DRPicker1.setVisible(true);
			DRPicker2.setVisible(true);
			drpLabel.setVisible(true);
		} else {
			DRPicker1.setVisible(false);
			DRPicker2.setVisible(false);
			drpLabel.setVisible(false);
			datePicker.setVisible(false);

			searchTF.setVisible(true);
		}
	}

	@FXML
	private void searchMed(ActionEvent event) throws ParseException {

		String option = searchOptions.getValue();
		medicationTable.getItems().clear();

		if (option == null) {
			grabMeds();
		} else if (option == "Name") {

			if (!checkName()) {
				optionName();

				// clear the search values
				searchOptions.setValue(null);
				searchTF.setText(null);
			}

		} else if (option == "Date") {

			if (checkDate()) {
				optionDate();

				datePicker.setValue(null);
				datePicker.getEditor().setText(null);
				searchOptions.setValue(null);
			}

		} else if (option == "Date Range") {

			if (checkDateRange()) {
				optionDateRange();

				DRPicker1.setValue(null);
				DRPicker1.getEditor().setText(null);
				DRPicker2.setValue(null);
				DRPicker2.getEditor().setText(null);
				searchOptions.setValue(null);
			}

		}
	}

	boolean checkName() {

		String name = searchTF.getText();
		name = name.trim();

		Pattern p = Pattern.compile("[^a-zA-Z]+\\s?[^a-zA-Z]*$");
		Matcher nam = p.matcher(name);
		boolean n = nam.find();

		lblSearch.setText(null);

		if (name.equals("")) {
			lblSearch.setText("Please enter a medication name.");
			flag = true;
		} else if (n) {
			lblSearch.setText("No special characters, numbers, or extra spaces.");
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	boolean checkDate() {

		System.out.println("VALIDATING DATE...");

		lblSearch.setText(null);

		String dateString = datePicker.getEditor().getText();

		// rough check of dob format
		Pattern p = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");
		Matcher m = p.matcher(dateString);
		boolean b = m.matches();

		System.out.println("Date " + dateString);

		// date is empty
		if (dateString.equals(null) || dateString.equals("")) {
			lblSearch.setText("Please enter a date");
			System.out.println("DATE EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b) {
			System.out.println("INVALID DATE FORMAT");
			lblSearch.setText("Invalid date format. MM/DD/YYYY");
			return false;
		} else {

			try {
				// parse the date to see if it is a real date
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				df.setLenient(false);
				df.parse(dateString);

				// get current date and convert date into date object,
				// use fully qualified name because of import conflict
				java.util.Date curDate = new java.util.Date();
				java.util.Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);

				// date is after current date
				if (date.after(curDate)) {
					System.out.println("DATE CANNOT BE AFTER TODAY'S DATE");
					lblSearch.setText("Date cannot be after today's date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				lblSearch.setText("Incorrect date.");
				return false;
			}
		}
	}

	boolean checkDateRange() {

		System.out.println("VALIDATING DATE RANGE...");

		lblSearch.setText(null);

		String d1String = DRPicker1.getEditor().getText();
		String d2String = DRPicker2.getEditor().getText();

		// rough check of dob format
		Pattern p = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");
		Matcher m1 = p.matcher(d1String);
		Matcher m2 = p.matcher(d2String);
		boolean b1 = m1.matches();
		boolean b2 = m2.matches();

		System.out.println("Dates " + d1String + " -- " + d2String);

		// date is empty
		if ((d1String.equals(null) || d1String.equals("")) || (d2String.equals(null) || d2String.equals(""))) {
			lblSearch.setText("Please enter both dates");
			System.out.println("DATES EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b1 || !b2) {
			System.out.println("INVALID DATE FORMAT");
			lblSearch.setText("Invalid date format. MM/DD/YYYY");
			return false;
		} else {

			try {
				// parse the date to see if it is a real date
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				df.setLenient(false);
				df.parse(d1String);
				df.parse(d2String);

				// get current date and convert date into date object,
				// use fully qualified name because of import conflict
				java.util.Date curDate = new java.util.Date();
				java.util.Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(d1String);
				java.util.Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(d2String);

				// date is after current date
				if (date1.after(curDate)) {
					System.out.println("DATE 1 CANNOT BE AFTER TODAY'S DATE");
					lblSearch.setText("From date cannot be after today's date");
					return false;
				}
				else if(date2.after(curDate))
				{
					System.out.println("DATE 2 CANNOT BE AFTER TODAY'S DATE");
					lblSearch.setText("To date cannot be after today's date");
					return false;
				}

				// if d1 after d2
				else if (date1.after(date2)) {
					System.out.println("DATE 1 AFTER DATE 2");
					lblSearch.setText("Invalid date range.");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				lblSearch.setText("Incorrect date.");
				return false;
			}
		}
	}

	void optionName() {

		String search = searchTF.getText();

		Connection connection = null;
		PreparedStatement namePS = null;
		ResultSet rs = null;

		// try database connection first
		try {
			connection = DataSource.getInstance().getConnection();
			// search by name
			String curNameSearch = search;

			String nameQ = "SELECT * FROM currentMeds WHERE medName = ? AND patientCode = ? ORDER BY prescribDate DESC";
			namePS = connection.prepareStatement(nameQ);
			namePS.setString(1, curNameSearch);
			namePS.setString(2, LoginController.currentPatientID);
			rs = namePS.executeQuery();

			// create model
			MedModel tempMed;
			String patientCode;
			String medName;
			String medDate;
			String medDetails;
			String doc;
			String medDose;
			String medDoseType;
			String purpose;
			String dateAdded;
			String addedBy;
			String medID;
			String dateUpdated;
			String updatedBy;

			// set values from search
			while (rs.next()) {
				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				// System.out.println(doc);
				medDose = rs.getString("medDosage");
				medDoseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				dateAdded = rs.getString("dateAdded");
				addedBy = rs.getString("addedBy");
				medID = rs.getString("medID");
				dateUpdated = rs.getString("dateUpdated");
				updatedBy = rs.getString("updatedBy");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDoseType, medDetails,
						dateAdded, addedBy, medID, null, null, dateUpdated, updatedBy);

				// add to list
				patientMeds.add(tempMed);
				System.out.println("RESULT FROM NAME SEARCH CURRENT... " + tempMed);
			}

		} catch (SQLException e) {
			DBConfig.displayException(e);
			System.out.println("FAILED SEARCH CURRENT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// close the connections
		finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (namePS != null) {
				try {
					namePS.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} // end finally

	}// end method

	void optionDate() throws ParseException {
		String dp = datePicker.getEditor().getText();

		// format date for database
		Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date datePickerDate = new SimpleDateFormat("MM/dd/yyyy").parse(dp);
		String formatDate = formatter2.format(datePickerDate);
		System.out.println("Before format " + dp + " After format " + formatDate);

		String query = "SELECT * FROM currentMeds WHERE prescribDate = ? AND patientCode = ? ORDER BY prescribDate DESC";

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			connection = DataSource.getInstance().getConnection();

			ps = connection.prepareStatement(query);
			ps.setString(1, formatDate);
			ps.setString(2, LoginController.currentPatientID);

			rs = ps.executeQuery();

			while (rs.next()) {

				MedModel tempMed;
				String patientCode;
				String medName;
				String medDate;
				String medDetails;
				String doc;
				String medDose;
				String medDoseType;
				String purpose;
				String dateAdded;
				String addedBy;
				String medID;
				String dateUpdated;
				String updatedBy;

				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				medDose = rs.getString("medDosage");
				medDoseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				dateAdded = rs.getString("dateAdded");
				addedBy = rs.getString("addedBy");
				medID = rs.getString("medID");
				dateUpdated = rs.getString("dateUpdated");
				updatedBy = rs.getString("updatedBy");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDoseType, medDetails,
						dateAdded, addedBy, medID, null, null, dateUpdated, updatedBy);

				// add to list
				patientMeds.add(tempMed);

			}

		} catch (SQLException ex) {
			DBConfig.displayException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// close the connections
		finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	void optionDateRange() throws ParseException {

		String date1String = DRPicker1.getEditor().getText();
		String date2String = DRPicker2.getEditor().getText();
		
		// format date for database
		Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(date1String);
		java.util.Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(date2String);

		String formatDate1 = formatter2.format(date1);
		String formatDate2 = formatter2.format(date2);

		System.out.println("Before format " + date1String + "__" + date2String+  
				" After format " + formatDate1 + "__" + formatDate2);

		String query = "SELECT * FROM currentMeds WHERE prescribDate >= ? AND prescribDate <= ? AND patientCode = ? ORDER BY prescribDate DESC";

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			connection = DataSource.getInstance().getConnection();

			ps = connection.prepareStatement(query);
			ps.setString(1, formatDate1);
			ps.setString(2, formatDate2);
			ps.setString(3, LoginController.currentPatientID);
			rs = ps.executeQuery();

			while (rs.next()) {

				MedModel tempMed;
				String patientCode;
				String medName;
				String medDate;
				String medDetails;
				String doc;
				String medDose;
				String medDoseType;
				String purpose;
				String dateAdded;
				String addedBy;
				String medID;
				String dateUpdated;
				String updatedBy;

				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				medDose = rs.getString("medDosage");
				medDoseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				dateAdded = rs.getString("dateAdded");
				addedBy = rs.getString("addedBy");
				medID = rs.getString("medID");
				dateUpdated = rs.getString("dateUpdated");
				updatedBy = rs.getString("updatedBy");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, medDoseType, medDetails,
						dateAdded, addedBy, medID, null, null, dateUpdated, updatedBy);

				// add to list
				patientMeds.add(tempMed);

			}

		} catch (SQLException ex) {
			DBConfig.displayException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (rs != null) {
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
	private void addMed(ActionEvent event) {

		try {
			// Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/NewMedView.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void viewDetails(ActionEvent event) throws IOException {

		// get the med to edit and set it into a static variable
		curDetailMed = medicationTable.getSelectionModel().getSelectedItem();

		Stage detailStage = new Stage();
		Parent detailRoot = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/ViewDetailsCurrentMeds.fxml"));
		
		
		Scene detailScene = new Scene(detailRoot);
		detailStage.setScene(detailScene);
		detailStage.getIcons().add(new Image("/application/4getmenot1.jpg"));
		detailStage.initModality(Modality.APPLICATION_MODAL);
		detailStage.show();
	}
	

	// activates when the archive button is selected and moves med
	@FXML
	private void archiveMed(ActionEvent event) {

		// get selected med
		MedModel moveMed = medicationTable.getSelectionModel().getSelectedItem();
		System.out.println("MED TO MOVE CURRENT..." + moveMed);

		// dialog for archive reason
		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.initModality(Modality.APPLICATION_MODAL);

		DialogPane dialogPane = dialog.getDialogPane();
		// css for info alert box
		dialogPane.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());

		// dialogPane.getStyleClass().add("alert");

		dialog.setTitle("Archive Reason");
		dialog.setHeaderText("Enter archival reason below:");

		// Set the button types.
		ButtonType createBtn = new ButtonType("Add Reason");
		ButtonType btnCancel = new ButtonType("Cancel");
		dialog.getDialogPane().getButtonTypes().addAll(btnCancel, createBtn);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 90, 10, 10));

		TextArea notes = new TextArea();
		notes.setPromptText("Enter archive reason here");

		grid.add(notes, 1, 0);

		dialog.getDialogPane().setContent(grid);

		notes.setEditable(true);
		notes.setWrapText(true);
		// sets grid dimensions
		// notes.setMaxWidth(Double.MAX_VALUE);
		// notes.setMaxHeight(Double.MAX_VALUE);
		// GridPane.setVgrow(notes, Priority.ALWAYS);
		// GridPane.setHgrow(notes, Priority.ALWAYS);

		// grid.setMaxWidth(Double.MAX_VALUE);
		Optional result = dialog.showAndWait();
		// makes dialog box expandable -> default is not

		// expands window to view full text area -> hides automatically if this is not
		// set
		dialog.getDialogPane().setExpanded(true);
		String no = notes.getText();

		try {
			result.get();

			if (result.get() == createBtn && !no.equals("") && no.length() < 500) {
				System.out.println("NOTES... " + notes.getText());

				Connection connection = null;
				PreparedStatement moveMedPS = null;
				PreparedStatement deleteMedPS = null;
				ResultSet rs = null;

				try {

					connection = DataSource.getInstance().getConnection();

					// ********************************
					// MOVE MED TO ARCHIVE TABLE
					// ********************************

					// grab values to insert into archive
					String patientCode = moveMed.getPatientCode().get();
					String medName = moveMed.getMedName().get();
					String medDosage = moveMed.getMedDosage().get();
					String medDoseType = moveMed.getDoseType().get();
					String medDescript = moveMed.getDetails().get();
					String prescribDoc = moveMed.getDoc().get();
					String purpPresrcipt = moveMed.getPurpose().get();
					String prescribDate = moveMed.getDate().get();
					String dateArchived = java.time.LocalDate.now().toString();
					String archiveReason = notes.getText();

					String medID = moveMed.getMedID().get();

					String moveMedQ = "INSERT INTO archivedMeds(patientCode, medName, medDosage, doseType, medDescript, "
							+ "prescribDoc, purpPresrcipt, prescribDate, dateArchived, archiveReason) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?)";

					moveMedPS = connection.prepareStatement(moveMedQ);

					// set strings
					moveMedPS.setString(1, patientCode);
					moveMedPS.setString(2, medName);
					moveMedPS.setString(3, medDosage);
					moveMedPS.setString(4, medDoseType);
					moveMedPS.setString(5, medDescript);
					moveMedPS.setString(6, prescribDoc);
					moveMedPS.setString(7, purpPresrcipt);
					moveMedPS.setString(8, prescribDate);
					moveMedPS.setString(9, dateArchived);
					moveMedPS.setString(10, archiveReason);

					moveMedPS.execute();
					System.out.println("MEDICATION MOVED CURRENT");

					// ************************************
					// DELETE MED AFTER MOVING FROM CURRENT
					// ************************************
					String deleteMedQ = "DELETE FROM currentMeds WHERE patientCode = ? AND medID = ?";
					deleteMedPS = connection.prepareStatement(deleteMedQ);

					deleteMedPS.setString(1, patientCode);
					deleteMedPS.setString(2, medID);

					deleteMedPS.execute();
					System.out.println("MEDICATION DELETED CURRENT");

					// reload current med page
					grabMeds();

				} catch (SQLException e) {

					DBConfig.displayException(e);
					System.out.println("FAILED ARCHIVCE");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// close connections
				finally {
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (moveMedPS != null) {
						try {
							moveMedPS.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (deleteMedPS != null) {
						try {
							deleteMedPS.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} // finally

			} // end if
			else if (result.get() == btnCancel) {
				dialog.hide();
			} else if (no.equals("")) {
				Alert failure = new Alert(AlertType.ERROR);
				// safely catches error by pop-up alert.
				failure.setContentText("Must enter archive reason");
				failure.getDialogPane().getStylesheets()
						.add(getClass().getResource("/application/application.css").toExternalForm());
				Optional<ButtonType> error = failure.showAndWait();
			} else if (no.length() > 500) {
				Alert failure = new Alert(AlertType.ERROR);
				// safely catches error by pop-up alert.
				failure.setContentText("Notes must be less than 500 characters");
				failure.getDialogPane().getStylesheets()
						.add(getClass().getResource("/application/application.css").toExternalForm());
				Optional<ButtonType> error = failure.showAndWait();
			}
		} catch (NoSuchElementException e) {
			dialog.close();
		}

	}// method

	@FXML
	private void editMed(ActionEvent event) {

		try {

			// Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/EditCurrentMeds.fxml");

			// get the med to edit and set it into a static variable
			curEditMed = medicationTable.getSelectionModel().getSelectedItem();

			temp = FXMLLoader.load(toPane);

			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MedModel getEdit() {
		return curEditMed;
	}

	public MedModel getDetail() {
		return curDetailMed;
	}

}
