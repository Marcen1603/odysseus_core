package de.uniol.inf.is.odysseus.salsa.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class UnnestPO<T extends IMetaAttribute> extends
        AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {
    private static Logger LOG = LoggerFactory.getLogger(UnnestPO.class);

    private final SDFAttributeList schema;
    private final int attributePos;

    /**
     * @param schema
     * @param attribute
     */
    public UnnestPO(final SDFAttributeList schema, final SDFAttribute attribute) {
        this.schema = schema;
        this.attributePos = schema.indexOf(attribute);
    }

    /**
     * @param po
     */
    public UnnestPO(final UnnestPO<T> po) {
        this.schema = po.schema;
        this.attributePos = po.attributePos;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#clone()
     */
    @Override
    public UnnestPO<T> clone() {
        return new UnnestPO<T>(this);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#getOutputMode()
     */
    @Override
    public OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#getOutputSchema()
     */
    @Override
    public SDFAttributeList getOutputSchema() {
        return this.schema;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#process_next(java.lang.Object,
     * int)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final RelationalTuple<T> object, final int port) {
        try {
            final List<Object> unnestAttribute = (List<Object>) object
                    .getAttribute(this.attributePos);
            for (final Object nestedValue : unnestAttribute) {
                final RelationalTuple<T> output = new RelationalTuple<T>(this.schema.size());
                output.setMetadata((T) object.getMetadata().clone());
                for (int i = 0; i < object.getAttributeCount(); i++) {
                    if (i == this.attributePos) {
                        output.setAttribute(i, nestedValue);
                    }
                    else {
                        output.setAttribute(i, object.getAttribute(i));
                    }
                }
                this.transfer(output);
            }
        }
        catch (final Exception e) {
            UnnestPO.LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.physicaloperator.ISink#processPunctuation(de.uniol.inf.is.odysseus
     * .metadata.PointInTime, int)
     */
    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {
        this.sendPunctuation(timestamp);
    }
}
