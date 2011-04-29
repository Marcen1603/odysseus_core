package de.uniol.inf.is.odysseus.labdataserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RDFStreamGenerator {

	public static void main(String[] args) throws IOException {
		for (int fileNo=1;fileNo<=1;fileNo++){
			FileWriter writer = new FileWriter(new File("labdata_cfg/rdfStream"+fileNo+".csv"));
			System.out.println("RDF "+fileNo);
			Random r = new Random();
			
			String[] possiblePredicates = {"wtur:TotWh", "wtur:StrCnt", "wtur:StopCnt", "wtur:err", 
											"wrot:RotSpd", "wrot:RotPos", "wrot:err", 
											"rdfs:seeAlso", 
											"ex:info"};
			
			String[] possibleErrMsgs = {"fatal", "serious", "major", "minor", "remark"};
			
			writer.write("subject;predicate;object;\n");
			
			for (int graph=1;graph<= 1000; graph++){
				int turbineNo = r.nextInt(10);
				int rotorNo = r.nextInt(10);
				int errMsg = r.nextInt(5);
				
				ArrayList<String> statements = new ArrayList<String>();
				
				boolean add = false;
				
				String s0 = "iec:wtur" + turbineNo + ";" + 
							"wtur:TotWh;"+ 
							r.nextInt() + "\n";
				
				add = toBoolean(r.nextInt(2));
				if(add){
					statements.add(s0);
				}
				
				String s1 = "iec:wtur" + turbineNo + ";" +
							"wtur:StrCnt;" +
							r.nextInt() + "\n";
				
				add = toBoolean(r.nextInt(2));
				if(add){
					statements.add(s1);
				}
				
				String s2 = "iec:wtur" + turbineNo + ";" +
							"wtur:StopCnt;" +
							r.nextInt() + "\n";
				
				add = toBoolean(r.nextInt(2));
				if(add){
					statements.add(s2);
				}
				
				int noOfTurErrs = r.nextInt(6);
				ArrayList<String> turErrs = new ArrayList<String>();
				for(int i = 0; i<noOfTurErrs; i++){
					String err = "iec:wtur" + turbineNo + ";" +
								"wtur:err;" +
								"err:wtur" + turbineNo + "turErr" + i + "\n";
		
					String errInfo = "err:wtur" + turbineNo + "turErr" + i + ";" +
								"ex:Info;" +
								possibleErrMsgs[r.nextInt(5)] + "\n";
					
					turErrs.add(err);
					turErrs.add(errInfo);
				}
				
				for(String err: turErrs){
					statements.add(err);
				}
				
				
				// adding rotor information ?
				boolean addRot = toBoolean(r.nextInt(2));
				
				if(addRot){
					String s4 = "iec:wtur" + turbineNo + ";" +
								"rdfs:seeAlso;" + 
								"wrot:wtur" + turbineNo + "wrot" + rotorNo + "\n";
					
					statements.add(s4);
					
					String s5 = "wrot:wtur" + turbineNo + "wrot" + rotorNo + ";" +
								"wrot:RotSpd;" +
								r.nextInt() + "\n";
					
					add = toBoolean(r.nextInt(2));
					if(add){
						statements.add(s5);
					}
					
					String s6 = "wrot:wtur" + turbineNo + "wrot" + rotorNo + ";" +
								"wrot:RotPos;" +
								r.nextDouble() + "\n";
					
					add = toBoolean(r.nextInt(2));
					if(add){
						statements.add(s6);
					}
					
					
					// random no, of error messages
					int noOfRotErrs = r.nextInt(6);
					ArrayList<String> rotErrs = new ArrayList<String>();
					
					for(int i = 0; i<noOfRotErrs; i++){
						String err = "wrot:wtur" + turbineNo + "wrot" + rotorNo + ";" +
									"wrot:err;" +
									"err:wtur" + turbineNo + "wrot" + rotorNo + "rotErr" + i + "\n";
						
						String errInfo = "err:wtur" + turbineNo + "wrot" + rotorNo + "rotErr" + i + ";" +
										"ex:Info;" +
										possibleErrMsgs[r.nextInt(5)] + "\n";
						
						rotErrs.add(err);
						rotErrs.add(errInfo);
					}
					
					for(String err: rotErrs){
						statements.add(err);
					}
				}
				
				Collections.shuffle(statements);
				
				
				for(String stmt : statements){
					writer.write(stmt);
				}
			}
			writer.flush();
		}
	}
	
	public static boolean toBoolean(int i){
		return i == 1;
	}
}
