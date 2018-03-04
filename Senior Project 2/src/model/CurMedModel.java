package model;

import java.sql.Date;
import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CurMedModel {
	
	StringProperty medName;
	StringProperty medDosage;
	StringProperty doc;
	StringProperty purpose;
	StringProperty date;
	StringProperty details;
	
	
	public CurMedModel(String medName, String medDate, String doc, String purpose,
			String medDose, String details) {
		super();
		this.medName = new SimpleStringProperty(medName);
		this.medDosage = new SimpleStringProperty(medDose);
		this.doc = new SimpleStringProperty(doc);
		this.purpose = new SimpleStringProperty(purpose);
		this.date = new SimpleStringProperty(medDate);
		this.details = new SimpleStringProperty(details);
	}

	public CurMedModel() {
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
	
	public String toString() {
		return "CurMedModel [medName=" + medName + ", medDosage=" + medDosage + ", doc=" + doc + ", purpose=" + purpose
				+ ", date=" + date + ", details=" + details + "]";
	}
	

	

}
