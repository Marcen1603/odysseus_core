package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * 
 * @author Marco Grawunder
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DISTINCT", doc = "This operator removes duplicates.", category = { LogicalOperatorCategory.BASE })
public class DistinctAO extends UnaryLogicalOp {

    private static final long serialVersionUID = -1992998023364461468L;

    // private List<SDFAttribute> attributes;
    /**
     * Class constructor.
     *
     */
    public DistinctAO() {
        super();
    }

    public DistinctAO(DistinctAO distinctAO) {
        super(distinctAO);
        // this.attributes = distinctAO.attributes;
    }

    // @Parameter(type = ResolvedSDFAttributeParameter.class, name =
    // "ATTRIBUTES", optional = true, isList = true, doc =
    // "A list of attributes that should be used.")
    // public void setAttr(List<SDFAttribute> outputSchema) {
    // this.attributes = outputSchema;
    // }
    //
    // @Override
    // protected SDFSchema getOutputSchemaIntern(int pos) {
    // if (pos > 0) {
    // throw new
    // IllegalArgumentException("No output port other than 0 defined!");
    // }
    // return new SDFSchema(getInputAO().getOutputSchema(), attributes);
    // }
    //
    // public List<SDFAttribute> getAttributes() {
    // return attributes;
    // }

    @Override
    public AbstractLogicalOperator clone() {
        return new DistinctAO(this);
    }

}
