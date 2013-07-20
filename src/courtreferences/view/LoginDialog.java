package courtreferences.view;

/* 
 * This class contains the components for prompting the user to enter login details
 * Purpose : Functionalities for getting the login credentials and authenticating the user
 */

import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import courtreferences.model.*;

import java.awt.Color;

public class LoginDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JPasswordField passwordField;
	private int userStatus;
	private String username;
	private JButton btnLogin;
	private JButton btnCancel;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblInvalidPassword;

	public void setUserStatus(int ustatus){
		this.userStatus = ustatus; 
	}
	
	public int getUserStatus(){
		return this.userStatus;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Create the dialog.
	 */
	public LoginDialog() {
		/*	Initialize all the components	*/
		initComponents();
		/*	Initialize the Event handlers	*/
		createEvents();
	}
	
	private void initComponents(){
		setResizable(false);
		setModal(true);
		setTitle("Login Window");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JPanel controlsPanel = new JPanel();
		controlsPanel.setToolTipText("controlsPanel");
		controlsPanel.setBounds(0, 24, 450, 187);
		getContentPane().add(controlsPanel);
		controlsPanel.setLayout(null);
		
		lblUsername = new JLabel("UserName");
		lblUsername.setBounds(59, 30, 93, 38);
		controlsPanel.add(lblUsername);
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(59, 96, 87, 45);
		controlsPanel.add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(181, 34, 145, 31);
		controlsPanel.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(180, 103, 145, 31);
		controlsPanel.add(passwordField);
		
		lblInvalidPassword = new JLabel("Invalid Credentials");
		lblInvalidPassword.setVisible(false);
		lblInvalidPassword.setForeground(Color.RED);
		lblInvalidPassword.setBackground(Color.LIGHT_GRAY);
		lblInvalidPassword.setBounds(59, 140, 208, 35);
		controlsPanel.add(lblInvalidPassword);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setToolTipText("buttonsPanel");
		buttonsPanel.setBounds(0, 212, 450, 76);
		getContentPane().add(buttonsPanel);
		buttonsPanel.setLayout(null);
		
		btnLogin = new JButton("LogIn");
		btnLogin.setBounds(72, 12, 106, 33);
		buttonsPanel.add(btnLogin);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(223, 12, 106, 33);
		buttonsPanel.add(btnCancel);
	}
	
	private void createEvents(){
		
		/* Gets the credentials in this window and passes to the model for authentication	*/
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verifyCredentials();
			}
		});
		
		/* Clicking on cancel button will just close the dialog box	*/
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButtonAction();
			}
		});
	}
	
	/*	The obtained credentials are obtained from the user through the dialog box and the result of the authenticity can be given to the main window	*/ 
	
	private void verifyCredentials(){
		LoginAuthenticator ln = new LoginAuthenticator();
		String username = textField.getText();
		String password = passwordField.getText();
		int userstatus = ln.verifyAuthentication(username,password);
		System.out.println("status of the user : " + userstatus);
		this.setUsername(username);
		this.setUserStatus(userstatus);
		displayStatus();
	}
	
	/*	When the credentials are wrong error message will be displayed
	 * 	When the credentials are right the dialog box will be closed
	 * */
	private void displayStatus(){
		if(this.getUserStatus() == -1){
			this.lblInvalidPassword.setVisible(true);
			this.lblInvalidPassword.setText("Invalid Credentials...");
		}
		else{
			System.out.println("Closing the window");
			this.textField.setText("");
			this.passwordField.setText("");
			this.dispose();
		}
	}
	
	/* Clicking on cancel button will just close the window	*/
	
	private void cancelButtonAction(){
		Window win = SwingUtilities.getWindowAncestor(this);
	      if (win != null) {
	         win.dispose();
	      }
	}
}