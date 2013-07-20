package courtreferences.model;

/*
 * Model class for ForeignCase objects
 * Defines search pattern for each Country and Court name combination
 */

public class ForeignReferences{
	private String countryName;
	private String courtName;
	private String searchRegex;
	
	public ForeignReferences(){
		
	}
	public ForeignReferences(String countryName, String courtName, String regex){
		this.setCountryName(countryName);
		this.setCourtName(courtName);
		this.setSearchRegex(regex);
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
	public String getSearchRegex() {
		return searchRegex;
	}
	public void setSearchRegex(String searchRegex) {
		this.searchRegex = searchRegex;
	}
}
