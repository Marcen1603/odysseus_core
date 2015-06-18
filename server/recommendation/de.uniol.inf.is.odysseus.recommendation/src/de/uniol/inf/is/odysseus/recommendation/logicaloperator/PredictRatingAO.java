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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.recommendation.transform.FindAttributeHelper;

/**
 * This is the logical operator for predicting a rating score for a user-item
 * pair.
 *
 * @author Cornelius Ludmann
 */
@LogicalOperator(name = "PREDICT_RATING", minInputPorts = 1, maxInputPorts = 2, category = { LogicalOperatorCategory.MINING }, doc = "This operator predicts a rating score for a user-item pair.")
public class PredictRatingAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 3891045799640876899L;

	private SDFAttribute userAttribute;
	private SDFAttribute itemAttribute;
	private SDFAttribute modelAttribute;

	public final static String DEFAULT_USER_ATTRIBUTE_NAME = TrainRecSysModelAO.DEFAULT_USER_ATTRIBUTE_NAME;
	public final static String DEFAULT_ITEM_ATTRIBUTE_NAME = TrainRecSysModelAO.DEFAULT_ITEM_ATTRIBUTE_NAME;
	public final static String DEFAULT_MODEL_ATTRIBUTE_NAME = TrainRecSysModelAO.MODEL_ATTRIBUTE_NAME;

	public final static String PREDICTED_RATING_ATTRIBUTE_NAME = "predicted_rating";
	public static final String USER_ATTRIBUTE_NAME = "user";

	/**
	 * Default constructor.
	 */
	public PredictRatingAO() {
		super();
	}

	/**
	 * Copy constructor.
	 *
	 * @param predictRatingAO
	 *            Another object.
	 */
	public PredictRatingAO(final PredictRatingAO predictRatingAO) {
		super(predictRatingAO);
		this.userAttribute = predictRatingAO.userAttribute;
		this.itemAttribute = predictRatingAO.itemAttribute;
		this.modelAttribute = predictRatingAO.modelAttribute;
	}

	/**
	 * @return the userAttribute
	 */
	public SDFAttribute getUserAttribute() {
		return this.userAttribute;
	}

	/**
	 * @param userAttribute
	 *            the userAttribute to set
	 */
	@Parameter(name = "user", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the user.", optional = true)
	public void setUserAttribute(final SDFAttribute userAttribute) {
		this.userAttribute = userAttribute;
	}

	/**
	 * @return the itemAttribute
	 */
	public SDFAttribute getItemAttribute() {
		return this.itemAttribute;
	}

	/**
	 * @param itemAttribute
	 *            the itemAttribute to set
	 */
	@Parameter(name = "item", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the item.", optional = true)
	public void setItemAttribute(final SDFAttribute itemAttribute) {
		this.itemAttribute = itemAttribute;
	}

	/**
	 * @return the modelAttribute
	 */
	public SDFAttribute getModelAttribute() {
		return this.modelAttribute;
	}

	/**
	 * @param modelAttribute
	 *            the modelAttribute to set
	 */
	@Parameter(name = "model", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the recommender model.", optional = true)
	public void setModelAttribute(final SDFAttribute modelAttribute) {
		this.modelAttribute = modelAttribute;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {

		SDFAttribute itemAttribute = getItemAttribute();
		if (itemAttribute == null) {
			itemAttribute = FindAttributeHelper.findAttributeByName(this,
					PredictRatingAO.DEFAULT_ITEM_ATTRIBUTE_NAME);
		}

		final int port = FindAttributeHelper.findPortWithAttribute(this,
				itemAttribute);

		SDFAttribute modelAttribute = getModelAttribute();
		if (modelAttribute == null) {
			modelAttribute = FindAttributeHelper.findAttributeByName(this,
					PredictRatingAO.DEFAULT_MODEL_ATTRIBUTE_NAME);
		}

		final List<SDFAttribute> attributes = new ArrayList<>();
		for (final SDFAttribute oldAttribute : this.getInputSchema(port)
				.getAttributes()) {
			if (!oldAttribute.equals(modelAttribute)) {
				attributes
						.add(new SDFAttribute(null, oldAttribute
								.getAttributeName(),
								oldAttribute.getDatatype(), oldAttribute
										.getUnit(), oldAttribute
										.getDtConstraints()));
			}
		}

		final SDFAttribute attributeId = new SDFAttribute(null,
				PredictRatingAO.PREDICTED_RATING_ATTRIBUTE_NAME,
				SDFDatatype.DOUBLE, null, null, null);
		attributes.add(attributeId);

		final SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(
				attributes, getInputSchema(port));
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
		return new PredictRatingAO(this);
	}

}
