package model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MedModel {
	
	StringProperty patientCode;
	StringProperty medName;
	StringProperty medDosage;
	StringProperty doseType;
	StringProperty doc;
	StringProperty purpose;
	StringProperty date;
	StringProperty details;
	StringProperty archiveDate;
	StringProperty archiveReason;
	StringProperty dateAdded;
	StringProperty medID;
	StringProperty dateUpdated;

	
	
	//used for current patient
	public MedModel(String patientCode, String medName, String medDate, String doc, String purpose,
			String medDose, String doseType, String details, String dateAdded, 
			String medID, String archiveDate, String archiveReason, String dateUpdated) {
		super();
		this.patientCode = new SimpleStringProperty(patientCode);
		this.medName = new SimpleStringProperty(medName);
		this.medDosage = new SimpleStringProperty(medDose);
		this.doseType = new SimpleStringProperty(doseType);
		this.doc = new SimpleStringProperty(doc);
		this.purpose = new SimpleStringProperty(purpose);
		this.date = new SimpleStringProperty(medDate);
		this.details = new SimpleStringProperty(details);
		this.dateAdded = new SimpleStringProperty(dateAdded);
		this.medID = new SimpleStringProperty(medID);
		this.archiveDate = new SimpleStringProperty(archiveDate);
		this.archiveReason = new SimpleStringProperty(archiveReason);
		this.dateUpdated = new SimpleStringProperty(dateUpdated);

	}

	public MedModel() {
		super();
	}

	
	public StringProperty getPatientCode() {
		return patientCode;
	}

	public void setPatientCode(StringProperty patientCode) {
		this.patientCode = patientCode;
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
	
	public StringProperty getDoseType() {
		return doseType;
	}

	public void setDoseType(StringProperty doseType) {
		this.doseType = doseType;
	}
	
	public String getTypeAndAmount()
	{
		String doseAndType;
		String dose = getMedDosage().get();
		String type = getDoseType().get();
		doseAndType = dose + " " + type;
		
		return doseAndType;
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
	
	public StringProperty getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(StringProperty dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public StringProperty getMedID() {
		return medID;
	}

	public void setMedID(StringProperty medID) {
		this.medID = medID;
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
	
	public StringProperty getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(StringProperty dateUpdated) {
		this.dateUpdated = dateUpdated;
	}


	
	public String toString() {
		return "CurMedModel [patientCode=" + patientCode + ", medName=" + medName + ", medDosage=" + medDosage + ", doseType= " + doseType + " \ndoc=" + doc + ", purpose=" + purpose
				+ ", date=" + date + ", \ndetails=" + details + ", dateAdded=" + dateAdded + ", medID=" + medID +
				", \narchiveDate=" + archiveDate + ", archiveReason=" + archiveReason + ", dateUpdated=" + dateUpdated + "]";
	}
	

	

}
