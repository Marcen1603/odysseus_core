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
package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticMapPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {
    private int[][]         variables;
    private SDFExpression[] expressions;
    private SDFSchema       inputSchema;

    public ProbabilisticMapPO(final SDFSchema inputSchema, final SDFExpression[] expressions) {
        this.inputSchema = inputSchema;
        this.init(inputSchema, expressions);
    }

    public ProbabilisticMapPO(final ProbabilisticMapPO<T> probabilisticMapPO) {
        this.init(probabilisticMapPO.inputSchema, probabilisticMapPO.expressions);
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    @Override
    protected void process_next(final Tuple<T> object, final int port) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {
        this.sendPunctuation(timestamp);
    }

    @Override
    public AbstractPipe<Tuple<T>, Tuple<T>> clone() {
        return new ProbabilisticMapPO<T>(this);
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    public boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
        if (!(ipo instanceof RelationalMapPO)) {
            return false;
        }
        final ProbabilisticMapPO pmpo = (ProbabilisticMapPO) ipo;
        if (this.hasSameSources(pmpo) && (this.inputSchema.compareTo(pmpo.inputSchema) == 0)) {
            if (this.expressions.length == pmpo.expressions.length) {
                for (int i = 0; i < this.expressions.length; i++) {
                    if (!this.expressions[i].equals(pmpo.expressions[i])) {
                        return false;
                    }
                }
            }
            else {
                return false;
            }
            return true;
        }
        return false;
    }

    private void init(final SDFSchema schema, final SDFExpression[] expressions) {
        this.expressions = new SDFExpression[expressions.length];
        for (int i = 0; i < expressions.length; ++i) {
            this.expressions[i] = expressions[i].clone();
        }
        this.variables = new int[expressions.length][];
        int i = 0;
        for (final SDFExpression expression : expressions) {
            final List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            final int[] newArray = new int[neededAttributes.size()];
            this.variables[i++] = newArray;
            int j = 0;
            for (final SDFAttribute curAttribute : neededAttributes) {
                newArray[j++] = schema.indexOf(curAttribute);
            }
        }
    }
}
