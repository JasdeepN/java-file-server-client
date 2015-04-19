

import java.io.*;
import java.util.*;

public class Top {
	public static void main(String []args) {

		ParseFile x = new ParseFile();
		Groupby g = new Groupby();
		List<Record> recordList = new ArrayList<Record>();

		recordList = x.load(args[0].toLowerCase());
		g.setData(recordList);
		try{
			switch (args[2]) 
			{ 
				case "sector":
				g.groupby(0);
				break; 

				case "employer":
				g.groupby(1);
				break;

				case "position":
				g.groupby(2);
				break;

				case "name":
				g.groupby(3);
				break;
			} 

			g.printTopK(Integer.parseInt(args[1]));	
		} catch (ArrayIndexOutOfBoundsException e){
			System.err.println ("Error parsing input");
		}
	}
}