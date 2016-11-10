import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.*;

import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class FileParser {
	 private String fPath;
	    private Map<String, IndividualInfo> indiMap = new LinkedHashMap<String, IndividualInfo>();
	    private Map<String, Family> familyMap = new LinkedHashMap<String, Family>();

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
	            String lastName = null;
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
	                            warnings.add("\nWARNING--Sprint-1(US-22:Unique IDs) The person with id "+iInfo.getId()+" already Exists");
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
	                            iInfo.setLastName(split[3]);
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
	            
	            //Sprint-3 Divorce before death
	            if(!(indiMap.get(fa.getValue().getHusbId()).isAlive()) || !(indiMap.get(fa.getValue().getWifeId()).isAlive())){
	            	if((!(indiMap.get(fa.getValue().getHusbId()).isAlive())) &&(fa.getValue().getDiv()!=null)){
	            		if(indiMap.get(fa.getValue().getHusbId()).getDeath().before(fa.getValue().getDiv())){
	            			warnings.add("\nWARNING--Sprint-3(US-06:Divorce before death): Husband "+fa.getValue().getHusbId()
	            					     +" is dead before his divorce");
	            		}
	            		
	            	}
	            	
	            	if( !(indiMap.get(fa.getValue().getWifeId()).isAlive())&&(fa.getValue().getDiv()!=null)){
	            		if(indiMap.get(fa.getValue().getWifeId()).getDeath().before(fa.getValue().getDiv())){
	            			warnings.add("\nWARNING--Sprint-3(US-06:Divorce before death): Wife "+fa.getValue().getHusbId()
	            					     +"is dead before his divorce");
	            		}
	            		
	            	}
	            }

	            //Sprint-1 Check for correct Genders for Husband and Wife
	            if(indiMap.get((fa.getValue().getHusbId())).getGender().equals("F")){
	                //System.err.println("WARNING-- Husband Gender of "+fa.getValue().getHusbId() +" cannot be female");
	                warnings.add("\nWARNING--Sprint-1(US-21:Correct gender for role): Husband Gender of "+fa.getValue().getHusbId()+" belonging to family "+fa.getValue().getFid() +" cannot be female");
	            }

	            if(indiMap.get((fa.getValue().getWifeId())).getGender().equals("M")){
	                //System.err.println("WARNING-- Wife Gender cannot be male");
	                warnings.add("\nWARNING--Sprint-1(US-21:Correct gender for role): Wife Gender of "+fa.getValue().getWifeId()+" belonging to family "+fa.getValue().getFid() +" cannot be male");

	            }
	            //Sprint-2 US05	Marriage before death
	            if((indiMap.get((fa.getValue().getHusbId())).getDeath()!=null)) {
	            	if(indiMap.get((fa.getValue().getHusbId())).getDeath().before(fa.getValue().getMarr())){
	            		warnings.add("\nWARNING--Sprint-2(US-05:Marriage before death): "+indiMap.get((fa.getValue().getHusbId())).getName()+" is dead before marriage" );
	            		
	            	}
	            }
	            if((indiMap.get((fa.getValue().getWifeId())).getDeath()!=null)){
	            	if(indiMap.get((fa.getValue().getWifeId())).getDeath().before(fa.getValue().getMarr())){
	            		warnings.add("\nWARNING--Sprint-2(US-05:Marriage before death): "+indiMap.get((fa.getValue().getHusbId())).getName()+" is dead before marriage" );
	            		
	            	}
	            	
	            }
	            
	                        
	            //Sprint-2 US08 Birth before marriage of parents
	            if(fa.getValue().getChildId()!=null){
	            	for(String child:fa.getValue().getChildId()){
	            		IndividualInfo i= indiMap.get(child);
	            		
	            		if(fa.getValue().getMarr().after(i.getBirth())){
	            			warnings.add("\nWARNING--Sprint-2(US-08:Birth before marriage of parents): "+i.getName()+" is born before marriage" );
	            		}
	            	}
	            }
	            
	          //Sprint-2 US08 Birth before divorce of parents
	            if(fa.getValue().getChildId()!=null){
	            	for(String child:fa.getValue().getChildId()){
	            		IndividualInfo i= indiMap.get(child);
	            		
	            		if(fa.getValue().getDiv()!=null && i.getBirth()!=null){
	            			
	            		if(fa.getValue().getDiv().after(i.getBirth())){
	            			warnings.add("\nWARNING--Sprint-2(US-08:Birth before divorce of parents): "+i.getName()+" is born before divorce" );
	            			}
	            		}
	            	}
	            }
	            
	            //Sprint-2 US09 Birth before death of parents
	            if(fa.getValue().getChildId()!=null){
	            	
	            	for(String child:fa.getValue().getChildId()){
	            		IndividualInfo i= indiMap.get(child);
	            		IndividualInfo i1= indiMap.get(fa.getValue().getWifeId());
	            		
	            		//Child should be born before death of mother
	            		if(fa.getValue().getDiv()!=null && i.getBirth()!=null && fa.getValue().getWifeId()!=null && i1.getDeath()!=null && (i.getBirth().before(i1.getDeath()))) 
	            			{
	            			warnings.add("\nWARNING--Sprint-2(US-09:Birth before death of parents): "+i.getName()+" is born after death of parents" );
	            			}
	            		
	            		//before 9 months after death of father
		            	if(fa.getValue().getHusbId()!=null && indiMap.get(fa.getValue().getHusbId()).getDeath()!=null ){
		            		Date referenceDate = indiMap.get(fa.getValue().getHusbId()).getDeath();
			            	Calendar c = Calendar.getInstance(); 
			            	c.setTime(referenceDate); 
			            	c.add(Calendar.MONTH, -9);
			            	c.getTime();
	            		
	            		if(i.getBirth()!=null && c.getTime().after(i.getBirth())){
	            			warnings.add("\nWARNING--Sprint-2(US-09:Birth before death of parents): "+i.getName()+" is born after death of parents" );
	            			}
		            	}
	            	}
	            }
	          
	            //Sprint-3 US15 by Pooja Kamat	
	            if(fa.getValue().getChildId().size()>=15){
	            	warnings.add("\nWARNING--Sprint-3(US-15:Family ("+fa.getValue().getFid()+") should have fewer than 15 siblings." );
	            	}
	            
	            //Sprint-3 US33 by Pooja Kamat
	            if(indiMap.get(fa.getValue().getHusbId()).getAge() >= 2*indiMap.get(fa.getValue().getWifeId()).getAge() || 2*indiMap.get(fa.getValue().getHusbId()).getAge() <= indiMap.get(fa.getValue().getWifeId()).getAge()){
	            	warnings.add("\nWARNING--Sprint-3(US-34:Large age difference) Husband: " + indiMap.get(fa.getValue().getHusbId()).getName() +" and Wife: " + indiMap.get(fa.getValue().getWifeId()).getName());
	            }

	            
	            //Sprint-1 US01- check for Date before current date by Pooja Kamat
	            //check marriage date
	            if(fa.getValue().getMarr()!=null && fa.getValue().getMarr().after(new Date())){
	                warnings.add("\nWARNING--Sprint-1(US-01:Date before current date): Marriage Date "+sdf.format(fa.getValue().getMarr()) +" of "+indiMap.get(fa.getValue().getHusbId()).getName()+" occurs after current date "+sdf.format(currentDate));
	            }

	            //check divorce date
	            if(fa.getValue().getDiv()!=null && fa.getValue().getDiv().after(new Date())){
	                warnings.add("\nWARNING--Sprint-1(US-01:Date before current date): Divorce Date "+sdf.format(fa.getValue().getDiv()) +" of "+indiMap.get(fa.getValue().getHusbId()).getName()+" occurs after current date "+sdf.format(currentDate));

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
	            
	            sibingsShouldNotMarry(fa.getValue());
	            
	            maleLastNames(fa.getValue());
	            
	            marriageAfter14(fa.getValue());
	            siblingSpacing(fa.getValue());
				
	            System.out.print(husbName + "\t\t"
	                    + wifeName + "\t"
	                    + fa.getValue().getChildId());
	            System.out.println();
	        }
	    }
	  //Shubham Sprint 3
	    
	   
		//Sprint-1 by Pooja
		private void checkMarriageBeforeDivorce(FamInfo fa) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Date currentDate = new Date();
	        if( fa.getDiv()!=null && fa.getMarr()!=null && fa.getDiv().before(fa.getMarr())){
	            warnings.add("\nWARNING--Sprint-1(US-04:Marriage before divorce): Divorce Date "+sdf.format(fa.getDiv()) +" of "+indiMap.get(fa.getHusbId()).getName()+" occurs before marriage date "+sdf.format(fa.getMarr()));

	        }
	    }

		//Sprint-1 by Pooja
	    private void checkBirthDate(IndividualInfo individualInfo_) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Date currentDate = new Date();
	        if(individualInfo_.getDeath()!=null && individualInfo_.getBirth()!=null && individualInfo_.getBirth().after(individualInfo_.getDeath())){
	            warnings.add("\nWARNING--Sprint-1(US-01:Date before current date): Birth Date: "+sdf.format(individualInfo_.getBirth()) +" of "+individualInfo_.getName()+" occurs after current date "+sdf.format(currentDate));
	        }
	    }

	  //Sprint-1 by Pooja
	    private void checkDeathDate(IndividualInfo individualInfo_) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Date currentDate = new Date();
	        if(individualInfo_.getBirth()!=null && individualInfo_.getDeath()!=null && individualInfo_.getDeath().after(new Date())){
	            warnings.add("\nWARNING--Sprint-1(US-01:Date before current date): Death Date: "+sdf.format(individualInfo_.getBirth()) +" of "+individualInfo_.getName()+" occurs after current date"+sdf.format(currentDate));
	        }
	    }
	    
	  //Shubham Sprint 4 US 13 Sibling Spacing
	    
	    public void siblingSpacing (FamInfo fam )  {

	    		List<String> list = fam.getChildId();

	    		SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");


	    		for (int j = 0; j < fam.getChildId().size(); j++) {
	    			Date birthDate_j= null;
	    			IndividualInfo childList = indiMap.get(list.get(j));
	    			if (childList.getBirth()!=null){
	    				birthDate_j = childList.getBirth();
	    				
	    			}
	    			for (int k = 0; k < fam.getChildId().size(); k++){
	    				if (j!=k){
	    					Date birthDate_k = null;
	    					IndividualInfo innerChildList = indiMap.get(list.get(k));
	    					if (innerChildList.getBirth()!=null){
	    						birthDate_k = innerChildList.getBirth();
	    						 
	    						if (birthDate_j != null && birthDate_k !=null){
	    							int diff =(int) getDateDiff(birthDate_j,birthDate_k,TimeUnit.DAYS);
	    							if (diff > 2 && diff <243){
	    					            warnings.add("\nWARNING--Sprint-13(US13: Birth dates of siblings) : "+ childList.getName() + " (" +childList.getId()+")" +" and "+ innerChildList.getName()  + " (" +innerChildList.getId()+")" +" should be more than 8 months or less than 2 days apart in family "+fam.getDiv());

	    							}
	    						}
	    					}
	    				}	
	    			}
	    		}

	    	}

	    	public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    		long diffInMillies = date2.getTime() - date1.getTime();
	    		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	    	}

	    //Shubham Sprint 4 US 35 List Recent Births
	    public void RecentBirths(IndividualInfo indiInfo) {

	    	
	    	try 
	        {
	            //SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");

	            Date birthDate = indiInfo.getBirth();
	            //warnings.add("Test:"+birthDate);
	            Date currentDate = new Date();

	            long diff = currentDate.getTime() - birthDate.getTime();
	            long diffHours = diff / (60 * 60 * 1000);
	            long diffDays = diffHours / 24; // 48
	            
	            if (birthDate.compareTo(currentDate) < 0 && diffDays < 30) 
	            {
	            	warnings.add("\nWARNING--Sprint-35(US-35: List of Recent Birthdates) : " + " Individual is born in the last 30 days Id: " + indiInfo.getId() + " Name: " + indiInfo.getName()) ;

	            }
	        } catch (Exception ex) {

	        }
	    }
	    //Shubham Sprint 3
	 
	    public void marriageAfter14(FamInfo fam) {
	    	//warnings.add("Hallelujah");
	    	Date marr_date = fam.getMarr();
	    	Date wife_birthDate = indiMap.get(fam.getWifeId()).getBirth();
	    	Date husb_birthDate = indiMap.get(fam.getHusbId()).getBirth();
	    	
            try {
                //SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");

                //Date wifebirthDate = format.parse(wife_birthDate.toString());
                //Date husbandbirthDate = format.parse(husb_birthDate.toString());
                //Date marriageDate = format.parse(marr_date.toString());

                long wifediff = marr_date.getTime() - wife_birthDate.getTime();
                long wifediffHours = wifediff / (60 * 60 * 1000);
                long wifediffDays = wifediffHours / 24; // 48
                long wifediffYear = wifediffDays / 365;
                //warnings.add("Wife age at Marriage:"+wifediffYear);
                
                long husbdiff = marr_date.getTime() - husb_birthDate.getTime();
                long husbdiffHours = husbdiff / (60 * 60 * 1000);
                long husbdiffDays = husbdiffHours / 24; // 48
                long husbdiffYear = husbdiffDays / 365;
                //warnings.add("Husband age at Marriage:"+husbdiffYear);
                if (wifediffYear < 14) {
                	warnings.add("\nWARNING--Sprint-3(US-10:Marriage after 14): Marriage should occur after 14 years for "+ indiMap.get(fam.getWifeId()).getName() +" (" +indiMap.get(fam.getWifeId()).getId() +") "+ "in Family " +fam.getFid());
                }
                
                if (husbdiffYear < 14) {
                	warnings.add("\nWARNING--Sprint-3(US-10:Marriage after 14): Marriage should occur after 14 years for "+ indiMap.get(fam.getHusbId()).getName() +" (" +indiMap.get(fam.getHusbId()).getId() +") "+ "in Family " +fam.getFid());
                }
            } catch (Exception ex) {
            	warnings.add(ex.toString());
            }
        }
	    
	    public void maleLastNames(FamInfo fam) 
	    {
            List<String> nameStringList = new ArrayList<String>();
            
            nameStringList.add(indiMap.get(fam.getHusbId()).getLastName());

            for (int i = 0; i < fam.getChildId().size(); i++) {
            	//warnings.add("Gender:"+indiMap.get(fam.getChildId().get(i)).getGender());
                if (indiMap.get(fam.getChildId().get(i)).getGender().equals("M"))
                    nameStringList.add(indiMap.get(fam.getChildId().get(i)).getLastName());
            }
            //warnings.add("Size of list:"+nameStringList.toString());
            
            for (int i = 0; i < nameStringList.size(); i++) {
                for (int j = i + 1; j < nameStringList.size(); j++) {
                    if (!nameStringList.get(i).equals(nameStringList.get(j)))
                    	warnings.add("\nWARNING--Sprint-3(US-16:Male last names): " +nameStringList.get(i) + " and " +nameStringList.get(j) +" of family " +fam.getFid()+ " have different last names");

                        
                }
            }
            
        }
	    
	    
	    
		//Shubham Sprint 2
		private void checkUniqueNameBirth(IndividualInfo ind) 
		{
	        String indGivenName = (String)ind.getName();
	        //String indSurName = ind.getSurName();
	        Date indBirth = (Date)ind.getBirth();
			String indid = (String)ind.getId();
			
			Set set = indiMap.entrySet();
			Iterator it = set.iterator();
	        while(it.hasNext())
			{
				Map.Entry me = (Map.Entry)it.next();
				IndividualInfo value = (IndividualInfo)me.getValue();
				
	            String givenName = (String)value.getName();
	            //String surName = individuals.get(i).getSurName();
	            Date birth = (Date)value.getBirth();
	            String id = (String)value.getId();
	            //System.out.println("hiiii");
	            if (indGivenName!= null && indBirth != null && givenName!=null && birth!=null && !indid.equals(id) )
				{
	            	//System.out.println(indBirth+indGivenName+givenName+birth);
	            	String dt1 = indBirth.toString();
	            	String dt2 = birth.toString();
					if (indGivenName.equalsIgnoreCase(givenName) && dt1.equalsIgnoreCase(dt2)) 
					{
						warnings.add("\nWARNING--Sprint-2(US-23:Unique name and birth date): Individual "+ indGivenName + " (" +indid +") " +"matches with another Individual" +" (" +id +") " + "with same name and birth date: " + birth);
					}
				}
	        }
	        //return true;
	    }
		
		//Shubham Sprint-2
		public void sibingsShouldNotMarry (FamInfo fam)
		{
			List<String> childList = (fam.getChildId());
			//System.out.println("Size of childList:"+childList.size());
			//Collections.sort(childList);
			Set set = familyMap.entrySet();
			Iterator it = set.iterator();
			//Entry<String, IndividualInfo> i : indiMap.entrySet()
			for(Entry<String, Family> i:familyMap.entrySet())
			{
				Family fmly = i.getValue();
				List<String> couples = new ArrayList<String>();
				List<IndividualInfo> spouses = fmly.getSpouse();
				for(IndividualInfo ind: spouses)
				{
					couples.add(ind.getId());
				}
				
				int count =0;
				for(String cpl:couples)
				{
					if(childList.contains(cpl))
					{
						count++;
					}
				}
				if(count>1)
				{
					warnings.add("\nWARNING--Sprint-2(US-18:Siblings as couple): Family ID "+fmly.getfId()+" The siblings are couple");
				}
			}
			
			
		}
		
		
		//US-02 Shubham Sprint 1
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
						warnings.add("\nWARNING--Sprint-1(US-02:Birth before marriage): Marriage Date:" + sdf.format(fa.getMarr())+ " of "+indiMap.get(fa.getHusbId()).getName()+" occurs before birth date:"+ sdf.format(indiMap.get(fa.getHusbId()).getBirth()));
					}
				}
				if(fa.getWifeId()!=null)
				{
					Date wifeBirth = indiMap.get(fa.getWifeId()).getBirth();
					if(wifeBirth.after(marrDate))
					{
						warnings.add("\nWARNING--Sprint-1(US-02:Birth before marriage): Marriage Date: "+ sdf.format(fa.getMarr())+ " of "+indiMap.get(fa.getWifeId()).getName()+" occurs before birth date:"+sdf.format(indiMap.get(fa.getWifeId()).getBirth()));
					}
				}
			}
		}
		//US-03 Shubham Sprint 1
		private void checkDeathBeforeBirth(IndividualInfo individualInfo_) 
		 { SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        if(individualInfo_.getDeath()!=null && individualInfo_.getBirth()!=null && individualInfo_.getBirth().after(individualInfo_.getDeath())){
	            warnings.add("\nWARNING--Sprint-1(US-03:Birth before death): Birth Date:"+ sdf.format(individualInfo_.getBirth()) +" of "+ individualInfo_.getName() +" occurs after death date : "+  sdf.format(individualInfo_.getDeath()));
	        }
	    }
		
		
		public int findAge(Date target){
			Calendar currDate = Calendar.getInstance();
			// System.out.println(currDate);
			 		 
			 int age = currDate.get(Calendar.YEAR) - target.getYear();
			return age-1900;
		}
		
	    public void displayindividualInfo(Map<String, FamInfo> fmap) {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	        // display individual information

	        System.out.println("ID\tNAME\t\tSEX\tDOB\t\tAlive\t\tDOD\t\tAGE(US-27)\tSPOUSE\t\t\tCHILDREN");
	        System.out.println("-----*-----------*-----------*--------------*--------------*------------------*----------*---------------------------*-------------------------*");

	        for (Entry<String, IndividualInfo> i : indiMap.entrySet()) {
	            IndividualInfo value = i.getValue();
	            //calculate age of individuals
	           int age= findAge(i.getValue().getBirth());
	            
	            //List all the dead people
	            if(!value.isAlive()){
	            	warnings.add("\nWARNING--Sprint-2(US-29:List deceased): "+value.getName());
	            }
	            System.out.print(value.getId() + "\t");
	            if (value == null) {
	                continue;
	            }

	            System.out.print(value.getName() + "\t\t");
	            System.out.print(value.getGender() + "\t");
	            if (value.getBirth()!= null) {
	                System.out.print(sdf.format(value.getBirth()) + "");
	            }else{
	            	System.out.print("---\t");
	            }
	            System.out.print("\t"+value.isAlive());
	            if (value.getDeath() != null) {
	                System.out.print("\t\t" + sdf.format(value.getDeath()));
	            }else{
	            	System.out.print("\t\t---\t");
	            }
	            System.out.print("\t"+age);


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
	                System.out.print("c->" + sb.toString() + "\t\t");

	            }
	            System.out.println();
				
				//Shubham
				checkUniqueNameBirth(value);
				RecentBirths(value);

	        }// for ends
	        

	    }
	    
	    public void displayWarnings() {
	    	for (String warn : warnings) {
	            System.out.println(warn);
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