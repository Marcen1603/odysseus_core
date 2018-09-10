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

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.GenericFragmentAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class GenericFragmentPO extends AbstractStaticFragmentPO<Tuple<IMetaAttribute>> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(GenericFragmentPO.class);
    /** The partition expression. */
    private RelationalExpression<IMetaAttribute> expr;

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
     * Initialize operator configuration.
     * 
     * @param schema
     *            The input schema.
     * @param expression
     *            The partition expression.
     */
    private void init(SDFSchema schema, SDFExpression expression) {    	
        this.expr = new RelationalExpression<IMetaAttribute>(expression.clone());
        expr.initVars(schema);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected int route(IStreamObject<IMetaAttribute> object) {
        
    	try {
            LinkedList<Tuple<IMetaAttribute>> history = null;
			Object expr = this.expr.evaluate((Tuple<IMetaAttribute>) object, getSessions(), history);
            return ((Number) expr).intValue();
        }
        catch (Throwable t) {
            LOG.error(t.getMessage(), t);
        }
        return 0;
    }

}
