/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Dennis Geesen
 *
 */
public class MiningDatatypes implements IDatatypeProvider{
	
	public static final SDFDatatype FREQUENT_ITEM_SET = new SDFDatatype("FrequentItemSet");
	public static final SDFDatatype ASSOCIATION_RULE = new SDFDatatype("AssociationRule");
	public static final SDFDatatype CLASSIFIER = new SDFDatatype("Classifier");

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(FREQUENT_ITEM_SET);
		ret.add(ASSOCIATION_RULE);
		ret.add(CLASSIFIER);
		return ret;
	}
	
}
