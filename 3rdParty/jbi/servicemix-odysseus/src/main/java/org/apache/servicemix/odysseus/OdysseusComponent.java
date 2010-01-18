/*
 *  Copyright 2009 ckuka.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.apache.servicemix.odysseus;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.jbi.servicedesc.ServiceEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.common.DefaultComponent;
import org.apache.servicemix.common.Endpoint;
import org.apache.servicemix.common.util.IntrospectionSupport;
import org.apache.servicemix.common.util.URISupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.osgi.context.BundleContextAware;

/**
 *
 * @author Christian Kuka
 * @org.apache.xbean.XBean element="component"
 */
public class OdysseusComponent extends DefaultComponent implements BundleContextAware {

    private static final Log LOG = LogFactory.getLog(OdysseusComponent.class);
    private ServiceTracker serviceTracker;
    private BundleContext bundleContext;
    private IAdvancedExecutor odysseusExecutor;
    private OdysseusEndpointType[] endpoints;

    public OdysseusComponent() {
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.serviceTracker = new ServiceTracker(this.bundleContext, IAdvancedExecutor.class.getName(), null);
        this.serviceTracker.open();
        try {
        this.odysseusExecutor = (IAdvancedExecutor) this.serviceTracker.waitForService(0);
        } catch (InterruptedException e) {
            LOG.fatal(e.getLocalizedMessage());
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        this.bundleContext.addServiceListener(new DebugTracker());
        Bundle[] bundles = this.bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            LOG.warn("Bundle found: " + bundle.getBundleId() + " " + bundle.getLocation());
        }
    }

    @Override
    protected void doStop() throws Exception {
        this.serviceTracker.close();
        super.doStop();
    }

    public OdysseusEndpointType[] getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(OdysseusEndpointType[] endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    protected List getConfiguredEndpoints() {
        return asList(getEndpoints());
    }

    @Override
    protected Class[] getEndpointClasses() {
        return new Class[]{OdysseusConsumerEndpoint.class, OdysseusProviderEndpoint.class};
    }

    @Override
    protected Endpoint getResolvedEPR(ServiceEndpoint ep) throws Exception {
        // We receive an exchange for an EPR that has not been used yet.
        // Register a provider endpoint and restart processing.
        LOG.info("Request for: " + ep.getEndpointName());
        OdysseusProviderEndpoint odysseusEp = new OdysseusProviderEndpoint(this, ep);

        URI uri = new URI(ep.getEndpointName());

        Map map = URISupport.parseQuery(uri.getQuery());
        IntrospectionSupport.setProperties(odysseusEp, map);
        odysseusEp.validate();

        return odysseusEp;
    }

    public IAdvancedExecutor getOdysseusExecutor() {
        if (this.odysseusExecutor == null) {
            LOG.warn("No Advanced Executor found");
        } else {
            LOG.info("Advanced Executor found");
        }
        return this.odysseusExecutor;
    }

    public void setExecutor(IAdvancedExecutor exec) {
        this.odysseusExecutor = exec;
         if (this.odysseusExecutor == null) {
            LOG.warn("Advanced Executor unset");
        } else {
            LOG.info("Advanced Executor set");
        }
    }

    public void unsetExecutor(IAdvancedExecutor exec) {
        this.odysseusExecutor = null;
    }

    class DebugTracker implements ServiceListener {

        public void serviceChanged(ServiceEvent event) {
            LOG.warn("ServiceEvent: " + event.toString() + " " + event.getServiceReference().getClass().getName());
            getOdysseusExecutor();
        }
    }
}
