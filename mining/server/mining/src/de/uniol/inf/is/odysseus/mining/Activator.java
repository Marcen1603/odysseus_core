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

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;
import de.uniol.inf.is.odysseus.mining.frequentitem.IFrequentPatternMiner;

public class Activator implements BundleActivator {
	
	
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
	
	
	public void bindClassificationLearner(IClassificationLearner<ITimeInterval> learner){
		MiningAlgorithmRegistry.getInstance().addClassificationLearner(learner);
	}
	
	public void unbindClassificationLearner(IClassificationLearner<ITimeInterval> learner){
		MiningAlgorithmRegistry.getInstance().removeClassificationLearner(learner);
	}	
	
	public void bindClusterer(IClusterer<ITimeInterval> learner){
		MiningAlgorithmRegistry.getInstance().addClusterer(learner);
	}
	
	public void unbindClusterer(IClusterer<ITimeInterval> learner){
		MiningAlgorithmRegistry.getInstance().removeClusterer(learner);
	}
	
	public void bindFrequentPatternMiner(IFrequentPatternMiner<ITimeInterval> learner){
		MiningAlgorithmRegistry.getInstance().addFrequentPatternMiner(learner);
	}
	
	public void unbindFrequentPatternMiner(IFrequentPatternMiner<ITimeInterval> learner){
		MiningAlgorithmRegistry.getInstance().removeFrequentPatternMiner(learner);
	}	
	
	
	public void bindOperatorBuilderFactory(IOperatorBuilderFactory obf){
		builderfactory = obf;
		if(obf instanceof OperatorBuilderFactory){
			// TODO: operator builder factory not static...
			// ok, it should be correctly bound now, so that static should be accessible
		}
	}
	
	public void unbindOperatorBuilderFactory(IOperatorBuilderFactory obf){
		builderfactory = null;
		if(obf instanceof OperatorBuilderFactory){
			// TODO: operator builder factory not static...
			// ok, it should be correctly bound now, so that static should be accessible
		}
	}

	public static IOperatorBuilderFactory getBuilderfactory() {
		return builderfactory;
	}	

}
