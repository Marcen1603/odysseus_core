/**********************************************************************************
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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.recommendation.learner.TupleBasedRecommendationLearnerProvider;
import de.uniol.inf.is.odysseus.recommendation.learner.baseline_learner.BaselinePredictionRecommendationLearnerProvider;
import de.uniol.inf.is.odysseus.recommendation.learner.debug_learner.DebugPredictionRecommendationLearnerProvider;
import de.uniol.inf.is.odysseus.recommendation.registry.RecommendationLearnerRegistry;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		bindRecommendationLearner(new BaselinePredictionRecommendationLearnerProvider());
		// bindRecommendationLearner(new BRISMFLearnerProvider<>());
		bindRecommendationLearner(new DebugPredictionRecommendationLearnerProvider());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public void bindRecommendationLearner(
			final TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?> provider) {
		RecommendationLearnerRegistry.getInstance().addRecommendationLearner(
				provider);
	}

	public void unbindRecommendationLearner(
			final TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?> provider) {
		RecommendationLearnerRegistry.getInstance()
				.removeRecommendationLearner(provider);
	}
}
