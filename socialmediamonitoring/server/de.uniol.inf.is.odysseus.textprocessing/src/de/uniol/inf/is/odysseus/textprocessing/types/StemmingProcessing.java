package de.uniol.inf.is.odysseus.textprocessing.types;

import java.util.Iterator;
import java.util.Vector;

public class StemmingProcessing implements ITextProcessing {

	String kpiType = "stemmingprocessing";
	org.tartarus.snowball.SnowballStemmer stemmer;
	private String stopWordFlag = "true";
	
	public StemmingProcessing(){}
	
	public StemmingProcessing(String processType){
		this.kpiType = processType;
	}
	
	@Override
	public ITextProcessing getInstance(String process) {
		return new StemmingProcessing(this.kpiType);
	}

	@Override
	public void setTextProcessingTypeName(String process) {
		this.kpiType = process;
		
	}

	@Override
	public String getType() {
		return this.kpiType;
	}

	@Override
	public Vector<String> startTextProcessing(Vector<String> incomingText) {

		Vector<String> tmpResult = new Vector<String>();
		Iterator<String> iter = incomingText.iterator();
		
		
		if(this.stopWordFlag.equals("false"))
		{
			String[] tempText = iter.next().toString().split(" ");
			StringBuilder stringBuild = new StringBuilder();
			
			for(int i = 0; i< tempText.length; i++)
			{
				this.stemmer.setCurrent(tempText[i]);
				
				if(this.stemmer.stem())
					stringBuild.append(this.stemmer.getCurrent() + " ");
			}
			tmpResult.add(stringBuild.toString());
		}
		else
		{
			while(iter.hasNext())
			{
				this.stemmer.setCurrent(iter.next().toString());
				
				if(this.stemmer.stem())
					tmpResult.add(this.stemmer.getCurrent());
			}
		}
		return tmpResult;
	}

	@Override
	public void setOptions(String[] options) {
		
		if(options[0].equals("german"))
			this.stemmer = new org.tartarus.snowball.ext.germanStemmer();
		else if(options[0].equals("english"))
			this.stemmer = new org.tartarus.snowball.ext.englishStemmer();
		else
			this.stemmer = new org.tartarus.snowball.ext.porterStemmer();
		
		this.stopWordFlag = "true";
		
		if((options[1].equals("false")))
		{
			this.stopWordFlag = "false";
		}
	}
}
