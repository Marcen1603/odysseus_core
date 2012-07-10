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
package de.uniol.inf.is.odysseus.service.sensor.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "SensorRegistryServiceService", targetNamespace = "http://sensorregistry.odysseus.is.inf.uniol.de/", wsdlLocation = "http://localhost:9999/odysseus?wsdl")
public class SensorRegistryServiceService
    extends Service
{

    private final static URL SENSORREGISTRYSERVICESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(de.uniol.inf.is.odysseus.service.sensor.webservice.SensorRegistryServiceService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = de.uniol.inf.is.odysseus.service.sensor.webservice.SensorRegistryServiceService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:9999/odysseus?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:9999/odysseus?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        SENSORREGISTRYSERVICESERVICE_WSDL_LOCATION = url;
    }

    public SensorRegistryServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SensorRegistryServiceService() {
        super(SENSORREGISTRYSERVICESERVICE_WSDL_LOCATION, new QName("http://sensorregistry.odysseus.is.inf.uniol.de/", "SensorRegistryServiceService"));
    }

    /**
     * 
     * @return
     *     returns SensorRegistryService
     */
    @WebEndpoint(name = "SensorRegistryServicePort")
    public SensorRegistryService getSensorRegistryServicePort() {
        return super.getPort(new QName("http://sensorregistry.odysseus.is.inf.uniol.de/", "SensorRegistryServicePort"), SensorRegistryService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SensorRegistryService
     */
    @WebEndpoint(name = "SensorRegistryServicePort")
    public SensorRegistryService getSensorRegistryServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://sensorregistry.odysseus.is.inf.uniol.de/", "SensorRegistryServicePort"), SensorRegistryService.class, features);
    }

}
