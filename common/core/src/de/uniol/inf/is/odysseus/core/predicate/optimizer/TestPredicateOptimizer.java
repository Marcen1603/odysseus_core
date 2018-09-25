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
package de.uniol.inf.is.odysseus.core.predicate.optimizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.FalsePredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NullPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.predicate.RelatedTuplesPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TestPredicateOptimizer {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {

		while (true) {
			System.out.print("$:> ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			try {
				String line = in.readLine();
				if ("q".equalsIgnoreCase(line)) {
					break;
				}

				RelatedTuplesPredicate tuplePredicate1 = new RelatedTuplesPredicate((List<Integer>) null);
				RelatedTuplesPredicate tuplePredicate2 = new RelatedTuplesPredicate((List<Integer>) null);
				RelatedTuplesPredicate tuplePredicate3 = new RelatedTuplesPredicate((List<Integer>) null);

				TruePredicate tru = TruePredicate.getInstance();
				FalsePredicate fal = FalsePredicate.getInstance();
				NullPredicate nul = NullPredicate.getInstance();

				IPredicate<?> expression = new AndPredicate(new OrPredicate(
						new OrPredicate(new OrPredicate(nul, tru), new AndPredicate(fal, nul)).and(tuplePredicate1),
						(tuplePredicate2)), tuplePredicate3);

				System.out.println("<- " + expression);
				System.out.println("Optimized: " + PredicateOptimizer.optimize(expression));

				System.out.println("DNF: " + PredicateOptimizer.toDisjunctiveNormalForm(expression));

				System.out.println("CNF: " + PredicateOptimizer.toConjunctiveNormalForm(expression));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

}
