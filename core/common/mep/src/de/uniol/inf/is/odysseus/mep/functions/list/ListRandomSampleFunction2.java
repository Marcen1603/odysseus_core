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
package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Cornelius Ludmann
 *
 */
public class ListRandomSampleFunction2 extends AbstractFunction<List<?>> {
	private static final long serialVersionUID = -3377732697486919406L;

	private static final Logger LOG = LoggerFactory.getLogger(ListRandomSampleFunction2.class);

	private static Random rnd = new Random();
	private static long seed = -1;

	private static Random getRandom(long seed) {
		// double-check idiom (see Effective Java, Item 71)
		Random random = rnd;
		if (ListRandomSampleFunction2.seed != seed) {
			synchronized (rnd) {
				random = rnd;
				if (ListRandomSampleFunction2.seed != seed) {
					rnd = random = new Random(seed);
					ListRandomSampleFunction2.seed = seed;
				}
			}
		}
		return random;
	}

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists(),
			SDFDatatype.DISCRETE_NUMBERS, SDFDatatype.DISCRETE_NUMBERS };

	public ListRandomSampleFunction2() {
		super("random_sample", 3, accTypes, SDFDatatype.LIST);
	}

	@Override
	public List<?> getValue() {
		List<?> in = getInputValue(0);
		Number newSize = getInputValue(1);
		Number seed = getInputValue(2);
		HashSet<Object> res;
		if (in.size() <= newSize.intValue()) {
			res = new HashSet<>(in);
		} else {
			Random rnd = ListRandomSampleFunction2.getRandom(seed.longValue());
			// Floyd's Algorithm
			int m = newSize.intValue();
			res = new HashSet<>(m);
			int n = in.size();
			for (int i = n - m; i < n; i++) {
				int pos = rnd.nextInt(i + 1);
				Object item = in.get(pos);
				if (res.contains(item)) {
					res.add(in.get(i));
				} else {
					boolean added = res.add(item);
					if (!added) {
						LOG.warn("Items in list are not distinct. The resulting sample will not have the requested size.");
					}
				}
			}

		}
		return new ArrayList<>(res);
	}
}