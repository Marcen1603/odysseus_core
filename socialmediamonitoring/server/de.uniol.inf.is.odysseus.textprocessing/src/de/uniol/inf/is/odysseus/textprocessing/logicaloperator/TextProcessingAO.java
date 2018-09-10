package de.uniol.inf.is.odysseus.textprocessing.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name="TEXTPROCESSING", minInputPorts=1, maxInputPorts=1, category={LogicalOperatorCategory.BASE}, doc="Allows preprocessing of incoming text.")
public class TextProcessingAO extends UnaryLogicalOp  {

	private static final long serialVersionUID = -621181565304271532L;
	private int ngramSize;
	private boolean doNgram = false;
	private boolean doStemming = false;
	private boolean doRemoveStopwords = false;
	private String language = "porter";
	private SDFAttribute inputText;
	private int outputPort = 0;
		
	public TextProcessingAO()
	{
		super();
	}
	
	public TextProcessingAO(TextProcessingAO textProcessingAO)
	{
		super(textProcessingAO);
		
		this.ngramSize = textProcessingAO.ngramSize;
		this.inputText = textProcessingAO.inputText;
		this.outputPort = textProcessingAO.outputPort;
		this.doNgram = textProcessingAO.doNgram;
		this.doRemoveStopwords = textProcessingAO.doRemoveStopwords;
		this.doStemming = textProcessingAO.doStemming;
		this.language = textProcessingAO.language;
	}
	
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos){

		int posi = getInputSchema(0).findAttributeIndex(this.inputText.toString());
				
		List<SDFAttribute> attrList =  new ArrayList<>();
		attrList.addAll(getInputSchema(0).getAttributes());
		
		SDFDatatype list = new SDFDatatype(this.inputText.toString(), SDFDatatype.KindOfDatatype.LIST, SDFDatatype.STRING);
		SDFAttribute attr = new SDFAttribute("", this.inputText.getAttributeName().toString(), list);
		attrList.set(posi, attr);

		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attrList, getInputSchema(0)); 
		return outSchema;
	}
	
	@Parameter(name="doNgram", type = BooleanParameter.class, optional = true)
	public void setDoNgram(boolean doNgram) {
		this.doNgram = doNgram;
	}
	
	@Parameter(name="doRemoveStopwords", type = BooleanParameter.class, optional = false)
	public void setDoRemoveStopwords(boolean doRemoveStopwords) {
		this.doRemoveStopwords = doRemoveStopwords;
	}
	
	@Parameter(name="doStemming", type = BooleanParameter.class, optional = false)
	public void setDoStemming(boolean doStemming) {
		this.doStemming = doStemming;
	}
	
	@Parameter(name="inputText", type = ResolvedSDFAttributeParameter.class, optional = false)
	public void setInputText(SDFAttribute inputText) {
		this.inputText = inputText;
	}
		
	@Parameter(name="ngramSize", type = IntegerParameter.class, optional = true)
	public void setNGramSize(int ngramSize) {
		this.ngramSize = ngramSize;
	}
	
//	@Parameter(name="language", type = StringParameter.class, optional = true)
//	public void setLanguage(String  language) {
//		this.language = language;
//	}
		
//	public String getLanguage(){
//		return this.language;
//	}
	
	public boolean isDoNgram() {
		return this.doNgram;
	}

	public boolean isDoStemming() {
		return this.doStemming;
	}

	public boolean isDoRemoveStopwords() {
		return this.doRemoveStopwords;
	}
	public int getNGramSize()
	{
		return this.ngramSize;
	}
	
	public SDFAttribute getInputText(){
		return this.inputText;
	}
		
	public int getOutputPort()
	{
		return this.outputPort;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TextProcessingAO(this);
	}
	
	@Override
	public boolean isValid(){
		
		if(this.doNgram && (this.ngramSize == 0))
		{
			System.out.println("You set DoNGram true but the NGramSize is not bigger than zero.");
			return false;
		}
		
		if(!this.doNgram && (this.ngramSize > 0))
		{
			System.out.println("You set DoNGram false but the NGramSize is bigger than zero.");
			return true;
		}
		return true;
	}
}
