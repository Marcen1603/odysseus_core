package de.uniol.inf.is.odysseus.textprocessing.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryAdapter;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryPermission;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="TEXTPROCESSING", minInputPorts=1, maxInputPorts=1, category={LogicalOperatorCategory.BASE}, doc="Allows preprocessing of incoming text.")
public class TextProcessingAO extends UnaryLogicalOp  {

	private static final long serialVersionUID = -621181565304271532L;
	private int ngramSize;
	//private boolean doNgram = true;
	private boolean doStemming = true;
	private boolean doRemoveStopwords = true;
	//private boolean isThereTextNotToProcess = false;
	private SDFAttribute inputText;
	//private SDFAttribute textNotToProcess;
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
	//	this.doNgram = textProcessingAO.doNgram;
		this.doRemoveStopwords = textProcessingAO.doRemoveStopwords;
		this.doStemming = textProcessingAO.doStemming;
	//	this.textNotToProcess = textProcessingAO.textNotToProcess;
	//	this.isThereTextNotToProcess = textProcessingAO.isThereTextNotToProcess;
	}
	
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos){

		int posi = getInputSchema(0).findAttributeIndex(this.inputText.toString());
				
		List<SDFAttribute> attrList =  new ArrayList<>();
		attrList.addAll(getInputSchema(0).getAttributes());
		
		SDFDatatype list = new SDFDatatype(this.inputText.toString(), SDFDatatype.KindOfDatatype.LIST, SDFDatatype.STRING);
		SDFAttribute attr = new SDFAttribute("", this.inputText.getAttributeName().toString(), list, null);
		attrList.set(posi, attr);

		SDFSchema outSchema = new SDFSchema(getInputSchema(0), attrList);
		return outSchema;
	}
	
/*	@Parameter(name="isThereTextNotToProcess", type = BooleanParameter.class, optional = true)
	public void setIsThereTextNotToProcess(boolean isThereTextNotToProcess) {
		this.isThereTextNotToProcess = isThereTextNotToProcess;
	}
*/	
/*	@Parameter(name="doNgram", type = BooleanParameter.class, optional = false)
	public void setDoNgram(boolean doNgram) {
		this.doNgram = doNgram;
	}*/
	
	@Parameter(name="doRemoveStopwords", type = BooleanParameter.class, optional = false)
	public void setDoRemoveStopwords(boolean doRemoveStopwords) {
		this.doRemoveStopwords = doRemoveStopwords;
	}
	
	@Parameter(name="doStemming", type = BooleanParameter.class, optional = false)
	public void setDoStemming(boolean doStemming) {
		this.doStemming = doStemming;
	}
	
/*	@Parameter(name="textNotToProcess", type = ResolvedSDFAttributeParameter.class, optional = true)
	public void setTextNotToProcess(SDFAttribute textNotToProcess) {
		this.textNotToProcess = textNotToProcess;
	}
*/	
	@Parameter(name="inputText", type = ResolvedSDFAttributeParameter.class, optional = false)
	public void setInputText(SDFAttribute inputText) {
		this.inputText = inputText;
	}
		
	@Parameter(name="ngramSize", type = IntegerParameter.class, optional = false)
	public void setDomain(int ngramSize) {
		this.ngramSize = ngramSize;
	}
	
/*	public boolean isThereTextNotToProcess() {
		return this.isThereTextNotToProcess;
	}
*/	
/*	public boolean isDoNgram() {
		return this.doNgram;
	}
*/
	public boolean isDoStemming() {
		return this.doStemming;
	}

	public boolean isDoRemoveStopwords() {
		return this.doRemoveStopwords;
	}
	public int getngramSize()
	{
		return this.ngramSize;
	}
	
	public SDFAttribute getInputText(){
		return this.inputText;
	}
	
/*	public SDFAttribute getTextNotToProcess(){
		return this.textNotToProcess;
	}
*/	
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
		
		/*
		if(this.isThereTextNotToProcess && (this.getTextNotToProcess() == null))
		{
			System.out.println("You set <isThereTextNotToProcess> = true but forgot to set variable TEXTNOTTOPROCESS!");
			return false;
		}
		
		if((!this.isThereTextNotToProcess()) && (this.getTextNotToProcess() != null))
		{
			System.out.println("You set <isThereTextNotToProcess> = false but set variable TEXTNOTTOPROCESS!");
			return false;
		}
		*/
		return true;
	}
}
