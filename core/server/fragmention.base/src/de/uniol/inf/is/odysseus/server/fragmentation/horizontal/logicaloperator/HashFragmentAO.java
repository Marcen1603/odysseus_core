package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * A {@link HashFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link HashFragmentAO} must have exact one input and can have only one
 * parameter for the number of fragments to build the hash key from the whole
 * tuple or more parameters specifying the hash key by attributes. <br />
 * It can be used in PQL: <br />
 * <code>output = HASHFRAGMENT([FRAGMENTS=n], input)</code> or <br />
 * <code>output = HASHFRAGMENT([FRAGMENTS=n, ATTRIBUTES=[attr1...attrn]], input)</code>
 * 
 * @author Michael Brand
 */
@LogicalOperator(name = "HASHFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc = "Can be used to fragment incoming streams", category = { LogicalOperatorCategory.PROCESSING })
public class HashFragmentAO extends AbstractStaticFragmentAO {

    private static final long serialVersionUID = -6789007084291408905L;

    /**
     * The URIs of the attributes forming the hash key, if the key is not the
     * whole tuple.
     */
    private List<SDFAttribute> fragmentAttributes;

    private boolean optimizeDistribution;

    private List<String> stringAttributes;

    /**
     * Constructs a new {@link HashFragmentAO}.
     * 
     * @see UnaryLogicalOp#UnaryLogicalOp()
     */
    public HashFragmentAO() {
        super();
    }

    /**
     * Constructs a new {@link HashFragmentAO} as a copy of an existing one.
     * 
     * @param fragmentAO
     *            The {@link HashFragmentAO} to be copied.
     * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
     */
    public HashFragmentAO(HashFragmentAO fragmentAO) {
        super(fragmentAO);
        if (fragmentAO.fragmentAttributes != null) {
            this.fragmentAttributes = Lists.newArrayList(fragmentAO.fragmentAttributes);
        }
        this.optimizeDistribution = fragmentAO.optimizeDistribution;
    }

    @Override
    public AbstractLogicalOperator clone() {
        return new HashFragmentAO(this);
    }

    /**
     * Returns the URIs of the attributes forming the hash key, if the key is
     * not the whole tuple.
     */
    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        return fragmentAttributes;
    }

    /**
     * Sets the URIs of the attributes forming the hash key, if the key is not
     * the whole tuple.
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = true, isList = true)
    public void setAttributes(List<SDFAttribute> uris) {

        this.fragmentAttributes = uris;

        List<String> attributes = Lists.newArrayList();
        for (SDFAttribute uri : uris) {
            attributes.add("'" + uri.getAttributeName() + "'");
        }
        this.addParameterInfo("ATTRIBUTES", attributes);

    }

    public void setStringAttributes(List<String> list) {
        this.stringAttributes = list;
    }

    @Override
    public void initialize() {
        if (stringAttributes != null) {
            this.fragmentAttributes = Lists.newArrayList();
            SDFSchema inputSchema = getInputSchema(0);
            for (String attributeNameToFind : stringAttributes) {
                fragmentAttributes.add(inputSchema.findAttribute(attributeNameToFind));
            }
        }
    }

    public boolean isOptimizeDistribution() {
        return optimizeDistribution;
    }

    @Parameter(type = BooleanParameter.class, name = "optimizeDistribution", optional = true)
    public void setOptimizeDistribution(boolean optimizeDistribution) {
        this.optimizeDistribution = optimizeDistribution;
    }

}