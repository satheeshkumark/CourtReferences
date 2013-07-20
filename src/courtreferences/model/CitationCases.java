package courtreferences.model;

/*
 * There may be more than one case cited in each citations
 * This class describes the basic structure of each case such as country and court to which the case belongs to and also the citation id where this case is referred
 */

public class CitationCases {
	private String countryName;
	private String courtName;
	private String caseid;
	private int citationid;
	private int pageNumber;
	private String otherDetails;
	
	public CitationCases(){
		
	}
	
	public CitationCases(String countryName, String courtName, String caseid, int citationid, int pageNumber, String otherDetails){
		this.setCountryName(countryName);
		this.setCourtName(courtName);
		this.setCaseid(caseid);
		this.setCitationid(citationid);
		this.setPageNumber(pageNumber);
		this.setOtherDetails(otherDetails);
	}
	
	public String getCaseid() {
		return caseid;
	}
	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	public int getCitationid() {
		return citationid;
	}
	public void setCitationid(int citationid) {
		this.citationid = citationid;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
}
