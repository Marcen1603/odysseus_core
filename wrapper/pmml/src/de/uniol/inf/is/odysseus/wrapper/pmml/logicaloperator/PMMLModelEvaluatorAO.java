package de.uniol.inf.is.odysseus.wrapper.pmml.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Defines model evaluator to run PMML data-mining
 * 
 * 
 * @author Viktor Spadi
 *
 */


@LogicalOperator(name="PMML_EVAL", minInputPorts=2, maxInputPorts = 2,
doc = "PMML ModelEvaluator, needs a model as input, "
		+ "it can be gathered with the PMML_MODEL operator", 
category = { "PMML" })
public class PMMLModelEvaluatorAO extends AbstractLogicalOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7218116303334455820L;

	public PMMLModelEvaluatorAO() {
		super();
	}
	
	public PMMLModelEvaluatorAO(PMMLModelEvaluatorAO pmmlModelEvaluatorAO) {
		super(pmmlModelEvaluatorAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new PMMLModelEvaluatorAO(this);
	}
	
	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int port) {
		String name = "result";
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.add(new SDFAttribute(null, name, SDFDatatype.OBJECT, null, null, null));
		
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0));
		return schema;
	}


}
