import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Project1 {

	public static void main(String[] args) {
		//Change the path of file
		
		String path = "W:/Tech Study/Agile/Project-01 GEDCOM file.txt";

		
		Project1 obj = new Project1();
		File f = new File(path);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			// reading file
			int levelNo;
			String tagName;
			String line = null;
			List<String> tagList = obj.getValidTags();

			while ((line = br.readLine()) != null) {
				System.out.println(line );
				String[] split = line.split(" ");
				levelNo = Integer.parseInt(split[0]);
				tagName = split[1];
				System.out.println("levelNo- " + levelNo);
				if (levelNo != 0 && !(levelNo == 1 && tagName.equals("DATE"))
						&& (tagList.contains(tagName))) {
					System.out.println("Tag Name: " + tagName);
				} else if(tagList.contains(split[1])) {
						System.out.println("Valid Tag");}
				else if(split.length>=3 && tagList.contains(split[2])){
					System.out.println("Valid Tag");
				}
				else{
					System.out.println("Invalid tag");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private List<String> getValidTags() {
		List<String> validTag = new ArrayList<String>();
		validTag.add("INDI");
		validTag.add("NAME");
		validTag.add("SEX");
		validTag.add("BIRT");
		validTag.add("DEA");
		validTag.add("FAMC");
		validTag.add("FAMS");
		validTag.add("FAM");
		validTag.add("MARR");
		validTag.add("HUSB");
		validTag.add("WIFE");
		validTag.add("CHIL");
		validTag.add("DIV");
		validTag.add("DATE");
		validTag.add("HEAD");
		validTag.add("TRLR");
		validTag.add("NOTE");

		return validTag;
	}

}
