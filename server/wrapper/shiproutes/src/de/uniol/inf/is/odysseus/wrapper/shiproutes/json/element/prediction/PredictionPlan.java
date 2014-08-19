package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class PredictionPlan implements IShipRouteElement {

	private static final String ELEMENT_PREFIX = "predPlan";

	private static final String NUMBER_OF_PREDICTION_POINTS = "number_of_Prediction_points";

	private Integer number_of_Prediction_points;
	private List<PredictionPoint> pred_points;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (number_of_Prediction_points != null)
			map.addAttributeValue(elementPrefix + NUMBER_OF_PREDICTION_POINTS,
					number_of_Prediction_points);
		
		if (pred_points != null){
			for (PredictionPoint predictionPoint : pred_points) {
				predictionPoint.fillMap(map, prefix);
			}
		}
	}

	public Integer getNumber_of_Prediction_points() {
		return number_of_Prediction_points;
	}

	public void setNumber_of_Prediction_points(
			Integer number_of_Prediction_points) {
		this.number_of_Prediction_points = number_of_Prediction_points;
	}

	public List<PredictionPoint> getPred_points() {
		return pred_points;
	}

	public void setPred_points(List<PredictionPoint> pred_points) {
		this.pred_points = pred_points;
	}

}
