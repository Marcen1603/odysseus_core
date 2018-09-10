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
package de.uniol.inf.is.odysseus.recommendation.model.rating_predictor;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * <p>
 * This rating predictor returns always the same constant value.
 *
 * <p>
 * This implementation is immutable, if P is immutable (or a safe-copy).
 *
 * @author Cornelius Ludmann
 *
 */
public class ConstantValueRatingPredictor<T extends Tuple<M>, M extends IMetaAttribute, U, I, P>
extends AbstractTupleBasedRatingPredictor<T, M, U, I, P> {

	private final P value;

	/**
	 * Constructs a predictor that predicts always <code>value</code>.
	 *
	 * @param value
	 *            The value that should be predicted for each object.
	 */
	public ConstantValueRatingPredictor(final P value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.model.Model#predict(de.uniol
	 * .inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public P predict(final T object) {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.RatingPredictor#predict
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public P predict(final U user, final I item) {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.RatingPredictor#predict
	 * (de.uniol.inf.is.odysseus.core.collection.Tuple,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute)
	 */
	@Override
	public P predict(final T object, final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute) {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConstantValueRatingPredictor [value=" + this.value + "]";
	}

}
