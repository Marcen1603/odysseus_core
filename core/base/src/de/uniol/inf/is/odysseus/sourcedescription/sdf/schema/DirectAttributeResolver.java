package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

/**
 * @author Jonas Jacobi
 */
public class DirectAttributeResolver implements IAttributeResolver {

    private static final long serialVersionUID = 692060392529144987L;
    private final SDFAttributeList schema;

    public DirectAttributeResolver(SDFAttributeList schema) {
        this.schema = schema;
    }

    public DirectAttributeResolver(
            DirectAttributeResolver directAttributeResolver) {
        this.schema = directAttributeResolver.schema.clone();
    }

    public SDFAttribute getAttribute(String name)
            throws AmgigiousAttributeException, NoSuchAttributeException {
        String[] parts = name.split("\\.", 2);
        SDFAttribute found = null;
        for (SDFAttribute attr : schema) {
            if (parts.length == 1) {
                if ((attr).getAttributeName().equals(name)) {
                    if (found != null) {
                        throw new AmgigiousAttributeException(name);
                    }
                    found = attr;
                }
            }
            else {
                if (attr.getPointURI().equals(name)) {
                    return attr;
                }
            }
        }
        if (found == null) {
            throw new NoSuchAttributeException(name);
        }
        return found;
    }

    @Override
    public DirectAttributeResolver clone() {
        return new DirectAttributeResolver(this);
    }

    @Override
    public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated) {
        // Nothing to do
    }

    public SDFAttributeList getSchema() {
        return this.schema;
    }
}
