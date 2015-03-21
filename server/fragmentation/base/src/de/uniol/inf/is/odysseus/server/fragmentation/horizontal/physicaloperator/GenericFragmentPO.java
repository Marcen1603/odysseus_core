/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.GenericFragmentAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class GenericFragmentPO extends AbstractFragmentPO<Tuple<IMetaAttribute>> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(GenericFragmentPO.class);
    /** The partition expression. */
    private SDFExpression expression;
    /** Variable helper for expression evaluation. */
    private VarHelper[] variables;

    /**
     * Constructs a new {@link GenericFragmentPO}.
     * 
     * @param fragmentAO
     *            the {@link GenericFragmentAO} transformed to this
     *            {@link GenericFragmentPO}.
     */
    public GenericFragmentPO(GenericFragmentAO fragmentAO) {
        super(fragmentAO);
        init(fragmentAO.getInputSchema(), fragmentAO.getPartition().expression);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public AbstractPipe<Tuple<IMetaAttribute>, Tuple<IMetaAttribute>> clone() {
        throw new IllegalArgumentException("Currently not implemented");
    }

    /**
     * Initialize operator configuration.
     * 
     * @param schema
     *            The input schema.
     * @param expression
     *            The partition expression.
     */
    @SuppressWarnings("hiding")
    private void init(SDFSchema schema, SDFExpression expression) {
        this.expression = expression.clone();
        List<SDFAttribute> neededAttributes = this.expression.getAllAttributes();
        this.variables = new VarHelper[neededAttributes.size()];
        int j = 0;
        for (SDFAttribute curAttribute : neededAttributes) {
            this.variables[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected int route(IStreamObject<IMetaAttribute> object) {
        Object[] values = new Object[this.variables.length];
        IMetaAttribute[] meta = new IMetaAttribute[this.variables.length];
        for (int j = 0; j < this.variables.length; ++j) {
            Tuple<?> obj = (Tuple<?>) object;
            if (obj != null) {
                values[j] = obj.getAttribute(this.variables[j].pos);
                meta[j] = obj.getMetadata();
            }
        }
        try {
            this.expression.bindMetaAttribute(object.getMetadata());
            this.expression.bindAdditionalContent(object.getAdditionalContent());
            this.expression.bindVariables(meta, values);
            Object expr = this.expression.getValue();
            return ((Number) expr).intValue();
        }
        catch (Throwable t) {
            LOG.error(t.getMessage(), t);
        }
        return 0;
    }

    /**
     * 
     * Variable helper class
     */
    private class VarHelper {
        public int pos;
        public int objectPosToUse;

        /**
         * 
         * Class constructor.
         *
         * @param pos
         * @param objectPosToUse
         */
        public VarHelper(int pos, int objectPosToUse) {
            super();
            this.pos = pos;
            this.objectPosToUse = objectPosToUse;
        }

        /**
         * 
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return this.pos + " " + this.objectPosToUse;
        }
    }
}
