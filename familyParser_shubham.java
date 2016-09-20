import java.io.*;
import java.util.*;

class familyParser_shubham
{
	public static final String separator = " ";
	public static void main(String[] args)
	{
		try
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Shubhsz\\Desktop\\agile\\project 3\\Main-GEDCOM-ver-2.ged"));
					
			File file = new File("C:\\Users\\Desktop\\agile\\outputshubham.txt");
			if (!file.exists()) 
			{
				file.createNewFile();
			}
			
			FileOutputStream fileos = new FileOutputStream(file);
			BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(fileos));
			
			String entry;
			ArrayList lineList = new ArrayList();
			
			while((entry = bufferedReader.readLine()) != null)
			{
				lineList.add(entry);
			}
			
			HashMap mapIndividual = new HashMap();
			HashMap mapFamily = new HashMap();
			ArrayList details = new ArrayList();
			String dateData = "";

			for(int i = 0;i<lineList.size();i++)
			{
				String str = lineList.get(i).toString();
				
				System.out.println(str);
				bufferwriter.write(str);
				bufferwriter.newLine();
				String[] strdel = str.split(" ");
				System.out.println(strdel[0]);
				bufferwriter.write(strdel[0]);
				bufferwriter.newLine();
				if(str.contains("INDI") || str.contains("FAM"))
				{
					if(strdel[2].equals("INDI"))
					{
						details = new ArrayList();
						System.out.println(strdel[2]);
						bufferwriter.write(strdel[2]);
						bufferwriter.newLine();
						mapIndividual.put(strdel[1],details);
					}
					if(strdel[2].equals("FAM"))
					{
						details = new ArrayList();
						System.out.println(strdel[2]);
						bufferwriter.write(strdel[2]);
						bufferwriter.newLine();
						mapFamily.put(strdel[1],details);
					}
					/*else
					{
						System.out.println(strdel[1]);
						bufferwriter.write(strdel[1]);
						bufferwriter.newLine();
					}*/
				}
				else
				{
					// System.out.println(str);
					// bufferwriter.write(str);
					// bufferwriter.newLine();
					// String[] strdel = str.split(separator);
					// System.out.println(strdel[0]);
					// bufferwriter.write(strdel[0]);
					// bufferwriter.newLine();
					
					
					
						if(strdel[1].equals("WIFE"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								String strn = strdel[1]+":"+strdel[2];
								details.add(strn);
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("TRLR"))
						{
							if(strdel[0].equals("0"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("CHIL"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								String strn = strdel[1]+":"+strdel[2];
								details.add(strn);
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("HUSB"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								//output += strdel[1]+eol;
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								String strn = strdel[1]+":"+strdel[2];
								details.add(strn);
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("DEAT"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								dateData = "";
								dateData = strdel[1]+":";
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("NAME"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								String strn = strdel[1]+":"+strdel[2]+strdel[3];
								details.add(strn);
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						else if(strdel[1].equals("HEAD"))
						{
							if(strdel[0].equals("0"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("MARR"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								dateData = "";
								dateData = strdel[1]+":";
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("NOTE"))
						{
							if(strdel[0].equals("0"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						else if(strdel[1].equals("DIV"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								dateData = "";
								dateData = strdel[1]+":";
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						
						else if(strdel[1].equals("SEX"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								String strn = strdel[1]+":"+strdel[2];
								details.add(strn);
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						else if(strdel[1].equals("BIRT"))
						{
							if(strdel[0].equals("1"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								dateData = "";
								dateData = strdel[1]+":";
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						else if(strdel[1].equals("DATE"))
						{
							if(strdel[0].equals("2"))
							{
								System.out.println(strdel[1]);
								bufferwriter.write(strdel[1]);
								bufferwriter.newLine();
								dateData += strdel[2]+"-"+strdel[3]+"-"+strdel[4];
								details.add(dateData);
							}
							else
							{
								System.out.println("Invalid Tag");
								bufferwriter.write("Invalid Tag");
								bufferwriter.newLine();
							}
						}
						else
						{
							System.out.println("Invalid Tag");
							bufferwriter.write(strdel[1]);
							bufferwriter.newLine();
						}
				}
			}
			
			bufferwriter.close();
			System.out.println(mapIndividual.size());
			System.out.println(mapFamily.size());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}