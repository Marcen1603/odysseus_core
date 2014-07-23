package de.uniol.inf.is.odysseus.textprocessing.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class StopwordProcessing implements ITextProcessing {

	String kpiType = "stopwordprocessing";
	Vector<String> result = new Vector<String>();
	weka.core.Stopwords stopWords = new weka.core.Stopwords();
	
	public StopwordProcessing(){}
	
	public StopwordProcessing(String processType){
		this.kpiType = processType;
	}
	
	@Override
	public ITextProcessing getInstance(String processType) {
		return new StopwordProcessing(this.kpiType);
	}

	@Override
	public String getType() {
		return this.kpiType;
	}

	@Override
	public void setTextProcessingTypeName(String processType) {
		this.kpiType = processType;
	}

	@Override
	public Vector<String> startTextProcessing(Vector<String> incomingText) {
		
		Vector<String> result = new Vector<String>();
		Iterator<String> iter = incomingText.iterator();
		
		while(iter.hasNext())
		{
			String tempSentence = iter.next().toString();

			if(!this.stopWords.isStopword(tempSentence))			//Calls the WEKA-Stopword-methods
			{
				result.add(tempSentence);
			}
			else
			{
				System.out.println("Entfernt: " + tempSentence);
			}
		}
		return result;
	}
	
	private void setListWithAdditionalStopWords()
	{
		String[] parts;
		
		BufferedReader br = null;
        try 
        {
            br = new BufferedReader(new FileReader(new File("C:\\Users\\Rick\\workspace\\de.uniol.inf.is.odysseus.textprocessing\\OSGI-INF\\stopwords.txt")));
            String line = null;
            while((line = br.readLine()) != null) {            
                parts = line.split(",");
                for(String str : parts)
                	this.stopWords.add(str);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}

	@Override
	public void setOptions(String[] options) {
		setListWithAdditionalStopWords();
	}
}
