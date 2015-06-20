/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(name = "ASSOCIATIVESTORAGE", minInputPorts = 1, maxInputPorts = 1, doc = "This operator stores streaming data in an associative storage", category = { LogicalOperatorCategory.ADVANCED })
public class AssociativeStorageAO extends AbstractLogicalOperator implements IStatefulAO{
    /**
     * 
     */
    private static final long serialVersionUID = 9017006066838894642L;
    private List<SDFAttribute> hierarchyAttributes;
    private List<SDFAttribute> indexAttributes;
    private SDFAttribute valueAttribute;
    private List<Integer> sizes;
    private String storageName;

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
        this.hierarchyAttributes = new LinkedList<>(operator.hierarchyAttributes);
        this.indexAttributes = new LinkedList<>(operator.indexAttributes);
        this.valueAttribute = operator.valueAttribute;
        this.sizes = new LinkedList<>(operator.sizes);
        this.storageName = operator.storageName;
    }

    @Parameter(name = "HIERARCHY", optional = false, type = ResolvedSDFAttributeParameter.class, isList = true)
    public void setHierarchy(List<SDFAttribute> attributes) {
        this.hierarchyAttributes = attributes;
    }
    
    public List<SDFAttribute> getHierarchy() {
    	return hierarchyAttributes;
    }

    /**
     * @return the hierarchy attributes
     */
    public List<SDFAttribute> getHierarchyAttributes() {
        return this.hierarchyAttributes;
    }

    @Parameter(name = "INDEX", optional = false, type = ResolvedSDFAttributeParameter.class, isList = true)
    public void setIndex(List<SDFAttribute> attributes) {
        this.indexAttributes = attributes;
    }

    public List<SDFAttribute> getIndex() {
    	return indexAttributes;
    }
    
    /**
     * @return the index attributes
     */
    public List<SDFAttribute> getIndexAttributes() {
        return this.indexAttributes;
    }

    @Parameter(name = "VALUE", optional = false, type = ResolvedSDFAttributeParameter.class, isList = false)
    public void setValue(SDFAttribute attribute) {
        this.valueAttribute = attribute;
    }

    public SDFAttribute getValue() {
    	return valueAttribute;
    }
    
    /**
     * @return the value attribute
     */
    public SDFAttribute getValueAttribute() {
        return this.valueAttribute;
    }

    @Parameter(name = "SIZES", optional = false, type = IntegerParameter.class, isList = true)
    public void setSizes(List<Integer> sizes) {
        this.sizes = sizes;
    }

    public String getStorageName() {
		return storageName;
	}
    
    @Parameter(name = "StorageName", optional = false, type = StringParameter.class)
    public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
    
    /**
     * @return the sizes
     */
    public List<Integer> getSizes() {
        return this.sizes;
    }

    public int getDimension() {
        Objects.requireNonNull(this.sizes);
        return this.sizes.size();
    }

    public int getSize(int dimension) {
        Preconditions.checkElementIndex(dimension, this.sizes.size());
        return this.sizes.get(dimension);
    }

    public int getDepth() {
        return this.hierarchyAttributes.size();
    }

    public int[] getHierachyPositions() {
        SDFSchema schema = getInputSchema(0);
        List<SDFAttribute> hierachyAttributes = getHierarchyAttributes();
        Objects.requireNonNull(schema);
        Objects.requireNonNull(hierachyAttributes);
        final int[] pos = new int[hierachyAttributes.size()];
        int i = 0;
        for (final SDFAttribute attribute : hierachyAttributes) {
            pos[i++] = schema.indexOf(attribute);
        }
        return pos;
    }

    public int[] getIndexPositions() {
        SDFSchema schema = getInputSchema(0);
        List<SDFAttribute> attributes = getIndexAttributes();
        Objects.requireNonNull(schema);
        Objects.requireNonNull(attributes);
        final int[] pos = new int[attributes.size()];
        int i = 0;
        for (final SDFAttribute attribute : attributes) {
            pos[i++] = schema.indexOf(attribute);
        }
        return pos;
    }

    public int getValuePosition() {
        SDFSchema schema = getInputSchema(0);
        SDFAttribute attribute = getValueAttribute();
        Objects.requireNonNull(schema);
        Objects.requireNonNull(attribute);
        return schema.indexOf(attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return this.indexAttributes.size() == this.sizes.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new AssociativeStorageAO(this);
    }

}
