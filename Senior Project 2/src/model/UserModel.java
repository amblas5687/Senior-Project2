package model;

public class UserModel {
	
	    private String fname;
	    private String lname;
	    private String DOB;
	    private String email;
	    private String password;
	    private String relation;
	    
	    
		public UserModel(String fname, String lname, String dOB, String email, String password, String relation) {
			super();
			this.fname = fname;
			this.lname = lname;
			DOB = dOB;
			this.email = email;
			this.password = password;
			this.relation = relation;
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


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}


		public String getPassword() {
			return password;
		}


		public void setPassword(String password) {
			this.password = password;
		}


		public String getRelation() {
			return relation;
		}


		public void setRelation(String relation) {
			this.relation = relation;
		}


		@Override
		public String toString() {
			return "UserModel [fname=" + fname + ", lname=" + lname + ", DOB=" + DOB + ", email=" + email
					+ ", password=" + password + ", relation=" + relation + "]";
		}


}
