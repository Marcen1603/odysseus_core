/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.image;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    private static BundleContext context;

    /**
     *
     * @return
     */
    static BundleContext getContext() {
        return Activator.context;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        try {
            System.loadLibrary("opencv_java2411");
        }
        catch (final Exception e) {
            Activator.LOG.error(e.getMessage(), e);
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}