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

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.jbi.management.DeploymentException;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.NormalizedMessage;
import javax.jbi.servicedesc.ServiceEndpoint;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.common.endpoints.ProviderEndpoint;
import org.apache.servicemix.common.util.MessageUtil;
import org.apache.servicemix.jbi.jaxp.SourceTransformer;

/**
 *
 * @author Christian Kuka
 * @org.apache.xbean.XBean element="source"
 */
public class OdysseusProviderEndpoint extends ProviderEndpoint implements OdysseusEndpointType {

    private static final Log LOG = LogFactory.getLog(OdysseusProviderEndpoint.class);
    private String schema;
    private String language;
    private Map schemaMap;
    private DefaultSource source;
    private static SourceTransformer sourceTransformer = new SourceTransformer();

    public OdysseusProviderEndpoint() {
    }

    public OdysseusProviderEndpoint(OdysseusComponent component, ServiceEndpoint endpoint) {
        super(component, endpoint);
    }

    @Override
    public synchronized void start() throws Exception {
        super.start();
        this.schemaMap = new HashMap<String, Class>();
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE STREAM ");
        builder.append(this.getEndpoint());
        builder.append("(");
        if (this.schema != null) {
            String[] schemaArray = getSchema().split(",");
            for (int i = 0; i < schemaArray.length; ++i) {
                String[] field = schemaArray[i].split(":");
                try {
                    schemaMap.put(field[0], Class.forName("java.lang." + field[1]));
                    if (i > 0) {
                        builder.append(",");
                    }
                    builder.append(field[0] + " " + field[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        builder.append(")");
        LOG.info("Event registered: " + this.getEndpoint());
        LOG.info("Command: " + builder.toString());
        OdysseusComponent component = (OdysseusComponent) this.getServiceUnit().getComponent();
        IAdvancedExecutor executor = component.getOdysseusExecutor();
        if (executor != null) {
            executor.addQuery(builder.toString(), this.language);
            SDFEntity entity = DataDictionary.getInstance().getEntity(this.getEndpoint());
            if (entity != null) {
                SDFAttributeList opSchema = DataDictionary.getInstance().getEntity(this.getEndpoint()).getAttributes();
                this.source = new DefaultSource(opSchema);
                WrapperPlanFactory.putAccessPlan(this.getEndpoint(), this.source);
            } else {
                throw new DeploymentException("No SDFEntity found");
            }
        } else {
            throw new DeploymentException("No IAdvancedExecutor found");
        }
    }

    @Override
    public void validate() throws DeploymentException {
        super.validate();
        if ((this.schema == null) || ("".equals(this.schema))) {
            throw new DeploymentException("Unknown schema");
        }
        if ((this.language == null) || ("".equals(this.language))) {
            this.language = "CQL";
        }

    }

    @Override
    protected void processInOnly(MessageExchange exchange, NormalizedMessage in) throws Exception {
        MessageUtil.enableContentRereadability(in);
        process(exchange, in);
    }

    @Override
    protected void processInOut(MessageExchange exchange, NormalizedMessage in, NormalizedMessage out) throws Exception {
        MessageUtil.enableContentRereadability(in);
        process(exchange, in);
        MessageUtil.transfer(in, out);
    }

    public void process(MessageExchange exchange, NormalizedMessage in) throws Exception {
        String data = new SourceTransformer().contentToString(in);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(new StringReader(data));
        Map event = new HashMap<String, Object>();
        while (reader.hasNext()) {
            XMLEvent xmlEvent = reader.nextEvent();
            xmlEvent = reader.peek();
            if ((xmlEvent != null) && (xmlEvent.getEventType() == XMLEvent.START_ELEMENT)) {
                Attribute attr = xmlEvent.asStartElement().getAttributeByName(new QName("id"));
                if ((attr != null) && (this.schemaMap.containsKey(attr.getValue()))) {
                    String key = attr.getValue();
                    xmlEvent = reader.nextEvent();
                    xmlEvent = reader.peek();
                    if ((xmlEvent != null) && (xmlEvent.isCharacters())) {
                        String content = xmlEvent.asCharacters().getData();
                        Class cls = (Class) this.schemaMap.get(key);
                        Class type = Class.forName("java.lang.String");
                        java.lang.reflect.Constructor construct = cls.getConstructor(type);
                        event.put(key, construct.newInstance(content));
                    }
                }
            }
        }
        if (this.source != null) {
            LOG.info("Process: " + sourceTransformer.toDOMDocument(in).getDocumentElement().getTextContent());

            this.source.pushElement(event);
        }
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
