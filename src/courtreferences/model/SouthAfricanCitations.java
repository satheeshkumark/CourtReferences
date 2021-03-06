package courtreferences.model;

/*
 * Contains attributes from Citations class from where it is extended
 * Contains methods to search foreign citations, international references and treaties
 * It has to implement methods in Search interface since it has extracted the abstract method which implemented the Search interface
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SouthAfricanCitations extends Citations{

	/* Loads foreign reference formats, International court formats and treaty formats when the class is first invoked	and only once for all the citation objects*/
	
	static {
		loadForeignReferenceFormats();
	}
	
	public SouthAfricanCitations() {
		// TODO Auto-generated constructor stub
	}
	
	public SouthAfricanCitations(String caseid, String countryName, String courtName, int pdfcitationid, String citationString, String citationBodyString, int pageNo){
		/* initialize the citation object with the values passed to the function	*/
		initializeValues(caseid, countryName, courtName, pdfcitationid, citationString, citationBodyString, pageNo);
	}
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getCourtname() {
		return courtname;
	}
	public void setCourtname(String courtname) {
		this.courtname = courtname;
	}
	public String getCitationString() {
		return citationString;
	}
	public void setCitationString(String citationString) {
		this.citationString = citationString;
	}
	public String getCitationBodyString() {
		return citationBodyString;
	}
	public void setCitationBodyString(String citationBodyString) {
		this.citationBodyString = citationBodyString;
	}
	public static List<ForeignCourt> getSearchObjs() {
		return searchObjs;
	}
	public static void setSearchObjs(List<ForeignCourt> searchObjs) {
		Citations.searchObjs = searchObjs;
	}
	public List<CitationCases> getRefCases() {
		return refCases;
	}
	public void setRefCases(ArrayList<CitationCases> refCases) {
		this.refCases = refCases;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getCitationid() {
		return citationid;
	}
	public void setCitationid(int citationid) {
		this.citationid = citationid;
	}
	public List<String> getCitationCaseTitles() {
		return citationCaseTitles;
	}
	public void setCitationCaseTitles(List<String> citationCaseTitles) {
		this.citationCaseTitles = citationCaseTitles;
	}
	
	/* Load the format of the foreign references from the table	*/
	
	public static void loadForeignReferenceFormats(){
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		System.out.println("Loading Foreign References");
		try{
			String CitationFetchQuery = "select CountryTable.CountryId, CourtTable.CourtId, CountryTable.CountryName, CourtTable.CourtName, CourtTable.CitationRegex from CountryDetails CountryTable inner join CourtDetails CourtTable on CountryTable.CountryId = CourtTable.CountryId";
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(CitationFetchQuery);
			while(resultSet.next()){
				ForeignCourt currentobj = new ForeignCourt(resultSet.getInt(1),resultSet.getInt(2),resultSet.getString(3), resultSet.getString(4),resultSet.getString(5));
				SouthAfricanCitations.searchObjs.add(currentobj);
			}
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		System.out.println("Done with loading foreign references");
	}
	
	/* Calls respective functions to extract the citation content from the documents	*/
	
	private void initializeValues(String caseid, String countryName, String courtName, int pdfcitationid, String citationString, String citationBodyString, int pageNo){
		this.setCaseId(caseid);
		this.setCountryname(countryName);
		this.setCourtname(courtName);
		this.setCitationid(pdfcitationid);
		this.setCitationString(citationString);
		this.setCitationBodyString(citationBodyString);
		this.setPageNo(pageNo);
		this.refCases = new ArrayList<CitationCases>();
		this.searchForeignReferences();
		this.searchRefCaseTitle();
		System.out.println("Number of titles : " + this.getCitationCaseTitles().size());
	}
	
	/* Searches the occurence of all the foreign references in each and every citation	*/
	
	public void searchForeignReferences() {
		// TODO Auto-generated method stub
		for(ForeignCourt fObj : SouthAfricanCitations.getSearchObjs()){
			if(fObj.getSearchRegex() != null)
				searchCitationFormats(fObj);
		}
		return;
	}
	
	/* Gets the foreign reference object as input which contain country name, court name and corresponding format which will be searched
	 * Uses the citation string and the body string to find out whether any case has been cited and adds the cited case to RefCases list
	 */
	
	public void searchCitationFormats(ForeignCourt fObj){
		String citationPatternString = fObj.getSearchRegex();
		Pattern citationPattern = Pattern.compile(citationPatternString);
		Matcher citationMatcher = citationPattern.matcher(this.getCitationString());
		while(citationMatcher.find()){
			int startIndex = citationMatcher.start();
			int endIndex = citationMatcher.end();
			String refString = this.getCitationString().substring(startIndex,endIndex);
			CitationCases currentCitation = new CitationCases(fObj.getCountryName(), fObj.getCourtName(), refString, this.getCitationid(), this.getPageNo(), refString);
			this.getRefCases().add(currentCitation);
		}
		return;
	}
	
	/* Searches for the title of the cases in the citation	*/
	public void searchRefCaseTitle(){
		this.setCitationCaseTitles(new ArrayList<String>());
		for(String title : extractRefCaseTitlesSA(this.getCitationString())){
			this.getCitationCaseTitles().add(title);
			//System.out.println("Title Here 1 : " + title);
		}
		for(String title : extractRefCaseTitlesSA(this.getCitationBodyString())){
			this.getCitationCaseTitles().add(title);
			//System.out.println("Title Here 2 : " + title);
		}
	}
	
	/* */
	
	private List<String> extractRefCaseTitlesSA(String citationString){
		//System.out.println("Citation String inside extract case title : " + citationString);
		String csubString = null;
		List<String> outputList = new ArrayList<String>();
		String caseTitlePattern = "([A-Z][^0-9^\\s]*\\s)+v ([A-Z][^0-9]+\\s)+";
		Pattern citationPattern = Pattern.compile(caseTitlePattern);
		while(true){
			Matcher citationMatcher = citationPattern.matcher(citationString);
			if(citationMatcher.find()){
				int startIndex = citationMatcher.start();
				int middleIndex = citationString.indexOf(" v ", startIndex);
				int endIndex = citationMatcher.end();
				if(middleIndex != -1 && startIndex == -1 && endIndex == -1){
					csubString = citationString.substring(middleIndex - 50, middleIndex + 50);
					outputList.add(csubString);
					break;
				}
				//System.out.println(startIndex + "  " + middleIndex + "   " + endIndex);
				if(middleIndex - startIndex > 70)
					startIndex = middleIndex - 70;
				if(endIndex - middleIndex > 70)
					endIndex = middleIndex + 70;
				csubString = citationString.substring(startIndex, endIndex);
				int semiIndex = citationString.indexOf(";", middleIndex);
				if(semiIndex >= 0 && semiIndex < csubString.length()){
					csubString = csubString.substring(0,semiIndex);
					//System.out.println("Citation String extraction part : " + csubString);
					endIndex = semiIndex;
				}
				outputList.add(csubString);
				citationString = citationString.substring(endIndex);
			}
			else{
				if(citationString.indexOf(" v ") != -1){
					int middleIndex = citationString.indexOf(" v ");
					int startIndex = 0;
					int endIndex = citationString.length();
					if(startIndex + 50 < middleIndex)
						startIndex = middleIndex - 50;
					if(endIndex - 50 > middleIndex)
						endIndex = middleIndex + 50;
					//System.out.println(startIndex + "  " + middleIndex + "   " + endIndex);
					csubString = citationString.substring(startIndex, endIndex);
					outputList.add(csubString);
				}
				break;
			}
		}
		return outputList;
	}
}
