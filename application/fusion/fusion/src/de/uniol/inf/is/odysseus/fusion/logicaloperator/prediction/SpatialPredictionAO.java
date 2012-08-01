package de.uniol.inf.is.odysseus.fusion.logicaloperator.prediction;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SpatialPredict")
public class SpatialPredictionAO extends AbstractLogicalOperator  {
	
	@SuppressWarnings("unused")
	private String predictionFunction = null;
	
	private static final long serialVersionUID = 3958945913160253013L;

	public SpatialPredictionAO() {
        super();
    }

    public SpatialPredictionAO(final SpatialPredictionAO ao) {
        super(ao);

    }

    @Override
    public SpatialPredictionAO clone() {
        return new SpatialPredictionAO(this);
    }


	@Parameter(name = "Prediction", type = StringParameter.class)
	public void setContextSources(String prediction) {
		this.predictionFunction = prediction;
		System.out.println(prediction);
		//calcOutputSchema();
	}

}

