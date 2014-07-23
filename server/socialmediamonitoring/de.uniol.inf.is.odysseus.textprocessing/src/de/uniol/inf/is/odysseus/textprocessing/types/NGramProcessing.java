package de.uniol.inf.is.odysseus.textprocessing.types;

import java.util.Vector;
import weka.core.tokenizers.NGramTokenizer;

public class NGramProcessing implements ITextProcessing {

	String kpiType = "ngramprocessing";
	String[] options = new String[]{};
	Vector<String> result = new Vector<String>();

	public NGramProcessing(){}
	
	public NGramProcessing(String processType){
		this.kpiType = processType;
	}
	
	@Override
	public ITextProcessing getInstance(String process) {
		return new NGramProcessing(this.kpiType);
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
		try 
	    {                   
	      NGramTokenizer tokenizer = new NGramTokenizer();
	      this.result.clear();
	            
	      tokenizer.setOptions(this.options);
	      
	      for (int i = 0; i < incomingText.size(); i++) {
	        Vector<String> tmpResult = new Vector<String>();
	        tokenizer.tokenize(incomingText.get(i));
	        while (tokenizer.hasMoreElements()) {
	          tmpResult.add(tokenizer.nextElement().toString());
	        }
	        result.addAll(tmpResult);
	      }
	    } catch (Exception e1) {
	      e1.printStackTrace();
	    }    
		return this.result;
	}

	@Override
	public void setOptions(String[] paraOptions) {
		
		String[] tempOptions = new String[6];
		tempOptions[0] = "-max";
		tempOptions[1] = paraOptions[0];
		tempOptions[2] = "-min";
		tempOptions[3] = paraOptions[1];
		tempOptions[4] = "-delimiters"; 
		tempOptions[5] = " \r";
			
		this.options = tempOptions;
	}
}
