package courtreferences.model;

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
	static protected List<ForeignReferences> searchObjs = new ArrayList<ForeignReferences>();
	protected List<CitationCases> refCases;
	
	public Citations(){		
	}
	
	public Citations(String caseid, String countryName, String courtName, int pdfcitationid, String citationString, String citationBodyString, int pageNo){
	}
	
	
	public abstract void searchCitationFormats(ForeignReferences fObj);
	public abstract void searchRefCaseTitle();

}
