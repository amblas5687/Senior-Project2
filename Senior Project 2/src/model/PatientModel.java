package model;

public class PatientModel {
	
	private String fname;
	private String lname;
	private String DOB;
	private String doctor;
	private String stage;
	private String diagnosesDate;
	private String cargiver;
	private String patientCode;
	
	
	
	public PatientModel(String fname, String lname, String dOB, String doctor, String stage, String diagnosesDate,
			String cargiver, String patientCode) {
		super();
		this.fname = fname;
		this.lname = lname;
		DOB = dOB;
		this.doctor = doctor;
		this.stage = stage;
		this.diagnosesDate = diagnosesDate;
		this.cargiver = cargiver;
		this.patientCode = patientCode;
	}

	public PatientModel() {
		super();
	}




	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getDiagnosesDate() {
		return diagnosesDate;
	}

	public void setDiagnosesDate(String diagnosesDate) {
		this.diagnosesDate = diagnosesDate;
	}

	public String getCargiver() {
		return cargiver;
	}

	public void setCargiver(String cargiver) {
		this.cargiver = cargiver;
	}
	
	public String getPatientCode() {
		return patientCode;
	}

	public void setPatientCode(String patientCode) {
		this.patientCode = patientCode;
	}
	
	@Override
	public String toString() {
		return "PatientModel [fname=" + fname + ", lname=" + lname + ", DOB=" + DOB + ", doctor=" + doctor
				+ ", stage=" + stage + ", diagnosesDate=" + diagnosesDate + ", cargiver=" + cargiver 
				+ ", patientCode=" + patientCode + "]";
	}
	
	
}
