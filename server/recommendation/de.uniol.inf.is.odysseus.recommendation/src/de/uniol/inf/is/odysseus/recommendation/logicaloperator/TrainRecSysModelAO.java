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
package de.uniol.inf.is.odysseus.recommendation.logicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.recommendation.RecommendationDatatypes;
import de.uniol.inf.is.odysseus.recommendation.learner.baseline_learner.BaselinePredictionRecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.registry.RecommendationLearnerRegistry;
import de.uniol.inf.is.odysseus.recommendation.transform.FindAttributeHelper;

/**
 * This is the logical operator for creating a new or update a previous
 * recommender model.
 *
 * @author Cornelius Ludmann
 */
@LogicalOperator(name = "TRAIN_RECSYS_MODEL", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.MINING }, doc = "This operator learns a recommendation model. The result is a stream of recommendation models.")
public class TrainRecSysModelAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -8415363040870119369L;

	/**
	 * Default parameters.
	 */
	public static final String DEFAULT_USER_ATTRIBUTE_NAME = "user";
	public static final String DEFAULT_ITEM_ATTRIBUTE_NAME = "item";
	public static final String DEFAULT_RATING_ATTRIBUTE_NAME = "rating";
	public static final String DEFAULT_LEARNER_NAME = BaselinePredictionRecommendationLearner.NAME;

	/**
	 * Output attribute names.
	 */
	public static final String MODEL_ATTRIBUTE_NAME = "model";
	public static final String RATED_ITEMS_INFO_ATTRIBUTE_NAME = "rated_items_info";

	/**
	 * The ID of the learner. (configuration parameter)
	 */
	private String learner = DEFAULT_LEARNER_NAME;

	/**
	 * These attributes store the user, item and rating.
	 */
	private SDFAttribute userAttribute, itemAttribute, ratingAttribute;

	private boolean outputUsedItems = true;

	private Map<String, String> options;

	/**
	 * Default constructor.
	 */
	public TrainRecSysModelAO() {
		super();
	}

	public TrainRecSysModelAO(final TrainRecSysModelAO op) {
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
		return this.learner;
	}

	/**
	 * @param learner
	 *            the learner to set
	 */
	@Parameter(name = "learner", type = StringParameter.class, possibleValues = "getLearnerValues", doc = "The name of the learner that should be used.", optional = true)
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
		if (this.userAttribute == null) {
			this.userAttribute = FindAttributeHelper.findAttributeByName(this,
					DEFAULT_USER_ATTRIBUTE_NAME);
		}
		return this.userAttribute;
	}

	/**
	 * @param userAttribute
	 *            the userAttribute to set
	 */
	@Parameter(name = "user", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the user IDs.", optional = true)
	public void setUserAttribute(final SDFAttribute userAttribute) {
		this.userAttribute = userAttribute;
	}

	/**
	 * @return the itemAttribute
	 */
	public SDFAttribute getItemAttribute() {
		if (this.itemAttribute == null) {
			this.itemAttribute = FindAttributeHelper.findAttributeByName(this,
					DEFAULT_ITEM_ATTRIBUTE_NAME);
		}
		return this.itemAttribute;
	}

	/**
	 * @param itemAttribute
	 *            the itemAttribute to set
	 */
	@Parameter(name = "item", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the item IDs.", optional = true)
	public void setItemAttribute(final SDFAttribute itemAttribute) {
		this.itemAttribute = itemAttribute;
	}

	/**
	 * @return outputUsedItems
	 */
	public boolean getOutputUsedItems() {
		return this.outputUsedItems;
	}

	/**
	 * @param outputUsedItems
	 *            outputUsedItems to set
	 */
	@Parameter(name = "output_used_items", type = BooleanParameter.class, doc = "true, if the learner should output a list of used items.", optional = true)
	public void setOutputUsedItems(final boolean outputUsedItems) {
		this.outputUsedItems = outputUsedItems;
	}

	/**
	 * @return the ratingAttribute
	 */
	public SDFAttribute getRatingAttribute() {
		if (this.ratingAttribute == null) {
			this.ratingAttribute = FindAttributeHelper.findAttributeByName(
					this, DEFAULT_RATING_ATTRIBUTE_NAME);
		}
		return this.ratingAttribute;
	}

	/**
	 * @param ratingAttribute
	 *            the ratingAttribute to set
	 */
	@Parameter(name = "rating", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the rating IDs.", optional = true)
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
	protected SDFSchema getOutputSchemaIntern(final int port) {
		if (port == 0) {
			final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			// final SDFAttribute model = new SDFAttribute(null, "model",
			// new SDFDatatype("RatingPredictor"), null, null, null);
			final SDFAttribute model = new SDFAttribute(null,
					MODEL_ATTRIBUTE_NAME, new SDFDatatype("RatingPredictor"),
					null, null, null);
			attributes.add(model);
			final SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
			return outSchema;
		} else {
			if (getOutputUsedItems()) {
				final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				final SDFAttribute usedItemsMap = new SDFAttribute(null,
						RATED_ITEMS_INFO_ATTRIBUTE_NAME,
						RecommendationDatatypes.RECOMMENDATION_CANDIDATES,
						null, null, null);
				attributes.add(usedItemsMap);
				final SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
				return outSchema;
			}
		}
		return null;
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
		return new TrainRecSysModelAO(this);
	}

}
