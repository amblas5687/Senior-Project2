package controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import application.DBConfig;
import application.DataSource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.DoseModel;
import model.MedModel;

public class PreexistingArchiveController {

	Stage stage;
	Parent root;
	Scene scene;
	
	
    @FXML
    private AnchorPane content_view;

    @FXML
    private TextField medName;

    @FXML
    private TextField medDosage;

    @FXML
    private TextArea medDescript;

    @FXML
    private TextField prescribDoc;

    @FXML
    private TextField purpOfPrescript;
    
	@FXML
	private TextField medDoseMul;

    @FXML
    private DatePicker DOPPicker;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button cancelBTN;

    @FXML
    private ComboBox<String> doseType;

    @FXML
    private Label nameLBL;

    @FXML
    private Label doctorLBL;

    @FXML
    private Label doseLBL;

    @FXML
    private Label purposeLBL;

    @FXML
    private Label datePrescribedLBL;
    
    @FXML
    private Label lblDescript;
    
    @FXML
    private Label lblSuccess;
    
	@FXML
	private Label lblFreq;
    
	@FXML
	private RadioButton rbSingle;
	
	@FXML
	private RadioButton rbMultiple;
	
	private ToggleGroup rbFreq = new ToggleGroup();
    
	TableView<DoseModel> freqTable = new TableView<DoseModel>();
	TableColumn<DoseModel, String> dose = new TableColumn<DoseModel, String>("Dose");
	TableColumn<DoseModel, String> type = new TableColumn<DoseModel, String>("Type");
	TableColumn<DoseModel, String> time = new TableColumn<DoseModel, String>("Time");
	
	MedModel tempMed = new MedModel();
	
	ObservableList<DoseModel> multipleMed = FXCollections.observableArrayList();	
	
