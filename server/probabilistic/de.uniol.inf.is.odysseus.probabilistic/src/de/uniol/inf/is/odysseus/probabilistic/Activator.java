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
package de.uniol.inf.is.odysseus.probabilistic;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticRelationalPredicateBuilder;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Activator implements BundleActivator {
    /** The bundle context. */
    private static BundleContext context;

    /**
     * Gets the bundle context.
     * 
     * @return The bundle context
     */
    static BundleContext getContext() {
        return Activator.context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public final void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        OperatorBuilderFactory.putExpressionBuilder(new ProbabilisticRelationalPredicateBuilder<>());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public final void stop(final BundleContext bundleContext) throws Exception {
        OperatorBuilderFactory.removeExpressionBuilder(new ProbabilisticRelationalPredicateBuilder<>());
    }

}
