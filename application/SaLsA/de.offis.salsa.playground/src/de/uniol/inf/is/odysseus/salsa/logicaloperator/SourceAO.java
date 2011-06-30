package de.uniol.inf.is.odysseus.salsa.logicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "SOURCE")
public class SourceAO extends AbstractLogicalOperator implements OutputSchemaSettable {

    /**
     * 
     */
    private static final long serialVersionUID = 4463347403946884857L;
    private static Logger LOG = LoggerFactory.getLogger(SourceAO.class);
    private final Map<Integer, SDFAttributeList> outputSchema = new HashMap<Integer, SDFAttributeList>();
    private String adapterName;

    /**
     * 
     */
    public SourceAO() {
        super();
    }

    /**
     * @param ao
     */
    public SourceAO(final SourceAO ao) {
        super(ao);
        for (final Entry<Integer, SDFAttributeList> entry : ao.outputSchema.entrySet()) {
            this.outputSchema.put(entry.getKey(), entry.getValue().clone());
        }
        this.adapterName = ao.adapterName;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
     */
    @Override
    public SourceAO clone() {
        return new SourceAO(this);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema()
     */
    @Override
    public SDFAttributeList getOutputSchema() {
        return this.getOutputSchema(0);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#getOutputSchema(int)
     */
    @Override
    public SDFAttributeList getOutputSchema(final int port) {
        return this.outputSchema.get(port);
    }

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable#setOutputSchema(de.uniol.inf
     * .is.odysseus.sourcedescription.sdf.schema.SDFAttributeList)
     */
    @Override
    public void setOutputSchema(final SDFAttributeList outputSchema) {
        this.setOutputSchema(outputSchema, 0);
    }

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable#setOutputSchema(de.uniol.inf
     * .is.odysseus.sourcedescription.sdf.schema.SDFAttributeList, int)
     */
    @Override
    public void setOutputSchema(final SDFAttributeList outputSchema, final int port) {
        SourceAO.LOG.debug("Set output schema on port {} to {}", port, outputSchema);
        this.outputSchema.put(port, outputSchema);
    }

    /**
     * Defines the source output schema through the builder
     * 
     * @param schemaAttributes
     *            The source schema in attribute:type notation
     * @FIXME Use {@link CreateSDFAttributeParameter} when newInstance bug is fixed
     */
    @Parameter(name = "SCHEMA", type = StringParameter.class, isList = true)
    public void setOutputSchemaWithList(final List<String> schemaAttributes) {
        final SDFAttributeList schema = new SDFAttributeList();
        for (final String item : schemaAttributes) {
            final String[] schemaInformation = item.split(":");
            final SDFAttribute attribute = new SDFAttribute(null, schemaInformation[0]);
            attribute.setDatatype(GlobalState.getActiveDatadictionary().getDatatype(schemaInformation[1]));
            schema.add(attribute);
        }
        this.setOutputSchema(schema, 0);
    }

    @Parameter(name = "NAME", type = StringParameter.class)
    public void setAdapterName(final String adapterName) {
        this.adapterName = adapterName;
    }

    public String getAdapterName() {
        return this.adapterName;
    }
}
