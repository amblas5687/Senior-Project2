package model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MedModel {
	
	StringProperty medName;
	StringProperty medDosage;
	StringProperty doc;
	StringProperty purpose;
	StringProperty date;
	StringProperty details;
	StringProperty archiveDate;
	StringProperty archiveReason;
	
	
	//used for current patient
	public MedModel(String medName, String medDate, String doc, String purpose,
			String medDose, String details) {
		super();
		this.medName = new SimpleStringProperty(medName);
		this.medDosage = new SimpleStringProperty(medDose);
		this.doc = new SimpleStringProperty(doc);
		this.purpose = new SimpleStringProperty(purpose);
		this.date = new SimpleStringProperty(medDate);
		this.details = new SimpleStringProperty(details);
	}
	
	
	//for archived meds
	public MedModel(String medName, String medDose, String doc, String purpose,
			String medDate, String details, String archiveDate, String archiveReason) {
		super();
		this.medName = new SimpleStringProperty(medName);
		this.medDosage = new SimpleStringProperty(medDose);
		this.doc = new SimpleStringProperty(doc);
		this.purpose = new SimpleStringProperty(purpose);
		this.date = new SimpleStringProperty(medDate);
		this.details = new SimpleStringProperty(details);
		this.archiveDate = new SimpleStringProperty(archiveDate);;
		this.archiveReason = new SimpleStringProperty(archiveReason);;
	}




	public MedModel() {
		super();
	}

	public StringProperty getMedName() {
		return medName;
	}

	public void setMedName(StringProperty medName) {
		this.medName = medName;
	}

	public StringProperty getMedDosage() {
		return medDosage;
	}

	public void setMedDosage(StringProperty medDosage) {
		this.medDosage = medDosage;
	}

	public StringProperty getDoc() {
		return doc;
	}

	public void setDoc(StringProperty doc) {
		this.doc = doc;
	}

	public StringProperty getPurpose() {
		return purpose;
	}

	public void setPurpose(StringProperty purpose) {
		this.purpose = purpose;
	}

	public StringProperty getDate() {
		return date;
	}

	public void setDate(StringProperty date) {
		this.date = date;
	}

	public StringProperty getDetails() {
		return details;
	}

	public void setDetails(StringProperty details) {
		this.details = details;
	}
	
	public StringProperty getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(StringProperty archiveDate) {
		this.archiveDate = archiveDate;
	}

	public StringProperty getArchiveReason() {
		return archiveReason;
	}

	public void setArchiveReason(StringProperty archiveReason) {
		this.archiveReason = archiveReason;
	}


	
	public String toString() {
		return "CurMedModel [medName=" + medName + ", medDosage=" + medDosage + ", doc=" + doc + ", purpose=" + purpose
				+ ", date=" + date + ", details=" + details + 
				"archiveDate=" + archiveDate + "archiveReason=" + archiveReason + "]";
	}
	

	

}
