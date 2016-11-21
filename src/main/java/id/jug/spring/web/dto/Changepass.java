package id.jug.spring.web.dto;

/**
 * Created by galih.lasahido@gmail.com
 */
public class Changepass {

	private String password;
	private String newpassword;
	private String confirmpassword;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public String getConfirmpassword() {
		return confirmpassword;
	}
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
	@Override
	public String toString() {
		return "Changepass [password=" + password + ", newpassword=" + newpassword + ", confirmpassword="
				+ confirmpassword + ", getPassword()=" + getPassword() + ", getNewpassword()=" + getNewpassword()
				+ ", getConfirmpassword()=" + getConfirmpassword() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
