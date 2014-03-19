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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * Implementation of a probabilistic Select operator.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticContinuousSelectPO<T extends IMetaAttribute> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticContinuousSelectPO.class);
    /** Attribute positions list required for variable bindings. */
    private VarHelper[][] variables; // Expression.Index
    /** The expressions. */
    private SDFProbabilisticExpression[] expressions;
    /** The predicate. */
    private final IPredicate<?> predicate;
    /** The heartbeat generation strategy. */
    private IHeartbeatGenerationStrategy<ProbabilisticTuple<T>> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<ProbabilisticTuple<T>>();
    /** The input schema. */
    private final SDFSchema inputSchema;

    /**
     * Default constructor.
     * 
     * @param inputSchema
     *            The input schema
     * @param iPredicate
     *            The predicate
     */
    public ProbabilisticContinuousSelectPO(final SDFSchema inputSchema, final IPredicate<?> iPredicate) {
        this.inputSchema = inputSchema;
        this.predicate = iPredicate.clone();
        this.init(this.inputSchema, this.predicate);
    }

    /**
     * Clone constructor.
     * 
     * @param po
     *            The copy
     */
    public ProbabilisticContinuousSelectPO(final ProbabilisticContinuousSelectPO<T> po) {
        this.inputSchema = po.inputSchema.clone();
        this.predicate = po.predicate.clone();
        this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy.clone();
        this.init(this.inputSchema, this.predicate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
     * getOutputMode()
     */
    @Override
    public final OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    /**
     * Initialize the required structures for the Select PO.
     * 
     * @param schema
     *            The schema
     * @param selectPredicate
     *            The predicate
     */
    private void init(final SDFSchema schema, final IPredicate<?> selectPredicate) {
        final List<SDFExpression> expressionsList = PredicateUtils.getExpressions(selectPredicate);
        this.expressions = new SDFProbabilisticExpression[expressionsList.size()];
        for (int i = 0; i < expressionsList.size(); i++) {
            this.expressions[i] = new SDFProbabilisticExpression(expressionsList.get(i));
        }
        this.variables = new VarHelper[this.expressions.length][];
        final Set<SDFAttribute> neededAttributesSet = new HashSet<>();

        for (final SDFExpression expression : expressionsList) {
            final List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            neededAttributesSet.addAll(neededAttributes);
        }
        int i = 0;
        for (final SDFExpression expression : expressionsList) {
            final List<SDFAttribute> neededAttributes = expression.getAllAttributes();

            final VarHelper[] newArray = new VarHelper[neededAttributes.size()];

            this.variables[i++] = newArray;
            int j = 0;
            for (final SDFAttribute curAttribute : neededAttributes) {
                newArray[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
            }
            // if
            // (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE))
            // {
            // distributions++;
            // }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
     * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
     */
    @Override
    protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
        final ProbabilisticTuple<T> outputVal = object.clone();
        double jointProbability = ((IProbabilistic) outputVal.getMetadata()).getExistence();
        synchronized (this.expressions) {
            double scale = 1.0;
            for (int i = 0; i < this.expressions.length; ++i) {
                int d = -1;
                SDFProbabilisticExpression expression = this.expressions[i];
                final Object[] values = new Object[this.variables[i].length];
                for (int j = 0; j < this.variables[i].length; ++j) {
                    Object attribute = outputVal.getAttribute(this.variables[i][j].getPos());
                    if (attribute.getClass() == ProbabilisticDouble.class) {
                        int index = ((ProbabilisticDouble) attribute).getDistribution();
                        attribute = outputVal.getDistribution(index);
                        scale = ((MultivariateMixtureDistribution) attribute).getScale();
                        // FIXME What happens if we have more than one
                        // distribution inside an expression or even other
                        // functions? (CKu 17.12.2013)
                        if (d >= 0) {
                            throw new IllegalArgumentException("More than one distribution not supported inside predicate");
                        }
                        d = index;
                    }
                    values[j] = attribute;
                }

                expression.bindMetaAttribute(outputVal.getMetadata());
                expression.bindDistributions(outputVal.getDistributions());
                expression.bindAdditionalContent(outputVal.getAdditionalContent());
                expression.bindVariables(values);

                final Object expr = expression.getValue();
                if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE)) {
                    final MultivariateMixtureDistribution distribution = (MultivariateMixtureDistribution) expr;
                    jointProbability *= scale / distribution.getScale();
                    // distribution.getAttributes()[0] = i;
                    outputVal.setDistribution(d, distribution);
                    // outputVal.setAttribute(i, new
                    // ProbabilisticContinuousDouble(d));
                    ((IProbabilistic) outputVal.getMetadata()).setExistence(jointProbability);
                    d++;
                }
                else {
                    if (!(Boolean) expr) {
                        jointProbability = 0.0;
                        ((IProbabilistic) outputVal.getMetadata()).setExistence(jointProbability);
                    }
                }
            }
        }

        // Transfer the tuple iff the joint probability is positive (maybe set
        // quality filter later to reduce the number of tuples)
        if (jointProbability > 0.0) {
            // KTHXBYE
            this.transfer(outputVal);
        }
        else if (ProbabilisticContinuousSelectPO.LOG.isTraceEnabled()) {
            ProbabilisticContinuousSelectPO.LOG.trace("Drop tuple: " + outputVal.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
     * process_open()
     */
    @Override
    public final void process_open() {
        this.predicate.init();
    }

    /**
     * Gets the value of the predicate property.
     * 
     * @return The predicate
     */
    public final IPredicate<?> getPredicate() {
        return this.predicate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone
     * ()
     */
    @Override
    public final ProbabilisticContinuousSelectPO<T> clone() {
        return new ProbabilisticContinuousSelectPO<T>(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#toString
     * ()
     */
    @Override
    public final String toString() {
        return super.toString() + " predicate: " + this.getPredicate().toString();
    }

    /**
     * Gets the heartbeat generation strategy.
     * 
     * @return The heartbeat generation strategy
     */
    public final IHeartbeatGenerationStrategy<ProbabilisticTuple<T>> getHeartbeatGenerationStrategy() {
        return this.heartbeatGenerationStrategy;
    }

    /**
     * Sets the heartbeat generation strategy.
     * 
     * @param heartbeatGenerationStrategy
     *            The heartbeat generation strategy
     */
    public final void setHeartbeatGenerationStrategy(final IHeartbeatGenerationStrategy<ProbabilisticTuple<T>> heartbeatGenerationStrategy) {
        this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#
     * process_isSemanticallyEqual
     * (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
     */
    @Override
    public final boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
        if (!(ipo instanceof ProbabilisticContinuousSelectPO<?>)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final ProbabilisticContinuousSelectPO<T> spo = (ProbabilisticContinuousSelectPO<T>) ipo;
        // Different sources
        if (!this.hasSameSources(spo)) {
            return false;
        }
        // Predicates match
        if (this.predicate.equals(spo.getPredicate()) || (this.predicate.isContainedIn(spo.getPredicate()) && spo.getPredicate().isContainedIn(this.predicate))) {
            return true;
        }

        return false;
    }

}