	//lets you know if they selected and entered multiple medications to disable error checking
	private boolean multiMed = false;
    
	
	public void initialize() {

		System.out.println("*******PREEXISTING ARCHIVE MED*******");
		
		doseType.getItems().addAll("mg", "g", "kg", "oz", "tab", "tsp", "tbsp");
		
		rbSingle.setToggleGroup(rbFreq);
		rbMultiple.setToggleGroup(rbFreq);
	}

    
    @FXML
    void returnMain(ActionEvent event) {
    	
    	try {
			stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
			scene = new Scene(root, 1200, 670);
			stage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
	@FXML
	void selectFreq(ActionEvent event) {
    	
		multiMed = false;
    	if(rbSingle.isSelected()) {
    		//hide radio buttons
    		rbSingle.setVisible(false);
    		rbMultiple.setVisible(false);
    		//display dose
    		lblFreq.setText("Medication Dosage:");
    		medDosage.setVisible(true);
    		doseType.setVisible(true);
    	} else {
    		multiMed = true;
    		Alert alert = new Alert(AlertType.INFORMATION);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			//dialogPane.setMaxHeight(200);
			dialogPane.getStyleClass().add("alert");
			alert.setTitle("Medication Frequency");
			
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/application/logo_wbg.png"));
			
			freqTable.setPrefHeight(100);
			dose.setPrefWidth(296);
			type.setPrefWidth(296);
			time.setPrefWidth(296);
			freqTable.getColumns().addAll(dose, type, time);
			alert.getDialogPane().setContent(freqTable);
			
			GridPane grid = new GridPane();
			//label
			Label lblDose = new Label("Medication Dosage:");
			lblDose.setPadding(new Insets(18, 12, 15, 15));
			//textfield
			TextField doseTF = new TextField();
			doseTF.setPromptText("Dose");
			doseTF.setPrefHeight(40);
			doseTF.setPrefWidth(110);
			//combo box
			ComboBox<String> dType = new ComboBox<String>();
			dType.setPromptText("Select...");
			dType.setPrefHeight(40);
			dType.setPrefWidth(110);
			dType.getItems().addAll("Select...", "mg", "g", "kg", "oz", "tab", "tsp", "tbsp");
			//label
			Label lblTime = new Label("Time Taken:");
			lblTime.setPadding(new Insets(18, 0, 15, 15));
			//textfield
			TextField hourTF = new TextField();
			hourTF.setPromptText("Hour");
			hourTF.setPrefHeight(40);
			hourTF.setPrefWidth(110);
			//Radio Buttons
			RadioButton rbAM = new RadioButton("AM");
			RadioButton rbPM = new RadioButton("PM");
			//toggle group
			ToggleGroup rbTime = new ToggleGroup();
			rbAM.setToggleGroup(rbTime);
			rbAM.setPadding(new Insets(0, 0, 0, 17));
			rbPM.setToggleGroup(rbTime);
			rbPM.setPadding(new Insets(0, 0, 0, 17));
			//submit
			Button btnSubmit = new Button("Add");
			btnSubmit.setPrefHeight(40);
			btnSubmit.setOnAction(click -> {
				String tim = "";
				
				if(rbAM.isSelected()) {
					tim = hourTF.getText() + rbAM.getText();
				} else {
					tim = hourTF.getText() + rbPM.getText();
				}
				
				//set fields
				DoseModel tempDose = new DoseModel(doseTF.getText(), dType.getValue(), tim);
				multipleMed.add(tempDose);
				System.out.println(tempDose.getDose());
				//get fields
				dose.setCellValueFactory(cellData -> cellData.getValue().getDose());
				type.setCellValueFactory(cellData -> cellData.getValue().getType());
				time.setCellValueFactory(cellData -> cellData.getValue().getTime());
				
				freqTable.setItems(multipleMed);
				
				//clear
				doseTF.setText(null);
				dType.setValue("Select...");
				hourTF.setText(null);
				rbAM.setSelected(false);
				rbPM.setSelected(false);
			});
			//separator
			Label sep = new Label("   ");
			Label sep2 = new Label("   ");
			Label sep3 = new Label("   ");
			Label sep4 = new Label(" ");
			sep3.setPadding(new Insets(0, 2, 0, 0));
			//table
			freqTable.setPrefHeight(300);
			//add to grid
			grid.add(lblDose, 0, 0);
			grid.add(doseTF, 1, 0);
			grid.add(sep, 2, 0);
			grid.add(dType, 3, 0);
			grid.add(lblTime, 4, 0);
			grid.add(sep2, 5, 0);
			grid.add(hourTF, 6, 0);
			grid.add(rbAM, 7, 0);
			grid.add(rbPM, 8, 0);
			grid.add(sep3, 9, 0);
			grid.add(btnSubmit, 10, 0);
			grid.add(sep4, 11, 0);
			grid.setPrefHeight(60);
			alert.getDialogPane().setHeader(grid);

			alert.getButtonTypes().setAll(ButtonType.FINISH, ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == ButtonType.FINISH) {
				//TODO: concatenate doses into medDoseMul
				StringBuilder medConcat = new StringBuilder();
				for(DoseModel dose: multipleMed) {
					medConcat.append(dose.toString());
					medConcat.append(", ");
				}
				System.out.println("medConcat " + medConcat);
				String medConcatSTR = medConcat.toString().substring(0, medConcat.toString().length()-2);

				//enable
				rbSingle.setVisible(false);
				rbMultiple.setVisible(false);
				
				medDoseMul.setVisible(true);
				
				medDoseMul.setText(medConcatSTR);
				
			} else {
				//System.out.println("Cancelled hit");
				rbSingle.setSelected(false);
				rbMultiple.setSelected(false);
				
				freqTable.getColumns().clear();
				multipleMed.clear();
			}

    	}
    }
	
	@FXML
	void addMultiple(MouseEvent event) {
		freqTable.getColumns().clear();
		
		Alert alert = new Alert(AlertType.INFORMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		//dialogPane.setMaxHeight(200);
		dialogPane.getStyleClass().add("alert");
		alert.setTitle("Medication Frequency");
		
		stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/application/logo_wbg.png"));
		
		freqTable.setPrefHeight(100);
		dose.setPrefWidth(296);
		type.setPrefWidth(296);
		time.setPrefWidth(296);
		freqTable.getColumns().addAll(dose, type, time);
		alert.getDialogPane().setContent(freqTable);
		
		GridPane grid = new GridPane();
		//label
		Label lblDose = new Label("Medication Dosage:");
		lblDose.setPadding(new Insets(18, 12, 15, 15));
		//textfield
		TextField doseTF = new TextField();
		doseTF.setPromptText("Dose");
		doseTF.setPrefHeight(40);
		doseTF.setPrefWidth(110);
		//combo box
		ComboBox<String> dType = new ComboBox<String>();
		dType.setPromptText("Select...");
		dType.setPrefHeight(40);
		dType.setPrefWidth(110);
		dType.getItems().addAll("Select...", "mg", "g", "kg", "oz", "tab", "tsp", "tbsp");
		//label
		Label lblTime = new Label("Time Taken:");
		lblTime.setPadding(new Insets(18, 0, 15, 15));
		//textfield
		TextField hourTF = new TextField();
		hourTF.setPromptText("Hour");
		hourTF.setPrefHeight(40);
		hourTF.setPrefWidth(110);
		//Radio Buttons
		RadioButton rbAM = new RadioButton("AM");
		RadioButton rbPM = new RadioButton("PM");
		//toggle group
		ToggleGroup rbTime = new ToggleGroup();
		rbAM.setToggleGroup(rbTime);
		rbAM.setPadding(new Insets(0, 0, 0, 17));
		rbPM.setToggleGroup(rbTime);
		rbPM.setPadding(new Insets(0, 0, 0, 17));
		//submit
		Button btnSubmit = new Button("Add");
		btnSubmit.setPrefHeight(40);
		btnSubmit.setOnAction(click -> {
			String tim = "";
			
			if(rbAM.isSelected()) {
				tim = hourTF.getText() + rbAM.getText();
			} else {
				tim = hourTF.getText() + rbPM.getText();
			}
			
			//set fields
			DoseModel tempDose = new DoseModel(doseTF.getText(), dType.getValue(), tim);
			multipleMed.add(tempDose);
			System.out.println(tempDose.getDose());
			//get fields
			dose.setCellValueFactory(cellData -> cellData.getValue().getDose());
			type.setCellValueFactory(cellData -> cellData.getValue().getType());
			time.setCellValueFactory(cellData -> cellData.getValue().getTime());
			
			freqTable.setItems(multipleMed);
			
			//clear
			doseTF.setText(null);
			dType.setValue("Select...");
			hourTF.setText(null);
			rbAM.setSelected(false);
			rbPM.setSelected(false);
		});
		//separator
		Label sep = new Label("   ");
		Label sep2 = new Label("   ");
		Label sep3 = new Label("   ");
		Label sep4 = new Label(" ");
		sep3.setPadding(new Insets(0, 2, 0, 0));
		//table
		freqTable.setPrefHeight(300);
		//add to grid
		grid.add(lblDose, 0, 0);
		grid.add(doseTF, 1, 0);
		grid.add(sep, 2, 0);
		grid.add(dType, 3, 0);
		grid.add(lblTime, 4, 0);
		grid.add(sep2, 5, 0);
		grid.add(hourTF, 6, 0);
		grid.add(rbAM, 7, 0);
		grid.add(rbPM, 8, 0);
		grid.add(sep3, 9, 0);
		grid.add(btnSubmit, 10, 0);
		grid.add(sep4, 11, 0);
		grid.setPrefHeight(60);
		alert.getDialogPane().setHeader(grid);

		alert.getButtonTypes().setAll(ButtonType.FINISH, ButtonType.CANCEL);
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.FINISH) {
			//TODO: concatenate doses into medDoseMul
			StringBuilder medConcat = new StringBuilder();
			for(DoseModel dose: multipleMed) {
				medConcat.append(dose.toString());
				medConcat.append(", ");
			}
			System.out.println("medConcat " + medConcat);
			String medConcatSTR = medConcat.toString().substring(0, medConcat.toString().length()-2);

			//enable
			rbSingle.setVisible(false);
			rbMultiple.setVisible(false);
			
			medDoseMul.setVisible(true);
			
			medDoseMul.setText(medConcatSTR);
			
		} else {
			rbSingle.setSelected(false);
			rbSingle.setVisible(true);
			rbMultiple.setSelected(false);
			rbMultiple.setVisible(true);
			
			freqTable.getColumns().clear();
			multipleMed.clear();
			medDoseMul.setVisible(false);
			medDoseMul.setText("");
		}

	}

    @FXML
	void submit(ActionEvent event) {

		boolean validName, validDose, validDoseType, validDoctor, validPurpose, validDate, validDescript = true;

		validName = validateName();
		
		//if they selected a single med, error check
		if(multiMed == false)
		{
			validDose = validateDose();
			validDoseType = validateDoseType();
		}
		//otherwise just let it through for now
		else {
			validDose = true;
			validDoseType = true;
		}
		validDoctor = validateDoctor();
		validPurpose = validatePurpose();
		validDate = validateDate();
		validDescript = validateMedDescription();

		if (validName && validDose && validDoseType && validDoctor && validPurpose && validDate && validDescript) {

			String mName = medName.getText();
			String mDosage = "";
			String mDoseType = "";
			
			if(multiMed == false)
			{
				mDosage = medDosage.getText();
				mDoseType = doseType.getValue().toString();
			}
			else {
				mDosage = medDoseMul.getText();
			}
			
			String mDescript = medDescript.getText();
			String pDoc = prescribDoc.getText();
			String pPurpose = purpOfPrescript.getText();
			String pDate = DOPPicker.getEditor().getText();
			String dateArchived = java.time.LocalDate.now().toString();

			Connection connection = null;
			PreparedStatement ps = null;

			String query = "INSERT INTO archivedMeds (patientCode, medName, medDosage, doseType, medDescript, prescribDoc, purpPresrcipt, prescribDate, dateArchived)"
					+ "VALUES (?,?,?,?,?,?,?,?,?)";

			try {
				connection = DataSource.getInstance().getConnection();

				ps = connection.prepareStatement(query);
				ps.setString(1, LoginController.currentPatientID);
				ps.setString(2, mName);
				ps.setString(3, mDosage);
				ps.setString(4, mDoseType);
				ps.setString(5, mDescript);
				ps.setString(6, pDoc);
				ps.setString(7, pPurpose);
				ps.setString(8, pDate);
				ps.setString(9, dateArchived);

				ps.execute();

				System.out.println("Successful insertion of preexisting archive medication");

			} catch (SQLException e) {

				DBConfig.displayException(e);

				System.out.println("Failed insertion of preexisting archive information.");
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
			} // end finally

			// Clears fields after medication insertion
			medName.setText("");
			medDosage.setText("");
			doseType.setValue(null);
			medDescript.setText("");
			prescribDoc.setText("");
			purpOfPrescript.setText("");
			DOPPicker.setValue(null);
			lblSuccess.setText("Medication successfully inserted!");
			
			Timeline timeline = new Timeline(new KeyFrame(
	    	        Duration.seconds(3),
	    	        ae -> lblSuccess.setText(null)));
	    	timeline.play();
			
			//TODO fix insets
			//cancelBTN.setStyle("-fx-background-insets: 0 130px 0 0;");
			cancelBTN.setLayoutX(130);

		} // end if

	}// end submit

	private boolean validateName() {
		nameLBL.setText(null);
		System.out.println("VALIDATING MED NAME");
		boolean nameFlag = true;

		String name = medName.getText();
		name = name.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z]+\\s?[a-zA-Z]*$");// . represents single character
		Matcher m = p.matcher(name);
		boolean b = m.matches();

		if (name == null || name.equals(null) || name.equals("")) {
			nameLBL.setText("Enter medication name");
			nameFlag = false;
			System.out.println("MED NAME IS EMPTY...");
		} else if (!b) {
			nameLBL.setText("Remove numbers and special characters");
			System.out.println("MED NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			nameFlag = false;
		}
		else if(name.length()>=60)
		{
			nameLBL.setText("Medication name length greater than 60 characters");
			nameFlag = false;
			System.out.println("NAME TOO LONG");
		}
		return nameFlag;
	}

	private boolean validateDose() {
		System.out.println("VALIDATING MED DOSE");
		doseLBL.setText(null);
		boolean doseFlag = true;

		String dose = medDosage.getText();
		dose = dose.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[0-9]+(.[0-9]+)?$");// . represents single character
		Matcher m = p.matcher(dose);
		boolean b = m.matches();

		if (dose == null || dose.equals(null) || dose.equals("")) {
			doseLBL.setText("Enter dosage amount");
			doseFlag = false;
			System.out.println("DOSE IS EMPTY...");
		} else if (!b) {
			doseLBL.setText("Dose must be a whole number or decimal number");
			System.out.println("DOSE HAD LETTERS OR TOO MANY DECIMALS");
			doseFlag = false;
		}
		else if(dose.length()>=25)
		{
			doseLBL.setText("Dose length greater than 25 characters");
			doseFlag = false;
			System.out.println("DOSE TOO LONG");
		}
		return doseFlag;
	}
	
	private boolean validateDoseType() {
		System.out.println("VALIDATING DOSAGE TYPE");
		boolean dtFlag = true;
		String dose = medDosage.getText();
		
		if(doseType.getValue() == null && (dose == null || dose.equals(null) || dose.equals(""))) {
			dtFlag = false;
			doseLBL.setText("Please enter dosage amount and select a dosage type");
		} else if(doseType.getValue() ==  null) {
			dtFlag = false;
			doseLBL.setText("Please select a dosage type");
		}
		
		return dtFlag;
	}

	private boolean validateDoctor() {
		doctorLBL.setText(null);
		System.out.println("VALIDATING DOCTOR");
		boolean doctorFlag = true;

		String doctor = prescribDoc.getText();
		doctor = doctor.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z\\.\\-\\']+(\\s?[a-zA-Z\\.\\-\\'])*$");// . represents single character
		Matcher m = p.matcher(doctor);
		boolean b = m.matches();

		if (doctor == null || doctor.equals(null) || doctor.equals("")) {
			doctorLBL.setText("Enter doctor name");
			doctorFlag = false;
			System.out.println("DOCTOR NAME IS EMPTY...");
		} else if (!b) {
			doctorLBL.setText("Remove numbers and special characters");
			System.out.println("DOCTOR NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			doctorFlag = false;
		}
		else if(doctor.length()>=60)
		{
			doctorLBL.setText("Doctor name length greater than 60 characters");
			doctorFlag = false;
			System.out.println("NAME TOO LONG");
		}
		return doctorFlag;
	}

	private boolean validatePurpose() {
		purposeLBL.setText(null);
		System.out.println("VALIDATING PURPOSE");
		boolean purposeFlag = true;

		String purpose = purpOfPrescript.getText();
		purpose = purpose.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z\\.\\-\\']+(\\s?[a-zA-Z\\.\\-\\'])*$");// . represents single character
		Matcher m = p.matcher(purpose);
		boolean b = m.matches();

