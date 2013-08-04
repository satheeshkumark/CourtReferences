package courtreferences.model;

/*
 * Abstract class which defines the overall structure of each citation object that needs to be extracted from the pdf content and loaded into the database
 * Implements search interface which defines methods to extract Title information and citation information from each pdf document
 * The classes which are extended from this class should have give implementation to those methods
 */

import java.util.ArrayList;
import java.util.List;

public abstract class Citations implements Search{
	
	protected int citationid;
	protected String caseId;
	protected String countryname;
	protected String courtname;
	protected String citationString;
	protected String citationBodyString;
	protected int pageNo;
	protected List<String> citationCaseTitles;
	static protected List<ForeignCourt> searchObjs = new ArrayList<ForeignCourt>();
	protected List<CitationCases> refCases;
	
	public Citations(){		
	}
	
	public Citations(String caseid, String countryName, String courtName, int pdfcitationid, String citationString, String citationBodyString, int pageNo){
	}
	
	/* Needs to be implemented in the class which is extending it	*/
	
	public abstract void searchCitationFormats(ForeignCourt fObj);
	public abstract void searchRefCaseTitle();

}
