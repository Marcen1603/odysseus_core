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

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.recommendation.transform.FindAttributeHelper;
import de.uniol.inf.is.odysseus.recommendation.transform.TTestPredictionAORule;

/**
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(name = "TEST_PREDICTION", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.MINING }, doc = "This operator computes a set of recommendations.")
public class TestPredictionAO extends UnaryLogicalOp {

	/**
	 * @author Cornelius Ludmann
	 *
	 */
	public enum TestPredictionTupleSchema {
		USER, ITEM, RATING, PREDICTED_RATING
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -7341143823860056239L;

	public static final String DEFAULT_USER_ATTRIBUTE_NAME = TrainRecSysModelAO.DEFAULT_USER_ATTRIBUTE_NAME;
	public static final String DEFAULT_ITEM_ATTRIBUTE_NAME = TrainRecSysModelAO.DEFAULT_ITEM_ATTRIBUTE_NAME;
	public static final String DEFAULT_RATING_ATTRIBUTE_NAME = TrainRecSysModelAO.DEFAULT_RATING_ATTRIBUTE_NAME;
	public static final String DEFAULT_PREDICTED_RATING_ATTRIBUTE_NAME = PredictRatingAO.PREDICTED_RATING_ATTRIBUTE_NAME;
	public static final String DEFAULT_METRIC = "RMSE";

	public static final String MODEL_ERROR_ATTRIBUTE_NAME = "model_error";

	/**
	 * These attributes store the user, item and rating.
	 */
	private SDFAttribute userAttribute, itemAttribute, ratingAttribute, predictedRatingAttribute;

	/**
	 * The evaluation metric.
	 */
	private String metric = DEFAULT_METRIC;

	/**
	 * Aggregation Window Size
	 */
	private TimeValueItem aggregationWindowSize = null;

	/**
	 * Default constructor.
	 */
	public TestPredictionAO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public TestPredictionAO(final TestPredictionAO op) {
		super(op);
		this.userAttribute = op.userAttribute;
		this.itemAttribute = op.itemAttribute;
		this.ratingAttribute = op.ratingAttribute;
		this.predictedRatingAttribute = op.predictedRatingAttribute;
		this.metric = op.metric;
		this.aggregationWindowSize = op.aggregationWindowSize;
	}

	/**
	 * @return the userAttribute
	 */
	public SDFAttribute getUserAttribute() {
		if (this.userAttribute == null) {
			this.userAttribute = FindAttributeHelper.findAttributeByName(this,
					TestPredictionAO.DEFAULT_USER_ATTRIBUTE_NAME);
		}
		return this.userAttribute;
	}

	/**
	 * @param userAttribute
	 *            the userAttribute to set
	 */
	@Parameter(name = "user", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the user IDs. Default: "
			+ DEFAULT_USER_ATTRIBUTE_NAME, optional = true)
	public void setUserAttribute(final SDFAttribute userAttribute) {
		this.userAttribute = userAttribute;
	}

	/**
	 * @return the itemAttribute
	 */
	public SDFAttribute getItemAttribute() {
		if (this.itemAttribute == null) {
			this.itemAttribute = FindAttributeHelper.findAttributeByName(this,
					TestPredictionAO.DEFAULT_ITEM_ATTRIBUTE_NAME);
		}
		return this.itemAttribute;
	}

	/**
	 * @param itemAttribute
	 *            the itemAttribute to set
	 */
	@Parameter(name = "item", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the item IDs. Default: "
			+ DEFAULT_ITEM_ATTRIBUTE_NAME, optional = true)
	public void setItemAttribute(final SDFAttribute itemAttribute) {
		this.itemAttribute = itemAttribute;
	}

	/**
	 * @return the ratingAttribute
	 */
	public SDFAttribute getRatingAttribute() {
		if (this.ratingAttribute == null) {
			this.ratingAttribute = FindAttributeHelper.findAttributeByName(this,
					TestPredictionAO.DEFAULT_RATING_ATTRIBUTE_NAME);
		}
		return this.ratingAttribute;
	}

	/**
	 * @param ratingAttribute
	 *            the ratingAttribute to set
	 */
	@Parameter(name = "rating", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the ratings. Default: "
			+ DEFAULT_RATING_ATTRIBUTE_NAME, optional = true)
	public void setRatingAttribute(final SDFAttribute ratingAttribute) {
		this.ratingAttribute = ratingAttribute;
	}

	/**
	 * @return the predictedRatingAttribute
	 */
	public SDFAttribute getPredictedRatingAttribute() {
		if (this.predictedRatingAttribute == null) {
			this.predictedRatingAttribute = FindAttributeHelper.findAttributeByName(this,
					TestPredictionAO.DEFAULT_PREDICTED_RATING_ATTRIBUTE_NAME);
		}
		return this.predictedRatingAttribute;
	}

	/**
	 * @param predictedRatingAttribute
	 *            the predictedRatingAttribute to set
	 */
	@Parameter(name = "predicted_rating", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the predicted ratings. Default: "
			+ DEFAULT_PREDICTED_RATING_ATTRIBUTE_NAME, optional = true)
	public void setPredictedRatingAttribute(final SDFAttribute predictedRatingAttribute) {
		this.predictedRatingAttribute = predictedRatingAttribute;
	}

	/**
	 * @return the metric
	 */
	public String getMetric() {
		return this.metric;
	}

	/**
	 * @param metric
	 *            the metric to set
	 */
	// TODO: Restrict possible values.
	@Parameter(name = "metric", type = StringParameter.class, doc = "The evaluation metric. Default: "
			+ DEFAULT_METRIC, optional = true)
	public void setMetric(final String metric) {
		this.metric = metric;
	}

	/**
	 * @return the aggregationWindowSize
	 */
	public TimeValueItem getAggregationWindowSize() {
		return this.aggregationWindowSize;
	}

	/**
	 * @param aggregationWindowSize
	 *            the aggregationWindowSize to set
	 */
	@Parameter(name = "aggregation_window_size", type = TimeParameter.class, doc = "Size of the aggregation window. Default: unbounded", optional = true)
	public void setAggregationWindowSize(final TimeValueItem aggregationWindowSize) {
		this.aggregationWindowSize = aggregationWindowSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.
	 * AbstractLogicalOperator #getOutputSchemaIntern(int)
	 */
	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {
		if (pos == 0) {
			final SDFSchema subSchema = getInputSchema(0);
			final List<SDFAttribute> attributes = new LinkedList<SDFAttribute>();
			attributes.add(new SDFAttribute(null, TTestPredictionAORule.ERROR_ATTRIBUTE_NAME, SDFDatatype.DOUBLE));
			final SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, subSchema);
			return outputSchema;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.
	 * AbstractLogicalOperator #clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new TestPredictionAO(this);
	}

}
