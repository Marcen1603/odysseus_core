package de.uniol.inf.is.odysseus.wrapper.pmml.physicaloperator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.AffinityDistribution;
import org.jpmml.evaluator.Classification;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.Value;
import org.jpmml.evaluator.association.Association;
import org.jpmml.evaluator.clustering.ClusterAffinityDistribution;
import org.jpmml.evaluator.tree.NodeScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
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
	private ModelEvaluator evaluator;
	private Stack<T> unprocessedElements = new Stack<T>();
	
	private Map<String, InputField> fieldNameMap = new LinkedHashMap<>();
	private Map<FieldName, FieldValue> fieldMap = new LinkedHashMap<>();
	private Map<String, Integer> inputSchemaMapping = new LinkedHashMap<>();
	private Map<FieldName, Boolean> outputFields = new LinkedHashMap<>();
	
	public PMMLModelEvaluatorPO(String modelName, EvaluatorOutputMode outputMode) {
		this.modelName = modelName;
		this.outputMode = outputMode;
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
			return null;
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
		for(InputField field: this.fieldNameMap.values()) {
			Integer index = this.inputSchemaMapping.get(field.getName().getValue());
			this.fieldMap.put(field.getName(), field.prepare(object.getAttribute(index)));
		}
		
		Map<FieldName,?> predictions = this.evaluator.evaluate(this.fieldMap);
		this.transferPredictions(object, predictions);
	}
	
	private void transferPredictions(T object, Map<FieldName, ?> predictions) {
		// check if association, this is the only computable case where a "UnsupportedOperationException" may be thrown
		// in this case we need to call "getRuleValues"
		for(Object o: predictions.values()) {
			if(o instanceof Association) {
				// TODO implement output of association rules
			} else {
				
			}
		}
		
		ClusterAffinityDistribution distribution = (ClusterAffinityDistribution)predictions.get(null);
		Tuple t = new Tuple(1, false);
		
		Classification c;
		AffinityDistribution a;
		NodeScore n;
		Value v;
		ClusterAffinityDistribution ca;
		Association ac; // -> Computable
		
		
		t.setAttribute(0, distribution.getResult());
		t.setMetadata(object.getMetadata().clone());
		transfer((T)t, 0);
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		switch(port) {
			case 0:
				// here comes the model
				Object model = object.getAttribute(0);
				if(model instanceof PMML) {
					PMML pmml = (PMML)model;
					this.evaluator = this.getEvaluatorByName(pmml, this.modelName);
				}
				
				if(this.evaluator != null && this.evaluator instanceof ModelEvaluator) {
					this.evaluator = (ModelEvaluator)this.evaluator;
					List<InputField> inputFields = this.evaluator.getInputFields();
					// clear local fieldNames -> but input schema should not change
					this.fieldNameMap.clear();
					for(InputField inputField : inputFields){
		                fieldNameMap.put(inputField.getName().getValue(), inputField);
		            }
					logger.info("received new evaluator");
					if(!this.unprocessedElements.isEmpty()) {
						// process elements that were not processed 
					}
				}
				break;
			case 1:
				// here comes the data
				if(this.evaluator != null) {
					this.evaluate(object);
				} else {
					// it is possible, that the first tuple comes before the model
					// so i built a simple cache 
					unprocessedElements.push(object);
				}
				//InputField inputField = fieldNameMap.get(fieldName);
				//this.fieldMap.put(inputField.getName(), inputField.prepare(value));
				//Map<FieldName, ?> evaluation = this.evaluator.evaluate(this.fieldMa);
				break;
			default:
				break;
		}
	}


}
