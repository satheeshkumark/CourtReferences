package courtreferences.view;


/*
 * This class contains the components of the main launching window of the application
 * An empty main window with the default menu bars at the top will be displayed
 * This class is the "Entry point of the application" 
 */


import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JDesktopPane;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;

public class Main extends JFrame {

	/**
	 * The components of the main Window
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuItem mntmExit;
	private JMenu mnFile;
	private JMenu mnSearch;
	private JMenuItem mntmSearch;
	private JMenu mnReports;
	private JMenu mnSettings;
	private JMenuItem mntmConfiguresettings;
	private JMenu mnAbout;
	private JMenuItem mntmLogin;
	private JMenuItem mntmLogout;
	private JMenuItem mntmAdduser;
	private JMenuItem mntmRemoveuser;
	private JMenuItem mntmChangepassword;
	private JMenuItem mntmUploaddoc;
	private LoginDialog dialog1;
	private DBConfiguration dbConfigDialog;
	private JDesktopPane desktopPane;
	private String username;
	private int userstatus;
	private AddUserDialog adduserdialog;
	private RemoveUserDialog removeuserdialog;
	private UploadDocDialog uploaddocdialog;
	
	public void setUserStatus(int status){
		this.userstatus = status;
	}
	
	public int getUserStatus(){
		return this.userstatus;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			/*	Defines the look and feel of the native system where the application runs	*/
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setTitle("Court References - International Relations");
		/*	Initializes all the components required for the main window	*/
		initComponents();
		/*	Initializes the events associated with the components of the main window	*/
		createEvents();
		//this.setUserStatus(dialog1.getUserStatus());
	}
	
	/*	Creates all the components and adds to the main window	*/
	
	private void initComponents(){
		setTitle("My Java GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 808, 472);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.LIGHT_GRAY);
		menuBar.setForeground(Color.DARK_GRAY);
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmLogin = new JMenuItem("LogIn");
		mnFile.add(mntmLogin);
		
		mntmLogout = new JMenuItem("LogOut");
		mntmLogout.setEnabled(false);
		mnFile.add(mntmLogout);
		
		mntmAdduser = new JMenuItem("AddUser");
		mntmAdduser.setEnabled(false);
		mnFile.add(mntmAdduser);
		
		mntmRemoveuser = new JMenuItem("RemoveUser");
		mntmRemoveuser.setEnabled(false);
		mnFile.add(mntmRemoveuser);
		
		mntmChangepassword = new JMenuItem("ChangePassword");
		mntmChangepassword.setEnabled(false);
		mnFile.add(mntmChangepassword);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mnSearch = new JMenu("Search");
		mnSearch.setEnabled(false);
		menuBar.add(mnSearch);
		
		mntmUploaddoc = new JMenuItem("UploadDoc");
		mnSearch.add(mntmUploaddoc);
		
		mntmSearch = new JMenuItem("Search");
		mnSearch.add(mntmSearch);
		
		mnReports = new JMenu("Reports");
		mnReports.setEnabled(false);
		menuBar.add(mnReports);
		
		mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		mntmConfiguresettings = new JMenuItem("ConfigureSettings");
		mnSettings.add(mntmConfiguresettings);
		
		mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		desktopPane = new JDesktopPane();
		contentPane.add(desktopPane);
		desktopPane.setLayout(new BorderLayout(0, 0));
	}
	
	/* Contains all the event handler mechanism	*/
	
	private void createEvents(){
		
		/*	Exit when the "Exit" button is pressed	*/
		
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		/*	Open "Login dialog box" when the Login button is pressed	*/
		
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openDialogAction();				
			}
		});	
		
		/*	Disable all the entities and close the db connection when the logout button is pressed	*/
		
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disableControls();
				//LoginAuthenticator ln = new LoginAuthenticator();
				//ln.logOutAction();
			}
		});
		
		/*	Launch Open db setting window when this button is pressed */
		
		mntmConfiguresettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configureDbSettings();
			}
		});
		
		/*	Launch "Add New User dialog box" when the "Add User" button is pressed	*/
		
		mntmAdduser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewUserToDB();
			}
		});
		
		
		/*	Launch "Delete Existing User dialog box" when the "Delete User" button is pressed	*/
		
		mntmRemoveuser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeUserFromDB();
			}
		});
		
		/*	Launch the upload documents dialog box when this buttons is pressed	*/
		
		mntmUploaddoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayUploadDocDialog();
			}
		});
		
	}
	
	/*	Create a new window object for uploading a Document	*/
	
	private void displayUploadDocDialog(){
		if(uploaddocdialog == null){
			uploaddocdialog = new UploadDocDialog();
		}
		uploaddocdialog.setVisible(true);
	}
	
	/*	Create a new Add user dialog box object	*/
	
	private void addNewUserToDB(){
		if (this.adduserdialog == null) {
	         this.adduserdialog = new AddUserDialog();
	      }
	      this.adduserdialog.setVisible(true);
	}
	
	/*	Create a new Remove user dialog box object	*/
	
	private void removeUserFromDB(){
		if (this.removeuserdialog == null) {
	         this.removeuserdialog = new RemoveUserDialog();
	      }
		this.removeuserdialog.setVisible(true);
	}
	
	/*	Create a Configure Db Settings dialog box object	*/
	
	private void configureDbSettings(){
		 if (this.dbConfigDialog == null) {
	         this.dbConfigDialog = new DBConfiguration();
	      }
	      this.dbConfigDialog.setVisible(true);
	}
	
	/*	Method to check user validity and enable all the controls if the user is legitimate	*/
	
	private void openDialogAction(){
	      if (dialog1 == null) {
	         dialog1 = new LoginDialog();
	      }
	      dialog1.setVisible(true);
	      this.setUsername(dialog1.getUsername());
	      this.setUserStatus(dialog1.getUserStatus());
	      System.out.println("Returned value from the dialog");
	      System.out.println(this.getUserStatus());
	      
	      /*	Enable the controls in the main window only when the user is being authenticated by our authentication system	*/
	      
	      if(this.getUserStatus() == 0 || this.getUserStatus() == 1){
	    	  enableControls();
	      }
	}
	
	/*	Enables all the controls in the main window	*/
	
	private void enableControls(){
		if(this.getUserStatus() == 0){
			this.mntmRemoveuser.setEnabled(true);
			this.mntmAdduser.setEnabled(true);
		}
		this.mntmLogout.setEnabled(true);
		this.mntmChangepassword.setEnabled(true);
		this.mnReports.setEnabled(true);
		this.mnSearch.setEnabled(true);
	}
	
	/*	Disable all the controls in the main window	*/
	
	private void disableControls(){
		this.mntmRemoveuser.setEnabled(false);
		this.mntmAdduser.setEnabled(false);
		this.mntmLogout.setEnabled(false);
		this.mntmChangepassword.setEnabled(false);
		this.mnReports.setEnabled(false);
		this.mnSearch.setEnabled(false);
	}
}