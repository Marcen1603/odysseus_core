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
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(name = "RECOMMENDATION_CANDIDATES", minInputPorts = 1, maxInputPorts = 2, category = { LogicalOperatorCategory.MINING }, doc = "This operator computes a stream of recommendation candidates.")
public class RecommendationCandidatesAO extends AbstractLogicalOperator {

	/**
	 *
	 */
	private static final long serialVersionUID = -7858178732877188425L;

	/**
	 * Default input attribute names.
	 */
	public static final String DEFAULT_REQUESTING_USER_ATTRIBUTE_NAME = "user";
	public static final String DEFAULT_RATED_ITEMS_INFO_ATTRIBUTE_NAME = TrainRecSysModelAO.RATED_ITEMS_INFO_ATTRIBUTE_NAME;

	public static final String ITEM_ATTRIBUTE_NAME = "item";

	/**
	 * These attributes store the requesting user, rated items and rated items
	 * by user map.
	 */
	private SDFAttribute requestingUserAttribute, ratedItemsInfoAttribute;

	/**
	 * Default constructor.
	 */
	public RecommendationCandidatesAO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public RecommendationCandidatesAO(final RecommendationCandidatesAO op) {
		super(op);
		this.requestingUserAttribute = op.requestingUserAttribute;
		this.ratedItemsInfoAttribute = op.ratedItemsInfoAttribute;
	}

	/**
	 * @return the requestingUserAttribute
	 */
	public SDFAttribute getRequestingUserAttribute() {
		if (this.requestingUserAttribute == null) {
			this.requestingUserAttribute = FindAttributeHelper
					.findAttributeByName(this,
							DEFAULT_REQUESTING_USER_ATTRIBUTE_NAME);
		}
		return this.requestingUserAttribute;
	}

	/**
	 * @param requestingUserAttribute
	 *            the requestingUserAttribute to set
	 */
	@Parameter(name = "requesting_user", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the user IDs of the requesting users.", optional = true)
	public void setRequestingUserAttribute(
			final SDFAttribute requestingUserAttribute) {
		this.requestingUserAttribute = requestingUserAttribute;
	}

	/**
	 * @return the ratedItemsInfoAttribute
	 */
	public SDFAttribute getRatedItemsInfoAttribute() {
		if (this.ratedItemsInfoAttribute == null) {
			this.ratedItemsInfoAttribute = FindAttributeHelper
					.findAttributeByName(this,
							DEFAULT_RATED_ITEMS_INFO_ATTRIBUTE_NAME);
		}
		return this.ratedItemsInfoAttribute;
	}

	/**
	 * @param ratedItemsInfoAttribute
	 *            the ratedItemsInfoAttribute to set
	 */
	@Parameter(name = "rated_items_info", type = ResolvedSDFAttributeParameter.class, doc = "The attribute with the lists of the rated items.", optional = true)
	public void setRatedItemsAttribute(
			final SDFAttribute ratedItemsInfoAttribute) {
		this.ratedItemsInfoAttribute = ratedItemsInfoAttribute;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {

		SDFAttribute requestingUserAttribute = getRequestingUserAttribute();
		if (requestingUserAttribute == null) {
			requestingUserAttribute = FindAttributeHelper.findAttributeByName(
					this, DEFAULT_REQUESTING_USER_ATTRIBUTE_NAME);
		}

		SDFAttribute ratedItemsInfoAttribute = getRatedItemsInfoAttribute();
		if (ratedItemsInfoAttribute == null) {
			ratedItemsInfoAttribute = FindAttributeHelper.findAttributeByName(
					this, DEFAULT_RATED_ITEMS_INFO_ATTRIBUTE_NAME);
		}

		final int port = FindAttributeHelper.findPortWithAttribute(this,
				requestingUserAttribute);

		final List<SDFAttribute> attributes = new ArrayList<>();
		for (final SDFAttribute oldAttribute : this.getInputSchema(port)
				.getAttributes()) {
			if (!oldAttribute.equals(ratedItemsInfoAttribute)) {
				attributes
						.add(new SDFAttribute(null, oldAttribute
								.getAttributeName(),
								oldAttribute.getDatatype(), oldAttribute
										.getUnit(), oldAttribute
										.getDtConstraints()));
			}
		}

		final SDFAttribute attributeId = new SDFAttribute(null,
				ITEM_ATTRIBUTE_NAME, SDFDatatype.LONG, null, null, null);
		attributes.add(attributeId);

		final SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(port));
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
		return new RecommendationCandidatesAO(this);
	}

}
