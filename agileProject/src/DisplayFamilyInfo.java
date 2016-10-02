import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DisplayFamilyInfo {
	private String fPath;

	public DisplayFamilyInfo(String fp) {
		fPath = fp;
	}

	public Map<String, FamInfo> parseFamily() {
		File f = new File(fPath);
		Map<String, FamInfo> famMAp = new HashMap<>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			// reading file
			int levelNo;
			String tagName;
			String thridArg = null;
			String line = null;
			FamInfo famInfo = null;

			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				
				// 3 fields of a line
				levelNo = Integer.parseInt(split[0]);
				tagName = split[1];
				//System.out.println("tagnmae"+ tagName);
				if (split.length > 2) {
					thridArg = split[2];
				}
				// populate family data
				if (levelNo == 0 && null != thridArg && thridArg.equals("FAM")) {
					if (famInfo != null) {
						famMAp.put(famInfo.getFid(), famInfo);
					}
					famInfo = new FamInfo();
					famInfo.setFid(tagName.split("@")[1]);
				}
				if (famInfo != null) {
					if(null != thridArg && thridArg.split("@").length>1){
						thridArg = thridArg.split("@")[1];
					}
					
					switch (tagName) {
					case "HUSB":
						famInfo.setHusbId(thridArg);
						break;

					case "WIFE":
						famInfo.setWifeId(thridArg);
						break;

					case "CHIL":
						//System.out.println("inside child");
						famInfo.getChildId().add(thridArg);
						break;

					case "MARR":
						//System.out.println("marr");

						String l = br.readLine();
						String[] lSplit = l.split(" ");
						int day = Integer.parseInt(lSplit[2]);
						int month = Month.valueOf(lSplit[3]).ordinal();
						int year = Integer.parseInt(lSplit[4]);
						
						Calendar marrday = Calendar.getInstance();
						marrday.set(year, month, day);
						famInfo.setMarr(marrday.getTime());
						break;

					case "DIV":
						//System.out.println("div");
						 l = br.readLine();
						 lSplit = l.split(" ");
						 day = Integer.parseInt(lSplit[2]);
						 month = Month.valueOf(lSplit[3]).ordinal();
						 year = Integer.parseInt(lSplit[4]);
						
						Calendar divday = Calendar.getInstance();
						divday.set(year, month, day);
						famInfo.setDiv(divday.getTime());
						break;
					}

				}

			}
			if (famInfo != null) {
				famMAp.put(famInfo.getFid(), famInfo);
			}
		}// try
		catch (FileNotFoundException e) {
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
		return famMAp;
	}// close function

}// close class

