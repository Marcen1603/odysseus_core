package de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Logical operator for a visible sink.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "VKINECTSINK",
        doc = "Zeigt ein Fenster mit den Bildern der Kinect an.", category={LogicalOperatorCategory.SINK})
public class VisualKinectSinkAO extends AbstractLogicalOperator {
    /** Auto generated serial UID. */
    private static final long serialVersionUID = -200760768896205654L;

    /**
     * Standard constructor.
     */
    public VisualKinectSinkAO() {
        super();
    }

    /**
     * Copy constructor.
     * @param ao
     * Instance to copy.
     */
    public VisualKinectSinkAO(final VisualKinectSinkAO ao) {
        super(ao);
    }

    @Override
    public VisualKinectSinkAO clone() {
        return new VisualKinectSinkAO(this);
    }
    
    @Override
    public boolean isSourceOperator() {
    	return false;
    }
}
