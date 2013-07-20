package courtreferences.view;

/* 
 * This class contains the components of the "UploadDocument Dialog box"
 * Purpose : Functionalities for uploading the documents and extract content from the files 
 */

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
		/*	Initializes the components required for collecting the details of the space where the folder which is needed to be uploaded is present	*/
		initComponents();
		/*	Initializes corresponding event handlers	*/
		createEvents();
	}
	
	/*	Initializes the components required for collecting the details of the space where the folder which is needed to be uploaded is present	*/
	
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
	
	/*	Initializes corresponding event handlers	*/
	
	private void createEvents(){
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		/*	Open the FileChooser dialog box when the mouse is clicked within the textbox	
		 * 	Get the directory path as input from the window
		*/
		
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
		
		/* Close the dialog box when the Cancel button is pressed	*/
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		/*  Pass the entire directory structure to the model.
		 * 	Process all the files in the directory 
		 * 	Enter the extracted entities from those pdfs into database
		 *  Close the dialog box once the process is done
		 */
		
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String countryname = (String) cmbCountryNames.getSelectedItem();
				String courtname = (String) cmbCourtNames.getSelectedItem();
				System.out.println("Selected country name : " + countryname + "  " + courtname);
				parseCaseDocuments(countryname,courtname);
				closeDialog();
			}
		});
		
		/*Do nothing when the state of Courts combo box is changed	*/
		
		cmbCourtNames.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		
		/*	Load the names of courts belonging to corresponding country when the country name is changed	*/
		
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
	
	/*	Method which invokes parsing the pdf content to extract the required information	*/
	
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
	
	/*	Gets the name of the courts to be loaded into the combo box for corresponding country	*/
	
	public Vector<String> getCourtComboValues(String countryname){
		CountryModel upObj = new CountryModel();
		this.setCourtNameVector(upObj.getCourtValues(countryname));
		return this.getCourtNameVector();
	}
	
	/* Gets the values of the Country name combox box	*/
	
	public void getCountryComboValues(){
		CountryModel upObj = new CountryModel();
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
