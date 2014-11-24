/**
a * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.evaluation.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Stephan Wessels
 */
@LogicalOperator(name = "RECOMMENDATION_EVALUATE", minInputPorts = 2, maxInputPorts = 2, category = { LogicalOperatorCategory.MINING }, doc = "This operator evaluates a recommender incrementally. Needs a stream of recommenders and a stream of tuples from which the recommender is being learned.")
public class RecommendationEvaluateAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -3692054332410214993L;
	
	private SDFAttribute recommenderAttribute, userAttribute, itemAttribute, ratingAttribute;
	private List<String> metrics = new ArrayList<>();
	private Double fadingFactor = 1.0;
	
	public RecommendationEvaluateAO() {
		super();
	}

	public RecommendationEvaluateAO(final RecommendationEvaluateAO recommendationEvaluateAO) {
		super(recommendationEvaluateAO);
		this.recommenderAttribute = recommendationEvaluateAO.recommenderAttribute;
		this.userAttribute = recommendationEvaluateAO.userAttribute;
		this.itemAttribute = recommendationEvaluateAO.itemAttribute;
		this.ratingAttribute = recommendationEvaluateAO.ratingAttribute;
		this.metrics = new ArrayList<String>(recommendationEvaluateAO.metrics);
		this.fadingFactor = recommendationEvaluateAO.fadingFactor;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RecommendationEvaluateAO(this);
	}

	public SDFAttribute getRecommenderAttribute() {
		return recommenderAttribute;
	}

	@Parameter(name = "recommender", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the recommender model.", optional = false)
	public void setRecommenderAttribute(final SDFAttribute recommenderAttribute) {
		this.recommenderAttribute = recommenderAttribute;
	}

	public SDFAttribute getUserAttribute() {
		return userAttribute;
	}

	@Parameter(name = "user", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the user IDs.")
	public void setUserAttribute(final SDFAttribute userAttribute) {
		this.userAttribute = userAttribute;
	}

	public SDFAttribute getItemAttribute() {
		return itemAttribute;
	}

	@Parameter(name = "item", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the item IDs.")
	public void setItemAttribute(final SDFAttribute itemAttribute) {
		this.itemAttribute = itemAttribute;
	}

	public SDFAttribute getRatingAttribute() {
		return ratingAttribute;
	}

	@Parameter(name = "rating", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the rating IDs.")
	public void setRatingAttribute(final SDFAttribute ratingAttribute) {
		this.ratingAttribute = ratingAttribute;
	}

	public List<String> getMetrics() {
		return this.metrics;
	}
	
	@Parameter(name = "metrics", type = StringParameter.class, doc = "A list of evaluation metrics to be computed.", isList = true, optional = false)
	public void setMetrics(List<String> metrics) {
		this.metrics = metrics;
	}
	
	public Double getFading() {
		return this.fadingFactor;
	}
	
	@Parameter(name = "fading", type = DoubleParameter.class, doc="The factor by how much old data should fade. Use values 1.0 for no fading (default) to 0.0 for maximal fading.", optional = true)
	public void setFading(Double fadingFactor) {
		this.fadingFactor = fadingFactor;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {

		final List<SDFAttribute> attributes = new ArrayList<>();
		for (final SDFAttribute oldAttribute : this.getInputSchema(0).getAttributes()) {
			attributes.add(new SDFAttribute(null, oldAttribute.getAttributeName(), oldAttribute.getDatatype(),oldAttribute.getUnit(), oldAttribute.getDtConstraints()));
		}
		for (String metric : this.getMetrics()) {
			SDFAttribute metricAttribute = new SDFAttribute(null, metric, SDFDatatype.DOUBLE, null, null, null);
			attributes.add(metricAttribute);
		}
		
		SDFSchema outSchema = new SDFSchema(getInputSchema(0), attributes);
		return outSchema;
	}

}
