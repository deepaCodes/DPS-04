import java.util.Map;

public class ParseMain {

	public static void main(String[] args) {
		// Change the path of file

		String path = "W:/Tech Study/Agile/P02/TempGedcom.txt";

		FileParser fp = new FileParser(path);
		fp.parseFile();
		
		DisplayFamilyInfo di = new DisplayFamilyInfo(path);
		Map<String, FamInfo> fmap = di.parseFamily();
		System.out.println("FAMILY INFORMATION");
		System.out.println();
		System.out.println("Familyid\tMarried\tDivorced\t Husband\tWife\tChildren");
		fp.displayFamInfo(fmap);
		
		System.out.println("\nINDIVIDUAL INFORMATION");
		System.out.println();
		fp.displayindividualInfo(fmap);
	}

}
