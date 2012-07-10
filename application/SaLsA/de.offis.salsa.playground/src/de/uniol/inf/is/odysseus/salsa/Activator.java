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
package de.uniol.inf.is.odysseus.salsa;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.salsa.function.BoundingBox;
import de.uniol.inf.is.odysseus.salsa.function.ExtractSegments;
import de.uniol.inf.is.odysseus.salsa.function.IEPF;
import de.uniol.inf.is.odysseus.salsa.function.IsPedestrian;
import de.uniol.inf.is.odysseus.salsa.function.MergeGeometries;
import de.uniol.inf.is.odysseus.salsa.function.ObjectSize;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Activator implements BundleActivator {
    private static Logger LOG = LoggerFactory.getLogger(BundleActivator.class);
    private static BundleContext context;

    /**
     * @return The Activator Context
     */
    static BundleContext getContext() {
        return Activator.context;
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        try {
            MEP.registerFunction(new ExtractSegments());
            MEP.registerFunction(new IsPedestrian());
            MEP.registerFunction(new MergeGeometries());
            MEP.registerFunction(new BoundingBox());
            MEP.registerFunction(new ObjectSize());
            MEP.registerFunction(new IEPF());
        }
        catch (final Exception e) {
            Activator.LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
        try {
            MEP.unregisterFunction("ExtractSegments");
            MEP.unregisterFunction("IsPedestrian");
            MEP.unregisterFunction("MergeGeometries");
            MEP.unregisterFunction("BoundingBox");
            MEP.unregisterFunction("ObjectSize");
            MEP.unregisterFunction("IEPF");
        }
        catch (final Exception e) {
            Activator.LOG.error(e.getMessage(), e);
        }
    }

}
