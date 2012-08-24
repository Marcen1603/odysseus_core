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
package de.uniol.inf.is.odysseus.parser.pql.relational;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.relational.base.predicate.ForPredicate.Type;


public class Activator implements BundleActivator {

	private static final String RELATIONAL_PREDICATE = "RELATIONALPREDICATE";
	private static final String FOR_ALL_PREDICATE = "FORALLPREDICATE";
	private static final String FOR_ANY_PREDICATE = "FORANYPREDICATE";

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putPredicateBuilder(RELATIONAL_PREDICATE, new RelationalPredicateBuilder());
		OperatorBuilderFactory.putPredicateBuilder(FOR_ALL_PREDICATE, new ForPredicateBuilder(Type.ALL));
		OperatorBuilderFactory.putPredicateBuilder(FOR_ANY_PREDICATE, new ForPredicateBuilder(Type.ANY));
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removePredicateBuilder(RELATIONAL_PREDICATE);
		OperatorBuilderFactory.removePredicateBuilder(FOR_ALL_PREDICATE);
		OperatorBuilderFactory.removePredicateBuilder(FOR_ANY_PREDICATE);
	}

}
