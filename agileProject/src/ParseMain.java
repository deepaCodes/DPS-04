import java.util.Map;

public class ParseMain {

	public static void main(String[] args) {
		
		String path = "C:/Users/pkama/OneDrive/Documents/GitHub/DPS-04/agileProject/tESTING.ged";
/*
		Change the path of file
		String path ="W:/GitHub-Work/DPS-04/agileProject/Sprint 1.ged";
		String path = "C:/Users/pkama/OneDrive/Documents/GitHub/DPS-04/agileProject/Sprint 1.ged";
	    String path = "C:/Users/Shubhsz/Desktop/agile/DPS-04/agileProject/Sprint 1.ged";
*/
	        FileParser fp = new FileParser(path);
	        fp.parseFile();

	        DisplayFamilyInfo di = new DisplayFamilyInfo(path);
	        Map<String, FamInfo> fmap = di.parseFamily();
	        
	        //Display Family Information
	        System.out.println("FAMILY INFORMATION");
	        System.out.println();
	        System.out.println("FAMILYID\tMARRIED\t\tDIVORCED\t HUSBAND\tWIFE\tCHILDREN");
	        System.out.println("---------------*-------------*---------------*-----------*-------------*------------*");
	        fp.displayFamInfo(fmap);
	        
	        //Display Individual Information
	        System.out.println("\nINDIVIDUAL INFORMATION");
	        System.out.println();
	        fp.displayindividualInfo(fmap);
	        
	        //Display Warning Information

	        System.out.println();
	        System.out.println();
	        System.out.println("----------------------------------------List of Warnings or Errors------------------------------------------------");
	        fp.displayWarnings();
	        
	    }
}
