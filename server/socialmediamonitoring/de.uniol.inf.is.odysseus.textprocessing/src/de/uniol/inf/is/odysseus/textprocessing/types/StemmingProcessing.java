package de.uniol.inf.is.odysseus.textprocessing.types;

import java.util.Iterator;
import java.util.Vector;

public class StemmingProcessing implements ITextProcessing {

	String kpiType = "stemmingprocessing";
	org.tartarus.snowball.SnowballStemmer stemmer;
	
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
		
		while(iter.hasNext())
		{
			this.stemmer.setCurrent(iter.next().toString());
			
			if(this.stemmer.stem())
				tmpResult.add(this.stemmer.getCurrent());	
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
	}
}
