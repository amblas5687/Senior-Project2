package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ViewUserController {

	@FXML
	private TextField fnameTF;
	@FXML
	private TextField lnameTF;
	@FXML
	private DatePicker DOBPicker;
	@FXML
	private TextField emailTF;
	@FXML
	private PasswordField password1TF;
	@FXML
	private PasswordField password2TF;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnSubmit;
	@FXML
	private Button cancelBTN;
	@FXML
	private ComboBox<String> relationBox;
	@FXML
	private AnchorPane content_view;

	@FXML
	private Label lblFname;

	@FXML
	private Label lblLname;

	@FXML
	private Label lblDOB;

	@FXML
	private Label lblRelation;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblPassword1;

	@FXML
	private Label lblPassword2;

	@FXML
	private Label lblVerifyPass;

	@FXML
	private Label lblAll;

	private int count;
	private URL toPane;
	private AnchorPane temp;
	private Stage stage;

	private String currentEmail = "";

	public void initialize() {

		System.out.println("*******VIEW USER INFO*******");

		relationBox.getItems().addAll("Son/Daughter", "Spouse", "Grandchild", "Friend", "Medical Professional");

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String dob = "", relation = "";
		String query = "SELECT * FROM user WHERE userID = ?";

		try {
			connection = DataSource.getInstance().getConnection();

			ps = connection.prepareStatement(query);
			ps.setString(1, LoginController.currentUserID);
			rs = ps.executeQuery();

			while (rs.next()) {
				fnameTF.setText(rs.getString("fname"));
				lnameTF.setText(rs.getString("lname"));
				dob = rs.getString("DOB");
				emailTF.setText(rs.getString("email"));
				currentEmail = emailTF.getText();
				relation = rs.getString("pRelation");
				password1TF.setText(rs.getString("password"));
				password2TF.setText(rs.getString("password"));

			}

			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(dob, df);
			DOBPicker.setValue(date);
			relationBox.setValue(relation);

			System.out.println("PULLED USER INFO");

		} catch (SQLException e) {
			DBConfig.displayException(e);
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
		} // END FINALLY

	}// END METHOD

	@FXML
	void edit(ActionEvent event) {
		
		btnEdit.setVisible(false);
		btnSubmit.setVisible(true);
		cancelBTN.setVisible(true);
		
		fnameTF.setDisable(false);
		lnameTF.setDisable(false);
		DOBPicker.setDisable(false);
		relationBox.setDisable(false);
		emailTF.setDisable(false);
		password1TF.setDisable(false);

	}
	
    @FXML
    void clickPassword(MouseEvent event) {
    	
    	password2TF.setText(null);
    	lblPassword1.setText(null);
    	
    	// safely catches error by pop-up alert
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		//changes standard stage icon to logo
		stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/application/logo_wbg.png"));
		
		alert.setTitle("Change Password");
		alert.setHeaderText("Change Password Process");
		alert.getDialogPane().getStylesheets()
				.add(getClass().getResource("/application/application.css").toExternalForm());
		
		GridPane grid = new GridPane();
		Label label = new Label("Enter original password:  ");
		PasswordField passField = new PasswordField();
		passField.setPrefWidth(300);

		grid.add(label, 0, 0);
		grid.add(passField, 1, 0);
		alert.getDialogPane().setContent(grid);

		ButtonType btnSubmit = new ButtonType("Submit");
		ButtonType btnCancel = new ButtonType("Cancel");
		alert.getDialogPane().getButtonTypes().setAll(btnCancel, btnSubmit);
		
		if(!password2TF.isVisible()) {
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == btnSubmit) {
				
				String oldPass = passField.getText();
				
				if(password1TF.getText().equals(oldPass)) {
			    	
					password1TF.setEditable(true);
					password1TF.setText(null);
					
					password2TF.setVisible(true);
					lblVerifyPass.setVisible(true);
					lblPassword2.setVisible(true);
				} else {
					alert.hide();
					lblPassword1.setText("Incorrect original password");
				}
			}
		}
		

    }
    
    @FXML
    void disableEdit() {
    	
		btnEdit.setVisible(true);
		cancelBTN.setVisible(false);
		btnSubmit.setVisible(false);
		
		fnameTF.setDisable(true);
		lnameTF.setDisable(true);
		DOBPicker.setDisable(true);	
		relationBox.setDisable(true);
		emailTF.setDisable(true);
		password1TF.setDisable(true);

    }

	@FXML
	void submit(ActionEvent event) throws ParseException {

		count = 0;
		lblAll.setText(null);
		// validate fields
		boolean validDOB, validFname, validLname, validRelation, validEmail, validPassword = false;
		validDOB = validateDOB();
		validFname = validateFName();
		validLname = validateLName();
		// validRelation = validateRelation();
		validEmail = validateEmail();
		validPassword = validatePassword();

		if (count > 0) {
			lblAll.setText("Please fill in all fields");
		} else if (validDOB && validFname && validLname && validEmail && validPassword) {

			String firstName = fnameTF.getText().trim();
			String lastName = lnameTF.getText().trim();

			// grab date
			String DOB = DOBPicker.getEditor().getText();

			// format date for database
			Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
			Date DOBDate = new SimpleDateFormat("MM/dd/yyyy").parse(DOB);
			String formatDate = formatter2.format(DOBDate);
			System.out.println("Before format " + DOB + " After format " + formatDate);

			String relation = relationBox.getValue();
			String email = emailTF.getText();
			String password = password1TF.getText();

			String query = "UPDATE user SET fname = ?, lname = ?, DOB = ?, pRelation = ?, "
					+ "email = ?, password = ? WHERE userID = ?";

			Connection connection = null;
			PreparedStatement ps = null;

			try {
				connection = DataSource.getInstance().getConnection();

				ps = connection.prepareStatement(query);
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, formatDate);
				ps.setString(4, relation);
				ps.setString(5, email);
				ps.setString(6, password);
				ps.setString(7, LoginController.currentUserID);

				ps.execute();

				System.out.println("CHANGED USER INFO");
				password2TF.setVisible(false);
				lblPassword2.setVisible(false);
				// password2TF.setText(null);
				lblVerifyPass.setVisible(false);

			} catch (SQLException e) {
				DBConfig.displayException(e);
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

			} // end finally

			disableEdit();
		}
	}

	// user cancels submission
	@FXML
	void returnMain(ActionEvent event) {

		try {

			// Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/ViewUserInfo.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end method

	private boolean validateDOB() {

		lblDOB.setText(null);

		String dobString = DOBPicker.getEditor().getText();

		// rough check of dob format
		Pattern p = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");
		Matcher m = p.matcher(dobString);
		boolean b = m.matches();

		System.out.println("Date " + dobString);

		// date is empty
		if (dobString == null || dobString.equals(null) || dobString.equals("")) {
			lblDOB.setText("Please select or enter your date of birth");
			count++;
			System.out.println("DOB EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b) {
			System.out.println("INVALID DATE FORMAT");
			lblDOB.setText("Invalid date format. Please use MM/DD/YYYY");
			return false;
		} else {

			try {
				// parse the date to see if it is a real date
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				df.setLenient(false);
				df.parse(dobString);

				// get current date and convert date into date object
				Date curDate = new Date();
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dobString);

				// date is after current date
				if (date.after(curDate)) {
					System.out.println("DATE CANNOT BE AFTER TODAY'S DATE");
					lblDOB.setText("Date of birth cannot be after today's date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				lblDOB.setText("Incorrect date. Please enter a valid date");
				return false;
			}
		}

	}// end method

	private boolean validateFName() {

		lblFname.setText(null);
		System.out.println("VALIDATING FIRST NAME");

		String fname = fnameTF.getText();
		fname = fname.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z]+\\s?[a-zA-Z]*$");// . represents single character
		Matcher m = p.matcher(fname);
		boolean b = m.matches();

		if (fname == null || fname.equals(null) || fname.equals("")) {
			lblFname.setText("Enter your first name");
			count++;
			System.out.println("FIRST NAME IS EMPTY...");
			return false;
		} else if (!b) {
			lblFname.setText("Remove numbers and special characters");
			System.out.println("FIRST NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			return false;
		}
		else if(fname.length() >= 30)
		{
			lblFname.setText("First name length greater than 30 characters");
			System.out.println("FIRST NAME LONGER THAN 30");
			return false;
		}

		return true;

	}// end method

	private boolean validateLName() {

		lblLname.setText(null);
		System.out.println("VALIDATING LAST NAME");

		String lname = lnameTF.getText();
		lname = lname.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z]+((\\.\\s)|[\\.\\s\\-])?[a-zA-Z]*$");// . represents single character
		Matcher m = p.matcher(lname);
		boolean b = m.matches();

		if (lname ==  null || lname.equals(null) || lname.equals("")) {
			lblLname.setText("Enter your last name");
			count++;
			System.out.println("LAST NAME IS EMPTY...");
			return false;
		} else if (!b) {
			lblLname.setText("Remove numbers and special characters");
			System.out.println("LAST NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			return false;
		}
		else if(lname.length() >= 30)
		{
			lblLname.setText("Last name lenght greater than 30 characters");
			System.out.println("LAST NAME 30 CHARACTERS");
			return false;
		}

		return true;
	}// end method

	private boolean validateRelation() {

		lblRelation.setText(null);
		System.out.println("VALIDATING RELATION");

		if (relationBox.getValue() == null) {
			lblRelation.setText("Please select your relation to patient");
			count++;
			return false;
		}

		return true;
	}

	private boolean validateEmail() {

		System.out.println("VALIDATING EMAIL");
		lblEmail.setText(null);
		boolean takenEmail = true;
		String email = emailTF.getText();
		// ^([a-zA-Z0-9~!$%^&*_=+}{\'?]+(\.[a-zA-Z0-9~!$%^&*_=+}{\'?]+)*@[a-zA-Z0-9_-]+(\.com|\.net|\.edu|\.gov|\.mil))$
		Pattern p = Pattern.compile(
				"^([a-zA-Z0-9~!$%^&*_=+}{\\'?]+(\\.[a-zA-Z0-9~!$%^&*_=+}{\\'?]+)*@[a-zA-Z0-9_-]+(\\.com|\\.net|\\.edu|\\.gov|\\.mil))$");// .
																																			// represents
																																			// single
																																			// character
		Matcher m = p.matcher(email);
		boolean b = m.matches();

		if (email == null || email.equals(null) || email.equals("")) {
			lblEmail.setText("Enter email address");
			count++;
			System.out.println("EMAIL IS EMPTY...");
			return false;
		} else if (!b) {
			lblEmail.setText("Invalid email format for email address");
			System.out.println("EMAIL WAS NOT IN PROPER FORMAT");
			return false;
		}
		else if(email.length() >= 35)
		{
			lblEmail.setText("Email length greater than 35 characters");
			System.out.println("EMAIL LONGER THAN 35");
			return false;
		}

		// see if email taken
		if (!currentEmail.equals(email)) {
			takenEmail = checkUserExist();
		}
		if (takenEmail) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkUserExist() {

		System.out.println("CHECKING IF EMAIL IS TAKEN");
		lblEmail.setText(null);

		String email = emailTF.getText();

		Connection connection = null;
		PreparedStatement userEmail = null;
		ResultSet rs = null;
		boolean emailFlag = true;

		// check if patient code in database
		try {
			String emailQ = "SELECT email FROM user WHERE email = ?";

			connection = DataSource.getInstance().getConnection();
			userEmail = connection.prepareStatement(emailQ);
			userEmail.setString(1, email);
			rs = userEmail.executeQuery();

			if (rs.isBeforeFirst()) {
				System.out.println("USER EMAIL TAKEN");
				lblEmail.setText("Email is already in use. Please enter a different email address");
				emailFlag = false;

			}
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

			if (userEmail != null) {
				try {
					userEmail.close();
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

		return emailFlag;
	}

	private boolean validatePassword() {

		String p1 = password1TF.getText();
		String p2 = password2TF.getText();

		lblPassword1.setText(null);
		lblPassword2.setText(null);

		boolean passwordFlag = true;

		/*
		 * Pattern p = Pattern.compile("^$");// . represents single character Matcher m
		 * = p.matcher(email); boolean b = m.matches();
		 */

		if ((p1 == null || p1.equals(null) || p1.equals("")) && (p2 == null || p2.equals(null) || p2.equals(""))) {
			lblPassword1.setText("Enter your password");
			lblPassword2.setText("Enter password verification");
			count++;
			System.out.println("BOTH PASSWORDS EMPTY...");
			passwordFlag = false;
		} else if (p1 == null || p1.equals(null) || p1.equals("")) {
			lblPassword1.setText("Enter your password");
			count++;
			System.out.println("PASSWORD EMPTY...");
			passwordFlag = false;
		} else if (p1.length() < 4) {
			lblPassword1.setText("Your password must be at least 4 digits long");
			System.out.println("PASSWORD < 4");
			passwordFlag = false;
		} else if(p1.length() >= 35) {
			lblPassword1.setText("Your password must be less than 35 digits long");
			System.out.println("PASSWORD > 35");
			passwordFlag = false;
		} else if (p2 == null || p2.equals(null) || p2.equals("")) {
			lblPassword2.setText("Enter password verification");
			count++;
			System.out.println("PASSWORD EMPTY...");
			passwordFlag = false;
		} else if (!p1.equals(p2)) {
			// lblPassword1.setText("Password does not match");
			lblPassword2.setText("Mismatched passwords");
			System.out.println("PASSWORDS DON'T MATCH...");
			passwordFlag = false;
		}

		return passwordFlag;
	}

}
