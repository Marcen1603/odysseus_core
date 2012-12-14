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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilderRegistry;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Activator implements BundleActivator {

    private static BundleContext                     context;
    private static IAggregateFunctionBuilderRegistry aggregateFunctionBuilderRegistry;

    static BundleContext getContext() {
        return Activator.context;
    }

    public static IAggregateFunctionBuilderRegistry getAggregateFunctionBuilderRegistry() {
        return Activator.aggregateFunctionBuilderRegistry;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    @SuppressWarnings("unchecked")
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void stop(final BundleContext bundleContext) throws Exception {

        Activator.context = null;
    }

    public void bindAggregateFunctionBuilderRegistry(
            final IAggregateFunctionBuilderRegistry aggregateFunctionBuilderRegistry) {
        Activator.aggregateFunctionBuilderRegistry = aggregateFunctionBuilderRegistry;
    }

    public void unbindAggregateFunctionBuilderRegistry(
            final IAggregateFunctionBuilderRegistry aggregateFunctionBuilderRegistry) {
        Activator.aggregateFunctionBuilderRegistry = null;
    }
}
