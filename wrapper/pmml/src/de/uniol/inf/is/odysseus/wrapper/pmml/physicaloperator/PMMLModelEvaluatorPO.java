package de.uniol.inf.is.odysseus.wrapper.pmml.physicaloperator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Computable;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.Value;
import org.jpmml.evaluator.association.Association;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.pmml.logicaloperator.PMMLModelEvaluatorAO;
import de.uniol.inf.is.odysseus.wrapper.pmml.logicaloperator.PMMLModelEvaluatorAO.EvaluatorOutputMode;

/**
 * PhysicalOperator implementation of {@link PMMLModelEvaluatorAO}
 * 
 * 
 * @author Viktor Spadi
 *
 */


@SuppressWarnings({"rawtypes", "unchecked"})
public class PMMLModelEvaluatorPO<T extends Tuple> extends AbstractPipe<T, T>{

	private static final Logger logger = LoggerFactory.getLogger(PMMLModelEvaluatorPO.class.getSimpleName());
	private static final String defaultModelName = "default";
	private String modelName;
	private EvaluatorOutputMode outputMode;
	private int stackSize;
	private ModelEvaluator evaluator;
	private Stack<T> unprocessedElements = new Stack<T>();
	
	private Map<String, InputField> fieldNameMap = new LinkedHashMap<>();
	private Map<FieldName, FieldValue> fieldMap = new LinkedHashMap<>();
	private Map<String, Integer> inputSchemaMapping = new LinkedHashMap<>();
	
	public PMMLModelEvaluatorPO(String modelName, EvaluatorOutputMode outputMode, int stackSize) {
		this.modelName = modelName;
		this.outputMode = outputMode;
		this.stackSize = stackSize;
		
		if(this.modelName == null || this.modelName.isEmpty()) {
			this.modelName = defaultModelName;
		}
	}
	
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	private ModelEvaluator getEvaluatorByName(PMML pmml, String name) {
		List<Model> models = pmml.getModels();
		
		// get model with same name, case sensitive at the moment
		Optional<Model> optionalModel = models.stream().filter(m -> modelName.equals(m.getModelName())).findAny();
		Model pmmlModel = null;
		if(optionalModel.isPresent()) {
			pmmlModel = optionalModel.get();
		} else if(defaultModelName.equals(this.modelName) && models.size() >0) {
			pmmlModel = models.get(0);
		}
		 
		if(pmmlModel != null) {
			ModelEvaluatorFactory factory = ModelEvaluatorFactory.newInstance();
			ModelEvaluator evaluator = factory.newModelEvaluator(pmml, pmmlModel);
			
			return evaluator;
		} else {
			logger.error("could not find any models in given PMML");
			// TODO maybe throw Exception over here
			// at the moment there will just be no output from the operator
			return null;
		}
	}
	
	private void setEvaluator(T object) {
		// here comes the model
		Object model = object.getAttribute(0);
		if(model instanceof PMML) {
			PMML pmml = (PMML)model;
			this.evaluator = this.getEvaluatorByName(pmml, this.modelName);
		}
		
		if(this.evaluator != null) {
			List<InputField> inputFields = this.evaluator.getInputFields();
			// clear local fieldNames -> but input schema should not change
			this.fieldNameMap.clear();
			for(InputField inputField : inputFields){
                fieldNameMap.put(inputField.getName().getValue(), inputField);
            }
			
			logger.info(String.format("received new evaluator with %s mining type", this.evaluator.getMiningFunction().name()));
			
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			
			SDFSchema inputSchema = getInputSchema(1);
			boolean fullSchema = this.outputMode == EvaluatorOutputMode.INPUT_SCHEMA;
			if(this.outputMode != EvaluatorOutputMode.NONE) {
				for(int i = 0; i < inputSchema.getAttributes().size(); i++) {
					SDFAttribute attribute = inputSchema.getAttribute(i);
					if(this.fieldNameMap.containsKey(attribute.getAttributeName()) || fullSchema) {
						outputAttributes.add(new SDFAttribute(null, attribute.getAttributeName(), attribute.getDatatype(), null, null, null));
					}
				}
			}
			// add result
			outputAttributes.add(new SDFAttribute(null, PMMLModelEvaluatorAO.RESULT_NAME, SDFDatatype.OBJECT, null, null, null));
			
			SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0));
			
