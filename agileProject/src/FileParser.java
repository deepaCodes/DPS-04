import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileParser {
	 private String fPath;
	    private Map<String, IndividualInfo> indiMap = new HashMap<String, IndividualInfo>();
	    private Map<String, Family> familyMap = new HashMap<String, Family>();

	    private List<String> warnings = new ArrayList<>();
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
	                        if(indiMap.containsKey(iInfo.getId())){
	                            warnings.add("\nWARNING--(US-22:Unique IDs) The person with id "+iInfo.getId()+" already Exists");
	                        }
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
	        Date currentDate = new Date();
	        for (Entry<String, FamInfo> fa : fMap.entrySet()) {
	            System.out.print(fa.getValue().getFid() + "\t\t");
	            if(fa.getValue().getMarr()!=null){
	                System.out.print(sdf.format(fa.getValue().getMarr())+"\t");

	            }else{
	            	System.out.print("---\t\t");
	            }
	            if(fa.getValue().getDiv()!=null){
	                System.out.print(sdf.format(fa.getValue().getDiv())+"\t");

	            }else{
	            	System.out.print("---\t\t");
	            }
	            //Display the names of husband and wife
	            String husbName =indiMap.get((fa.getValue().getHusbId())).getName() ;
	            String wifeName =indiMap.get((fa.getValue().getWifeId())).getName() ;

	            //Sprint-1 Check for correct Genders for Husband and Wife
	            if(indiMap.get((fa.getValue().getHusbId())).getGender().equals("F")){
	                //System.err.println("WARNING-- Husband Gender of "+fa.getValue().getHusbId() +" cannot be female");
	                warnings.add("\nWARNING--(US-21:Correct gender for role) Husband Gender of "+fa.getValue().getHusbId()+" belonging to family "+fa.getValue().getFid() +" cannot be female");
	            }

	            if(indiMap.get((fa.getValue().getWifeId())).getGender().equals("M")){
	                //System.err.println("WARNING-- Wife Gender cannot be male");
	                warnings.add("\nWARNING--(US-21) Wife Gender of "+fa.getValue().getWifeId()+" belonging to family "+fa.getValue().getFid() +" cannot be male");

	            }

	            //Sprint-1 check for Date before current date
	            //check marriage date
	            if(fa.getValue().getMarr()!=null && fa.getValue().getMarr().after(new Date())){
	                warnings.add("\nWARNING--(US-01:Date before current date) Marriage Date "+sdf.format(fa.getValue().getMarr()) +" of "+indiMap.get(fa.getValue().getHusbId()).getName()+" occurs after current date "+sdf.format(currentDate));
	            }

	            //check divorce date
	            if(fa.getValue().getDiv()!=null && fa.getValue().getDiv().after(new Date())){
	                warnings.add("\nWARNING--(US-01:Date before current date) Divorce Date "+sdf.format(fa.getValue().getDiv()) +" of "+indiMap.get(fa.getValue().getHusbId()).getName()+" occurs after current date "+sdf.format(currentDate));

	            }

	            //check the birth date of individuals
	            checkBirthDate(indiMap.get(fa.getValue().getWifeId()));
	            checkBirthDate(indiMap.get(fa.getValue().getHusbId()));

	            //check the death date of individuals
	            checkDeathDate(indiMap.get((fa.getValue().getWifeId())));
	            checkDeathDate(indiMap.get((fa.getValue().getHusbId())));

	            //check marriage before divorce
	            checkMarriageBeforeDivorce(fa.getValue());
				
				checkMarriageBeforeBirth(fa.getValue());
				//checkDeathBeforeBirth();
	            checkDeathBeforeBirth(indiMap.get((fa.getValue().getWifeId())));
	            checkDeathBeforeBirth(indiMap.get((fa.getValue().getHusbId())));

	            System.out.print(husbName + "\t\t"
	                    + wifeName + "\t"
	                    + fa.getValue().getChildId());
	            System.out.println();
	        }
	        for (String warn : warnings) {
	            System.out.println(warn);
	        }
	    }

	    private void checkMarriageBeforeDivorce(FamInfo fa) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Date currentDate = new Date();
	        if( fa.getDiv()!=null && fa.getMarr()!=null && fa.getDiv().before(fa.getMarr())){
	            warnings.add("\nWARNING--(US-04:Marriage before divorce) Divorce Date "+sdf.format(fa.getDiv()) +" of "+indiMap.get(fa.getHusbId()).getName()+" occurs before marriage date "+sdf.format(fa.getMarr()));

	        }
	    }

	    private void checkBirthDate(IndividualInfo individualInfo_) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Date currentDate = new Date();
	        if(individualInfo_.getDeath()!=null && individualInfo_.getBirth()!=null && individualInfo_.getBirth().after(individualInfo_.getDeath())){
	            warnings.add("\nWARNING--(US-01:Date before current date) Birth Date: "+sdf.format(individualInfo_.getBirth()) +" of "+individualInfo_.getName()+" occurs after current date "+sdf.format(currentDate));
	        }
	    }

	    private void checkDeathDate(IndividualInfo individualInfo_) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Date currentDate = new Date();
	        if(individualInfo_.getBirth()!=null && individualInfo_.getDeath()!=null && individualInfo_.getDeath().after(new Date())){
	            warnings.add("\nWARNING--(US-01:Date before current date) Death Date: "+sdf.format(individualInfo_.getBirth()) +" of "+individualInfo_.getName()+" occurs after current date"+sdf.format(currentDate));
	        }
	    }
		//US-02 Shubham
		private void checkMarriageBeforeBirth(FamInfo fa) 
		{
			Date marrDate = fa.getMarr();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(marrDate!=null)
			{
				if(fa.getHusbId()!=null)
				{
					Date husBirth = indiMap.get(fa.getHusbId()).getBirth();
					if(husBirth.after(marrDate))
					{
						warnings.add("\nWARNING--(US-02:Birth before marriage) Marriage Date:" + sdf.format(fa.getMarr())+ " of "+indiMap.get(fa.getHusbId()).getName()+" occurs before birth date:"+ sdf.format(indiMap.get(fa.getHusbId()).getBirth()));
					}
				}
				if(fa.getWifeId()!=null)
				{
					Date wifeBirth = indiMap.get(fa.getWifeId()).getBirth();
					if(wifeBirth.after(marrDate))
					{
						warnings.add("\nWARNING--(US-02:Birth before marriage) Marriage Date:  "+ sdf.format(fa.getMarr())+ " of "+indiMap.get(fa.getWifeId()).getName()+" occurs before birth date:"+sdf.format(indiMap.get(fa.getWifeId()).getBirth()));
					}
				}
			}
		}
		//US-03 Shubham
		private void checkDeathBeforeBirth(IndividualInfo individualInfo_) 
		 { SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        if(individualInfo_.getDeath()!=null && individualInfo_.getBirth()!=null && individualInfo_.getBirth().after(individualInfo_.getDeath())){
	            warnings.add("\nWARNING--(US-03:Birth before death) Birth Date:"+ sdf.format(individualInfo_.getBirth()) +" of "+ individualInfo_.getName() +" occurs after death date : "+  sdf.format(individualInfo_.getDeath()));
	        }
	    }
		
	    public void displayindividualInfo(Map<String, FamInfo> fmap) {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	        // display individual information
	        System.out.println("ID\tNAME\tSEX\tDOB\tAlive\t\tDOD\t\tSPOUSE\t\t\tCHILDREN");
	        System.out.println("---------------*-------------*---------------*-----------*-------------*------------*-------------*");

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
	            }else{
	            	System.out.print("---\t");
	            }
	            System.out.print(value.isAlive());
	            if (value.getDeath() != null) {
	                System.out.print(sdf.format(value.getDeath()) + "\t");
	            }else{
	            	System.out.print("\t\t---");
	            }

	            if (!value.getSpouseFamilyList().isEmpty()) {

	                StringBuffer sb = new StringBuffer();
	                sb.append("[");

	                for (String fid : value.getSpouseFamilyList()) {

	                    Family f = this.familyMap.get(fid);

	                    if (f != null) {
	                        for (IndividualInfo ind : f.getSpouse()) {
	                            if (!ind.getId().equals(value.getId())) {
	                                sb.append(ind.getName() + ",");
	                            }
	                        }
	                    }
	                    

	                }

	                sb.append("]");
	                
	                System.out.print("\t\ts->"+ sb.toString() + "\t\t");

	            }

	            if (!value.getSpouseFamilyList().isEmpty()) {

	                StringBuffer sb = new StringBuffer();
	                sb.append("[");

	                for (String fid : value.getSpouseFamilyList()) {

	                    Family f = this.familyMap.get(fid);

	                    if (f != null) {
	                        for (IndividualInfo ind : f.getChild()) {
	                            if (!ind.getId().equals(value.getId())) {
	                                sb.append(ind.getName() + ",");
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
