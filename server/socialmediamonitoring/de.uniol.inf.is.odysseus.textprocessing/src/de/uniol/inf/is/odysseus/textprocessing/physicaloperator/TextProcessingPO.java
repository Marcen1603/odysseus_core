package de.uniol.inf.is.odysseus.textprocessing.physicaloperator;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.textprocessing.types.ITextProcessing;
import de.uniol.inf.is.odysseus.textprocessing.types.TPRegistry;

public class TextProcessingPO<M extends ITimeInterval, R extends Tuple<M>> extends AbstractPipe<R, Tuple<M>>
{
	ITextProcessing algo;
	DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	
	private int ngramSize;
	private SDFAttribute inputText;
	//private SDFAttribute textNotToProcess;
	private int outputPort;
	
	private boolean doNgram = true;
	private boolean doStemming = true;
	private boolean doRemoveStopwords = true;
	//private boolean isThereTextNotToProcess = false;
	
	private int positionInputText = -1;
	//private int positionTextNotToProcess = -1;
		
	public TextProcessingPO(int outputPort, SDFAttribute inputText, int ngramSize, /*boolean doNgram,*/ boolean doStemming, boolean doRemoveStopwords/*, SDFAttribute textNotToProcess, boolean isThereTextNotToProcess*/)
	{
		super();
		this.outputPort = outputPort;
		this.inputText = inputText;
		this.ngramSize = ngramSize;
	//	this.doNgram = doNgram;
		this.doStemming = doStemming;
		this.doRemoveStopwords = doRemoveStopwords;
	//	this.textNotToProcess = textNotToProcess;
	//	this.isThereTextNotToProcess = isThereTextNotToProcess;
	}
	
	public TextProcessingPO(TextProcessingPO<M,R> textProcessingPO)
	{
		super(textProcessingPO);
		this.outputPort = textProcessingPO.outputPort;
		this.inputText = textProcessingPO.inputText;
		this.ngramSize = textProcessingPO.ngramSize;
	//	this.doNgram = textProcessingPO.doNgram;
		this.doStemming = textProcessingPO.doStemming;
		this.doRemoveStopwords = textProcessingPO.doRemoveStopwords;
		//	this.textNotToProcess = textProcessingPO.textNotToProcess;
		//	this.isThereTextNotToProcess = textProcessingPO.isThereTextNotToProcess;
	}
		
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		SDFSchema schema = getSubscribedToSource(0).getSchema();
				
		this.positionInputText = schema.indexOf(this.inputText);
	}

	@Override
	protected void process_next(R object, int port) 
	{
		manageTextProcessing(object);		
	}

	private void manageTextProcessing(R object) {

	/*	Date date = new Date();
		long tim = date.getTime();
		double me = (double)tim;
		
		System.out.println("----------START---------");
		System.out.println("LONG: " + tim);
		System.out.println("DOUBLE: " + tim);
		System.out.println("----------ENDE---------");
		
		DateFormat df = new SimpleDateFormat("HH:mm");
		Date d = new Date((long) (me));
		System.out.println("Uhrzeit: " + df.format(d));
	*/	
		synchronized (object) 
		{
			Vector<String> res = new Vector<String>();
			
			res.add(object.getAttribute(this.positionInputText).toString());
				
			if(this.doNgram)
			{
				algo = TPRegistry.getTextProcessingTypeByName("ngramprocessing");
				algo.setOptions(setOptionsForTextProcessingType());
				res = algo.startTextProcessing(res);
			}
			
			if(this.doStemming)
			{
				algo = TPRegistry.getTextProcessingTypeByName("stemmingprocessing");
				res = algo.startTextProcessing(res);
			}
			
			if(this.doRemoveStopwords)
			{
				algo = TPRegistry.getTextProcessingTypeByName("stopwordprocessing");
				algo.setOptions(null);
				res = algo.startTextProcessing(res);
			}	
			moveResultToOuputPort(object, res);
		}			
	}
	
	private String[] setOptionsForTextProcessingType()
	{
		String[] options = new String[2];
		options[0] = String.valueOf(this.ngramSize);
		options[1] = String.valueOf(this.ngramSize);

	    return options;
	}

	private void moveResultToOuputPort(R object, Vector<String> res) {
		
		/*Tuple outputTuple;
		
		
		if(this.isThereTextNotToProcess)
		{
			outputTuple = new Tuple(2, false);
			outputTuple.setAttribute(1, object.getAttribute(this.positionTextNotToProcess).toString());
		}
		else
		{
			outputTuple = new Tuple(1, false);
		}
		
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());
		*/		
						
		Iterator<String> iter = res.iterator();		
		List<String> li = new ArrayList<String>();
		
		while(iter.hasNext())
		{
			li.add(iter.next().toString());
		}
		
		object.setAttribute(this.positionInputText, li);
		transfer(object, 0);
		//outputTuple.setAttribute(0, li);
		
		/***Alternativ durch StringBuilder***/
		/*StringBuilder sb = new StringBuilder();		
	
		while(iter.hasNext())
			sb.append(iter.next() + " ");*/
		
		//object.setAttribute(this.positionInputText, sb);
		//transfer(object, 0);
		//outputTuple.setAttribute(0, sb.toString());
	
	}
}
