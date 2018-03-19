package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.DBConfig;
import application.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
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

	public void initialize() {

		System.out.println("*******ARCHIVED MED*******");

		// bind columns
		mName.setCellValueFactory(cellData -> cellData.getValue().getMedName());
		mDosage.setCellValueFactory(cellData -> cellData.getValue().getTypeAndAmount());
		mDate.setCellValueFactory(cellData -> cellData.getValue().getDate());
		mDoc.setCellValueFactory(cellData -> cellData.getValue().getDoc());
		archiveDate.setCellValueFactory(cellData -> cellData.getValue().getArchiveDate());
		archiveReason.setCellValueFactory(cellData -> cellData.getValue().getArchiveReason());
		archiveTable.setItems(archivedMeds);

		searchOptions.getItems().addAll("Name", "Date", "Date Range");

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

			String medQ = "SELECT * FROM archivedMeds WHERE patientCode = ?";

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
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null, null,
						archiveDate, archiveReason, null);

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
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null, null,
						archiveDate, archiveReason, null);

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
		System.out.println(selectedMed);

		if (selectedMed != null) {
			btnDetails.setDisable(false);
		}
	}

	@FXML
	private void viewDetails(ActionEvent event) {

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
	private void searchMed(ActionEvent event) {

		boolean searchFlag = true;
		String option = searchOptions.getValue();
		archiveTable.getItems().clear();

		if (option == null) {
			archiveTable.getItems().clear();
			grabAllMeds();
		} else if (option == "Name") {
			searchFlag = validateName();
			if (searchFlag == true) {
				optionName();
				searchOptions.setValue(null);
				searchTF.setText(null);
			}

		} else if (option == "Date") {
			searchFlag = validateDate();
			if(searchFlag == true)
			{
				optionDate();
				datePicker.setValue(null);
				searchOptions.setValue(null);
			}
		} else if (option == "Date Range") {
			searchFlag = validateDateRange();
			if(searchFlag == true)
			{
				optionDateRange();
				DRPicker1.setValue(null);
				DRPicker2.setValue(null);
				searchOptions.setValue(null);
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

			String nameQ = "SELECT * FROM archivedMeds WHERE medName = ? AND patientCode = ?";
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
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null, null,
						archiveDate, archiveReason, null);

				archivedMeds.add(tempMed);

				System.out.println("RESULT FROM NAME SEARCH ARCHIVE... " + tempMed);
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

	void optionDate() {
		LocalDate dp = datePicker.getValue();
		Date date = java.sql.Date.valueOf(dp);

		String query = "SELECT * FROM archivedMeds WHERE prescribDate = ?";

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			connection = DataSource.getInstance().getConnection();

			ps = connection.prepareStatement(query);
			ps.setDate(1, date);

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
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null, null,
						archiveDate, archiveReason, null);

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

	void optionDateRange() {

		LocalDate drp1 = DRPicker1.getValue();
		LocalDate drp2 = DRPicker2.getValue();
		Date date1 = java.sql.Date.valueOf(drp1);
		Date date2 = java.sql.Date.valueOf(drp2);

		String query = "SELECT * FROM archivedMeds WHERE prescribDate >= '" + date1 + "' AND prescribDate <= '" + date2
				+ "';";

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			connection = DataSource.getInstance().getConnection();

			ps = connection.prepareStatement(query);
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
				archiveReason = rs.getString("archiveReason");

				tempMed = new MedModel(patientCode, medName, medDate, doc, purpose, medDose, doseType, medDetails, null, null,
						archiveDate, archiveReason, null);

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

		if (search == null || search.equals("")) {
			errLBL.setText("Name cannot be empty");
			nameFlag = false;
			System.out.println("NAME IS EMPTY...");
		} else if (!b) {
			errLBL.setText("No numbers, special characters, or extra white spaces");
			System.out.println("CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR STARTED WITH SPACES");
			nameFlag = false;
		}
		return nameFlag;
	}

	private boolean validateDate() {
		System.out.println("VALIDATING DATE...");
		errLBL.setText(null);
		boolean dateFlag = true;

		if (datePicker.getValue() == null) {
			errLBL.setText("Please select a date.");
			System.out.println("DATE NOT SET");
			dateFlag = false;
		} else {
			dateFlag = true;
		}

		return dateFlag;
	}

	private boolean validateDateRange() {
		System.out.println("VALIDATING DATE RANGE...");
		
		boolean dateRangeFlag = true;
		if (DRPicker1.getValue() == null || DRPicker2.getValue() == null) {
			errLBL.setText("Please fill in both dates.");
			System.out.println("DATE NOT SET");
			return dateRangeFlag = false;
		}

		LocalDate dp1 = DRPicker1.getValue();
		LocalDate dp2 = DRPicker2.getValue();

		Date date1 = java.sql.Date.valueOf(dp1);
		Date date2 = java.sql.Date.valueOf(dp2);

		errLBL.setText(null);

		if (date2.before(date1)) {
			errLBL.setText("Invalid date range.");
			System.out.println("DATE RANGE INVALID");
			dateRangeFlag = false;
		} else {
			dateRangeFlag = true;
		}

		return dateRangeFlag;
	}

}
