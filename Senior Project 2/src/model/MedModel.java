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
	StringProperty archivedBy;
	StringProperty archiveReason;
	StringProperty dateAdded;
	StringProperty addedBy;
	StringProperty medID;
	StringProperty dateUpdated;
	StringProperty updatedBy;

	
	
	//used for current patient
	public MedModel(String patientCode, String medName, String medDate, String doc, String purpose,
			String medDose, String doseType, String details, String dateAdded, String addedBy,
			String medID, String archiveDate, String archivedBy, String archiveReason, String dateUpdated, String updatedBy) {
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
		this.addedBy = new SimpleStringProperty(addedBy);
		this.medID = new SimpleStringProperty(medID);
		this.archiveDate = new SimpleStringProperty(archiveDate);
		this.archivedBy = new SimpleStringProperty(archivedBy);
		this.archiveReason = new SimpleStringProperty(archiveReason);
		this.dateUpdated = new SimpleStringProperty(dateUpdated);
		this.updatedBy = new SimpleStringProperty(updatedBy);

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
	
	public StringProperty getTypeAndAmount()
	{
		StringProperty doseAndType;
		String doseAndTypeString;
		String dose = getMedDosage().get();
		String type = getDoseType().get();
		doseAndTypeString = dose + " " + type;
		doseAndType = new SimpleStringProperty(doseAndTypeString);
		
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
	
	public StringProperty getAddedBY() {
		return addedBy;
	}

	public void setAddedBy(StringProperty addedBy) {
		this.addedBy = addedBy;
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
	
	public StringProperty getArchiveBy() {
		return archivedBy;
	}

	public void setArchiveBy(StringProperty archivedBy) {
		this.archivedBy = archivedBy;
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
	
	public StringProperty getUpdatedBy() {
		return updatedBy;
	}

	public void updatedBy(StringProperty updatedBy) {
		this.updatedBy = updatedBy;
	}


	
	public String toString() {
		return "CurMedModel [patientCode=" + patientCode + ", medName=" + medName + ", medDosage=" + medDosage + ", doseType= " + doseType + " \ndoc=" + doc + ", purpose=" + purpose
				+ ", date=" + date + ", \ndetails=" + details + ", dateAdded=" + dateAdded + ", medID=" + medID +
				", \narchiveDate=" + archiveDate + ", archiveReason=" + archiveReason + ", dateUpdated=" + dateUpdated + "]";
	}
	

	

}
