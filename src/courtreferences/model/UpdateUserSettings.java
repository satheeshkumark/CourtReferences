package courtreferences.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class UpdateUserSettings {
	private Statement stmt = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private ConnectionHandler connHndlr = null;
	
	public UpdateUserSettings(){
		
	}	
	
	public int insertNewUser(String uname,String pswd,String role){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = this.connHndlr.getConn();
		int returnvalue = 0;
		
		uname = uname.toLowerCase();		
		try{
			String vfnQuery = "select * from LoginDetails l where l.username = '" + uname + "'";
			this.stmt = conn.createStatement();
			this.resultSet = this.stmt.executeQuery(vfnQuery);
			if(!this.resultSet.next()){
				String insQuery = "insert into LoginDetails(username,password,user_role) values(?,?,?)";
				this.preparedStatement = conn.prepareStatement(insQuery);
				this.preparedStatement.setString(1,uname);
				this.preparedStatement.setString(2,pswd);
				this.preparedStatement.setString(3,role);
				this.preparedStatement.executeUpdate();
				returnvalue = 1;
			}
			else{
				returnvalue = 0;
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return returnvalue;
	}
	
	public int deleteExistingUser(String uname){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = this.connHndlr.getConnection();
		int returnvalue = 0;
		
		uname = uname.toLowerCase();		
		try{
			String vfnQuery = "select * from LoginDetails l where l.username = '" + uname + "'";
			this.stmt = conn.createStatement();
			this.resultSet = this.stmt.executeQuery(vfnQuery);
			if(this.resultSet.next()){
				String delQuery = "delete from LoginDetails where username = '" + uname + "'" ;
				this.preparedStatement = conn.prepareStatement(delQuery);
				this.preparedStatement.executeUpdate();
				returnvalue = 1;
			}
			else{
				returnvalue = 0;
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return returnvalue;
	}
	
	public int updatePassword(String uname,String pswd,String newpaswd){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = this.connHndlr.getConnection();
		int returnvalue = 0;
		
		uname = uname.toLowerCase();		
		try{
			String vfnQuery = "select * from LoginDetails l where l.username = '" + uname + "' AND l.password = '" + pswd + "'";
			this.stmt = conn.createStatement();
			this.resultSet = this.stmt.executeQuery(vfnQuery);
			if(this.resultSet.next()){
				String updtQuery = "update LoginDetails set password = ? where username = ?";
				this.preparedStatement = conn.prepareStatement(updtQuery);
				this.preparedStatement.setString(1,pswd);
				this.preparedStatement.setString(2,uname);
				this.preparedStatement.executeUpdate();
				returnvalue = 1;
			}
			else{
				returnvalue = 0;
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return returnvalue;
	}
	
	public Vector<String> getCountryValues(){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		this.stmt = null;
		Connection conn = this.connHndlr.getConnection();
		Vector<String> countryNames = new Vector<String>();
		this.resultSet = null;
		String getCntryNamesQuery = "select CountryName from CountryDetails";
		try{
			stmt = conn.createStatement();
			this.resultSet = stmt.executeQuery(getCntryNamesQuery);
			while(this.resultSet.next()){
			    String cntryname = resultSet.getString("CountryName");
			    countryNames.add(cntryname);
			}
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return countryNames;
	}
	
	public Vector<String> getCourtValues(String countryname){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Statement stmt = null;
		Statement stmt1 = null;
		Connection conn = this.connHndlr.getConnection();
		Vector<String> courtNames = new Vector<String>();
		int cntryid = 0;
		String getCntryIdQuery = "select CountryId from CountryDetails where CountryName ='" + countryname + "'";
		try{
			stmt = conn.createStatement();
			this.resultSet = stmt.executeQuery(getCntryIdQuery);
			if(this.resultSet.next()){
			    cntryid = this.resultSet.getInt("CountryId");
			    String getCourtDetailsQuery = "select CourtName from CourtDetails where CountryId = " + cntryid;
			    stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery(getCourtDetailsQuery);
				while(rs1.next()){
				    String courtname = rs1.getString("CourtName");
				    courtNames.add(courtname);
				}
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return courtNames;
	}
}
