package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.MedModel;

public class ArcMedDetailsController {

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
	private DatePicker DOPPicker;

	@FXML
	private DatePicker ArchivePicker;

	@FXML
	private Button btnOK;

	@FXML
	private TextArea reasonTF;
	
    @FXML
    private TextField archiveByTF;

	private MedModel medDetails;
	
	

	public void initialize() {

		System.out.println("*******VIEW ARCHIVE DETAILS*******");

		ArchivedMedsController test = new ArchivedMedsController();
		medDetails = test.getDetail();
		//System.out.println("SETTING MED DETAILS FOR... " + medDetails);
		getDetails();
	}

	private void getDetails() {
		
		medName.setText(medDetails.getMedName().get());

		// concatenate dose and type
		String dosage = medDetails.getMedDosage().get() + " " + medDetails.getDoseType().get();

		medDosage.setText(dosage);
		medDescript.setText(medDetails.getDetails().get());
		prescribDoc.setText(medDetails.getDoc().get());		
		purpOfPrescript.setText(medDetails.getPurpose().get());
		reasonTF.setText(medDetails.getArchiveReason().get());

		// setting the datepicker DOP
		String prescribDateString = medDetails.getDate().get();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate prescribDate = LocalDate.parse(prescribDateString, formatter);
		DOPPicker.setValue(prescribDate);

		// setting the datepicker archive date
		String archiveDate = medDetails.getArchiveDate().get();
		if (archiveDate != null) {
			LocalDate archive = LocalDate.parse(archiveDate, formatter);
			ArchivePicker.setValue(archive);
		}
		archiveByTF.setText(medDetails.getArchiveBy().get());
	}

	@FXML
	void stopViewing(ActionEvent event) {

		System.out.println("CLOSING ARCHIVE DETAIL WINDOW...");

		Stage detailStage = (Stage) btnOK.getScene().getWindow();
		// do what you have to do
		detailStage.close();

	}

}
