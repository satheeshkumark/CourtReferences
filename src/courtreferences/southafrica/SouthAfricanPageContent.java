package courtreferences.southafrica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import courtreferences.model.CitationCases;
import courtreferences.model.InternationalCourt;
import courtreferences.model.LoadSearchObjects;
import courtreferences.model.PageContent;
import courtreferences.model.SoftLaw;
import courtreferences.model.Treaty;

public class SouthAfricanPageContent extends PageContent{
	static final int boundarylength = 400;
	/*
	 * Class which has data structures and methods for extracting and processing case references in the body of the Court documents 
	 */
	public SouthAfricanPageContent(){
		
	}
	public SouthAfricanPageContent(int pageNo, String pageContent){
		this.setPageNo(pageNo);
		this.setPageContent(pageContent);
		this.setRefCases(new ArrayList<CitationCases>());
	}
	
	public void searchForeignReferencesInBodyContent(){
		searchForeignReferences();
	}
	
	/* Searches the occurrence of all the foreign references in each and every citation	*/
	
	public void searchForeignReferences() {
		// TODO Auto-generated method stub
		for(Map.Entry<String, List<String>> entry: LoadSearchObjects.getForeignCourtCountryNames().entrySet()){
				String courtName = entry.getKey();
				List<String> countryNames = entry.getValue();
				searchForeignCourtReferences(courtName, countryNames);
		}
		for(InternationalCourt iCObj : LoadSearchObjects.getiCSearchObjs()){
			if(iCObj.getiFormatPattern() != null || iCObj.getiTitlePattern() != null)
				this.searchInternationalCourtReferences(iCObj);
		}
		for(SoftLaw sLawObj : LoadSearchObjects.getsLawSearchObjs()){
			if(sLawObj.getsTitlePattern() != null || sLawObj.getsTitlePattern() != null)
				this.searchSoftLaws(sLawObj);
		}
		for(Treaty tObj : LoadSearchObjects.getTreatySearchObjs()){
			if(tObj.gettTitlePattern() != null || tObj.gettFormatPattern() != null)
				this.searchTreaties(tObj);
		}
		return;
	}
	
	/* Gets the foreign reference object as input which contain country name, court name and corresponding format which will be searched
	 * Uses the citation string and the body string to find out whether any case has been cited and adds the cited case to RefCases list
	 */
	
	
	public void searchForeignCourtReferences(String courtName, List<String> countryNames){
		Pattern citationPattern = Pattern.compile(courtName);
		Matcher citationMatcher = citationPattern.matcher(this.getPageContent());
		while(true){
			if(citationMatcher.find() == false)
				break;
			int startIndex = citationMatcher.start();
			int endIndex = citationMatcher.end();
			for(String countryName : countryNames){
				if(searchEntities(countryName, this.getPageContent(), startIndex, endIndex)){
					int refid = CitationCases.getRefid();
					String refString = this.pageContent.substring(startIndex,endIndex);
					CitationCases.setRefid(refid+1);
					int CourtId = LoadSearchObjects.getCourtIdByName(courtName);
					int CountryId = LoadSearchObjects.getCountryIdByName(countryName);
					int CitationType = 0;
					CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, CourtId, countryName.toUpperCase(), courtName.toUpperCase(), refString, -1, this.getPageNo(), refString);
					this.getRefCases().add(currentCitation);
				}
			}
		}
		return;
	}
	
	public void searchInternationalCourtReferences(InternationalCourt iCObj) {
		// TODO Auto-generated method stub
		String titlePatternString = iCObj.getiTitlePattern();
		if(titlePatternString != null){
			if(this.searchEntities(titlePatternString, this.getPageContent(), 0, this.getPageContent().length())){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 1;
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, iCObj.getiCourtId(), "IC", iCObj.getiCourtName1(), iCObj.getiCourtName1(), -1, this.getPageNo(), iCObj.getiCourtName1());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
		String formatPatternString = iCObj.getiFormatPattern();
		if(formatPatternString != null){
			if(this.searchEntities(formatPatternString, this.getPageContent(), 0, this.getPageContent().length())){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 1;
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, iCObj.getiCourtId(), "IC", iCObj.getiCourtName1(), iCObj.getiCourtName1(), -1, this.getPageNo(), iCObj.getiCourtName1());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
	}

	//@Override
	public void searchSoftLaws(SoftLaw sLawObj) {
		// TODO Auto-generated method stub
		//System.out.println("Searching SoftLaws");
		String titlePatternString = sLawObj.getsTitlePattern();
		if(titlePatternString != null){
			if(this.searchEntities(titlePatternString, this.getPageContent(), 0, this.getPageContent().length())){
				int refid = CitationCases.getRefid();
				int CountryId = -1;
				int CitationType = 2;
				CitationCases.setRefid(refid+1);
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, sLawObj.getsLawId(), "SL", sLawObj.getsLawName1(), sLawObj.getsLawName1(), -1, this.getPageNo(), sLawObj.getsLawName1());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
		String formatPatternString = sLawObj.getsFormatPattern();
		if(formatPatternString != null){
			if(this.searchEntities(formatPatternString, this.getPageContent(), 0, this.getPageContent().length())){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 1;
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, sLawObj.getsLawId(), "SL", sLawObj.getsLawName1(), sLawObj.getsLawName1(), -1, this.getPageNo(), sLawObj.getsLawName1());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
	}

	//@Override
	public void searchTreaties(Treaty tObj) {
		// TODO Auto-generated method stub
		String titlePatternString = tObj.gettTitlePattern();
		if(titlePatternString != null){
			if(this.searchEntities(titlePatternString, this.getPageContent(), 0, this.getPageContent().length())){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 1;
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, tObj.gettId(), "TR", tObj.gettTitle1_Eng(), tObj.gettTitle1_Eng(), -1, this.getPageNo(), tObj.gettTitle1_Eng());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
		String formatPatternString = tObj.gettFormatPattern();
		if(formatPatternString != null){
			if(this.searchEntities(formatPatternString, this.getPageContent(), 0, this.getPageContent().length())){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 1;
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, tObj.gettId(), "TR", tObj.gettTitle1_Eng(), tObj.gettTitle1_Eng(), -1, this.getPageNo(), tObj.gettTitle1_Eng());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
	}
	
	private boolean searchEntities(String patternString, String inputString, int startIndex, int endIndex){
		startIndex = startIndex - boundarylength >=0 ? startIndex - boundarylength : 0;
		endIndex = endIndex + boundarylength < inputString.length() ? endIndex + boundarylength : inputString.length();
		Pattern citationPattern = Pattern.compile(patternString);
		Matcher citationMatcher = citationPattern.matcher(inputString.substring(startIndex, endIndex));
		if(citationMatcher.find())
			return true;
		return false;
	}
}
