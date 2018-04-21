package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DoseModel {
	
	StringProperty dose;
	StringProperty type;
	StringProperty time;
	
	public DoseModel(String dose, String type, String time) {
		super();
		this.dose = new SimpleStringProperty(dose);
		this.type = new SimpleStringProperty(type);
		this.time = new SimpleStringProperty(time);
	}
	
	public DoseModel() {
		super();
	}

	
	public StringProperty getDose() {
		return dose;
	}

	public void setDose(StringProperty dose) {
		this.dose = dose;
	}
	
	public StringProperty getType() {
		return type;
	}

	public void setType(StringProperty type) {
		this.type = type;
	}
	
	public StringProperty getTime() {
		return time;
	}

	public void setTime(StringProperty time) {
		this.time = time;
	}
	
	public String toString() {
		return dose.get() + "_" +type.get() + "_" + time.get();
	}
}
