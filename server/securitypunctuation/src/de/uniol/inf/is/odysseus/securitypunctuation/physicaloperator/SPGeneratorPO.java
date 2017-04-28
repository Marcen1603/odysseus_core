package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;


public  class SPGeneratorPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{
	

	String csvFile="C:\\Users\\Lennart\\Desktop\\SecurityPunctuation.csv";
	BufferedReader br=null;
	String line="";
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
		
		
	}
	public SPGeneratorPO(){
		super();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		int randomizer=(int)(Math.random()*5);
		if(randomizer==4){
			String[] temp=readCSV();
			sendPunctuation(new SecurityPunctuation(temp[0],temp[1],temp[2],temp[3],true,true,new Long(232)));
			
		}transfer(object);
		
	}
	public String[] readCSV(){
		 try {

	            br = new BufferedReader(new FileReader(csvFile));
	            while ((line = br.readLine()) != null) {

	                
	                String[] SPAttributes = line.split(";");

	                return SPAttributes;

	            }
		 } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		return null;
		

	}
	
	



	
	
	


}
