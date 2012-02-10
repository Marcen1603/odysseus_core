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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalUnNestPO<T extends IMetaAttribute> extends
        AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {
    private static Logger LOG = LoggerFactory.getLogger(RelationalUnNestPO.class);

    private int nestedAttribute;
    private final SDFSchema inputSchema;

    /**
     * @param schema
     * @param attribute
     */
    public RelationalUnNestPO(final SDFSchema inputSchema, final int nestedAttribute) {
        this.inputSchema = inputSchema;
        this.nestedAttribute = nestedAttribute;
    }

    /**
     * @param po
     */
    public RelationalUnNestPO(final RelationalUnNestPO<T> po) {
        this.inputSchema = po.inputSchema;
        this.nestedAttribute = po.nestedAttribute;
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
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#process_next(java.lang.Object,
     * int)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final RelationalTuple<T> tuple, final int port) {
        int depth = ((List<RelationalTuple<?>>) tuple.getAttribute(nestedAttribute)).size();
        for (int d = 0; d < depth; d++) {
            try {
                final RelationalTuple<T> outputTuple = new RelationalTuple<T>(this
                        .getOutputSchema().size());
                outputTuple.setMetadata((T) tuple.getMetadata().clone());
                int pos = 0;
                for (int i = 0; i < this.inputSchema.getAttributeCount(); i++) {
                    if (i == this.nestedAttribute) {
                        final List<RelationalTuple<?>> nestedTuple = (List<RelationalTuple<?>>) tuple
                                .getAttribute(i);
                        for (int j = 0; j < nestedTuple.get(d).getAttributeCount(); j++) {
                            outputTuple.setAttribute(pos, nestedTuple.get(d).getAttribute(j));
                            pos++;
                        }

                    }
                    else {
                        outputTuple.setAttribute(pos, tuple.getAttribute(i));
                        pos++;
                    }
                }
                transfer(outputTuple);
            }
            catch (final Exception e) {
                RelationalUnNestPO.LOG.error(e.getMessage(), e);
            }
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
