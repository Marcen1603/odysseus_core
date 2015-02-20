/**
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
package de.uniol.inf.is.odysseus.recommendation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(name = "RECOMMEND", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.MINING }, doc = "This operator computes a set of recommendations.")
public class RecommendAO extends UnaryLogicalOp {

	/**
	 *
	 */
	private static final long serialVersionUID = 2338847266214065142L;

	public final String DEFAULT_PREDICTED_RATING_ATTRIBUTE_NAME = PredictRatingAO.PREDICTED_RATING_ATTRIBUTE_NAME;

	/**
	 * These attributes store the user, item and rating.
	 */
	private SDFAttribute predictedRatingAttribute = null;

	/**
	 * max. number of recommendations
	 */
	private int topN = -1;

	/**
	 * min. rating for a recommendation
	 */
	private double minRating = Double.NaN;

	/**
	 * Default constructor.
	 */
	public RecommendAO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public RecommendAO(final RecommendAO op) {
		super(op);
		this.topN = op.topN;
		this.minRating = op.minRating;
		this.predictedRatingAttribute = op.predictedRatingAttribute;
	}

	/**
	 * @return the predictedRatingAttribute
	 */
	public SDFAttribute getPredictedRatingAttribute() {
		return this.predictedRatingAttribute;
	}

	/**
	 * @param predictedRatingAttribute
	 *            the predictedRatingAttribute to set
	 */
	@Parameter(name = "predicted_rating", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the predicted ratings.", optional = true)
	public void setPredictedRatingAttribute(
			final SDFAttribute predictedRatingAttribute) {
		this.predictedRatingAttribute = predictedRatingAttribute;
	}

	/**
	 * @return the topN
	 */
	public int getTopN() {
		return this.topN;
	}

	/**
	 * @param topN
	 *            the topN to set
	 */
	@Parameter(name = "top_N", type = IntegerParameter.class, doc = "The max. number of recommendations.", optional = true)
	public void setTopN(final int topN) {
		this.topN = topN;
	}

	/**
	 * @return the minRating
	 */
	public double getMinRating() {
		return this.minRating;
	}

	/**
	 * @param minRating
	 *            the minRating to set
	 */
	@Parameter(name = "min_rating", type = DoubleParameter.class, doc = "The min. rating.", optional = true)
	public void setMinRating(final double minRating) {
		this.minRating = minRating;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new RecommendAO(this);
	}

}