package courtreferences.model;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class LoginAuthenticator {
	private int userstatus;
	private String username;
	private ConnectionHandler connHndlr;
	
	public LoginAuthenticator(){
		
	}
	
	public LoginAuthenticator(String username){
		this.setUsername(username);
		this.setUserStatus(-1);
		this.setConnHndlr(null);
	}
	
	public int getUserStatus(){
		return this.userstatus;
	}
	
	public void setUserStatus(int status){
		this.userstatus = status;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int verifyAuthentication(String uname,String pswd){
		Statement stmt = null;
		if(this.getConnHndlr() == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		uname = uname.toLowerCase();
		this.setUsername(uname);
		String vfnQuery = "select l.user_role from LoginDetails l where l.username = '" + uname + "' AND l.password = '" + pswd + "'";
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(vfnQuery);
			if(!rs.next()){
				System.out.println("User doesnt exist");
				this.setUserStatus(-1);
			}
			else{
				String userrole = rs.getString(1);
				if(userrole.equals("admin")){
					this.setUserStatus(0);
				}
				else{
					this.setUserStatus(1);
				}
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		finally{
		}
		return this.getUserStatus(); 
	}
	
	public void logOutAction(){
		this.connHndlr.closeConnection();
	}

	public ConnectionHandler getConnHndlr() {
		return connHndlr;
	}

	public void setConnHndlr(ConnectionHandler connHndlr) {
		this.connHndlr = connHndlr;
	}
}
