package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import courtreferences.model.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RemoveUserDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtUserName;
	private UpdateUserSettings updateObj = null;
	private JButton btnDeleteUser;
	private JButton cancelButton;
	private JLabel lblEnterUserName;

	 /**
	 * Create the dialog.
	 */
	public RemoveUserDialog() {
		initiateComponents();
		createEvents();
	}
	
	private void initiateComponents(){
		setResizable(false);
	 	setTitle("Remove User");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblEnterUserName = new JLabel("Enter User Name");
			lblEnterUserName.setBounds(52, 101, 149, 33);
			contentPanel.add(lblEnterUserName);
		}
		{
			txtUserName = new JTextField();
			txtUserName.setBounds(206, 104, 171, 27);
			contentPanel.add(txtUserName);
			txtUserName.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnDeleteUser = new JButton("RemoveUser");
				btnDeleteUser.setActionCommand("OK");
				buttonPane.add(btnDeleteUser);
				getRootPane().setDefaultButton(btnDeleteUser);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void createEvents(){
		btnDeleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeUserDetails();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
	}
	
	private void showFailureDeleteDialog(){
		String failure_msg = "The user " + this.txtUserName.getText() + " is not present in the system";
		JOptionPane.showMessageDialog(null,failure_msg);
	}
	
	private void showSuccessDeleteDialog(){
		String success_msg = "The user " + this.txtUserName.getText() + " has been succesfully removed the system";
		JOptionPane.showMessageDialog(null,success_msg);
	}
	
	private void removeUserDetails(){
		if(this.updateObj == null){
			this.updateObj = new UpdateUserSettings();
		}
		int returnvalue = this.updateObj.deleteExistingUser(this.txtUserName.getText());
		if(returnvalue == 0){
			showFailureDeleteDialog();
			clearValues();
		}
		else{
			showSuccessDeleteDialog();
			clearValues();
			this.dispose();
		}
	}
	
	private void clearValues(){
		this.txtUserName.setText("");
	}
	
	private void closeDialog(){
		this.dispose();
	}
}
