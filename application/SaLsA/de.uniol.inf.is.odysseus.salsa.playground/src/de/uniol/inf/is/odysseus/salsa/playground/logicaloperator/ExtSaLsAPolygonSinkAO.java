package de.uniol.inf.is.odysseus.salsa.playground.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ExtSaLsAPolygonSink")
public class ExtSaLsAPolygonSinkAO extends AbstractLogicalOperator  {

	private static final long serialVersionUID = 3958945913160253013L;

	public ExtSaLsAPolygonSinkAO() {
        super();
    }

    public ExtSaLsAPolygonSinkAO(final ExtSaLsAPolygonSinkAO ao) {
        super(ao);

    }

    @Override
    public ExtSaLsAPolygonSinkAO clone() {
        return new ExtSaLsAPolygonSinkAO(this);
    }


}

