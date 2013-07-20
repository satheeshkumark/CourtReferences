package courtreferences.model;

/*
 * Each (country,court) has its own format. Extraction mechanism varies depending on that.
 * Contains methods to extract case title and references to the foreign court in each citation
 * 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SouthAfricanFiles {
	private String countryName;
	private String courtName;
	private String caseId;
	private List<String> participantsName;
	private String decisionDate;
	private String heardDate;
	private List<SouthAfricanCitations> citationObjs;
	
	public SouthAfricanFiles(){
		
	}
	
	public SouthAfricanFiles(String countryName, String courtName){
		this.setCountryName(countryName);
		this.setCourtName(courtName);
		this.citationObjs = new ArrayList<SouthAfricanCitations>();
	}
	
	public List<SouthAfricanCitations> getCitationObjs() {
		return citationObjs;
	}

	public void setCitationObjs(List<SouthAfricanCitations> citationObjs) {
		this.citationObjs = citationObjs;
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

	public List<String> getParticipantsName() {
		return participantsName;
	}

	public void setParticipantsName(List<String> participantsName) {
		this.participantsName = participantsName;
	}

	public String getDecisionDate() {
		return decisionDate;
	}

	public void setDecisionDate(String decisionDate) {
		this.decisionDate = decisionDate;
	}

	public String getHeardDate() {
		return heardDate;
	}

	public void setHeardDate(String heardDate) {
		this.heardDate = heardDate;
	}
	
	/* Extracts case id for each SouthAfrican case document	*/
	
	void extractCaseId(String CourtName,String pdfFileContent){
		if(CourtName.equals("Constitutional Court")){
			this.setCaseId(extractCNSTCRTCaseId(pdfFileContent));
		}
		else{
			this.setCaseId(null);
		}
	}
	
	/* Extracts Participants name for each SouthAfrican case document	*/
	
	void extractParticipants(String CourtName,String pdfFileContent){
		if(CourtName.equals("Constitutional Court")){
			this.setParticipantsName(extractCNSTCRTParticipants(pdfFileContent));
		}
		else{
			this.setParticipantsName(null);
		}
	}

	/* Extracts decision date for each SouthAfrican case document	*/
	
	void extractDecisionDate(String CourtName,String pdfFileContent){
		if(CourtName.equals("Constitutional Court"))
			this.setDecisionDate(extractCNSTCRTDecisionDate(pdfFileContent));
		else
			this.setDecisionDate(null);
	}
	
	/* Extracts heard date for each SouthAfrican case document	*/
	
	void extractHeardDate(String CourtName,String pdfFileContent){
		if(CourtName.equals("Constitutional Court"))
			this.setHeardDate(extractCNSTCRTHeardDate(pdfFileContent));
		else
			this.setHeardDate(null);
	}
	
	/* Extracts citations for each SouthAfrican case document	*/
	
	void extractCitations(String CourtName,List<String> fileContent){
		if(CourtName.equals("Constitutional Court")){
			extractCNSTCRTCitations(fileContent);
			//this.setCitationObjs(extractCNSTCRTCitations(fileContent));
		}
		else{
			this.setCitationObjs(null);
		}		
	}
	
	/* Extracts Case id for SouthAfrican Constitutional Court case document*/
	
	String extractCNSTCRTCaseId(String pdfFileContent){
		return pdfFileContent.substring(checkCaseIdPattern(pdfFileContent,0,0),checkCaseIdPattern(pdfFileContent,0,1));
	}
	
	/* Extracts citations for SouthAfrican Constitutional Court case document*/
	
	void extractCNSTCRTCitations(List<String> fileContent){
		int currentCitationId = 1;
		int citationStartIndex = -1;
		int citationEndIndex = -1;
		int currentPageNo = 0;
		for(String pageContent : fileContent){
			int startIndex = 0;
			int footerStartIndex = -1;
			boolean footerStartFlag = false;
			while(true){
				citationStartIndex = this.checkCitationPattern(pageContent, currentCitationId, startIndex);
				if(!footerStartFlag){
					footerStartIndex = citationStartIndex;
					footerStartFlag = true;
				}
				if(citationStartIndex == -1)
					break;
				citationEndIndex = this.checkCitationPattern(pageContent, currentCitationId + 1, citationStartIndex);
				if(citationEndIndex == -1)
					citationEndIndex = pageContent.length() - 1;
				//System.out.println("Processing Citation id : " + currentCitationId);
				String currCitationString = pageContent.substring(citationStartIndex, citationEndIndex).replaceAll("\n", " ");
				String citationBodyString = extractCitationBodyString(pageContent.substring(0,footerStartIndex),currentCitationId);
				
				/*	South African Citation object is created for each citation found in the footer	*/
				
				SouthAfricanCitations currentCitationObj = new SouthAfricanCitations(this.getCaseId(),this.getCountryName(),this.getCourtName(),currentCitationId,currCitationString.trim(),citationBodyString,currentPageNo);
				//currentCitationObj.setCitationBodyString();
				this.getCitationObjs().add(currentCitationObj);
				System.out.println("Citation Body String : " + currentCitationObj.getCitationBodyString());
				currentCitationId += 1;
				startIndex = citationEndIndex;
				
			}
			currentPageNo += 1;
		}
		this.writeDetails();
		//filterCitations(citationMap);
		return;
	}
	
	/* Extracts the content around the position where the citation has been referred in the body of the text in SouthAfrican Constitutional Court case document*/
	
	String extractCitationBodyString(String pageContent, int currentCitationId){
		StringBuffer citationBodyString = new StringBuffer();
		String citationPatternString = "([^A-Z^0-9^\\[^\\(\\{]" + currentCitationId + ")(\\s|\\z|\\n)";
		Pattern citationPattern = Pattern.compile(citationPatternString);
		Matcher citationMatcher = citationPattern.matcher(pageContent);
		while(citationMatcher.find()){
			int startIndex = 0;
			if(citationMatcher.start() - 70 < 0)
				startIndex = 0;
			else{
				startIndex = pageContent.substring(0,citationMatcher.start() - 70).lastIndexOf(' ');
				startIndex = startIndex>=0?startIndex:0;
			}
			int endIndex = pageContent.length() - 1;
			if(citationMatcher.end() + 50 >= pageContent.length())
				endIndex = pageContent.length() - 1;
			else{
				endIndex = citationMatcher.end() + 50 + pageContent.substring(citationMatcher.end() + 50,pageContent.length() - 1).indexOf(' ');
				endIndex = endIndex>=0?endIndex:0;
			}
			citationBodyString.append(pageContent.substring(startIndex, endIndex).replaceAll("\n", " "));
			break;
		}
		return citationBodyString.toString();
	}
	
	/* Extracts the participants name from SouthAfrican Constitutional Court case document*/

	List<String> extractCNSTCRTParticipants(String pdfFileContent){
		int startIndex = checkCaseIdPattern(pdfFileContent, 0, 1) + 1;
		int caseyearIndex = checkCaseYearPattern(pdfFileContent, 0, 1);
		if(caseyearIndex != -1){
			startIndex = caseyearIndex + 1;
		}
		int endIndex = checkCaseIdPattern(pdfFileContent,startIndex, 1);
		int endIndex1 = checkHeardDatePattern(pdfFileContent, 0, 0);
		if(endIndex1 == -1){
			endIndex1 = checkDatePattern(pdfFileContent, 0, 0);
			if(endIndex1 == -1){
				endIndex1 = pdfFileContent.length();
				endIndex1 = (endIndex1 == -1)?endIndex1:endIndex1-1;
			}
			else
				endIndex1 = endIndex1 - 1;
		}
		if(endIndex != -1 && endIndex1 != -1)
			endIndex = (endIndex<endIndex1)?endIndex:endIndex1;
		else if(endIndex1 != -1)
			endIndex = endIndex1;
		return extractNames(pdfFileContent.substring(startIndex,endIndex));
	}
	
	/* Filters just the name of the participants	*/
	
	List<String> extractNames(String inputString){
		List<String> nameList = new ArrayList<String>();
		String[] inputLines = inputString.split("\n");
		for(int lineno = 0; lineno<inputLines.length; lineno++){
			StringBuffer outputString = new StringBuffer("");
			int startIndex = 0;
			int endIndex = 0;
			if(lineno == inputLines.length - 1){
				if(checkCaseIdPattern(inputLines[lineno], 0, 0) != -1){
					break;
				}
			}
			while(startIndex < inputLines[lineno].length()){
				endIndex = checkCapitalNamePattern(inputLines[lineno], startIndex, 1);
				startIndex = checkCapitalNamePattern(inputLines[lineno], startIndex, 0);
				if(startIndex == -1 || endIndex == -1)
					break;
				outputString.append(inputLines[lineno].substring(startIndex, endIndex));
				outputString.append(" ");
				startIndex = endIndex;
			}
			if(outputString.length() != 0)
				nameList.add(outputString.toString());
		}
		return nameList;
	}
	
	/* Pattern which matches the inputString and searches for the citationid	*/
	
	int checkCitationPattern(String inputString, int citationid, int startindex){
		String citationPatternString = "\n" + citationid + " .*";
		Pattern citationPattern = Pattern.compile(citationPatternString);
		Matcher citationMatcher = citationPattern.matcher(inputString);
		if(citationMatcher.find(startindex))
			return citationMatcher.start();
		else
			return -1;
	}
	
	/* Searches for the occurrence of any names in the input String	and returns start or end index depending on the request*/
	
	int checkCapitalNamePattern(String inputString, int startIndex, int option){
		String namePatternString = "\\b([A-Z0-9])+\\b";
		Pattern namePattern = Pattern.compile(namePatternString);
		Matcher nameMatcher = namePattern.matcher(inputString);
		if(nameMatcher.find(startIndex) && option == 0)
			return nameMatcher.start();
		else if(nameMatcher.find(startIndex) && option == 1)
			return nameMatcher.end();
		else
			return -1;
	}
	
	/* Searches the pattern with case id in the inputString	 and returns start or end index depending on the request*/ 
	
	int checkCaseIdPattern(String inputString, int startIndex, int option){
		String caseidPatternString = "[0-9]+/[0-9]+";
		Pattern caseidPattern = Pattern.compile(caseidPatternString);
		Matcher caseidMatcher = caseidPattern.matcher(inputString);
		if(caseidMatcher.find(startIndex) && option == 0)
			return caseidMatcher.start();
		else if(caseidMatcher.find(startIndex) && option == 1)
			return caseidMatcher.end();
		else
			return -1;
	}
	
	/* Searches the year pattern in the input String and retuns the start or end index depending on the request	*/
	
	int checkCaseYearPattern(String inputString, int startIndex,int option){
		String caseYearPatternString = "(?i)\\[([12][0-9]{3})\\]( )?ZACC( )?[0-9]+";
		Pattern caseYearPattern = Pattern.compile(caseYearPatternString);
		Matcher caseYearMatcher = caseYearPattern.matcher(inputString);
		if(caseYearMatcher.find(startIndex) && option == 0)
			return caseYearMatcher.start();
		else if(caseYearMatcher.find(startIndex) && option == 1)
			return caseYearMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the heard Date pattern in the input string and returns the start or end index depending on the request */
	
	int checkHeardDatePattern(String inputString, int startIndex, int opt){
		String heardDatePatternString = "(?i)Hear(d)?( )?(On)?";
		Pattern heardDatePattern = Pattern.compile(heardDatePatternString);
		Matcher heardDateMatcher = heardDatePattern.matcher(inputString);
		if(heardDateMatcher.find() && opt == 0)
			return heardDateMatcher.start();
		else if(heardDateMatcher.find() && opt == 1)
			return heardDateMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the Decision Date pattern in the input string and returns the start or end index depending on the request */
	
	int checkDecisionDatePattern(String inputString, int startIndex, int opt){
		String decisionDatePatternString = "(?i)(Deci(ded|sion)?|deli(vered)?)( )?(On)?(Date)?";
		Pattern decisionDatePattern = Pattern.compile(decisionDatePatternString);
		Matcher decisionDateMatcher = decisionDatePattern.matcher(inputString);
		if(decisionDateMatcher.find() && opt == 0)
			return decisionDateMatcher.start();
		else if(decisionDateMatcher.find() && opt == 1)
			return decisionDateMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the Date pattern in the input string and returns the start or end index depending on the request */
	
	int checkDatePattern(String inputString, int startIndex, int opt){
		String datePatternString = "(?i)([1-9]|[12][0-9]|3[01])[- /.](Jan(uary)?|Feb(ruary)?|Mar(ch)?|Apr(il)?|May|Jun(e)?|Jul(y)?|Aug(ust)?|Sep(tember)?|Oct(ober)?|Nov(ember)?|Dec(ember)?)[- /.](1[9][0-9][0-9]|2[0-9][0-9][0-9])";
		Pattern datePattern = Pattern.compile(datePatternString);
		Matcher dateMatcher = datePattern.matcher(inputString);
		if(dateMatcher.find(startIndex) && opt == 0)
			return dateMatcher.start();
		else if(dateMatcher.find(startIndex) && opt == 1)
			return dateMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the Decision Date Pattern in South African Constitutional court. If not found, the default date is set */
	
	String extractCNSTCRTDecisionDate(String pdfFileContent){
		String decisionDate  = null;
		int startIndex = 0;
		int endIndex = 0;
		startIndex = checkDecisionDatePattern(pdfFileContent, startIndex, 0);
		startIndex = (startIndex != -1?startIndex:0);
		endIndex = checkDatePattern(pdfFileContent, startIndex, 1);
		startIndex = checkDatePattern(pdfFileContent, startIndex, 0);
		if(endIndex != -1 && startIndex != -1)
			decisionDate = pdfFileContent.substring(startIndex,endIndex);
		else
			decisionDate = "1 January 1900";
		return decisionDate;
	}

	/* Searches for the Heard Date Pattern in South African Constitutional court. If not found, the default date is set */
	String extractCNSTCRTHeardDate(String pdfFileContent){
		String heardDate  = null;
		int startIndex = 0;
		int endIndex = 0;
		startIndex = checkHeardDatePattern(pdfFileContent, startIndex, 0);
		startIndex = (startIndex != -1?startIndex:0);
		endIndex = checkDatePattern(pdfFileContent, startIndex, 1);
		startIndex = checkDatePattern(pdfFileContent, startIndex, 0);
		if(endIndex != -1 && startIndex != -1)
			heardDate = pdfFileContent.substring(startIndex,endIndex);
		else
			heardDate = "1 January 1900";
		return heardDate;
	}
	
	/* Writes the output to the file	*/
	
	void writeDetails(){
		try{
			File file = new File("output.txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(file,true));
			output.write("====================NEW CASE STARTS HERE================");
			output.newLine();
			output.newLine();
			output.write("Country Name : " + this.getCountryName());
			output.newLine();
			output.write("Court Name : " + this.getCourtName());
			output.newLine();
			output.write("Case Id : " + this.getCaseId());
			output.newLine();
			output.write("Name of the Applicants : ");
			output.newLine();
			for(String aname : this.getParticipantsName()){
				output.write(aname);
				output.newLine();
			}
			output.write("Decision Date : " + this.getDecisionDate());
			output.newLine();
			output.write("Heard Date : " + this.getHeardDate());
			output.newLine();
			//output.write("File Length : " + this.getFileLength());
			//output.newLine();
			//output.write("Number of pages : " + this.getNoOfPages());
			//output.newLine();
			output.newLine();
			output.newLine();
			output.write("----CITATION DETAILS----");
			output.newLine();
			output.newLine();
			for(SouthAfricanCitations c : this.getCitationObjs()){
				output.write("Page No : " + c.getPageNo());
				output.newLine();
				output.newLine();
				output.write("CITATION ID : " + c.getCitationid());
				output.newLine();
				output.newLine();
				output.write("Citation String(text in FOOTNOTE) : " + c.getCitationString());
				output.newLine();
				output.newLine();
				output.write("Citation Body String(text in BODY) : " + c.getCitationBodyString());
				output.newLine();
				output.newLine();
				output.write("Citation References : ");
				output.newLine();
				
				for(String title : c.getCitationCaseTitles()){
					output.write("Reference case Title : " + title);
					output.newLine();
					output.newLine();
					//output.write(cs.getOtherDetails());
					//output.newLine();
				}
				
				for(CitationCases cs : c.getRefCases()){
					output.write("Citation Case ID : " + cs.getCaseid());
					output.newLine();
					output.write("Citation Country Name : " + cs.getCountryName());
					output.newLine();
					output.write("Citation Court Name : " + cs.getCourtName());
					output.newLine();
					output.newLine();
					output.newLine();
					//output.write(cs.getOtherDetails());
					//output.newLine();
				}
				output.newLine();
				output.newLine();
			}
			output.newLine();
			output.newLine();
			output.close();
		}
		catch (IOException ex){
		  // report
		}
	}
	
	void printDetails(){
		System.out.println("Country Name : " + this.getCountryName());
		System.out.println("Court Name : " + this.getCourtName());
		System.out.println("Case Id : " + this.getCaseId());
		System.out.println("Name of the Applicants : ");
		for(String aname : this.getParticipantsName()){
			System.out.println(aname);
		}
		System.out.println("Decision Date : " + this.getDecisionDate());
		System.out.println("Heard Date : " + this.getHeardDate());
		System.out.println();
		System.out.println();
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

}
