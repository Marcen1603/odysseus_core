package de.uniol.inf.is.odysseus.wrapper.pmml.physicaloperator;

import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.pmml.server.PMMLProtocolHandler;

import java.util.List;
import java.util.Optional;

/**
 * PhysicalOperator implementation of {@link PMMLModelSelectorAO}
 * 
 * 
 * @author Viktor Spadi
 *
 */

@SuppressWarnings({"unchecked", "rawtypes"})
public class PMMLModelSelectorPO<T extends Tuple> extends AbstractPipe<T, T> {

	private static final Logger logger = LoggerFactory.getLogger(PMMLModelSelectorPO.class.getSimpleName());
	private static final String defaultModelName = "default";
	
	private String modelName;
	private PMML seletedPMML;
	
	public PMMLModelSelectorPO(String modelName) {
		this.modelName = modelName;
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

	@Override
	protected synchronized void process_next(T object, int port) {
		switch(port) {
			case 0:
				Object model = object.getAttribute(0);
				if(model instanceof PMML) {
					this.seletedPMML = (PMML)model;
					
					List<Model> models = this.seletedPMML.getModels();
					
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
						ModelEvaluator evaluator = factory.newModelEvaluator(this.seletedPMML, pmmlModel);
						Tuple t = new Tuple(1, false);
						t.setAttribute(0, evaluator);
						transfer((T)t, 0);
					} else {
						logger.error("could not find any models in given PMML");
					}
					
				} else {
					logger.warn("unknown datatype on port 0: %s", object.getClass().getSimpleName());
				}
				break;
			default:
				logger.warn("message on unused port %d with class: %s", port, object.getClass().getSimpleName());
		}
		
	}

}
