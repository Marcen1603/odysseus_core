/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.mining;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.mining.predicate.RulePredicateBuilder;
import de.uniol.inf.is.odysseus.relational.base.predicate.ForPredicate.Type;

public class Activator implements BundleActivator {
	
	private static final String FOR_ALL_RULE_PREDICATE = "ForAllRulePredicate";
	private static final String FOR_ANY_RULE_PREDICATE = "ForAnyRulePredicate";
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private static IOperatorBuilderFactory builderfactory;
	

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public void bindDataDictionary(IDataDictionary dd){				
		dd.addDatatype(MiningDatatypes.FREQUENT_ITEM_SET.getURI(), MiningDatatypes.FREQUENT_ITEM_SET);
		dd.addDatatype(MiningDatatypes.ASSOCIATION_RULE.getURI(), MiningDatatypes.ASSOCIATION_RULE);
		dd.addDatatype(MiningDatatypes.CLASSIFIER.getURI(), MiningDatatypes.CLASSIFIER);	
	}
	
	public void unbindDataDictionary(IDataDictionary dd){
		dd.removeDatatype(MiningDatatypes.FREQUENT_ITEM_SET.getURI());
		dd.removeDatatype(MiningDatatypes.ASSOCIATION_RULE.getURI());
		dd.removeDatatype(MiningDatatypes.CLASSIFIER.getURI());
	}
	
	public void bindOperatorBuilderFactory(IOperatorBuilderFactory obf){
		builderfactory = obf;
		if(obf instanceof OperatorBuilderFactory){
			// TODO: operator builder factory not static...
			// ok, it should be correctly bound now, so that static should be accessible
			OperatorBuilderFactory.putPredicateBuilder(FOR_ALL_RULE_PREDICATE, new RulePredicateBuilder(Type.ALL));
			OperatorBuilderFactory.putPredicateBuilder(FOR_ANY_RULE_PREDICATE, new RulePredicateBuilder(Type.ANY));
		}
	}
	
	public void unbindOperatorBuilderFactory(IOperatorBuilderFactory obf){
		builderfactory = null;
		if(obf instanceof OperatorBuilderFactory){
			// TODO: operator builder factory not static...
			// ok, it should be correctly bound now, so that static should be accessible
			OperatorBuilderFactory.removePredicateBuilder(FOR_ALL_RULE_PREDICATE);
			OperatorBuilderFactory.removePredicateBuilder(FOR_ANY_RULE_PREDICATE);
		}
	}

	public static IOperatorBuilderFactory getBuilderfactory() {
		return builderfactory;
	}	

}
