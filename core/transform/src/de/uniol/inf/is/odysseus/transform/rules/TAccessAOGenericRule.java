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
package de.uniol.inf.is.odysseus.transform.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This rule handles all generic access operator implementations
 * 
 * @author Marco Grawunder
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TAccessAOGenericRule extends AbstractTransformationRule<AccessAO> {
    static Logger LOG = LoggerFactory.getLogger(TAccessAOGenericRule.class);

    @Override
    public int getPriority() {
        return 0;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute(AccessAO operator, TransformationConfiguration config) {
        IDataHandler<?> dataHandler = getDataHandler(operator);
        if (dataHandler == null) {
            LOG.error("No data handler {} found.", operator.getDataHandler());
            throw new TransformationException("No data handler " + operator.getDataHandler() + " found.");
        }

        IProtocolHandler<?> protocolHandler = getProtocolHandler(operator, dataHandler);
        if (protocolHandler == null) {
            LOG.error("No protocol handler {} found.", operator.getProtocolHandler());
            throw new TransformationException("No protocol handler " + operator.getProtocolHandler() + " found.");
        }

        ITransportHandler transportHandler = getTransportHandler(operator, protocolHandler);
        if (transportHandler == null) {
            LOG.error("No transport handler {} found.", operator.getTransportHandler());
            throw new TransformationException("No transport handler " + operator.getTransportHandler() + " found.");
        }

        String accessPOName = operator.getSourcename();
        ISource accessPO = null;
        if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
            accessPO = new AccessPO(protocolHandler);
        }
        else {
            accessPO = new ReceiverPO(protocolHandler);
        }

        // // New generic accessao style
        // if (operator.getTransportHandler() != null) {
        //
        // String protocolHandler = operator.getProtocolHandler();
        // Map<String, String> options = operator.getOptionsMap();
        // ITransportHandler transportHandler;
        // if (Activator.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
        // transportHandler = TransportHandlerRegistry.getInstance(
        // operator.getTransportHandler(), IAccessPattern.PULL,
        // options);
        // } else {
        // transportHandler = TransportHandlerRegistry.getInstance(
        // operator.getTransportHandler(), IAccessPattern.PUSH,
        // options);
        // }
        // if (transportHandler == null) {
        // throw new TransformationException("No transport handler "
        // + operator.getTransportHandler() + " found.");
        // }
        //
        // IDataHandler dataHandler = null;
        // if (operator.getDataHandler() != null) {
        // if (operator.getInputSchema() != null) {
        // dataHandler = DataHandlerRegistry.getDataHandler(
        // operator.getDataHandler(),
        // operator.getInputSchema());
        // } else if (operator.getOutputSchema() != null) {
        // dataHandler = DataHandlerRegistry.getDataHandler(
        // operator.getDataHandler(),
        // operator.getOutputSchema());
        // }
        // if (dataHandler == null) {
        // throw new TransformationException("No data handler "
        // + operator.getDataHandler() + " found.");
        // }
        // }
        //
        // if (Activator.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
        //
        // IProtocolHandler ph = ProtocolHandlerRegistry
        // .getInstance(protocolHandler, options,
        // transportHandler, dataHandler);
        // if (ph == null) {
        // throw new TransformationException("No protocol handler "
        // + protocolHandler + " found.");
        // }
        //
        // accessPO = new AccessPO(ph);
        // } else {
        // accessPO = new ReceiverPO();
        //
        // IProtocolHandler ph = ProtocolHandlerRegistry.getInstance(
        // protocolHandler, options, transportHandler,
        // dataHandler, (ITransferHandler) accessPO);
        // if (ph == null) {
        // throw new TransformationException("No protocol handler "
        // + protocolHandler + " found.");
        // }
        // ((ReceiverPO) accessPO).setProtocolHandler(ph);
        // }
        //
        // } else {
        // logger.warn("DEPRECATED: Use new generic AccessAO style");
        // // older generic accessao style
        // if (Activator.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
        //
        // IInputHandler inputHandlerPrototype = InputHandlerRegistry
        // .getInputHandler(operator.getInput());
        // if (inputHandlerPrototype == null) {
        // throw new TransformationException("No input handler "
        // + operator.getInput() + " found.");
        // }
        // IInputHandler input = inputHandlerPrototype
        // .getInstance(operator.getOptionsMap());
        //
        // IDataHandler dataHandler = null;
        // if (operator.getDataHandler() != null) {
        // if (operator.getInputSchema() != null) {
        // dataHandler = DataHandlerRegistry.getDataHandler(
        // operator.getDataHandler(),
        // operator.getInputSchema());
        // } else {
        // dataHandler = DataHandlerRegistry.getDataHandler(
        // operator.getDataHandler(),
        // operator.getOutputSchema());
        // }
        //
        // if (dataHandler == null) {
        // throw new TransformationException("No data handler "
        // + operator.getDataHandler() + " found.");
        // }
        // }
        //
        // ITransformer transformerPrototype = TransformerRegistry
        // .getTransformer(operator.getTransformer());
        // if (transformerPrototype == null) {
        // throw new TransformationException("No transformer "
        // + operator.getTransformer() + " found.");
        // }
        // ITransformer transformer = transformerPrototype.getInstance(
        // operator.getOptionsMap(), operator.getOutputSchema());
        //
        // if (transformer instanceof IToStringArrayTransformer) {
        // accessPO = new AccessPO(input,
        // (IToStringArrayTransformer) transformer,
        // dataHandler);
        // } else if (transformer instanceof IToObjectInputStreamTransformer) {
        // accessPO = new AccessPO(input,
        // (IToObjectInputStreamTransformer) transformer,
        // dataHandler);
        // }
        //
        // } else { // must be generic push, else isExecutable would not had
        // // returned true
        //
        // IDataHandler concreteHandler = null;
        // if (operator.getInputSchema() != null
        // && operator.getInputSchema().size() > 0) {
        // concreteHandler = DataHandlerRegistry.getDataHandler(
        // operator.getDataHandler(),
        // operator.getInputSchema());
        // } else {
        // concreteHandler = DataHandlerRegistry.getDataHandler(
        // operator.getDataHandler(),
        // operator.getOutputSchema());
        // }
        //
        // IAccessConnectionHandler connectionPrototype =
        // AccessConnectionHandlerRegistry
        // .get(operator.getAccessConnectionHandler());
        // if (connectionPrototype == null) {
        // throw new TransformationException(
        // "No access connection handler "
        // + operator.getAccessConnectionHandler()
        // + " found.");
        // }
        // IAccessConnectionHandler connection = connectionPrototype
        // .getInstance(operator.getOptionsMap());
        //
        // IObjectHandler objectHandlerPrototype = ObjectHandlerRegistry
        // .get(operator.getObjectHandler());
        // if (objectHandlerPrototype == null) {
        // throw new TransformationException("No object handler "
        // + operator.getObjectHandler() + " found!");
        // }
        //
        // IObjectHandler objectHandler = objectHandlerPrototype
        // .getInstance(concreteHandler);
        //
        // IInputDataHandler inputDataHandlerPrototype =
        // InputDataHandlerRegistry
        // .get(operator.getInputDataHandler());
        // if (inputDataHandlerPrototype == null) {
        // throw new TransformationException("No input data handler "
        // + operator.getInputDataHandler() + " found");
        // }
        //
        // IInputDataHandler inputDataHandler = inputDataHandlerPrototype
        // .getInstance(operator.getOptionsMap());
        //
        // accessPO = new ReceiverPO(objectHandler, inputDataHandler,
        // connection);
        // }
        // }

        getDataDictionary().putAccessPlan(accessPOName, accessPO);
        defaultExecute(operator, accessPO, config, true, true);
    }

    @Override
    public boolean isExecutable(AccessAO operator, TransformationConfiguration config) {
        return (getDataDictionary().getAccessPlan(operator.getSourcename()) == null && operator.getWrapper() != null && (Constants.GENERIC_PULL
                .equalsIgnoreCase(operator.getWrapper()) || Constants.GENERIC_PUSH.equalsIgnoreCase(operator
                .getWrapper())));
    }

    @Override
    public String getName() {
        return "AccessAO (generic) --> AccessPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.ACCESS;
    }

    @Override
    public Class<? super AccessAO> getConditionClass() {
        return AccessAO.class;
    }

    private ITransportHandler getTransportHandler(AccessAO operator, IProtocolHandler<?> protocolHandler) {
        ITransportHandler transportHandler = null;
        if (operator.getTransportHandler() != null) {
            transportHandler = TransportHandlerRegistry.getInstance(operator.getTransportHandler(), protocolHandler,
                    operator.getOptionsMap());
        }
        return transportHandler;
    }

    private IProtocolHandler<?> getProtocolHandler(AccessAO operator, IDataHandler<?> dataHandler) {
        IProtocolHandler<?> protocolHandler = null;
        if (operator.getProtocolHandler() != null) {
            if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
                protocolHandler = ProtocolHandlerRegistry.getInstance(operator.getProtocolHandler(),
                        ITransportDirection.IN, IAccessPattern.PULL, operator.getOptionsMap(), dataHandler);
            }
            else {
                protocolHandler = ProtocolHandlerRegistry.getInstance(operator.getProtocolHandler(),
                        ITransportDirection.IN, IAccessPattern.PUSH, operator.getOptionsMap(), dataHandler);
            }
        }
        return protocolHandler;
    }

    private IDataHandler<?> getDataHandler(AccessAO operator) {
        IDataHandler<?> dataHandler = null;
        if (operator.getDataHandler() != null) {
            if (operator.getOutputSchema() != null) {
                dataHandler = DataHandlerRegistry.getDataHandler(operator.getDataHandler(), operator.getOutputSchema());
            }

        }
        return dataHandler;
    }

}
