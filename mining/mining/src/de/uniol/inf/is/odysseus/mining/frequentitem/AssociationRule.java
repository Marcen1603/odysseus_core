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
package de.uniol.inf.is.odysseus.mining.frequentitem;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.Pattern;

/**
 * @author Dennis Geesen
 *
 */
public class AssociationRule<M extends IMetaAttribute> {

	/**
	 * @param premise
	 * @param consequence
	 * @param support
	 * @param confidence
	 */
	public AssociationRule(Pattern<M> premise, Pattern<M> consequence, int support, double confidence) {
		// TODO Auto-generated constructor stub
	}

}
