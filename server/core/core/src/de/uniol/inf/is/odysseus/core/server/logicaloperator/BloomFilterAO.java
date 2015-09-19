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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(name = "BLOOMFILTER", minInputPorts = 1, maxInputPorts = 1, doc = "Filter incoming streams using a Bloom filter", category = { LogicalOperatorCategory.PROCESSING })
public class BloomFilterAO extends UnaryLogicalOp {
    /**
     *
     */
    private static final long serialVersionUID = 5347520687937687414L;
    
    private List<SDFAttribute> attributes;
    private int expectedInsertions;
    private double falsePositiveProbability;

    /**
     * Constructs a new {@link BloomFilterAO}.
     *
     * @see UnaryLogicalOp#UnaryLogicalOp()
     */
    public BloomFilterAO() {
        super();
    }

    /**
     * Constructs a new {@link BloomFilterAO} as a copy of an existing
     * one.
     *
     * @param fragmentAO
     *            The {@link BloomFilterAO} to be copied.
     * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
     */
    public BloomFilterAO(final BloomFilterAO operator) {
        super(operator);
        this.attributes = new ArrayList<>(operator.attributes);
        this.expectedInsertions = operator.expectedInsertions;
        this.falsePositiveProbability = operator.falsePositiveProbability;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new BloomFilterAO(this);
    }

    /**
     * Returns the desired false positive probability.
     */
    @GetParameter(name = "FPP")
    public double getFalsePositiveProbability() {
        if (this.falsePositiveProbability == 0) {
            return 0.03;
        }
        return this.falsePositiveProbability;
    }

    /**
     * Sets the desired false positive probability.
     */
    @Parameter(type = DoubleParameter.class, name = "FPP", optional = true)
    public void setFalsePositiveProbability(final double falsePositiveProbability) {
        this.falsePositiveProbability = falsePositiveProbability;
        this.addParameterInfo("FPP", falsePositiveProbability);
    }

    /**
     * Returns the number of expected different attribute value combinations.
     */
    @GetParameter(name = "INSERTIONS")
    public int getExpectedInsertions() {
        if (this.expectedInsertions == 0) {
            return 500;
        }
        return this.expectedInsertions;
    }

    /**
     * Sets the number of expected different attribute value combinations.
     */
    @Parameter(type = IntegerParameter.class, name = "INSERTIONS", optional = true)
    public void setExpectedInsertions(final int expectedInsertions) {
        this.expectedInsertions = expectedInsertions;
        this.addParameterInfo("INSERTIONS", expectedInsertions);
    }

    /**
     * Returns the URIs of the attributes forming the hash key, if the key is
     * not the whole tuple.
     */
    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        return this.attributes;
    }

    /**
     * Sets the URIs of the attributes forming the hash key, if the key is not
     * the whole tuple.
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = true, isList = true)
    public void setAttributes(final List<SDFAttribute> attributes) {
        this.attributes = attributes;
        final List<String> attributeNames = Lists.newArrayList();
        for (final SDFAttribute uri : attributes) {
            attributeNames.add("'" + uri.getAttributeName() + "'");
        }
        this.addParameterInfo("ATTRIBUTES", attributeNames);
    }
}
