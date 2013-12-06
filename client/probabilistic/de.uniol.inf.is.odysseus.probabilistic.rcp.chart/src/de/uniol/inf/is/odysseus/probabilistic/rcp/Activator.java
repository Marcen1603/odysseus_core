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
package de.uniol.inf.is.odysseus.probabilistic.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.probabilistic.rcp.chart.datatype.ProbabilisticDataType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Activator extends AbstractUIPlugin {
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
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public final void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        ViewableDatatypeRegistry.getInstance().register(new ProbabilisticDataType());
    }

    /*
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public final void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }
}
