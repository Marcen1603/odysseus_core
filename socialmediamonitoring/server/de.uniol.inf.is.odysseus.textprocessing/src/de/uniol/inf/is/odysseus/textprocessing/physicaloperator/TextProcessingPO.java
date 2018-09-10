package de.uniol.inf.is.odysseus.textprocessing.physicaloperator;

/**
 * Christopher Licht
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.textprocessing.types.ITextProcessing;
import de.uniol.inf.is.odysseus.textprocessing.types.TPRegistry;

public class TextProcessingPO<M extends ITimeInterval, R extends Tuple<M>> extends AbstractPipe<R, Tuple<M>>
{
	ITextProcessing algo;
	
	private int ngramSize;
	private SDFAttribute inputText;
	//private int outputPort;
	private boolean doNgram = false;
	private boolean doStemming = true;
	private boolean doRemoveStopwords = true;
	private String language = "porter";
	private int positionInputText = -1;
		
	public TextProcessingPO(int outputPort, SDFAttribute inputText, int ngramSize, boolean doNgram, boolean doStemming, boolean doRemoveStopwords /*String language*/)
	{
		super();
		//this.outputPort = outputPort;
		this.inputText = inputText;
		this.ngramSize = ngramSize;
		this.doNgram = doNgram;
		this.doStemming = doStemming;
		this.doRemoveStopwords = doRemoveStopwords;
	//	this.language = language;
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

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	private void manageTextProcessing(R object) 
	{
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
				
				if(!this.doNgram)
				{
					algo.setOptions(new String[]{this.language, "false"});
				}
				else
				{
					algo.setOptions(new String[]{this.language, "true"});
				}
				res = algo.startTextProcessing(res);
			}
			
			if(this.doRemoveStopwords)
			{
				algo = TPRegistry.getTextProcessingTypeByName("stopwordprocessing");
				
				if(!this.doNgram)
				{
					algo.setOptions(new String[]{"false"});
				}
				else
				{
					algo.setOptions(new String[]{""});
				}
				
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

	private void moveResultToOuputPort(R object, Vector<String> res) 
	{
		Iterator<String> iter = res.iterator();		
		List<String> li = new ArrayList<String>();
		
		while(iter.hasNext())
		{
			li.add(iter.next().toString());
		}
		
		object.setAttribute(this.positionInputText, li);
		transfer(object, 0);	
	}
}
