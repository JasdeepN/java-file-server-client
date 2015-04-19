
import java.io.*;
import java.util.*;
import java.util.regex.*;


public class ParseFile implements RecordLoader{
	List<Record> recordList = new ArrayList<Record>();
	public List<Record> load(String filename){
		System.out.println("from server");

		Matcher matEmp ;
		Matcher matLNam ;
		Matcher matFNam ;
		Matcher matPos ;
		Matcher matSal ;
		Matcher matSec ;
		
		String Name = "";
		String Emp = "";
		String Sect = "";
		String Posi = "";
		Float Sala = 0.0f;

		try{

			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();

			String sector = "<h1> Public Sector Salary Disclosure for 2013: (?<sec>(.*))</h1>";
			//String employer = "(<td colspan=\"2\" align=\"left\" valign=\"top\"><span lang=\"en\">|<span lang=\"en\">)(?<emp>(.*))\\s+</span>";
			String employer = "<span lang=\"en\">(?<emp>(.*?))</span>";
			//String employer = "<span lang=\"en\">(?<emp>(.*?))</span>";
			String lastName = "<td align=\"left\" valign=\"top\">(?<last>(.*))</td>";
			String firstName = "<td colspan=\"2\" align=\"left\" valign=\"top\">(?<first>(.*))</td>";
			//String position = "^\\s+(?<pos>(.*?))(?=\\s?</span>)";
			//String test =   "<TAG\b[^>]*>(.*?)</TAG>";
			String position =  "^\\s+(?<pos>(.*?))</span>";
			String  salary = "<td align=\"right\" valign=\"top\">(?!<a)(?<sal>(.*))</td>";



			Pattern emp = Pattern.compile(employer);
			Pattern lnam = Pattern.compile(lastName);
			Pattern fnam = Pattern.compile(firstName);
			Pattern pos = Pattern.compile(position);
			Pattern sal = Pattern.compile(salary);
			Pattern sec = Pattern.compile(sector);


			
			while (line != null){

				matEmp = emp.matcher(line);
				matLNam = lnam.matcher(line);
				matFNam = fnam.matcher(line);
				matPos = pos.matcher(line);
				matSal = sal.matcher(line);
				matSec = sec.matcher(line);



				line = reader.readLine();

				


				while(matSec.find()){
					Sect = matSec.group("sec");
					//System.out.println("\t> Sector: " + matSec.group("sec"));
					//System.out.println(newRec.sector);

				}

				while(matEmp.find()){
					Emp =  matEmp.group("emp");
					//System.out.println("\t> Employer: " + matEmp.group("emp"));
				}

				while(matLNam.find()){
					Name = matLNam.group("last") + ", ";
					//System.out.print("\t> Name: " + matLNam.group("last"));
				}
				while(matFNam.find()){
					Name = Name + matFNam.group("first");
					//System.out.print(" " + matFNam.group("first") + "\n");
				}

				while(matPos.find()){
					Posi = matPos.group("pos");
					//System.out.println("\t> Position: " + matPos.group("pos"));
				}

				while(matSal.find()){
					String str1 = matSal.group("sal");
					str1 = str1.replace(",", "");
					str1 = str1.replace("$", "");
					Sala = Float.parseFloat(str1);

					Record newREC = new Record();
					newREC.sector = Sect;
					newREC.employer = Emp;
					newREC.name = Name;
					newREC.position = Posi;
					newREC.salary = Sala;
					recordList.add(newREC);
					//System.out.println("\t> Salary: " + matSal.group("sal"));
				}

				
			}
			
		}
		catch (IOException ex){
			System.err.println(">>>> ERROR <<<<");
		}
		return recordList;
	}
}

