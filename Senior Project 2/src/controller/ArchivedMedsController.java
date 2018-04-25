package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
	private DatePicker datePicker;

	@FXML
	private DatePicker DRPicker1;

	@FXML
	private DatePicker DRPicker2;

	@FXML
	private Label drpLabel;

	@FXML
	private ComboBox<String> searchOptions;

	@FXML
	private AnchorPane content_view;

	@FXML
	private Label errLBL;

	private URL toPane;
	private AnchorPane temp;

	private ToggleGroup state = new ToggleGroup();
	
	
	// static variable for viewing details
	private static MedModel arcDetailMed;
	

	public void initialize() {

		System.out.println("*******ARCHIVED MED*******");

		// bind columns
		mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
		mDosage.setCellValueFactory(cellData -> cellData.getValue().getTypeAndAmount());
		mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
		mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
		archiveDate.setCellValueFactory(cellData -> cellData.getValue().getArchiveDate());
		archiveTable.setItems(archivedMeds);

		searchOptions.getItems().addAll("Select...", "Name", "Date", "Date Range");
		searchOptions.getSelectionModel().select(0);

		// add to toggle group
		currMed.setToggleGroup(state);
		archMed.setToggleGroup(state);

		archMed.setSelected(true);

		// grabs all the archived meds
		grabTopTen();

	}

	void grabAllMeds() {

		Connection connection = null;
		PreparedStatement arcMedsPS = null;
		ResultSet rs = null;

		try {

			connection = DataSource.getInstance().getConnection();

			String medQ = "SELECT * FROM archivedMeds WHERE patientCode = ? ORDER BY dateArchived DESC";

			arcMedsPS = connection.prepareStatement(medQ);
			arcMedsPS.setString(1, LoginController.currentPatientID);
			rs = arcMedsPS.executeQuery();

			MedModel tempMed;
			String patientCode;
			String medName;
			String medDose;
			String doseType;
			String doc;
			String purpose;
			String medDate;
			String medDetails;
			String archiveDate;
			String archivedBy;
			String archiveReason;

			while (rs.next()) {
				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				medDose = rs.getString("medDosage");
				doseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				archiveDate = rs.getString("dateArchived");
				archivedBy = rs.getString("archivedBy");
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null,
						null, null, archiveDate, archivedBy, archiveReason, null, null);

				archivedMeds.add(tempMed);
				System.out.println("GRABBING MEDS FROM ARCHIVE... " + tempMed);
			}
		} catch (SQLException e) {
			DBConfig.displayException(e);
			System.out.println("FAILED GRAB ARCHIVE");
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

			if (arcMedsPS != null) {
				try {
					arcMedsPS.close();
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

		// archiveTable.setItems(archivedMeds);
	}

	void grabTopTen() {

		Connection connection = null;
		PreparedStatement arcMedsPS = null;
		ResultSet rs = null;

		try {

			connection = DataSource.getInstance().getConnection();

			String medQ = "SELECT * FROM archivedMeds WHERE patientCode = ? ORDER BY dateArchived DESC LIMIT 10";

			arcMedsPS = connection.prepareStatement(medQ);
			arcMedsPS.setString(1, LoginController.currentPatientID);
			rs = arcMedsPS.executeQuery();

			MedModel tempMed;
			String patientCode;
			String medName;
			String medDose;
			String doseType;
			String doc;
			String purpose;
			String medDate;
			String medDetails;
			String archiveDate;
			String archivedBy;
			String archiveReason;

			while (rs.next()) {
				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				medDose = rs.getString("medDosage");
				doseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				archiveDate = rs.getString("dateArchived");
				archivedBy = rs.getString("archivedBy");
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null,
						null, null, archiveDate, archivedBy, archiveReason, null, null);

				archivedMeds.add(tempMed);
				System.out.println("GRABBING TOP 10 MEDS FROM ARCHIVE... " + tempMed);
			}
		} catch (SQLException e) {
			DBConfig.displayException(e);
			System.out.println("FAILED GRAB TOP 10 ARCHIVE");
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

			if (arcMedsPS != null) {
				try {
					arcMedsPS.close();
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

		// archiveTable.setItems(archivedMeds);
	}

	@FXML
	private void currentMed(ActionEvent event) {

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
	void clickMed(MouseEvent event) {

		MedModel selectedMed = archiveTable.getSelectionModel().getSelectedItem();
		System.out.println("SELECTED ARCHIVED MED..." + selectedMed);

		if (selectedMed != null) {
			btnDetails.setDisable(false);
		}
	}

	@FXML
	private void viewDetails(ActionEvent event) throws IOException {

		// get the med to edit and set it into a static variable
		arcDetailMed = archiveTable.getSelectionModel().getSelectedItem();

		Stage detailStage = new Stage();
		Parent detailRoot = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/ViewDetailsArchiveMed.fxml")); 

		
		Scene detailScene = new Scene(detailRoot);
		detailStage.setScene(detailScene);
		detailStage.getIcons().add(new Image("/application/logo_wbg.png"));
		detailStage.initModality(Modality.APPLICATION_MODAL);
		detailStage.show();
	}
	
	public MedModel getDetail() {
		return arcDetailMed;
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

		boolean searchFlag = true;
		String option = searchOptions.getValue();
		archiveTable.getItems().clear();

		if (option == null || option.equals("") || option == "Select...") {
			archiveTable.getItems().clear();
			grabAllMeds();
			
			errLBL.setText(null);
			
			if(searchTF != null && !searchTF.getText().equals("") && !searchTF.getText().equals(null)) {
				errLBL.setText("Select a search parameter for more refined results");
			}
		} else if (option == "Name") {
			searchFlag = validateName();
			if (searchFlag == true) {
				optionName();
				searchOptions.getSelectionModel().select(0);
				searchTF.setText(null);
				searchTF.setPromptText("Search...");
			}

		} else if (option == "Date") {
			searchFlag = validateDate();
			if (searchFlag == true) {
				optionDate();
				datePicker.setValue(null);
				datePicker.getEditor().setText(null);
				
				searchOptions.getSelectionModel().select(0);
				searchTF.setText(null);
				searchTF.setPromptText("Search...");
			}
		} else if (option == "Date Range") {
			searchFlag = validateDateRange();
			if (searchFlag == true) {
				optionDateRange();
				DRPicker1.setValue(null);
				DRPicker1.getEditor().setText(null);
				DRPicker2.setValue(null);
				DRPicker2.getEditor().setText(null);

				searchOptions.getSelectionModel().select(0);
				searchTF.setText(null);
				searchTF.setPromptText("Search...");
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

			String nameQ = "SELECT * FROM archivedMeds WHERE medName = ? AND patientCode = ?ORDER BY dateArchived DESC ";
			namePS = connection.prepareStatement(nameQ);
			namePS.setString(1, curNameSearch);
			namePS.setString(2, LoginController.currentPatientID);
			rs = namePS.executeQuery();

			// create model
			MedModel tempMed;
			String patientCode;
			String medName;
			String medDose;
			String doseType;
			String doc;
			String purpose;
			String medDate;
			String medDetails;
			String archiveDate;
			String archivedBy;
			String archiveReason;

			// set values from search
			while (rs.next()) {

				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				medDose = rs.getString("medDosage");
				doseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				archiveDate = rs.getString("dateArchived");
				archivedBy = rs.getString("archivedBy");
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null,
						null, null, archiveDate, archivedBy, archiveReason, null, null);

				archivedMeds.add(tempMed);

				//System.out.println("RESULT FROM NAME SEARCH ARCHIVE... " + tempMed);
			}

		} catch (SQLException e) {
			DBConfig.displayException(e);
			System.out.println("FAILED SEARCH ARCHIVE");
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
	}

	void optionDate() throws ParseException {
		String dp = datePicker.getEditor().getText();

		// format date for database
		Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date datePickerDate = new SimpleDateFormat("MM/dd/yyyy").parse(dp);
		String formatDate = formatter2.format(datePickerDate);
		System.out.println("Before format " + dp + " After format " + formatDate);

		String query = "SELECT * FROM archivedMeds WHERE prescribDate = ? AND patientCode = ? ORDER BY dateArchived DESC";

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			connection = DataSource.getInstance().getConnection();

			ps = connection.prepareStatement(query);
			ps.setString(1, formatDate);
			ps.setString(2, LoginController.currentPatientID);

			rs = ps.executeQuery();

			archiveTable.getItems().clear();

			while (rs.next()) {

				MedModel tempMed;
				String patientCode;
				String medName;
				String medDose;
				String doseType;
				String doc;
				String purpose;
				String medDate;
				String medDetails;
				String archiveDate;
				String archivedBy;
				String archiveReason;

				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				medDose = rs.getString("medDosage");
				doseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				archiveDate = rs.getString("dateArchived");
				archivedBy = rs.getString("archivedBy");
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null,
						null, null, archiveDate, archivedBy, archiveReason, null, null);

				archivedMeds.add(tempMed);

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

		System.out.println("Before format " + date1String + "__" + date2String + " After format " + formatDate1 + "__"
				+ formatDate2);

		String query = "SELECT * FROM archivedMeds WHERE prescribDate >= ? AND prescribDate <= ? AND patientCode = ? ORDER BY dateArchived DESC";

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

			archiveTable.getItems().clear();

			while (rs.next()) {

				MedModel tempMed;
				String patientCode;
				String medName;
				String medDose;
				String doseType;
				String doc;
				String purpose;
				String medDate;
				String medDetails;
				String archiveDate;
				String archivedBy;
				String archiveReason;

				patientCode = rs.getString("patientCode");
				medName = rs.getString("medName");
				medDate = rs.getString("prescribDate");
				medDetails = rs.getString("medDescript");
				doc = rs.getString("prescribDoc");
				medDose = rs.getString("medDosage");
				doseType = rs.getString("doseType");
				purpose = rs.getString("purpPresrcipt");
				archiveDate = rs.getString("dateArchived");
				archivedBy = rs.getString("archivedBy");
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null,
						null, null, archiveDate, archivedBy, archiveReason, null, null);

				archivedMeds.add(tempMed);

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

	// validate name
	private boolean validateName() {
		errLBL.setText(null);
		System.out.println("VALIDATING NAME...");
		boolean nameFlag = true;
		String search = searchTF.getText();
		search = search.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z]+\\s?[a-zA-Z]*$");// . represents single character
		Matcher m = p.matcher(search);
		boolean b = m.matches();

		if (search == null || search.equals(null) || search.equals("")) {
			errLBL.setText("Enter medication name");
			nameFlag = false;
			System.out.println("NAME IS EMPTY...");
		} else if (!b) {
			errLBL.setText("Remove numbers and special characters");
			System.out.println("CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR STARTED WITH SPACES");
			nameFlag = false;
		}
		return nameFlag;
	}

	private boolean validateDate() {
		System.out.println("VALIDATING DATE...");

		errLBL.setText(null);

		String dateString = datePicker.getEditor().getText();

		// rough check of dob format
		Pattern p = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");
		Matcher m = p.matcher(dateString);
		boolean b = m.matches();

		System.out.println("Date " + dateString);

		// date is empty
		if (dateString == null || dateString.equals(null) || dateString.equals("")) {
			errLBL.setText("Please select or enter a date of prescription");
			System.out.println("DATE EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b) {
			System.out.println("INVALID DATE FORMAT");
			errLBL.setText("Incorrect date format. Please use MM/DD/YYYY");
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
					errLBL.setText("Date of prescription cannot be after today's date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				errLBL.setText("Incorrect date. Please enter a valid date");
				return false;
			}
		}
	}

	private boolean validateDateRange() {

		System.out.println("VALIDATING DATE RANGE...");

		errLBL.setText(null);

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
		if ((d1String == null || d1String.equals(null) || d1String.equals("")) || (d2String == null || d2String.equals(null) || d2String.equals(""))) {
			errLBL.setText("Please select or enter a date of prescription for both fields");
			System.out.println("DATES EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b1 || !b2) {
			System.out.println("INVALID DATE FORMAT");
			errLBL.setText("Incorrect date format. Please use MM/DD/YYYY");
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

				// date 1 is after current date
				if (date1.after(curDate)) {
					System.out.println("DATE CANNOT BE AFTER TODAY'S DATE");
					errLBL.setText("First date of prescription cannot be after today's date");
					return false;
				}
				// date 2 after current date
				else if (date2.after(curDate)) {
					System.out.println("DATE 2 CANNOT BE AFTER TODAY'S DATE");
					errLBL.setText("Second date of prescription cannot be after today's date");
					return false;
				}
				// if d1 after d2
				else if (date1.after(date2)) {
					System.out.println("DATE 1 AFTER DATE 2");
					errLBL.setText("Invalid date range. Second date cannot occur before first date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				errLBL.setText("Incorrect date(s). Please enter valid date(s)");
				return false;
			}
		}
	}

}
