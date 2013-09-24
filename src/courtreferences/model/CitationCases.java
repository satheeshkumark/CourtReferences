package courtreferences.model;

public class CitationCases {
	/*
	 * There may be more than one case cited in each citations
	 * This class describes the basic structure of each case such as country and court to which the case belongs to and also the citation id where this case is referred
	 */
	
	private static int refid = 1;
	private int caseRefId;
	private int citationRefId;
	private int citationType;
	private int countryId;
	private int courtId;
	private String countryName;
	private String courtName;
	private String caseid;
	private String caseTitle;
	private int citationid;
	private int pageNumber;
	private String otherDetails;
	
	public CitationCases(){
		
	}
	
	public CitationCases(int citationRefId, int countryId, int citationType, int courtId, String countryName, String courtName, String caseid, int citationid, int pageNumber, String otherDetails){
		this.setCountryId(countryId);
		this.setCourtId(courtId);
		this.setCitationType(citationType);
		this.setCitationRefId(citationRefId);
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

	public static int getRefid() {
		return refid;
	}

	public static void setRefid(int refid) {
		CitationCases.refid = refid;
	}

	public int getCaseRefId() {
		return caseRefId;
	}

	public void setCaseRefId(int caseRefId) {
		this.caseRefId = caseRefId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getCourtId() {
		return courtId;
	}

	public void setCourtId(int courtId) {
		this.courtId = courtId;
	}

	public int getCitationRefId() {
		return citationRefId;
	}

	public void setCitationRefId(int citationRefId) {
		this.citationRefId = citationRefId;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public int getCitationType() {
		return citationType;
	}

	public void setCitationType(int citationType) {
		this.citationType = citationType;
	}
}
