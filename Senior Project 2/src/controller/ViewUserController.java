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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

	private URL toPane;
	private AnchorPane temp;

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
		fnameTF.setDisable(false);
		lnameTF.setDisable(false);
		DOBPicker.setDisable(false);
		relationBox.setDisable(false);
		emailTF.setDisable(false);
		password1TF.setDisable(false);
		cancelBTN.setVisible(true);
		
		password1TF.textProperty().addListener((observable, oldValue, newValue) -> {
			password2TF.setText(null);
		    System.out.println("textfield changed from " + oldValue + " to " + newValue);
		    password2TF.setVisible(true);
		    lblVerifyPass.setVisible(true);
		    lblPassword2.setVisible(true);
		});
	}

	@FXML
	void submit(ActionEvent event) throws ParseException {

		// validate fields
		boolean validDOB, validFname, validLname, validRelation, validEmail, validPassword = false;
		validDOB = validateDOB();
		validFname = validateFName();
		validLname = validateLName();
		//validRelation = validateRelation();
		validEmail = validateEmail();
		validPassword = validatePassword();

		if (validDOB && validFname && validLname && validEmail && validPassword) {

			String firstName = fnameTF.getText();
			String lastName = lnameTF.getText();
			
			
			
			// grab date
			String DOB = DOBPicker.getEditor().getText();

			// format date for database
			Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
			Date DOBDate = new SimpleDateFormat("MM/dd/yyyy").parse(DOB);
			String formatDate = formatter2.format(DOBDate);
			System.out.println("Before format " + DOB + " After format " + formatDate);
			
			
			
			String relation = relationBox.getValue();
			String email = emailTF.getText();

			String query = "UPDATE user SET fname = ?, lname = ?, DOB = ?, pRelation = ?, "
					+ "email = ? WHERE userID = ?";

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
				ps.setString(6, LoginController.currentUserID);

				ps.execute();

				System.out.println("CHANGED USER INFO");

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

			btnSubmit.setVisible(false);
			fnameTF.setDisable(true);
			lnameTF.setDisable(true);
			DOBPicker.setDisable(true);
			relationBox.setDisable(true);
			emailTF.setDisable(true);
			password1TF.setDisable(true);
			btnEdit.setVisible(true);
			cancelBTN.setVisible(false);
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
		if (dobString.equals(null) || dobString.equals("")) {
			lblDOB.setText("Please enter a date");
			System.out.println("DOB EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b) {
			System.out.println("INVALID DATE FORMAT");
			lblDOB.setText("Invalid date format. MM/DD/YYYY");
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
					lblDOB.setText("Date cannot be after today's date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				lblDOB.setText("Incorrect date.");
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

		if (fname.equals(null) || fname.equals("")) {
			lblFname.setText("First name cannot be empty");
			System.out.println("FIRST NAME IS EMPTY...");
			return false;
		} else if (!b) {
			lblFname.setText("No numbers or special characters");
			System.out.println("FIRST NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
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

		if (lname.equals(null) || lname.equals("")) {
			lblLname.setText("Last name cannot be empty");
			System.out.println("LAST NAME IS EMPTY...");
			return false;
		} else if (!b) {
			lblLname.setText("No numbers or special characters");
			System.out.println("LAST NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			return false;
		}

		return true;
	}// end method

	private boolean validateRelation() {

		lblRelation.setText(null);
		System.out.println("VALIDATING RELATION");

		if (relationBox.getValue() == null) {
			lblRelation.setText("Please provide a relation");
			return false;
		}

		return true;
	}

	private boolean validateEmail() {

		System.out.println("VALIDATING EMAIL");
		lblEmail.setText(null);
		String email = emailTF.getText();
		// ^([a-zA-Z0-9~!$%^&*_=+}{\'?]+(\.[a-zA-Z0-9~!$%^&*_=+}{\'?]+)*@[a-zA-Z0-9_-]+(\.com|\.net|\.edu|\.gov|\.mil))$
		Pattern p = Pattern.compile("^([a-zA-Z0-9~!$%^&*_=+}{\\'?]+(\\.[a-zA-Z0-9~!$%^&*_=+}{\\'?]+)*@[a-zA-Z0-9_-]+(\\.com|\\.net|\\.edu|\\.gov|\\.mil))$");// . represents single character
		Matcher m = p.matcher(email);
		boolean b = m.matches();

		if (email.equals(null) || email.equals("")) {
			lblEmail.setText("Email cannot be empty");
			System.out.println("EMAIL IS EMPTY...");
			return false;
		} else if (!b) {
			lblEmail.setText("Invalid email format");
			System.out.println("EMAIL WAS NOT IN PROPER FORMAT");
			return false;
		}

		return true;
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

		if ((p1.equals(null) || p1.equals("")) && (p2.equals(null) || p2.equals(""))) {
			lblPassword1.setText("Password cannot be empty");
			lblPassword2.setText("Password cannot be empty");
			System.out.println("BOTH PASSWORDS EMPTY...");
			passwordFlag = false;
		} else if (p1.equals(null) || p1.equals("")) {
			lblPassword1.setText("Password cannot be empty");
			System.out.println("PASSWORD EMPTY...");
			passwordFlag = false;
		} else if (p1.length() < 4) {
			lblPassword1.setText("Password not long enough, must be longer than 4");
			System.out.println("PASSWORD < 4");
			passwordFlag = false;
		} else if (p2 == null || p2.equals(null) || p2.equals("")) {
			lblPassword2.setText("Password cannot be empty");
			System.out.println("PASSWORD EMPTY...");
			passwordFlag = false;
		} else if (!p1.equals(p2)) {
			lblPassword1.setText("Password does not match");
			lblPassword2.setText("Password does not match");
			System.out.println("PASSWORDS DON'T MATCH...");
			passwordFlag = false;
		}

		return passwordFlag;
	}

}
