package de.uniol.inf.is.odysseus.nlp.logicaloperator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.ToolkitFactory;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPToolkitNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;
/**
 * Logical operator component of the ANNOTATE operator
 */
@LogicalOperator(name="ANNOTATENLP", minInputPorts=1, maxInputPorts=Integer.MAX_VALUE, doc = "Annotates a specific attribute of an input stream with natural language processing methods.", url = "http://example.com/MyOperator.html", category = { LogicalOperatorCategory.ADVANCED })
public class AnnotateAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = 2937642785475519576L;

	//List of information included in output stream (eg. tokens, sentences, pos-tags...)
	private List<String> pipeline;
 	
	//user-specified nlp-toolkit (eg. opennlp)
	private String toolkit;
	
	//user-specified attribute that is going to be analyzed
	private SDFAttribute attribute;
	
	
	//user-specified nlp-toolkit (eg. OpenNLPToolkit)
	private NLPToolkit nlpToolkit;
	
	private HashMap<String, Option> configuration;
	
	private SDFSchema outputSchema = null;
	
	private String outputType = "KeyValueObject";

	private SDFAttribute objectOutput = new SDFAttribute(null, "output",
            SDFDatatype.OBJECT, null, null, null);
	
	public AnnotateAO(){
        super();		

    }
     
    public AnnotateAO(AnnotateAO annotateAO){
        super(annotateAO);
        this.pipeline = annotateAO.pipeline;
        this.toolkit = annotateAO.toolkit;
        this.attribute = annotateAO.attribute;
        this.nlpToolkit = annotateAO.getNlpToolkit();
        this.configuration = annotateAO.configuration;
        this.outputType = annotateAO.outputType;
    }
    
    public NLPToolkit getNlpToolkit(){
    	if(nlpToolkit == null){
    		try {
    			nlpToolkit = ToolkitFactory.get(toolkit).getConstructor(List.class, Map.class).newInstance(pipeline, configuration);
    		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
    				| NoSuchMethodException | SecurityException | NLPToolkitNotFoundException e) {
    			throw new ParameterException(e.getMessage());
    		} catch (InvocationTargetException e) {
    			e.getCause().printStackTrace();
    			throw new ParameterException(e.getCause().toString()+":"+e.getCause().getMessage());
    		}		
    	}
    	return nlpToolkit;
    }

    
    @Parameter(name="toolkit", type = StringParameter.class, optional = false)
	public void setToolkit(String toolkit) {
		this.toolkit = toolkit;	
	}

    @Parameter(name="output", type = StringParameter.class, optional = true)
	public void setOutput(String output) {
    	switch(output){
	    	case "Object":
	    	case "KeyValueObject":
	    		this.outputType = output;
	    		break;
	    	default:
	    		throw new ParameterException("Output must be either Object or KeyValueObject.");
    	}
	}

    public String getOutputType(){
    	return outputType;
    }
    
    public String getToolkit(){
    	return toolkit;
    }
    
    @Parameter(name="pipeline", type = StringParameter.class, isList = true, optional = false)
	public void setPipeline(List<String> pipeline) {
    	this.pipeline = pipeline;
	}
    
	public List<String> getPipeline(){
		return this.pipeline;
	}
	
    
    @Parameter(name="options", type = OptionParameter.class, isList = true, optional = true)
    public void setOptions(List<Option> options){
    	this.configuration = new HashMap<String, Option>();
    	for(Option option : options){
    		this.configuration.put(option.getName().toLowerCase(), option);
    	}    	
    }
    
    public HashMap<String, Option> getConfiguration(){
    	return this.configuration;
    }

	@Parameter(name="attribute", type = ResolvedSDFAttributeParameter.class, optional = false)
	public void setAttribute(SDFAttribute attribute) {
		this.attribute = attribute;
	}
	
    @Override
    public AbstractLogicalOperator clone() {
        return new AnnotateAO(this);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public SDFSchema getOutputSchemaIntern(int pos) {
    	if(outputSchema != null)
    		return outputSchema;
    	
    	if(outputType.equals("KeyValueObject")){
    		outputSchema = SDFSchemaFactory.createNewSchema(
    			getInputSchema(0).getURI(),
    			(Class<? extends IStreamObject<?>>) KeyValueObject.class, getInputSchema(0)
    					.getAttributes(), getInputSchema(0));
    	
    	}else{
    		List<SDFAttribute> attributes = new ArrayList<>();
    		attributes.add(objectOutput);
    		outputSchema = SDFSchemaFactory.createNewAddAttributes(attributes, getInputSchema(0));
    	}
    	//List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
    	/*
    	if(information.contains(Annotation.TOKENID))
    		attributes.add(tokensAttribute);

    	if(information.contains(Annotation.SENTENCEID))
    		attributes.add(sentencesAttribute);
    	
    	if(information.contains(Annotation.NERID))
    		attributes.add(namedEntityAttribute);
    		*/
    	
        //SDFSchema schema = SDFSchemaFactory.createNewAddAttributes(attributes, getInputSchema(0));
        return getOutputSchemaIntern(pos);
    }


	public SDFAttribute getAttribute() {
		return attribute;
	}
    
}