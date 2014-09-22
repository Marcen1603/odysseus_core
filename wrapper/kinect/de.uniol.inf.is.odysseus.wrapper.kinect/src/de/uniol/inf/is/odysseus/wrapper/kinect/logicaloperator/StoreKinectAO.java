package de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "StoreKinect",
doc = "Stores the kinect stream to a file.", category={LogicalOperatorCategory.SINK})
public class StoreKinectAO extends AbstractLogicalOperator {
    /** Auto generated serial UID. */
    private static final long serialVersionUID = 7393782401453648015L;
    private String path = "";

    /**
     * Standard constructor.
     */
    public StoreKinectAO() {
        super();
    }

    /**
     * Copy constructor.
     * @param ao
     * Instance to copy.
     */
    public StoreKinectAO(final StoreKinectAO ao) {
        super(ao);
        this.path = ao.path;
    }

    @Override
    public StoreKinectAO clone() {
        return new StoreKinectAO(this);
    }
    
    @Parameter(type = StringParameter.class, name = "path")
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return this.path;
    }
    
    @Override
    public boolean isSourceOperator() {
    	return false;
    }
}
