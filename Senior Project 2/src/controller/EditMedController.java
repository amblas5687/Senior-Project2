package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import model.DoseModel;
import model.MedModel;

public class EditMedController {

	@FXML
	private AnchorPane content_view;

	@FXML
	private TextField medName;

	@FXML
	private TextField medDosage;

	@FXML
	private TextField prescribDoc;
	
	@FXML 
	private TextField medDoseMul;

	@FXML
	private Button btnSubmit;

	@FXML
	private Button cancelBTN;
	
	@FXML
	private RadioButton rbSingle;
	
	@FXML
	private RadioButton rbMultiple;

	@FXML
	private TextArea medDescript;

	@FXML
	private DatePicker DOPPicker;

	@FXML
	private TextField purpOfPrescript;
	
	@FXML
	private ComboBox<String> doseType;

	@FXML
	private Label nameLBL;

	@FXML
	private Label doseLBL;

	@FXML
	private Label doctorLBL;

	@FXML
	private Label purposeLBL;
	
	@FXML
	private Label lblDescript;
	
	@FXML 
	private Label lblFreq;
	
	TableView<DoseModel> freqTable = new TableView<DoseModel>();
	TableColumn<DoseModel, String> dose = new TableColumn<DoseModel, String>("Dose");
	TableColumn<DoseModel, String> type = new TableColumn<DoseModel, String>("Type");
	TableColumn<DoseModel, String> time = new TableColumn<DoseModel, String>("Time");
	
	MedModel tempMed = new MedModel();
	
	ObservableList<DoseModel> multipleMed = FXCollections.observableArrayList();	
	
	//lets you know if they selected and entered multiple medications to disable error checking
	private boolean multiMed = false;
	//counts how many times medDoseMul has been clicked;
	private int timeClick = 0;
	
	Stage stage;
	private URL toPane;
	private AnchorPane temp;

	private MedModel editMed;

	public void initialize() {
		System.out.println("*******EDIT MED*******");
		setMed();
		
		doseType.getItems().addAll("mg", "g", "kg", "oz", "tab", "tsp", "tbsp");
	}

	
	public void setMed() {
		CurrentMedsController test = new CurrentMedsController();
		editMed = test.getEdit();
		//System.out.println("MED TO EDIT... " + editMed);

		// prepopulate textfields
		medName.setText(editMed.getMedName().get());
		
		if(editMed.getMedDosage().get().contains("AM") || editMed.getMedDosage().get().contains("PM")) {
			rbSingle.setVisible(false);
			rbMultiple.setVisible(false);
			
			medDoseMul.setVisible(true);
			medDoseMul.setText(editMed.getMedDosage().get());
			
			rbMultiple.setSelected(true);
		} else {
			rbSingle.setVisible(false);
			rbMultiple.setVisible(false);
			
			medDosage.setVisible(true);
			doseType.setVisible(true);
			
			medDosage.setText(editMed.getMedDosage().get());
			doseType.setValue(editMed.getDoseType().get());
			
			rbSingle.setSelected(true);
		}
		
		medDescript.setText(editMed.getDetails().get());
		prescribDoc.setText(editMed.getDoc().get());
		purpOfPrescript.setText(editMed.getPurpose().get());

		// setting the datepicker
		String prescribDateString = editMed.getDate().get();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate prescribDate = LocalDate.parse(prescribDateString, formatter);
		DOPPicker.setValue(prescribDate);
		DOPPicker.setDisable(true);

	}

	@FXML
	void returnMain(ActionEvent event) {

		try {

			// Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/CurrentMedsView.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@FXML
	void addMultiple(MouseEvent event) {
		freqTable.getColumns().clear();
		timeClick++;
		
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
		
		if(timeClick == 1) {
			String dosage = editMed.getMedDosage().get();
			String[] times = dosage.split(", ");
			System.out.println(times.length);
			for(int i = 0; i < times.length; i++) {
				times[i] = times[i].replaceAll("_", " ");
				String[] sections = times[i].split(" ");
				System.out.println(sections);
				System.out.println(sections[0]);
				System.out.println(sections[1]);
				System.out.println(sections[2]);
				System.out.println(sections[3]);
				String dos = sections[0];
				String dosType = sections[1];
				String tim = sections[2] + sections[3];
				
				
				//set fields
				DoseModel tempDose = new DoseModel(dos, dosType, tim);
				multipleMed.add(tempDose);
				System.out.println(tempDose.getDose());
				//get fields
				dose.setCellValueFactory(cellData -> cellData.getValue().getDose());
				type.setCellValueFactory(cellData -> cellData.getValue().getType());
				time.setCellValueFactory(cellData -> cellData.getValue().getTime());
				
				freqTable.setItems(multipleMed);
			}
		}
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
			rbSingle.setVisible(true);
			rbMultiple.setSelected(false);
			rbMultiple.setVisible(true);
			lblFreq.setText("Medication Frequency:");
			
			freqTable.getColumns().clear();
			multipleMed.clear();
			medDoseMul.setVisible(false);
			medDoseMul.setText("");

		}

	}
	
	@FXML
	void selectFreq(ActionEvent event) {
		freqTable.getColumns().clear();
    	
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
    		lblFreq.setText("Medication Dosage:");
    		
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
	void submit(ActionEvent event) {

		boolean validName, validDose, validDoseType, validDoctor, validPurpose, validDescript = true;

		validName = validateName();
		validDose = validateDose();
		validDoseType = validateDoseType();
		validDoctor = validateDoctor();
		validPurpose = validatePurpose();
		validDescript = validateMedDescription();

		if (validName && validDose && validDoseType && validDoctor && validPurpose && validDescript) {
			String mName = medName.getText();
			String mDosage = medDosage.getText();
			String mDoseType = doseType.getValue();
			String mDescript = medDescript.getText();
			String pDoc = prescribDoc.getText();
			String pPurpose = purpOfPrescript.getText();
			String dateUpdated = java.time.LocalDate.now().toString();
			String updatedBy = LoginController.currentUserName;

			String query = "UPDATE currentMeds SET medName = ?, medDosage = ?, doseType = ?, medDescript = ?, "
					+ "prescribDoc = ?, purpPresrcipt = ?, dateUpdated = ?, updatedBy = ?" + "WHERE patientCode = ? AND medID = ?";

			Connection connection = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				connection = DataSource.getInstance().getConnection();

				ps = connection.prepareStatement(query);
				ps.setString(1, mName);
				ps.setString(2, mDosage);
				ps.setString(3, mDoseType);
				ps.setString(4, mDescript);
				ps.setString(5, pDoc);
				ps.setString(6, pPurpose);
				ps.setString(7, dateUpdated);
				ps.setString(8, updatedBy);

				// parameters from edit med to update by
				ps.setString(9, editMed.getPatientCode().get());
				ps.setString(10, editMed.getMedID().get());

				ps.execute();

				System.out.println("MEDICATION UPDATED");

				// go back to current meds
				try {

					// Replace content_view's current display with the view for this controller
					toPane = getClass().getResource("/view/CurrentMedsView.fxml");
					temp = FXMLLoader.load(toPane);
					content_view.getChildren().setAll(temp);

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {

				DBConfig.displayException(e);

				System.out.println("FAILED UPDATE");
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
			} // end finally
		}
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
