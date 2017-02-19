package de.uniol.inf.is.odysseus.nlp.logicaloperator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
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
@LogicalOperator(name="ANNOTATE", minInputPorts=1, maxInputPorts=9, doc = "Annotates a specific attribute of an input stream with natural language processing methods.", url = "http://example.com/MyOperator.html", category = { LogicalOperatorCategory.ADVANCED })
public class AnnotateAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = 2937642785475519576L;

	//List of information included in output stream (eg. tokens, sentences, pos-tags...)
	private List<String> information;
 	
	//user-specified nlp-toolkit (eg. opennlp)
	private String toolkit;
	
	//user-specified attribute that is going to be analyzed
	private SDFAttribute attribute;
	
	
	//user-specified nlp-toolkit (eg. OpenNLPToolkit)
	private NLPToolkit nlpToolkit;
	
	private HashMap<String, Option> configuration;
	
	private SDFSchema outputSchema = null;
	
	public AnnotateAO(){
        super();		

    }
     
    public AnnotateAO(AnnotateAO annotateAO){
        super(annotateAO);
        this.information = annotateAO.information;
        this.toolkit = annotateAO.toolkit;
        this.attribute = annotateAO.attribute;
        this.nlpToolkit = annotateAO.getNlpToolkit();
        this.configuration = annotateAO.configuration;
    }
    
    public NLPToolkit getNlpToolkit(){
    	if(nlpToolkit == null){
    		try {
    			nlpToolkit = ToolkitFactory.get(toolkit).getConstructor(List.class, HashMap.class).newInstance(information, configuration);
    		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
    				| NoSuchMethodException | SecurityException | NLPToolkitNotFoundException e) {
    			throw new ParameterException(e.getMessage());
    		} catch (InvocationTargetException e) {
    			throw new ParameterException(e.getCause().toString()+":"+e.getCause().getMessage());
    		}		
    	}
    	return nlpToolkit;
    }

    
    @Parameter(name="toolkit", type = StringParameter.class, optional = false)
	public void setToolkit(String toolkit) {
		this.toolkit = toolkit;	
	}

    public String getToolkit(){
    	return toolkit;
    }
    
    @Parameter(name="information", type = StringParameter.class, isList = true, optional = false)
	public void setInformation(List<String> information) {
    	this.information = information;
	}
    
	public List<String> getInformation(){
		return this.information;
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
    
    @Override
    public SDFSchema getOutputSchemaIntern(int pos) {
    	if (outputSchema==null) 
    		outputSchema = SDFSchemaFactory.createNewSchema(
    			getInputSchema(0).getURI(),
    			(Class<? extends IStreamObject<?>>) KeyValueObject.class, getInputSchema(0)
    					.getAttributes(), getInputSchema(0));
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
        return outputSchema;
    }


	public SDFAttribute getAttribute() {
		return attribute;
	}
    
}