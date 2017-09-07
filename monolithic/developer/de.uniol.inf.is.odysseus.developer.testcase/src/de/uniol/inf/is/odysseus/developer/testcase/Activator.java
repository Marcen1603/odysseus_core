/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.developer.testcase;

import java.util.Objects;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.developer.testcase.model.TestModel.SessionContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.test"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;
    // Executor instance
    private static IExecutor executor;

    /**
     * The constructor
     */
    public Activator() {
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        Activator.plugin = this;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void stop(final BundleContext context) throws Exception {
        Activator.plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return Activator.plugin;
    }

    public void bindExecutor(final IExecutor e) {
        Activator.executor = e;
    }

    public void unbindExecutor(final IExecutor e) {
        if (Activator.executor == e) {
            Activator.executor = null;
        }
    }

    public static IExecutor getExecutor() {
        Objects.requireNonNull(Activator.executor, "Executor instance not bonded");
        return Activator.executor;
    }

    public static ISession getSession() {
        final SessionContext context = new SessionContext("System", "manager");
        return SessionManagement.instance.login(context.getUsername(), context.getPassword().getBytes(), UserManagementProvider.instance.getDefaultTenant());
    }
}
