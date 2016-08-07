package Bussiness;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBLink {
	static Connection c = null;
	public DBLink() throws ClassNotFoundException, SQLException{
		
	    
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test");
	    
	}
	public void initializeDB() throws SQLException{
		
		
	      
	      Statement stmt = null;
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT name FROM sqlite_master WHERE type='table'" );
	      
	      if(!rs.next()){//Tables are not created
	    	  initUserTable();
	    	  initAccountTable();
	    	  initMessageTable();
	    	  initTransactionTable();
	    	  
	      }
	      
	      rs.close();
	      stmt.close();
	      
		
	}
	
	public void initUserTable() throws SQLException{
		Statement stmt = null;
		stmt = c.createStatement();
	      String sql = "CREATE TABLE User " +
	                   "(UserID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
	                   " Uname           CHAR(50) UNIQUE   NOT NULL," + 
	                   " Pwd            CHAR(50)     NOT NULL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	}
	
	public void initAccountTable() throws SQLException{
		Statement stmt = null;
		stmt = c.createStatement();
	      String sql = "CREATE TABLE Account " +
	                   "(AccountID CHAR(10) PRIMARY KEY NOT NULL," +
	                   " UserID     INTEGER    NOT NULL," +
	                   " CreateDate TIMESTAMP    NOT NULL," +
	                   " Amount     INTEGER     NOT NULL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	}
	
	public void initMessageTable() throws SQLException{
		Statement stmt = null;
		stmt = c.createStatement();
	      String sql = "CREATE TABLE Messages " +
	    		  		"(MessageID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
	                   " Message     TEXT    NOT NULL," +
	                   " CreateDate TIMESTAMP    NOT NULL," +
	                   " UserID  INTEGER    NOT NULL," +
	                   " Reply  TEXT  )"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	}
	
	public void initTransactionTable() throws SQLException{
		Statement stmt = null;
		stmt = c.createStatement();
	      String sql = "CREATE TABLE Transactions " +
	                   "(TransactionID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
	                   " UserID     INTEGER    NOT NULL," +
	                   " FromAccountID CHAR(10)    NOT NULL," +
	                   " ToAccountID CHAR(10)    NOT NULL," +
	                   " CreateDate TIMESTAMP    NOT NULL," +
	                   " Amount     INTEGER     NOT NULL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	}

	
	public boolean submitNewUser(String uname, String pwd) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);

	      
	      String sql = "INSERT INTO User (Uname,Pwd) " +
	                   "VALUES (?,?);";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1,uname);
	      stmt.setString(2, pwd);
	      int result=stmt.executeUpdate();
	      stmt.close();
	      c.commit();
	      if(result==1){
	    	  return true;
	      }
		return false;
		
	}

	public static boolean isAccountExsist() {
		// TODO Auto-generated method stub
		return false;
	}


	public static boolean createAccount(String AccountID,int UserID) throws SQLException{
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);

	      
	      String sql = "INSERT INTO Account (AccountID,UserID,CreateDate,Amount) " +
	                   "VALUES (?,?,?,?);";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1,AccountID);
	      stmt.setInt(2, UserID);
	      
	      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	      Date date = new Date();
	      stmt.setString(3, dateFormat.format(date));
	      stmt.setInt(4, 0);
	      int result=stmt.executeUpdate();
	      stmt.close();
	      c.commit();
	      if(result==1){
	    	  return true;
	      }
		return false;
	}
	
	public static boolean isUserExsists(String Uname) throws SQLException{
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);

	      
	      String sql = "SELECT * FROM User WHERE Uname = ? ";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1,Uname);
	      ResultSet result=stmt.executeQuery();
	      
	      c.commit();
	      if(result.next()){
	    	//  System.out.println(result.getString(1));
	    	  stmt.close();
	    	  return true;
	      }
	      //System.out.println(result.getString(1));
	      stmt.close();
		return false;
		
	}

	public static int CheckLogin(String Uname,String Pwd) throws SQLException{
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);

	      
	      String sql = "SELECT * FROM User WHERE Uname = ? and Pwd = ?";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1,Uname);
	      stmt.setString(2,Pwd);
	      ResultSet result=stmt.executeQuery();
	      
	      c.commit();
	      if(result.next()){
	    	System.out.println(result.getString(1));
	    	  int res=result.getInt(1);
	    	  stmt.close();
	    	  return res;
	      }
	      System.out.println("No out");
	      stmt.close();
		return -1;

		
		
	}
	public static boolean putMessage(String msg, int uid, Date date) throws SQLException {
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);
		
		 
	      
	      String sql = "INSERT INTO Messages (Message,CreateDate,UserID) " +
	                   "VALUES (?,?,?);";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1,msg);
	      
	      
	      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	      stmt.setString(2, dateFormat.format(date));
	      stmt.setInt(3, uid);
	      int result=stmt.executeUpdate();
	      stmt.close();
	      c.commit();
	      if(result==1){
	    	  return true;
	      }
		return false;

		
	}

	public static HashMap<String, Account> getAccounts(int userID) throws SQLException{
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);

	      
	      String sql = "SELECT * FROM Account WHERE UserID = ?";
	      stmt = c.prepareStatement(sql);
	      stmt.setInt(1,userID);
	      ResultSet result=stmt.executeQuery();
	      HashMap<String, Account> accounts=new HashMap<String,Account>();
	      c.commit();
	      while(result.next()){
	    	  Account temp=new Account(result.getDouble(4),result.getString(1),new Date(result.getString(3)));
	    	  
	    	  accounts.put(result.getString(1), temp);
	    	  
	    	  
	    	  
	    	  
	      }
	      stmt.close();
	      return accounts;
	}
	public static ArrayList<Transaction> getTransactions(String accountNo) throws SQLException {
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);
      
         
	      String sql = "SELECT * FROM Transactions WHERE FromAccountID= ? or ToAccountID= ?";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1,accountNo);
	      stmt.setString(2,accountNo);
	      ResultSet result=stmt.executeQuery();
	      ArrayList<Transaction> transactions=new ArrayList<Transaction>();
	      c.commit();
	      while(result.next()){
	    	  Transaction temp=new Transaction(result.getDouble(6),result.getString(3),result.getString(4),new Date(result.getString(5)));	  
	    	  transactions.add(temp);
	    	  
	      }
	      stmt.close();
	      return transactions;

	}
	public static boolean submitTransaction(Transaction tr_temp, User byuser) throws SQLException {
		
		PreparedStatement stmt = null;
		 c.setAutoCommit(false);

	      
	      String sql = "INSERT INTO Transactions (UserID,FromAccountID,ToAccountID,CreateDate,Amount) " +
	                   "VALUES (?,?,?,?,?);";
	      stmt = c.prepareStatement(sql);
	      stmt.setInt(1,byuser.getId());
	      stmt.setString(2, tr_temp.getFromAccount());
	      
	      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	      Date date = new Date();
	      stmt.setString(3, tr_temp.getToAccount());
	      stmt.setString(4, dateFormat.format(date));
	      stmt.setDouble(5, tr_temp.amount);
	      int result=stmt.executeUpdate();
	      stmt.close();
	      c.commit();
	      if(result==1){
	    	  return true;
	      }
		return false;

		
	}
	public static void deposit() {
		// TODO Auto-generated method stub
		
	}
	

	
}
