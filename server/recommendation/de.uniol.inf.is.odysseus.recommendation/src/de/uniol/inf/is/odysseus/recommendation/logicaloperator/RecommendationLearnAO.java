/**********************************************************************************
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
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.recommendation.registry.RecommendationLearnerRegistry;

/**
 * This is the logical operator for creating a new recommender.
 * 
 * @author Cornelius Ludmann
 */
@LogicalOperator(name = "RECOMMENDATION_LEARN", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.MINING }, doc = "This operator learns a recommendation model. The result is a stream of recommendation models.")
public class RecommendationLearnAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -8415363040870119369L;

	/**
	 * The ID of the learner. (configuration parameter)
	 */
	private String learner;

	/**
	 * These attributes store the user, item and rating.
	 */
	private SDFAttribute userAttribute, itemAttribute, ratingAttribute;

	private Map<String, String> options;

	/**
	 * Default constructor.
	 */
	public RecommendationLearnAO() {
		super();
	}

	public RecommendationLearnAO(final RecommendationLearnAO op) {
		super(op);
		this.learner = op.learner;
		this.userAttribute = op.userAttribute;
		this.itemAttribute = op.itemAttribute;
		this.ratingAttribute = op.ratingAttribute;
		this.options = op.options;
	}

	/**
	 * @return the learner
	 */
	public String getLearner() {
		return learner;
	}

	/**
	 * @param learner
	 *            the learner to set
	 */
	@Parameter(name = "learner", type = StringParameter.class, possibleValues = "getLearnerValues", doc = "The name of the learner that should be used.")
	public void setLearner(final String learner) {
		this.learner = learner;
	}

	/**
	 * Returns a list of all registered learners.
	 * 
	 * @return A list of all registered learners.
	 */
	public List<String> getLearnerValues() {
		return RecommendationLearnerRegistry.getInstance().getLearnerList();
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
	@Parameter(name = "user", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the user IDs.")
	public void setUserAttribute(final SDFAttribute userAttribute) {
		this.userAttribute = userAttribute;
	}

	/**
	 * @return the itemAttribute
	 */
	public SDFAttribute getItemAttribute() {
		return itemAttribute;
	}

	/**
	 * @param itemAttribute
	 *            the itemAttribute to set
	 */
	@Parameter(name = "item", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the item IDs.")
	public void setItemAttribute(final SDFAttribute itemAttribute) {
		this.itemAttribute = itemAttribute;
	}

	/**
	 * @return the ratingAttribute
	 */
	public SDFAttribute getRatingAttribute() {
		return ratingAttribute;
	}

	/**
	 * @param ratingAttribute
	 *            the ratingAttribute to set
	 */
	@Parameter(name = "rating", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the rating IDs.")
	public void setRatingAttribute(final SDFAttribute ratingAttribute) {
		this.ratingAttribute = ratingAttribute;
	}

	@Parameter(name = "options", type = StringParameter.class, optional = true, isMap = true, possibleValues = "getOptionValues()", possibleValuesAreDynamic = true)
	public void setOptions(final Map<String, String> options) {
		this.options = options;
	}

	/**
	 * Returns a list of possible option values.
	 * 
	 * TODO: possible values for map does not work
	 * 
	 * @return
	 */
	public Map<String, String> getOptionValues() {
		return RecommendationLearnerRegistry.getInstance().getPossibleOptions(
				getLearner());
	}

	/**
	 * Returns the options.
	 * 
	 * @return the options.
	 */
	public Map<String, String> getOptions() {
		return this.options;
	}

	// /**
	// * Returns the options as map.
	// *
	// * @return the options as map.
	// */
	// public Map<String, String> getOptionsMap() {
	// if (options == null) {
	// return null;
	// }
	// final Map<String, String> optionsMap = new HashMap<>();
	// for (final Option o : this.options) {
	// optionsMap.put(o.getName(), o.getValue());
	// }
	// return optionsMap;
	// }

	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {
		final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		final SDFAttribute recommender = new SDFAttribute(null, "recommender",
				new SDFDatatype("Recommender"), null, null, null);
		attributes.add(recommender);
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
		return new RecommendationLearnAO(this);
	}

}
