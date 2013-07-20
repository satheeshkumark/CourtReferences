package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class DBConfiguration extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String dbconfigfile = new String("src/courtreferences/resources/dbconfig.txt");
	private final JPanel contentPanel = new JPanel();
	private JTextField txtHostName;
	private JTextField txtPortNumber;
	private JTextField txtDbName;
	private JTextField txtUserName;
	private JPasswordField passwordField;
	private JLabel lblHostName;
	private JLabel lblPortnumber;
	private JLabel lblDatabasename;
	private JLabel lblUserName;
	private JLabel lblPassword;
	private JButton UpdateButton;
	private JButton cancelButton;
	
	/**
	 * Create the dialog.
	 */
	public DBConfiguration() {
		setResizable(false);
		initComponents();
		loadDefaultValues();
		createEvents();
	}
	
	private void initComponents(){
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblHostName = new JLabel("HostName");
			lblHostName.setBounds(38, 12, 143, 37);
			contentPanel.add(lblHostName);
		}
		{
			lblPortnumber = new JLabel("PortNumber");
			lblPortnumber.setBounds(38, 51, 159, 42);
			contentPanel.add(lblPortnumber);
		}
		{
			lblDatabasename = new JLabel("DatabaseName");
			lblDatabasename.setBounds(38, 101, 159, 29);
			contentPanel.add(lblDatabasename);
		}
		{
			lblUserName = new JLabel("UserName");
			lblUserName.setBounds(38, 136, 159, 29);
			contentPanel.add(lblUserName);
		}
		{
			lblPassword = new JLabel("Password");
			lblPassword.setBounds(38, 173, 126, 37);
			contentPanel.add(lblPassword);
		}
		{
			txtHostName = new JTextField();
			txtHostName.setBounds(188, 21, 219, 29);
			contentPanel.add(txtHostName);
			txtHostName.setColumns(10);
		}
		{
			txtPortNumber = new JTextField();
			txtPortNumber.setBounds(188, 60, 219, 33);
			contentPanel.add(txtPortNumber);
			txtPortNumber.setColumns(10);
		}
		{
			txtDbName = new JTextField();
			txtDbName.setBounds(188, 107, 219, 29);
			contentPanel.add(txtDbName);
			txtDbName.setColumns(10);
		}
		{
			txtUserName = new JTextField();
			txtUserName.setBounds(188, 141, 219, 29);
			contentPanel.add(txtUserName);
			txtUserName.setColumns(10);
		}
		{
			passwordField = new JPasswordField();
			passwordField.setBounds(188, 179, 219, 31);
			contentPanel.add(passwordField);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				UpdateButton = new JButton("Update");
				UpdateButton.setActionCommand("OK");
				buttonPane.add(UpdateButton);
				getRootPane().setDefaultButton(UpdateButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void loadDefaultValues(){
		Scanner sc = null;
		try{			
			sc = new Scanner(new FileReader(DBConfiguration.dbconfigfile));	
			String hostname = sc.nextLine();
			String port 	= sc.nextLine();
			String dbname 	= sc.nextLine();
			String username = sc.nextLine();
			String password = sc.nextLine();
			this.txtHostName.setText(hostname);
			this.txtPortNumber.setText(port);
			this.txtDbName.setText(dbname);
			this.txtUserName.setText(username);
			this.passwordField.setText(password);
		}	
		catch(IOException e){
			System.out.println("IO Exception " + e.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception: " + e.getMessage());
		}
		finally{
			sc.close();			
		}
	}
	
	private void createEvents(){
		UpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = "Confirm DB Settings Update";
				String message = "Are you sure the DB Settings should be updated?";
				int reply = JOptionPane.showConfirmDialog(null,title,message,JOptionPane.YES_NO_OPTION);
				if(reply == 0){
					updateDbConfigFile();
					showSuccessDialog();
				}
				closeDialog();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
	}
	
	private void showSuccessDialog(){
		JOptionPane.showMessageDialog(null,"The Database settings are updated");
	}
	
	private void closeDialog(){
		this.dispose();
	}
	
	private void updateDbConfigFile(){
		FileWriter fstream;
		try {
			fstream = new FileWriter(DBConfiguration.dbconfigfile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(this.txtHostName.getText().toString()+"\n");
			out.write(this.txtPortNumber.getText().toString()+"\n");
			out.write(this.txtDbName.getText().toString()+"\n");
			out.write(this.txtUserName.getText().toString()+"\n");
			out.write(this.passwordField.getText().toString()+"\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while updating");
			e.printStackTrace();
		}       
	}
}
