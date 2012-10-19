/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalUnNestPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {
    @SuppressWarnings("unused")
	private static Logger   LOG = LoggerFactory.getLogger(RelationalUnNestPO.class);

    private final int       nestedAttributePos;
    private final SDFSchema inputSchema;

    private final boolean   isMultiValue;

    /**
     * @param schema
     * @param attribute
     */
    public RelationalUnNestPO(final SDFSchema inputSchema, final int nestedAttributePos, final boolean isMultiValue) {
        this.inputSchema = inputSchema;
        this.nestedAttributePos = nestedAttributePos;
        this.isMultiValue = isMultiValue;
    }

    /**
     * @param po
     */
    public RelationalUnNestPO(final RelationalUnNestPO<T> po) {
        this.inputSchema = po.inputSchema;
        this.nestedAttributePos = po.nestedAttributePos;
        this.isMultiValue = po.isMultiValue;
    }

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone
     * ()
     */
    @Override
    public RelationalUnNestPO<T> clone() {
        return new RelationalUnNestPO<T>(this);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
     * getOutputMode()
     */
    @Override
    public OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

	@Override
	protected boolean canHandleOutOfOrder() {
		return true;
	}
    
    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
     * process_next(java.lang.Object,
     * int)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Tuple<T> tuple, final int port) {
        if (this.isMultiValue) {
            final int depth = ((List<?>) tuple.getAttribute(this.nestedAttributePos)).size();
            for (int d = 0; d < depth; d++) {
                final Tuple<T> outputTuple = new Tuple<T>(this.getOutputSchema().size(), false);
                outputTuple.setMetadata((T) tuple.getMetadata().clone());
                for (int i = 0; i < this.inputSchema.size(); i++) {
                    if (i == this.nestedAttributePos) {
                        final List<?> nestedTuple = (List<?>) tuple.getAttribute(this.nestedAttributePos);
                        outputTuple.setAttribute(i, nestedTuple.get(d));
                    }
                    else {
                        outputTuple.setAttribute(i, tuple.getAttribute(i));
                    }
                }
                this.transfer(outputTuple);
            }

        }
        else {
            final Tuple<T> outputTuple = new Tuple<T>(this.getOutputSchema().size(), false);
            outputTuple.setMetadata((T) tuple.getMetadata().clone());
            int pos = 0;
            for (int i = 0; i < this.inputSchema.size(); i++) {
                if (i == this.nestedAttributePos) {
                    final Tuple<?> nestedTuple = (Tuple<?>) tuple.getAttribute(i);
                    for (int j = 0; j < nestedTuple.size(); j++) {
                        outputTuple.setAttribute(pos, nestedTuple.getAttribute(j));
                        pos++;
                    }
                }
                else {
                    outputTuple.setAttribute(pos, tuple.getAttribute(i));
                    pos++;
                }
            }
            this.transfer(outputTuple);
        }
    }

}
