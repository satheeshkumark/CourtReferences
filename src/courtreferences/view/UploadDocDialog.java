package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;


import javax.swing.JComboBox;

import courtreferences.model.*;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class UploadDocDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFileChooser jFileChooser;
	private JTextField textField;
	private JLabel lblChooseYourDocument;
	private JButton btnUpload;
	private JButton btnCancel;
	private ParsePdfDoc parseObj;
	private Vector<String> CountryNameVector;
	private Vector<String> CourtNameVector;
	private JComboBox cmbCourtNames;
	private JComboBox cmbCountryNames;
	private JLabel lblChooseTheCourt;
	private JLabel lblChooseTheCountry;
	
	/**
	 * Create the dialog.
	 */
	public UploadDocDialog() {
		setResizable(false);
		initComponents();
		createEvents();
	}
	
	private void initComponents(){
		this.CountryNameVector = new Vector<String>();
		setBounds(100, 100, 603, 393);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblChooseYourDocument = new JLabel("Choose your document");
		lblChooseYourDocument.setBounds(313, 40, 193, 43);
		contentPanel.add(lblChooseYourDocument);
		
		textField = new JTextField();
		textField.setBounds(33, 46, 218, 31);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		btnUpload = new JButton("Upload");
		btnUpload.setBounds(107, 280, 100, 30);
		contentPanel.add(btnUpload);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(269, 280, 114, 30);
		contentPanel.add(btnCancel);
		
		lblChooseTheCountry = new JLabel("Choose the country");
		lblChooseTheCountry.setBounds(313, 122, 193, 37);
		contentPanel.add(lblChooseTheCountry);
		
		this.getCountryComboValues();
		
		cmbCountryNames = new JComboBox(this.CountryNameVector);
		cmbCountryNames.setBounds(33, 122, 218, 31);
		contentPanel.add(cmbCountryNames);
		
		lblChooseTheCourt = new JLabel("Choose the Court");
		lblChooseTheCourt.setBounds(313, 195, 162, 30);
		contentPanel.add(lblChooseTheCourt);
		
		cmbCourtNames = new JComboBox();
		cmbCourtNames.setBounds(33, 195, 218, 30);
		contentPanel.add(cmbCourtNames);
	}
	
	private void createEvents(){
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				jFileChooser = new JFileChooser();
				jFileChooser.setCurrentDirectory(new java.io.File("."));
				jFileChooser.setDialogTitle("choosertitle");
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jFileChooser.setAcceptAllFileFilterUsed(false);
				if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                File directoryPath = jFileChooser.getCurrentDirectory();
	                textField.setText(directoryPath.getPath());
	                System.out.println("Name of the directory : " + directoryPath.getPath());
	            }
				else {
	            }				
			}
		});	
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String countryname = (String) cmbCountryNames.getSelectedItem();
				String courtname = (String) cmbCourtNames.getSelectedItem();
				System.out.println("Selected country name : " + countryname + "  " + courtname);
				parseCaseDocuments(countryname,courtname);
				closeDialog();
			}
		});
		
		cmbCourtNames.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		
		cmbCountryNames.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String countryname = (String) cmbCountryNames.getSelectedItem();
				Vector<String> courtNameVector = getCourtComboValues(countryname);
				cmbCourtNames.removeAllItems();
				Iterator<String> itr = courtNameVector.iterator();
				while(itr.hasNext()){
					String courtname = (String) itr.next();
					cmbCourtNames.addItem(courtname);
				}
			}
		});
	}
	
	private void parseCaseDocuments(String countryname,String courtname){
		File dir = new File(this.textField.getText());
		for(File child : dir.listFiles()){
			if(this.parseObj == null){
				this.parseObj = new ParsePdfDoc(countryname,courtname);
			}
			System.out.println("Processing input file :" + child.getAbsolutePath());
			this.parseObj.processPDFForCaseDetails(child.getAbsolutePath());
		}
	}
	
	private void closeDialog(){
		Window win = SwingUtilities.getWindowAncestor(this);
	      if (win != null) {
	         win.dispose();
	      }
	}
	
	public Vector<String> getCourtComboValues(String countryname){
		/*Statement stmt = null;
		Statement stmt1 = null;
		Connection conn = ConnectionHandler.getConnection();
		Vector<String> v = new Vector<String>();
		int cntryid = 0;
		String getCntryIdQuery = "select CountryId from CountryDetails where CountryName ='" + countryname + "'";
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getCntryIdQuery);
			if(rs.next()){
			    cntryid = rs.getInt("CountryId");
			    String getCourtDetailsQuery = "select CourtName from CourtDetails where CountryId = " + cntryid;
			    stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery(getCourtDetailsQuery);
				while(rs1.next()){
				    String courtname = rs1.getString("CourtName");
				    v.add(courtname);
				}
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}*/
		UpdateUserSettings upObj = new UpdateUserSettings();
		this.setCourtNameVector(upObj.getCourtValues(countryname));
		return this.getCourtNameVector();
	}
	
	public void getCountryComboValues(){
		/*
		Statement stmt = null;
		Connection conn = ConnectionHandler.getConnection();
		Vector<String> v = new Vector<String>();
		String getCntryNamesQuery = "select CountryName from CountryDetails";
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getCntryNamesQuery);
			while(rs.next()){
			    String cntryname = rs.getString("CountryName");
			    v.add(cntryname);
			}
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}*/
		UpdateUserSettings upObj = new UpdateUserSettings();
		this.setCountryNameVector(upObj.getCountryValues());
	}

	public Vector<String> getCountryNameVector() {
		return CountryNameVector;
	}

	public void setCountryNameVector(Vector<String> countryNameVector) {
		CountryNameVector = countryNameVector;
	}

	public Vector<String> getCourtNameVector() {
		return CourtNameVector;
	}

	public void setCourtNameVector(Vector<String> courtNameVector) {
		CourtNameVector = courtNameVector;
	}
}
