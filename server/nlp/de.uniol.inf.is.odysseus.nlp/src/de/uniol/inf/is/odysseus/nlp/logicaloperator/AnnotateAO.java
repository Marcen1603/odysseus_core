package de.uniol.inf.is.odysseus.nlp.logicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.nlp.toolkits.NLPToolkit;
import de.uniol.inf.is.odysseus.nlp.toolkits.ToolkitFactory;

/**
 * Logical operator component of the ANNOTATE operator.
 * @author yannickhabecker
 *
 */
@LogicalOperator(name="ANNOTATE", minInputPorts=1, maxInputPorts=1, doc = "Annotates a specific attribute of an input stream with natural language processing methods.", url = "http://example.com/MyOperator.html", category = { LogicalOperatorCategory.ADVANCED })
public class AnnotateAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -1462065636002048986L;
	
	//List of information included in output stream (eg. tokens, sentences, pos-tags...)
	private List<String> information;
	
	//user-specified nlp-toolkit (eg. opennlp)
	private String toolkit;
	
	//user-specified attribute that is going to be analyzed
	private SDFAttribute attribute;
	
	//output-attribute that represents the sentences 
	private SDFAttribute sentencesAttribute = new SDFAttribute(null, "sentence",
            SDFDatatype.LIST_STRING, null, null, null);
	//output-attribute that represents the tokens 
	private SDFAttribute tokensAttribute = new SDFAttribute(null, "token",
            SDFDatatype.LIST_STRING, null, null, null);
	//user-specified nlp-toolkit (eg. OpenNLPToolkit)
	private NLPToolkit nlpToolkit;
	
	public AnnotateAO(){
        super();
    }
     
    public AnnotateAO(AnnotateAO annotateAO){
        super(annotateAO);
        this.information = annotateAO.information;
        this.toolkit = annotateAO.toolkit;
        this.attribute = annotateAO.attribute;
        this.nlpToolkit = annotateAO.nlpToolkit;
        this.sentencesAttribute = annotateAO.sentencesAttribute;
        this.tokensAttribute = annotateAO.tokensAttribute;
    }
    
    /**
     * Getter for Field nlpToolkit, if it's already set. Otherwise it gets the user-specified NLP-Toolkit.
     * @return NLPToolkit user-specified Toolkit
     * @see NLPToolkit
     * @see ToolkitFactory#get(String, List)
     */
    public NLPToolkit getNlpToolkit(){
    	if(nlpToolkit == null){
    		nlpToolkit = ToolkitFactory.get(toolkit, information);
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
    	List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();

    	if(information.contains("token"))
    		attributes.add(tokensAttribute);

    	if(information.contains("sentence"))
    		attributes.add(sentencesAttribute);
    	
        SDFSchema schema = SDFSchemaFactory.createNewAddAttributes(attributes, getInputSchema(0));
        return schema;
    }

	
	public int getSentencesAttributeOutputPosition(){
		return getOutputSchemaIntern(0).indexOf(sentencesAttribute);
	}

	public SDFAttribute getAttribute() {
		return attribute;
	}
    
}