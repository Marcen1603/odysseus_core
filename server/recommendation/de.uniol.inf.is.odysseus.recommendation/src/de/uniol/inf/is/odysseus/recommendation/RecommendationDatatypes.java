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
package de.uniol.inf.is.odysseus.recommendation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Cornelius Ludmann
 *
 */
public class RecommendationDatatypes implements IDatatypeProvider {

	public static final SDFDatatype RATING_PREDICTOR = new SDFDatatype(
			"RatingPredictor");
	public static final SDFDatatype RECOMMENDATION_CANDIDATES = new SDFDatatype(
			"RecommendationCandidates");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider#getDatatypes()
	 */
	@Override
	public List<SDFDatatype> getDatatypes() {
		final List<SDFDatatype> ret = new ArrayList<>();
		ret.add(RATING_PREDICTOR);
		ret.add(RECOMMENDATION_CANDIDATES);
		return ret;
	}

}
