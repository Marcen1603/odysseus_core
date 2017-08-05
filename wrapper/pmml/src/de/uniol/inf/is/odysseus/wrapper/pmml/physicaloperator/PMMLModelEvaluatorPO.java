package de.uniol.inf.is.odysseus.wrapper.pmml.physicaloperator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.clustering.ClusterAffinityDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

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
	private ModelEvaluator evaluator;
	private Stack<T> unprocessedElements = new Stack<T>();
	private Map<String, InputField> fieldNameMap = new LinkedHashMap<>();
	private Map<FieldName, FieldValue> fieldMap = new LinkedHashMap<>();
	
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		switch(port) {
			case 0:
				// here comes the evaluator
				Object evaluator = object.getAttribute(0);
				if(evaluator != null && evaluator instanceof ModelEvaluator) {
					this.evaluator = (ModelEvaluator)evaluator;
					List<InputField> inputFields = this.evaluator.getInputFields();
					// clear local fieldNames -> but input schema should not change
					this.fieldNameMap.clear();
					for(InputField inputField : inputFields){
		                fieldNameMap.put(inputField.getName().getValue(), inputField);
		            }
					logger.info("received new evaluator");
				}
				break;
			case 1:
				// here comes the data
				if(this.evaluator != null) {
					// TODO need to check, if incoming tuple matches needed inputs of evaluated model
					int i = 0;
					for(InputField field: this.fieldNameMap.values()) {
						this.fieldMap.put(field.getName(), field.prepare(object.getAttribute(i)));
						i++;
					}
					
					Map<FieldName,?> predictions = this.evaluator.evaluate(this.fieldMap);
					ClusterAffinityDistribution distribution = (ClusterAffinityDistribution)predictions.get(null);
					Tuple t = new Tuple(1, false);
					
					t.setAttribute(0, distribution.getResult());
					transfer((T)t, 0);
					//predictions.
					logger.info("new evaluation");
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
