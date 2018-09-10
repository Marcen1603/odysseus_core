package de.uniol.inf.is.odysseus.textprocessing.types;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public class StopwordProcessing implements ITextProcessing {

	String kpiType = "stopwordprocessing";
	weka.core.Stopwords stopWords = new weka.core.Stopwords();
	private String stopWordFlag = "true";
	
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

	@SuppressWarnings("static-access")
	@Override
	public Vector<String> startTextProcessing(Vector<String> incomingText) {
		
		Vector<String> result = new Vector<String>();
		Iterator<String> iter = incomingText.iterator();
		
		if(this.stopWordFlag.equals("false"))
		{
			String[] tempText = iter.next().toString().split(" ");
			StringBuilder stringBuild = new StringBuilder();
			
			for(int i = 0; i< tempText.length; i++)
			{
				if(!this.stopWords.isStopword(tempText[i]))
				{
					stringBuild.append(tempText[i] + " ");
				}
				else
				{
					//System.out.println("Stopword:" + tempText[i] + " entfernt.");
				}
			}
			result.add(stringBuild.toString());
		}
		else
		{
			while(iter.hasNext())
			{
				String tempSentence = iter.next().toString();
	
				if(!this.stopWords.isStopword(tempSentence))
				{
					result.add(tempSentence);
				}
				else
				{
					//System.out.println("Stopword: " + tempSentence);
				}
			}
		}
		return result;
	}
	
	private void setListWithAdditionalStopWords()
	{
//		String[] parts;
//		
//		BufferedReader br = null;
//        try 
//        {
//        	br = new BufferedReader(new FileReader(new File("D:\\OdysseusOriginal\\trunk\\server\\socialmediamonitoring\\de.uniol.inf.is.odysseus.textprocessing\\OSGI-INF\\stopwords.txt")));
//            String line = null;
//            while((line = br.readLine()) != null) {            
//                parts = line.split(",");
//                for(String str : parts)
//                {
//                	this.stopWords.add(str);
//                }
//            }
//        } catch(FileNotFoundException e) {
//            e.printStackTrace();
//        } catch(IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(br != null) {
//                try {
//                    br.close();
//                } catch(IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
	}

	@Override
	public void setOptions(String[] options) {
		
		this.stopWordFlag = "true";
		
		if((options[0].equals("false")))
		{
			this.stopWordFlag = "false";
		}
		setListWithAdditionalStopWords();
	}
}