		if (purpose == null || purpose.equals(null) || purpose.equals("")) {
			purposeLBL.setText("Enter purpose of prescription");
			purposeFlag = false;
			System.out.println("PURPOSE IS EMPTY...");
		} else if (!b) {
			purposeLBL.setText("Remove numbers and special characters");
			System.out.println("PURPOSE CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			purposeFlag = false;
		}
		else if(purpose.length()>=60)
		{
			purposeLBL.setText("Purpose of prescription length greater than 60 characters");
			purposeFlag = false;
			System.out.println("PURPOSE TOO LONG");
		}
		return purposeFlag;
	}

	private boolean validateDate() {
		
		System.out.println("VALIDATING DATE...");

		datePrescribedLBL.setText(null);

		String dopString = DOPPicker.getEditor().getText();

		// rough check of dob format
		Pattern p = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");
		Matcher m = p.matcher(dopString);
		boolean b = m.matches();

		System.out.println("Date " + dopString);

		// date is empty
		if (dopString == null || dopString.equals(null) || dopString.equals("")) {
			datePrescribedLBL.setText("Please select or enter a date of prescription");
			System.out.println("DOP EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b) {
			System.out.println("INVALID DATE FORMAT");
			datePrescribedLBL.setText("Incorrect date format. Please use MM/DD/YYYY");
			return false;
		} else {

			try {
				// parse the date to see if it is a real date
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				df.setLenient(false);
				df.parse(dopString);

				// get current date and convert date into date object
				Date curDate = new Date();
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dopString);

				// date is after current date
				if (date.after(curDate)) {
					System.out.println("DATE CANNOT BE AFTER TODAY'S DATE");
					datePrescribedLBL.setText("Date of prescription cannot be after today's date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				datePrescribedLBL.setText("Incorrect date. Please enter a valid date");
				return false;
			}
		}
	}
	
	private boolean validateMedDescription() {
		System.out.println("VALIDATING MED DESCRIPTION...");
		
		lblDescript.setText(null);
		String details = medDescript.getText();
		
		if((details != null || !details.equals(null) || !details.equals("")) && details.length() >= 500)
		{
			System.out.println("DESCRIPTION TOO LONG");
			lblDescript.setText("Description exceeds maximum possible length");
			return false;
		}
		return true;
	}

}
