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
package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.util.Random;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ShuffleFragmentAO;

/**
 * Physical operator for shuffle fragment
 * @author ChrisToenjesDeye
 *
 * @param <T>
 */
public class ShuffleFragmentPO<T extends IStreamObject<IMetaAttribute>> extends
AbstractStaticFragmentPO<T> {

	private Random rand;
	
	public ShuffleFragmentPO(ShuffleFragmentAO fragmentAO) {
		super(fragmentAO);
		long seed = 4711234562348971262l;
		rand = new Random(seed);
	}

	/**
	 * routes object to an random output port
	 */
	@Override
	protected int route(IStreamObject<IMetaAttribute> object) {
		return rand.nextInt(numFragments);
	}

}
