package courtreferences.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;

import courtreferences.southafrica.SouthAfricanCourtDocument;

public class ParsePdfDoc {
	/* 
	 * Contains basic structure of each pdf file which is needed to be processed
	 * The countries which have pdf document as their court documents can be instantiated with this class
	 * Have methods to parse PDF documents and extract titles and the contents themselves
	 * 
	*/

	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	public ParsePdfDoc(){
	}
	
	public ParsePdfDoc(String countryname,String courtname, String processedUser){
		this.setCountryName(countryname);
		this.setCourtName(courtname);
		this.setProcessedUserName(processedUser);
	}
	
	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public int getFileLength() {
		return fileLength;
	}

	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}

	public int getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}
	
	public CourtDocument processPDFForCaseDetails(String pdfFile, String sourceFileName){
		//CitationCases.setRefid(1);
		if(this.countryName.equals("South Africa")){
			return processSouthAfricanCaseDetails(pdfFile, sourceFileName);
		}
		else{
			System.out.println("The files belongs to these country cannot be processed");
			return null;
		}
	}
	
	/* Processes South African pdf Files
	 * Similar methods can be added for other country files. It should be written based on the structure of those documents 
	 * */
	
	CourtDocument processSouthAfricanCaseDetails(String pdfFile, String sourceFileName){
		int startPage = 1;
		int endPage = 1;
		String pdfFileContent = extractContentFromPDF(pdfFile,startPage,endPage);
		int filelength = pdfFile.length();
		this.setFileLength(filelength);
		SouthAfricanCourtDocument saf = new SouthAfricanCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		
		saf.extractCaseId(this.getCourtName(),pdfFileContent);
		saf.extractParticipants(this.getCourtName(),pdfFileContent);
		saf.extractDecisionDate(this.getCourtName(),pdfFileContent);
		saf.extractHeardDate(this.getCourtName(), pdfFileContent);
		
		List<String> pageContentList = new ArrayList<String>();
		for(int pageNo=1; pageNo<=this.getNoOfPages(); pageNo++){
			pageContentList.add(extractContentFromPDF(pdfFile,pageNo,pageNo));	
		}
		saf.extractCitations(this.getCourtName(),pageContentList);
		return saf;
	}
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromPDF(String pdfFile,int startpage,int endpage){
		boolean force = false;
		String password = null;
		String encoding = null;
		int startPage = startpage;
		int endPage = endpage;
		boolean separateBeads = true;
		boolean sort = false;
		PDDocument document = null;
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = PDDocument.load(pdfFile, force);
	        	
				/* To check password protection */        		
				if( document.isEncrypted()){
					StandardDecryptionMaterial sdm = new StandardDecryptionMaterial( password );
	        		document.openProtection( sdm );
	        		AccessPermission ap = document.getCurrentAccessPermission();
	       			if( ! ap.canExtractContent() ){
	       				throw new IOException( "You do not have permission to extract text" );
	       			}
	       		} 
				
	       		this.setNoOfPages(document.getNumberOfPages());	 
	   			PDFTextStripper txtStripper = null;
	       		txtStripper = new PDFTextStripper(encoding);
	       		txtStripper.setForceParsing( force );
	       		txtStripper.setSortByPosition( sort );
	       		txtStripper.setShouldSeparateByBeads( separateBeads );
	       		txtStripper.setStartPage( startPage );
	       		txtStripper.setEndPage( endPage );
	        		
	       		textContent = txtStripper.getText(document);
	       		if( document != null )
	       			document.close();
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing pdf : " + e.getMessage());
	       	}
		}
		return textContent;
	}

	public String getProcessedUserName() {
		return processedUserName;
	}

	public void setProcessedUserName(String processedUserName) {
		this.processedUserName = processedUserName;
	}
}
