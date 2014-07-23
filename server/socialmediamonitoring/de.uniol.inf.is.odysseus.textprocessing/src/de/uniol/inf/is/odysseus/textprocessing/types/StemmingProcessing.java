package de.uniol.inf.is.odysseus.textprocessing.types;

import java.util.Iterator;
import java.util.Vector;

import org.tartarus.snowball.SnowballStemmer;

import weka.core.tokenizers.NGramTokenizer;

public class StemmingProcessing implements ITextProcessing {

	String kpiType = "stemmingprocessing";
	Vector<String> result = new Vector<String>();

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

		org.tartarus.snowball.ext.porterStemmer porterStemmer = new org.tartarus.snowball.ext.porterStemmer();
		Vector<String> tmpResult = new Vector<String>();
		
		Iterator<String> iter = incomingText.iterator();
		
		while(iter.hasNext())
		{
			porterStemmer.setCurrent(iter.next().toString());
			
			if(porterStemmer.stem())
				tmpResult.add(porterStemmer.getCurrent());	
		}
		
		return tmpResult;
	}

	@Override
	public void setOptions(String[] options) {
		// TODO Auto-generated method stub
	}
}
