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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

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
	
	public enum EvaluatorOutputMode {
		NONE, // just output model evaluation results
		INPUT_SCHEMA, // append evaluation to full input schema
		MODEL_SCHEMA // append evaluation to processed attributes
	};
	public static final String RESULT_NAME = "prediction";
	private static final long serialVersionUID = -7218116303334455820L;

	private String modelName;
	private EvaluatorOutputMode outputMode = EvaluatorOutputMode.NONE;
	private int stackSize = 100;
	
	public PMMLModelEvaluatorAO() {
		super();
	}
	
	public PMMLModelEvaluatorAO(PMMLModelEvaluatorAO pmmlModelEvaluatorAO) {
		super(pmmlModelEvaluatorAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		PMMLModelEvaluatorAO operator = new PMMLModelEvaluatorAO(this);
		operator.modelName = this.modelName;
		operator.outputMode = this.outputMode;
		operator.stackSize = this.stackSize;
		return operator;
	}
	
	@Parameter(type=StringParameter.class, isList=false, optional = false, doc = "This parameter sets the desired model name")
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	@Parameter(type=StringParameter.class, isList=false, optional = true, doc = "Sets output mode of operator, none for result only, input for all attributes and model for processed attributes only")
	public void setOutput(String output) {
		switch(output.toLowerCase()) {
			case "none":
				this.outputMode = EvaluatorOutputMode.NONE;
				break;
			case "input":
				this.outputMode = EvaluatorOutputMode.INPUT_SCHEMA;
				break;
			case "model":
				this.outputMode = EvaluatorOutputMode.MODEL_SCHEMA;
				break;
			default:
				this.outputMode = EvaluatorOutputMode.NONE;
		}
	}
	
	@Parameter(type=IntegerParameter.class, isList=false, optional = true, doc = "Sets maximum stack size for elements to be cached while no pmml evaluation model is arrived, default is 100")
	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}
	
	public String getModelName(){
		return this.modelName;
	}
	
	public int getStackSize() {
		return this.stackSize;
	}
	
	public EvaluatorOutputMode getOuputMode() {
		return this.outputMode;
	}
	
	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int port) {
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.add(new SDFAttribute(null, RESULT_NAME, SDFDatatype.OBJECT, null, null, null));
		
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0));
		return schema;
	}


}