			this.setOutputSchema(schema);
			
			// there may be some elements before model arrived
			if(!this.unprocessedElements.empty()) {
				while(!this.unprocessedElements.empty()) {
					this.evaluate(this.unprocessedElements.pop());
				}
			}
		}
	}
	
	@Override
	protected void process_open() {
		super.process_open();
		// parse input schema to output
		SDFSchema schema = this.getInputSchema(1);
		
		for(int i = 0; i < schema.getAttributes().size(); i++) {
			SDFAttribute attribute = schema.getAttribute(i);
			this.inputSchemaMapping.put(attribute.getAttributeName(), i);
		}
	}
	
	private void evaluate(T object) {
		// populate required fields 
		for(InputField field: this.fieldNameMap.values()) {
			Integer index = this.inputSchemaMapping.get(field.getName().getValue());
			this.fieldMap.put(field.getName(), field.prepare(object.getAttribute(index)));
		}
		
		// get predictions from evaluator and pass them to transfer function
		Map<FieldName,?> predictions = this.evaluator.evaluate(this.fieldMap);
		this.transferPredictions(object, predictions);
	}
	
	private void transferPredictions(T object, Map<FieldName, ?> predictions) {
		// check if association, this is the only computable case where a "UnsupportedOperationException" may be thrown
		// in this case we need to call "getRuleValues"
		for(Object o: predictions.values()) {
			Object value = null;
			if(o instanceof Association) {
				// not sure if this is correct, we will see if an association algorithm hits this point
				Association a = (Association)o;
				value = a.getAssociationRules();
			} else {
				if(o instanceof Computable) {
					value = ((Computable)o).getResult();
				} else if(o instanceof Value) {
					value = ((Value)o).getValue();
				} else if(o instanceof Number || o instanceof String) {
					value = o;
				} else {
					logger.warn("unsupported pmml evaluation result type: " + o.getClass().getSimpleName());
				}
				// there may be other types of values to return, but not supported at the moment
			}
			
			if(value != null) {
				Tuple t = null;
				// decide based on input type, if the tuple has to be populated with input tuple values
				switch(this.outputMode) {
					case NONE:
						t = new Tuple(1, false);
						t.setAttribute(0, value);
						break;
					case MODEL_SCHEMA:
						t = new Tuple(this.fieldNameMap.size()+1, false);
						int j = 0;
						for(InputField field: this.fieldNameMap.values()) {
							Integer index = this.inputSchemaMapping.get(field.getName().getValue());
							t.setAttribute(j++, object.getAttribute(index));
						}
						t.setAttribute(j, value);
						break;
					case INPUT_SCHEMA:
						t = new Tuple(object.getAttributes().length +1, false);
						for(int i = 0; i < object.getAttributes().length; i++) {
							t.setAttribute(i, object.getAttribute(i));
						}
						t.setAttribute(object.getAttributes().length, value);
						break;
				}
				
				if(t != null) {
					t.setMetadata(object.getMetadata().clone());
					transfer((T)t, 0);
				}
			}
		}
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		switch(port) {
			case 0:
				this.setEvaluator(object);
				break;
			case 1:
				// here comes the data
				if(this.evaluator != null) {
					this.evaluate(object);
				} else {
					// it is possible, that the first tuple comes before the model
					// so i built a simple cache 
					this.unprocessedElements.push(object);
					// make sure, that the stack does not exceed maximum size given by the parameter
					if(this.unprocessedElements.size() > this.stackSize) {
						this.unprocessedElements.remove(0);
					}
				}
				break;
			default:
				logger.warn(String.format("received unknown input on port %d", port));
				break;
		}
	}
}
