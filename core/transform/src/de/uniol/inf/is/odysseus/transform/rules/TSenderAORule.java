package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.Activator;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSenderAORule extends AbstractTransformationRule<SenderAO> {
    static Logger LOG = LoggerFactory.getLogger(TSenderAORule.class);

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(SenderAO operator, TransformationConfiguration config) {
        ISink<?> senderPO = null;
        if (operator.getTransportHandler() != null) {
            String protocolHandler = operator.getProtocolHandler();
            Map<String, String> options = operator.getOptionsMap();
            ITransportHandler transportHandler;
            if (Activator.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
                transportHandler = TransportHandlerRegistry.getInstance(operator.getTransportHandler(),
                        ITransportPattern.PULL, options);
            }
            else {
                transportHandler = TransportHandlerRegistry.getInstance(operator.getTransportHandler(),
                        ITransportPattern.PUSH, options);
            }
            if (transportHandler == null) {
                LOG.error("No transport handler {} found.", operator.getTransportHandler());
                throw new TransformationException("No transport handler " + operator.getTransportHandler() + " found.");
            }

            IDataHandler<?> dataHandler = null;
            if (operator.getDataHandler() != null) {
                if (operator.getOutputSchema() != null) {
                    dataHandler = DataHandlerRegistry.getDataHandler(operator.getDataHandler(),
                            operator.getOutputSchema());
                }
                if (dataHandler == null) {
                    LOG.error("No data handler {} found.", operator.getDataHandler());
                    throw new TransformationException("No data handler " + operator.getDataHandler() + " found.");
                }
            }
            if (Activator.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {

                IProtocolHandler<?> ph = ProtocolHandlerRegistry.getInstance(protocolHandler, options,
                        transportHandler, dataHandler);
                if (ph == null) {
                    LOG.error("No protocol handler {} found.", protocolHandler);
                    throw new TransformationException("No protocol handler " + protocolHandler + " found.");
                }

                // senderPO = new AccessPO(ph);
                LOG.error("PULL not supported in SenderAO");
                throw new TransformationException("PULL not supported in SenderAO");
            }
            else {
                senderPO = new SenderPO();
                IProtocolHandler<?> ph = ProtocolHandlerRegistry.getInstance(protocolHandler, options,
                        transportHandler, dataHandler, (ITransferHandler<?>) senderPO);
                if (ph == null) {
                    LOG.error("No protocol handler {} found.", protocolHandler);
                    throw new TransformationException("No protocol handler " + protocolHandler + " found.");
                }
                ((SenderPO) senderPO).setProtocolHandler(ph);
            }

        }
        defaultExecute(operator, senderPO, config, true, true);
    }

    @Override
    public boolean isExecutable(SenderAO operator, TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public String getName() {
        return "SenderAO (generic) --> SenderPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super SenderAO> getConditionClass() {
        return SenderAO.class;
    }
}
