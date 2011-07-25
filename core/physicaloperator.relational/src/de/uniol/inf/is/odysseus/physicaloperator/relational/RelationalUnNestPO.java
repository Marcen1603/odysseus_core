/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

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
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalUnNestPO<T extends IMetaAttribute> extends
        AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {
    private static Logger LOG = LoggerFactory.getLogger(RelationalUnNestPO.class);

    private final SDFAttributeList schema;
    private final int attributePos;

    /**
     * @param schema
     * @param attribute
     */
    public RelationalUnNestPO(final SDFAttributeList schema, final SDFAttribute attribute) {
        this.schema = schema;
        this.attributePos = schema.indexOf(attribute);
    }

    /**
     * @param po
     */
    public RelationalUnNestPO(final RelationalUnNestPO<T> po) {
        this.schema = po.schema;
        this.attributePos = po.attributePos;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#clone()
     */
    @Override
    public RelationalUnNestPO<T> clone() {
        return new RelationalUnNestPO<T>(this);
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
    protected void process_next(final RelationalTuple<T> listRelatinalTuple, final int port) {
        try {
            final List<T> relatinalTuples = (List<T>) listRelatinalTuple;
            for (final T relatinalTuple : relatinalTuples) {
                this.transfer((RelationalTuple)relatinalTuple);
            }
        }
        catch (final Exception e) {
            RelationalUnNestPO.LOG.error(e.getMessage(), e);
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
