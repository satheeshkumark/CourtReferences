package courtreferences.view;

/*
 * This class contains the components for the login dialog box
 * A login dialog box which prompts the user name and password will be displayed
 * This class has functionalities which authenticates user by communicating with the model 
 */

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import courtreferences.model.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginForm extends JInternalFrame {
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel label;
	private JButton btnLogIn;
	private JButton btnClear;
	private int userStatus;
	private String username;

	/**
	 * Create the frame.
	 */
	public LoginForm() {
		initComponents();
		createEvents();
	}
	
	public void setUserStatus(int ustatus){
		this.userStatus = ustatus; 
	}
	
	public int getUserStatus(){
		return Integer.parseInt(this.textField.getText());
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	private void initComponents(){
		setClosable(true);
		setTitle("Login Screen");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 24, 416, 148);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		label = new JLabel("User Name");
		label.setBounds(31, 38, 132, 26);
		panel.add(label);
		
		JLabel label_1 = new JLabel("Password");
		label_1.setBounds(31, 91, 115, 26);
		panel.add(label_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(170, 42, 142, 26);
		panel.add(textField);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(170, 95, 142, 26);
		panel.add(passwordField);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 176, 416, 61);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		btnLogIn = new JButton("LogIn");
		btnLogIn.setBounds(40, 12, 117, 25);
		panel_1.add(btnLogIn);
		
		btnClear = new JButton("Clear");
		btnClear.setBounds(186, 12, 117, 25);
		panel_1.add(btnClear);
	}
	
	private void createEvents(){
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*	Communicates with the model by passing user name and password to check the authenticity of the user */
				LoginAuthenticator ln = new LoginAuthenticator();
				String username = textField.getText();
				String password = passwordField.getText();
				int userstatus = ln.verifyAuthentication(username,password);
				System.out.println("status of the user : " + userstatus);
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				passwordField.setText("");
			}
		});
		
	}
}
