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
package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(name = "GENERICFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc = "Can be used to fragment incoming streams", category = { LogicalOperatorCategory.PROCESSING })
public class GenericFragmentAO extends AbstractStaticFragmentAO {

    /**
     * 
     */
    private static final long serialVersionUID = 7600978431841377303L;
    /** The partition expression. */
    private Optional<NamedExpression> expression;

    /**
     * Constructs a new {@link GenericFragmentAO}.
     * 
     * @see UnaryLogicalOp#UnaryLogicalOp()
     */
    public GenericFragmentAO() {
        super();
    }

    /**
     * Constructs a new {@link GenericFragmentAO} as a copy of an existing one.
     * 
     * @param fragmentAO
     *            The {@link GenericFragmentAO} to be copied.
     * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
     */
    public GenericFragmentAO(GenericFragmentAO fragmentAO) {
        super(fragmentAO);
        if (fragmentAO.expression.isPresent()) {
            this.expression = Optional.of(fragmentAO.expression.get());
        }
        else {
            this.expression = Optional.absent();
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new GenericFragmentAO(this);
    }

    /**
     * Returns the partition expression.
     */
    @GetParameter(name = "PARTITION")
    public NamedExpression getPartition() {
        return this.expression.get();
    }

    /**
     * Sets the partition expression.
     */
    @Parameter(type = NamedExpressionParameter.class, name = "PARTITION", optional = false)
    public void setPartition(NamedExpression expression) {
        this.expression = Optional.fromNullable(expression);
        this.addParameterInfo("PARTITION", this.expression);
    }

    @Override
    public boolean isValid() {
        if ((this.expression.isPresent() && this.expression.get().expression.getType().isNumeric())) {
            return true;
        }
        addError("Invalid PARTITION Parameter.");
        return false;

    }
}
