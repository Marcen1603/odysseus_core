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

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.jbi.management.DeploymentException;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.InOnly;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.NormalizedMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.common.endpoints.ConsumerEndpoint;
import org.apache.servicemix.jbi.jaxp.StringSource;

/**
 *
 * @author Christian Kuka
 * @org.apache.xbean.XBean element="sink"
 */
public class OdysseusConsumerEndpoint extends ConsumerEndpoint implements OdysseusEndpointType {

    private static final Log LOG = LogFactory.getLog(OdysseusConsumerEndpoint.class);
    private DefaultSink sink;
    private String language;
    private String statement;

    @Override
    public synchronized void start() throws Exception {
        super.start();
        this.sink = new DefaultSink(this);
        OdysseusComponent component = (OdysseusComponent) this.getServiceUnit().getComponent();
        IAdvancedExecutor executor = component.getOdysseusExecutor();
        if (executor != null) {
            Collection<Integer> queryIds;
            queryIds = executor.addQuery(this.statement, this.language, new ParameterDefaultRoot(this.sink));
            if (queryIds.size() != 1) {
                throw new RuntimeException(
                        "query string has to contain exactly 1 query");
            }
            int queryId = queryIds.iterator().next();
            IQuery query = executor.getSealedPlan().getQuery(queryId);

            SDFAttributeList schema = query.getSealedLogicalPlan().getOutputSchema();
            this.sink.setSchema(schema);
            Map<String, String> outputSchema = new HashMap<String, String>();
            for (SDFAttribute curAttr : schema) {
                outputSchema.put(curAttr.getURI(), curAttr.getDatatype().getURI());
            }
            executor.startQuery(queryId);
            if (!executor.isRunning()) {
                executor.startExecution();
            }
        }
    }

    @Override
    public void validate() throws DeploymentException {
        super.validate();
        if ((this.statement == null) || ("".equals(this.statement))) {
            throw new DeploymentException("Unknown statement");
        }
        if ((this.language == null) || ("".equals(this.language))) {
            this.language = "CQL";
        }
    }

    @Override
    public void process(MessageExchange exchange) throws Exception {
        if (exchange instanceof InOnly) {
            if (exchange.isTransacted() && ExchangeStatus.ERROR.equals(exchange.getStatus())) {
                throw exchange.getError();
            }
            return;
        }
        try {
            done(exchange);
        } catch (Exception e) {
            fail(exchange, e);
            throw e;
        }
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;

    }

    public void update(Map<String, String> event) {
        StringBuffer out = new StringBuffer();
        out.append("<sensor>");
        for (String propertyName : event.keySet()) {
            out.append("<data id=\"" + propertyName + "\">" + event.get(propertyName) + "</data>");
        }
        out.append("</sensor>");
        LOG.info("Update: " + out.toString());


        try {
            InOnly exchange = getContext().getDeliveryChannel().createExchangeFactory().createInOnlyExchange();
            NormalizedMessage message = exchange.createMessage();

            message.setContent(new StringSource(out.toString()));
            message.setProperty("sensorId", this.endpoint);
            exchange.setInMessage(message);
            configureExchangeTarget(exchange);
            send(exchange);
        } catch (Exception e) {
            LOG.fatal(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
