import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileParser {
	private String fPath;
	private Map<String, IndividualInfo> indiMap = new HashMap<String, IndividualInfo>();
	private Map<String, Family> familyMap = new HashMap<String, Family>();

	public FileParser(String fp) {
		fPath = fp;

	}

	public void parseFile() {
		File f = new File(fPath);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			// reading file
			int levelNo;
			String tagName;
			String thridArg = null;
			String line = null;
			List<String> tagList = this.getValidTags();
			// Read a line
			IndividualInfo iInfo = null;
			// Storing individual and family info

			Family fInfo = null;
			Family tempF = null;

			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				String[] split = line.split(" ");
				if (split.length < 3 && !split[1].equals("BIRT")) {
					continue;
				}
				// 3 fields of a line
				levelNo = Integer.parseInt(split[0]);
				tagName = split[1];
				if (split.length > 2) {
					thridArg = split[2];
				}
				// Populate individual data
				if (levelNo == 0 && thridArg.equals("INDI")) {
					if (iInfo != null) {
						indiMap.put(iInfo.getId(), iInfo);
					}

					iInfo = new IndividualInfo();
					iInfo.setId(tagName.split("@")[1]);
				}
				if (iInfo != null) {
					switch (tagName) {
					case "NAME":

						iInfo.setName(thridArg);
						break;
					case "SEX":

						iInfo.setGender(thridArg);
						break;
					case "FAMC":
						fInfo = new Family();
						fInfo.setfId(thridArg.split("@")[1]);
						iInfo.getChildFamilyList().add(thridArg.split("@")[1]);
						if (familyMap.containsKey(fInfo.getfId())) {
							tempF = familyMap.get(fInfo.getfId());
							tempF.addChild(iInfo);
						} else {
							fInfo.addChild(iInfo);
							familyMap.put(fInfo.getfId(), fInfo);

						}

						break;
					case "FAMS":
						fInfo = new Family();
						fInfo.setfId(thridArg.split("@")[1]);
						iInfo.getSpouseFamilyList().add(thridArg.split("@")[1]);
						if (familyMap.containsKey(fInfo.getfId())) {
							tempF = familyMap.get(fInfo.getfId());
							tempF.addSpouse(iInfo);
						} else {
							fInfo.addSpouse(iInfo);
							familyMap.put(fInfo.getfId(), fInfo);
						}
						break;
					case "BIRT":
						String l = br.readLine();
						String[] lSplit = l.split(" ");
						int day = Integer.parseInt(lSplit[2]);
						int month = Month.valueOf(lSplit[3]).ordinal();
						int year = Integer.parseInt(lSplit[4]);
						Calendar bDay = Calendar.getInstance();
						bDay.set(year, month, day);
						iInfo.setBirth(bDay.getTime());
						break;
					case "DEAT":
						l = br.readLine();
						lSplit = l.split(" ");
						day = Integer.parseInt(lSplit[2]);
						month = Month.valueOf(lSplit[3]).ordinal();
						year = Integer.parseInt(lSplit[4]);
						Calendar dDay = Calendar.getInstance();
						dDay.set(year, month, day);
						iInfo.setDeath(dDay.getTime());
						iInfo.setAlive(false);
						break;
					default:
						// System.out.println("haven't handled this condition");

					}
				}

			}
			if (iInfo != null) {
				indiMap.put(iInfo.getId(), iInfo);
			}
			//System.out.println("end of switch");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void displayFamInfo(Map<String, FamInfo> fMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (Entry<String, FamInfo> fa : fMap.entrySet()) {
			System.out.print(fa.getValue().getFid() + "\t");
			if(fa.getValue().getMarr()!=null){
				System.out.print(sdf.format(fa.getValue().getMarr())+"\t");

			}
			if(fa.getValue().getDiv()!=null){
				System.out.print(sdf.format(fa.getValue().getDiv())+"\t");

			}
					 
			System.out.print(fa.getValue().getHusbId() + "\t"
					+ fa.getValue().getWifeId() + "\t"
					+ fa.getValue().getChildId());
			System.out.println();
		}
	}

	public void displayindividualInfo(Map<String, FamInfo> fmap) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// display individual information
		System.out.println("ID\tNAME\tSEX\tDOB\tAlive\t\tDOD\t\tCHILDREN\tSPOUSE");
		for (Entry<String, IndividualInfo> i : indiMap.entrySet()) {
			IndividualInfo value = i.getValue();
			System.out.print(value.getId() + "\t");
			if (value == null) {
				continue;
			}

			System.out.print(value.getName() + "\t");
			System.out.print(value.getGender() + "\t");
			if (value.getDeath() != null) {
				System.out.print(sdf.format(value.getBirth()) + "\t");
			}
			System.out.print(value.isAlive());
			if (value.getDeath() != null) {
				System.out.print(sdf.format(value.getDeath()) + "\t");
			}

			if (!value.getSpouseFamilyList().isEmpty()) {

				StringBuffer sb = new StringBuffer();
				sb.append("[");

				for (String fid : value.getSpouseFamilyList()) {

					Family f = this.familyMap.get(fid);

					if (f != null) {
						for (IndividualInfo ind : f.getSpouse()) {
							if (!ind.getId().equals(value.getId())) {
								sb.append(ind.getId() + ",");
							}
						}
					}

				}

				sb.append("]");
				System.out.print("\ts->"+ sb.toString() + "\t");

			}
			
			if (!value.getSpouseFamilyList().isEmpty()) {

				StringBuffer sb = new StringBuffer();
				sb.append("[");

				for (String fid : value.getSpouseFamilyList()) {

					Family f = this.familyMap.get(fid);

					if (f != null) {
						for (IndividualInfo ind : f.getChild()) {
							if (!ind.getId().equals(value.getId())) {
								sb.append(ind.getId() + ",");
							}
						}
					}

				}

				
				sb.append("]");
				System.out.print("c->" + sb.toString() + "\t");

			}

			System.out.println();

		}// for ends

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
