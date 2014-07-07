/**
 * Copyright 2014 The Odysseus Team
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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * This is the logical operator for recommending a set of items for a certain
 * user.
 * 
 * @author Cornelius Ludmann
 */
@LogicalOperator(name = "RECOMMENDATION", minInputPorts = 2, maxInputPorts = 2, category = { LogicalOperatorCategory.MINING }, doc = "This operator computes a set of recommendations.")
public class RecommendationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 3891045799640876899L;

	private SDFAttribute userAttribute;
	private SDFAttribute recommenderAttribute;
	private int numberOfRecommendations = -1;

	/**
	 * Default constructor.
	 */
	public RecommendationAO() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param recommendationAO
	 *            Another object.
	 */
	public RecommendationAO(final RecommendationAO recommendationAO) {
		super(recommendationAO);
		this.userAttribute = recommendationAO.userAttribute;
		this.recommenderAttribute = recommendationAO.recommenderAttribute;
		this.numberOfRecommendations = recommendationAO.numberOfRecommendations;
	}

	/**
	 * @return the userAttribute
	 */
	public SDFAttribute getUserAttribute() {
		return userAttribute;
	}

	/**
	 * @param userAttribute
	 *            the userAttribute to set
	 */
	@Parameter(name = "user", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the user.", optional = false)
	public void setUserAttribute(final SDFAttribute userAttribute) {
		this.userAttribute = userAttribute;
	}

	/**
	 * @return the recommenderAttribute
	 */
	public SDFAttribute getRecommenderAttribute() {
		return recommenderAttribute;
	}

	/**
	 * @param recommenderAttribute
	 *            the recommenderAttribute to set
	 */
	@Parameter(name = "recommender", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the recommender model.", optional = false)
	public void setRecommenderAttribute(final SDFAttribute recommenderAttribute) {
		this.recommenderAttribute = recommenderAttribute;
	}

	/**
	 * @return the numberOfRecommendations
	 */
	public int getNumberOfRecommendations() {
		return numberOfRecommendations;
	}

	/**
	 * @param numberOfRecommendations
	 *            the numberOfRecommendations to set
	 */
	@Parameter(name = "no_of_recommendations", type = IntegerParameter.class, doc = "How many elements should be recommended?", optional = true)
	public void setNumberOfRecommendations(final int numberOfRecommendations) {
		if (numberOfRecommendations < 1) {
			this.numberOfRecommendations = -1;
		}
		this.numberOfRecommendations = numberOfRecommendations;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {

		final List<SDFAttribute> attributes = new ArrayList<>();
		for (final SDFAttribute oldAttribute : this.getInputSchema(0)
				.getAttributes()) {
			attributes.add(new SDFAttribute(null, oldAttribute
					.getAttributeName(), oldAttribute.getDatatype(),
					oldAttribute.getUnit(), oldAttribute.getDtConstraints()));
		}

		SDFAttribute attributeId = new SDFAttribute(null, "itemid",
				SDFDatatype.OBJECT, null, null, null);
		attributes.add(attributeId);
		attributeId = new SDFAttribute(null, "rating", SDFDatatype.DOUBLE,
				null, null, null);
		attributes.add(attributeId);

		final SDFSchema outSchema = new SDFSchema(getInputSchema(0), attributes);
		return outSchema;
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
		return new RecommendationAO(this);
	}

}
