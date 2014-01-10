/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(name = "ASSOCIATIVESTORAGE", minInputPorts = 1, maxInputPorts = 1, doc = "This operator streaming data in an associative storage", category = { LogicalOperatorCategory.ADVANCED })
public class AssociativeStorageAO extends AbstractLogicalOperator {
    /**
     * 
     */
    private static final long serialVersionUID = 9017006066838894642L;
    private List<SDFAttribute> hierarchyAttributes;
    private List<SDFAttribute> attributes;
    private List<Integer> sizes;

    /**
 * 
 */
    public AssociativeStorageAO() {
        super();
    }

    /**
     * @param associativeStorage
     */
    public AssociativeStorageAO(AssociativeStorageAO operator) {
    }

    @Parameter(name = "HIERARCHY", optional = false, type = ResolvedSDFAttributeParameter.class, isList = true)
    public void setHierarchy(List<SDFAttribute> attributes) {
        this.hierarchyAttributes = attributes;
    }

    /**
     * @return the hierarchy attributes
     */
    public List<SDFAttribute> getHierarchy() {
        return this.hierarchyAttributes;
    }

    @Parameter(name = "ATTRIBUTES", optional = false, type = ResolvedSDFAttributeParameter.class, isList = true)
    public void setAttributes(List<SDFAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the attributes
     */
    public List<SDFAttribute> getAttributes() {
        return this.attributes;
    }

    @Parameter(name = "SIZES", optional = false, type = IntegerParameter.class, isList = true)
    public void setSizes(List<Integer> sizes) {
        this.sizes = sizes;
    }

    /**
     * @return the sizes
     */
    public List<Integer> getSizes() {
        return this.sizes;
    }

    public int getDimension() {
        return this.sizes.size();
    }

    public int getSize(int dimension) {
        Preconditions.checkElementIndex(dimension, this.sizes.size());
        return this.sizes.get(dimension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return this.attributes.size() == this.sizes.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new AssociativeStorageAO(this);
    }
}
