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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * Implementation of a probabilistic Map operator.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class ProbabilisticMapPO<T extends IProbabilistic> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticMapPO.class);
    /** Attribute positions list required for variable bindings. */
    private VarHelper[][] variables;
    /** The expressions. */
    private SDFProbabilisticExpression[] expressions;
    /** The input schema used for semantic equal operations during runtime. */
    private final SDFSchema inputSchema;
    /** Flag indicating if this Map allows Null values. */
    private final boolean allowNull;
    /** The number of output distributions. */
    private int distributions;
    /** Positions of needed attributes. */
    private int[] neededAttributePos;

    /**
     * Default constructor used for probabilistic expression.
     * 
     * @param inputSchema
     *            The input schema
     * @param expressions
     *            The probabilistic expression.
     * @param allowNullInOutput
     *            Flag indicating if this Map allows Null values
     */
    public ProbabilisticMapPO(final SDFSchema inputSchema, final SDFProbabilisticExpression[] expressions, final boolean allowNullInOutput) {
        this.inputSchema = inputSchema;
        this.allowNull = allowNullInOutput;
        this.init(inputSchema, expressions);
    }

    /**
     * Default constructor used for expression.
     * 
     * @param inputSchema
     *            The input schema
     * @param expressions
     *            The expression.
     * @param allowNullInOutput
     *            Flag indicating if this Map allows Null values
     */
    public ProbabilisticMapPO(final SDFSchema inputSchema, final SDFExpression[] expressions, final boolean allowNullInOutput) {
        this.inputSchema = inputSchema;
        this.allowNull = allowNullInOutput;
        this.init(inputSchema, expressions);
    }

    /**
     * Default constructor.
     * 
     * @param inputSchema
     *            The input schema
     * @param allowNullInOutput
     *            Flag indicating if this Map allows Null values
     */
    public ProbabilisticMapPO(final SDFSchema inputSchema, final boolean allowNullInOutput) {
        this.inputSchema = inputSchema;
        this.allowNull = allowNullInOutput;
    }

    /**
     * Clone constructor.
     * 
     * @param po
     *            The copy
     */
    public ProbabilisticMapPO(final ProbabilisticMapPO<T> po) {
        this.inputSchema = po.inputSchema.clone();
        this.allowNull = po.allowNull;
        this.init(po.inputSchema, po.expressions);
    }

    /**
     * Initialize the operator with the given expressions.
     * 
     * @param schema
     *            The schema
     * @param expressionsList
     *            The expressions
     */
    private void init(final SDFSchema schema, final SDFExpression[] expressionsList) {
        final SDFProbabilisticExpression[] probabilisticExpressions = new SDFProbabilisticExpression[expressionsList.length];
        for (int i = 0; i < expressionsList.length; ++i) {
            probabilisticExpressions[i] = new SDFProbabilisticExpression(expressionsList[i].clone());
        }
        this.init(schema, probabilisticExpressions);
    }

    /**
     * Initialize the operator with the given probabilistic expressions.
     * 
     * @param schema
     *            The schema
     * @param expressionsList
     *            The expressions
     */
    private void init(final SDFSchema schema, final SDFProbabilisticExpression[] expressionsList) {
        this.expressions = new SDFProbabilisticExpression[expressionsList.length];
        final List<SDFAttribute> newSchemaAttributes = new ArrayList<>();
        for (int i = 0; i < expressionsList.length; ++i) {
            this.expressions[i] = expressionsList[i].clone();
            for (SDFAttribute attr : this.expressions[i].getAllAttributes()) {
                if (!newSchemaAttributes.contains(attr)) {
                    newSchemaAttributes.add(attr);
                }
            }
        }

        this.neededAttributePos = SchemaUtils.getAttributePos(this.inputSchema, newSchemaAttributes);
        final SDFSchema restrictedSchema = new SDFSchema(schema, newSchemaAttributes);
        this.variables = new VarHelper[expressionsList.length][];
        int i = 0;
        for (SDFExpression expression : expressions) {
            List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            VarHelper[] newArray = new VarHelper[neededAttributes.size()];
            this.variables[i++] = newArray;
            int j = 0;
            for (SDFAttribute curAttribute : neededAttributes) {
                newArray[j++] = initAttribute(restrictedSchema, curAttribute);
            }
            if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE)) {
                this.distributions++;
            }
        }
    }

    /**
     * 
     * @param schema
     *            The schema
     * @param curAttribute
     *            The attribute
     * @return An instance of {@link VarHelper} for that attribute
     */
    public VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
        return new VarHelper(schema.indexOf(curAttribute), 0);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
        boolean nullValueOccured = false;

        final ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(this.expressions.length, this.distributions, true);
        outputVal.setMetadata((T) object.getMetadata().clone());

        final ProbabilisticTuple<T> restrictedObject = object.restrict(this.neededAttributePos, false);
        LinkedList<ProbabilisticTuple<T>> preProcessResult = preProcess(restrictedObject);

        synchronized (this.expressions) {
            int distributionIndex = 0;
            for (int i = 0; i < this.expressions.length; ++i) {
                double existence = 1.0;
                Object[] values = new Object[this.variables[i].length];
                IMetaAttribute[] meta = new IMetaAttribute[this.variables[i].length];
                for (int j = 0; j < this.variables[i].length; ++j) {
                    ProbabilisticTuple<T> obj = determineObjectForExpression(restrictedObject, preProcessResult, i, j);
                    if (obj != null) {
                        values[j] = obj.getAttribute(this.variables[i][j].getPos());
                        if (values[j].getClass() == ProbabilisticDouble.class) {
                            MultivariateMixtureDistribution distribution = restrictedObject.getDistribution(((ProbabilisticDouble) values[j]).getDistribution());
                            RealMatrix restrictMatrix = MatrixUtils.createRealMatrix(1, distribution.getDimension());
                            if (distribution.getDimension() > 1) {
                                distribution = distribution.clone();
                            }
                            int d = distribution.getDimension(this.variables[i][j].getPos());
                            restrictMatrix.setEntry(0, d, 1.0);
                            distribution.restrict(restrictMatrix);
                            values[j] = distribution;
                            existence *= 1.0 / distribution.getScale();
                        }
                        meta[j] = obj.getMetadata();
                    }
                }

                try {
                    this.expressions[i].bindMetaAttribute(restrictedObject.getMetadata());
                    this.expressions[i].bindAdditionalContent(restrictedObject.getAdditionalContent());
                    this.expressions[i].bindVariables(meta, values);
                    Object expr = this.expressions[i].getValue();
                    if (expr == null) {
                        nullValueOccured = true;
                    }
                    else {
                        if (this.expressions[i].getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE)) {
                            final MultivariateMixtureDistribution distribution = (MultivariateMixtureDistribution) expr;

                            if (distribution.getDimension() == 1) {
                                distribution.getAttributes()[0] = i;
                            }
                            else {
                                LOG.error("Multivariate distribution not supported as a result");
                            }
                            outputVal.setDistribution(distributionIndex, distribution);
                            outputVal.setAttribute(i, new ProbabilisticDouble(distributionIndex));
                            distributionIndex++;
                            // FIXED Setting the output existence?? Yes, because
                            //  the metadata holds the probability and all
                            // distributions are normalized using the scale
                            // factor. Thus, all changes on the distribution has
                            // to be stored back in the meta data
                            outputVal.getMetadata().setExistence(outputVal.getMetadata().getExistence() * (1.0 / distribution.getScale()) / existence);
                        }
                        else {
                            outputVal.setAttribute(i, expr);
                        }
                    }
                }
                catch (Exception e) {
                    nullValueOccured = true;
                    if (!(e instanceof NullPointerException)) {
                        LOG.error("Cannot calc result for " + object + " with expression " + expressions[i], e);
                    }
                }
                if (this.expressions[i].getType().requiresDeepClone()) {
                    outputVal.setRequiresDeepClone(true);
                }
            }
        }
        if (!nullValueOccured || (nullValueOccured && allowNull)) {
            // KTHXBYE
            transfer(outputVal);
        }
    }

    public ProbabilisticTuple<T> determineObjectForExpression(ProbabilisticTuple<T> object, LinkedList<ProbabilisticTuple<T>> preProcessResult, int i, int j) {
        return object;
    }

    public LinkedList<ProbabilisticTuple<T>> preProcess(ProbabilisticTuple<T> object) {
        return null;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final ProbabilisticMapPO<T> clone() {
        return new ProbabilisticMapPO<T>(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "rawtypes" })
    public final boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
        if (!(ipo instanceof ProbabilisticMapPO)) {
            return false;
        }
        final ProbabilisticMapPO po = (ProbabilisticMapPO) ipo;

        if (!this.getOutputSchema().equals(po.getOutputSchema())) {
            return false;
        }

        if (this.inputSchema.compareTo(po.inputSchema) == 0) {
            if (this.expressions.length == po.expressions.length) {
                for (int i = 0; i < this.expressions.length; i++) {
                    if (!this.expressions[i].equals(po.expressions[i])) {
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

}
