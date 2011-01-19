package de.uniol.inf.is.odysseus.datamining.classification;

public class QualityPrinter {
	
	private int count;
	private int correctCount;
	
	public void printQuality(boolean correct){
		count++;
		if(correct){
			correctCount++;
		}if(count == 600)
		System.out.println("count: "+ count  + " quality: " + ((double)correctCount) / count);
	}

}
