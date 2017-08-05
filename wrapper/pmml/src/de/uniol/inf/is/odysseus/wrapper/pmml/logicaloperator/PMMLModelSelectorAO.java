package de.uniol.inf.is.odysseus.wrapper.pmml.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * Defines Model selector to create ModelEvaluator for running PMML data-mining
 * 
 * 
 * @author Viktor Spadi
 *
 */

@LogicalOperator(name="PMML_MODEL", minInputPorts=1, maxInputPorts = 1,
	doc = "PMML Modelselector, needs models as input, "
			+ "they can be gathered with the ACCESS operator using PMML as protocol", 
	category = { "PMML" })
public class PMMLModelSelectorAO extends AbstractLogicalOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9009207831581048186L;
	
	private String modelName;

	public PMMLModelSelectorAO() {
		super();
	}
	
	public PMMLModelSelectorAO(PMMLModelSelectorAO pmmlModelSelectorAO) {
		super(pmmlModelSelectorAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		PMMLModelSelectorAO operator = new PMMLModelSelectorAO(this);
		operator.modelName = this.modelName;
		return operator;
	}
	
	@Parameter(type=StringParameter.class, isList=false, optional = false, doc = "This parameter sets the desired model name")
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public String getModelName() {
		return this.modelName;
	}
	
	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int port) {
		String name = "model";
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.add(new SDFAttribute(null, name, SDFDatatype.OBJECT, null, null, null));
		
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0));
		return schema;
	}

}
