package de.uniol.inf.is.odysseus.wrapper.inertiacube.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "StoreInertia",
doc = "Stores the inertia cube stream to a file.", category={LogicalOperatorCategory.SINK})
public class StoreInertiaAO extends AbstractLogicalOperator {
    /** Auto generated serial UID. */
    private static final long serialVersionUID = 7393782401453648015L;
    private String path = "";

    /**
     * Standard constructor.
     */
    public StoreInertiaAO() {
        super();
    }

    /**
     * Copy constructor.
     * @param ao
     * Instance to copy.
     */
    public StoreInertiaAO(final StoreInertiaAO ao) {
        super(ao);
        this.path = ao.path;
    }

    @Override
    public StoreInertiaAO clone() {
        return new StoreInertiaAO(this);
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
