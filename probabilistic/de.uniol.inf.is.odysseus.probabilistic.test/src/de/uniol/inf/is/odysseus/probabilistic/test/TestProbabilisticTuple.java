/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.test;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@Test(singleThreaded = true)
public class TestProbabilisticTuple {
	/**
	 * Test restrict method of probabilistic tuple.
	 * 
	 * @param input
	 *            The input tuple
	 * @param restrictMatrix
	 *            The restriction matrix
	 * @param output
	 *            The expected result
	 */
	@Test(dataProvider = "tuple")
	public final void testRestrict(final ProbabilisticTuple<IMetaAttribute> input, final int[] restrictMatrix, final ProbabilisticTuple<IMetaAttribute> output) {
		System.out.println("Input: " + input + " -> " + output);
		final ProbabilisticTuple<IMetaAttribute> restricted = input.restrict(restrictMatrix, false);
		System.out.println("Restricted: " + restricted + " -> " + output);
		Assert.assertEquals(restricted, output);
		for (int i = 0; i < restricted.getAttributes().length; i++) {
			if (restricted.getAttribute(i) instanceof ProbabilisticContinuousDouble) {
				final int distribution = ((ProbabilisticContinuousDouble) restricted.getAttribute(i)).getDistribution();
				boolean contains = false;
				for (int d = 0; d < restricted.getDistribution(distribution).getAttributes().length; d++) {
					if (restricted.getDistribution(distribution).getAttribute(d) == i) {
						contains = true;
					}
				}

				Assert.assertTrue(contains);
			}
		}
	}

	/**
	 * Probabilistic tuple provider.
	 * 
	 * @return A array of probabilistic tuples
	 */
	@DataProvider(name = "tuple")
	public final Object[][] provideTuple() {
		return new Object[][] { { this.provideUnivariateTuple(new Integer[] { 0, 1 }, 2), new int[] { 0 }, this.provideUnivariateTuple(new Integer[] { 0 }, 1) }, { this.provideUnivariateTuple(new Integer[] { 0, 2 }, 3), new int[] { 0, 1 }, this.provideUnivariateTuple(new Integer[] { 0 }, 2) },
				{ this.provideUnivariateTuple(new Integer[] { 0, 1 }, 2), new int[] { 1 }, this.provideUnivariateTuple(new Integer[] { 0 }, 1) }, { this.provideUnivariateTuple(new Integer[] { 0, 2 }, 3), new int[] { 1, 2 }, this.provideUnivariateTuple(new Integer[] { 1 }, 2) },
				{ this.provideUnivariateTuple(new Integer[] { 1, 2 }, 3), new int[] { 0, 1 }, this.provideUnivariateTuple(new Integer[] { 1 }, 2) }, { this.provideUnivariateTuple(new Integer[] { 1, 2 }, 3), new int[] { 0, 2 }, this.provideUnivariateTuple(new Integer[] { 1 }, 2) },
				{ this.provideUnivariateTuple(new Integer[] { 1, 3 }, 4), new int[] { 0, 1, 2 }, this.provideUnivariateTuple(new Integer[] { 1 }, 3) }, { this.provideUnivariateTuple(new Integer[] { 1, 3 }, 4), new int[] { 0, 2, 3 }, this.provideUnivariateTuple(new Integer[] { 2 }, 3) } };
	}

	/**
	 * Generator for univariate prob. tuples.
	 * 
	 * @param pos
	 *            The positions for prob. distributions
	 * @param length
	 *            The length of the payload
	 * @return The prob. tuple
	 */
	private IStreamObject<?> provideUnivariateTuple(final Integer[] pos, final int length) {
		final NormalDistributionMixture[] mixtures = new NormalDistributionMixture[pos.length];
		final Object[] attrs = new Object[length];

		final List<Integer> positionList = Arrays.asList(pos);
		for (Integer i = 0; i < length; i++) {
			if (positionList.contains(i)) {
				mixtures[positionList.indexOf(i)] = new NormalDistributionMixture(new double[] { 1.5 }, new double[][] { { 2.5 } });
				attrs[i] = new ProbabilisticContinuousDouble(positionList.indexOf(i));
				mixtures[positionList.indexOf(i)].setAttributes(new int[] { i });
			} else {
				attrs[i] = "StringAttribute";
			}
		}

		return new ProbabilisticTuple<>(attrs, mixtures, true);
	}

}
