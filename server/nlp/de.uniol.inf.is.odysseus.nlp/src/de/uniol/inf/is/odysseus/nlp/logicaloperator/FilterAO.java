package de.uniol.inf.is.odysseus.nlp.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.filter.FilterFactory;
import de.uniol.inf.is.odysseus.nlp.filter.IFilter;
import de.uniol.inf.is.odysseus.nlp.filter.exception.FilterNotFoundException;

@LogicalOperator(name="FILTERNLP", minInputPorts=1, maxInputPorts=1, doc = "Enables rudimentary filtering of NLP-annotated text with the ANNOTATENLP operator.", url = "http://example.com/MyOperator.html", category = { LogicalOperatorCategory.ADVANCED })
public class FilterAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = 6361019707888699748L;
	
	private String filter;
	private List<Option> options;
	
	private SDFSchema outputSchema = null;

	private OptionMap optionmap;

	private IFilter filterObject;

	private String expression;
	
	public FilterAO(){
        super();		

    }
     
    public FilterAO(FilterAO filterAO){
        super(filterAO);
        this.filter = filterAO.filter;
        this.options = filterAO.options;
        this.expression = filterAO.expression;
    }
    



    @Parameter(name="filter", type = StringParameter.class, optional = false)
	public void setFilter(String filter) {
		this.filter = filter;
	}

    public String getFilter(){
    	return filter;
    }

    @Parameter(name="expression", type = StringParameter.class, optional = false)
	public void setExpression(String expression) {
		this.expression = expression;
	}

    public String getExpression(){
    	return expression;
    }
	
    
    @Parameter(name="options", type = OptionParameter.class, isList = true, optional = true)
    public void setOptions(List<Option> options){
    	this.options = options;
    	this.optionmap = new OptionMap(options);   	
    }

    public List<Option> getOptions(){
    	return this.options;
    }
    public OptionMap getOptionMap(){
    	return this.optionmap;
    }

	
    @Override
    public AbstractLogicalOperator clone() {
        return new FilterAO(this);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public SDFSchema getOutputSchemaIntern(int pos) {
    	if (outputSchema==null) {
    		outputSchema = SDFSchemaFactory.createNewSchema(
    			getInputSchema(0).getURI(),
    			(Class<? extends IStreamObject<?>>) KeyValueObject.class, getInputSchema(0)
    					.getAttributes(), getInputSchema(0));
    	}
    	
        return outputSchema;
    }

	public IFilter getFilterObject() {
		if(filterObject == null)
			try {
				filterObject = FilterFactory.get(filter, expression);
			} catch (FilterNotFoundException e) {
				throw new ParameterException("Filter "+filter+" was not found.");
			}
		return filterObject;
	}
	
}
