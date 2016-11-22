package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationUtils;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartTransmissionException;
import de.uniol.inf.is.odysseus.net.querydistribute.util.IOperatorGenerator;

public class NetworkConnectionOperatorGenerator implements IOperatorGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(NetworkConnectionOperatorGenerator.class);

	private static final long WAIT_TIME_MILLIS = 10 * 1000;

	private final IOdysseusNodeCommunicator nodeCommunicator;

	private ILogicalOperator sourceOp;
	private IOdysseusNode sourceNode;

	private int sourcePort;

	public NetworkConnectionOperatorGenerator(IOdysseusNodeCommunicator communicator) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");

		nodeCommunicator = communicator;
	}

	@Override
	public void beginDisconnect(ILogicalQueryPart sourceQueryPart, ILogicalOperator sourceOperator, IOdysseusNode sourceNode, ILogicalQueryPart sinkQueryPart, ILogicalOperator sinkOperator, IOdysseusNode sinkNode) throws QueryPartTransmissionException {
		LOG.debug("Create JXTA-Connection between {} and {}", new Object[] { sourceOperator, sinkOperator });

		sourceOp = sourceOperator;
		this.sourceNode = sourceNode;

		try {
			sourcePort = determineSourcePort(sourceNode);
			if( sourcePort == -1  ) {
				// could not reserve port
				throw new QueryPartTransmissionException(Lists.newArrayList(sourceNode));
			}
		} catch (OdysseusNodeCommunicationException e) {
			throw new QueryPartTransmissionException(Lists.newArrayList(sourceNode));
		}
	}

	private int determineSourcePort(IOdysseusNode sourceNode) throws OdysseusNodeCommunicationException {
		ReserveServerPortMessage reserveMsg = new ReserveServerPortMessage();

		IMessage msg = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(nodeCommunicator, sourceNode, reserveMsg, WAIT_TIME_MILLIS, ServerPortReservedMessage.class);
		ServerPortReservedMessage answer = (ServerPortReservedMessage)msg;

		return answer.getReservedPort();
	}

	@Override
	public ILogicalOperator createSourceofSink(ILogicalQueryPart sinkQueryPart, ILogicalOperator sink, IOdysseusNode sinkNode) {
		AccessAO access = new AccessAO();
		access.setTransportHandler("tcpclient");
		access.setDataHandler("tuple");
		access.setWrapper("GenericPush");
		access.setProtocolHandler("Odysseus");

		OptionMap options = new OptionMap();
		options.setOption("host", sourceNode.getProperty("serverAddress").get());
		options.setOption("port", sourcePort + "");
		options.setOption("autoreconnect", String.valueOf(1000));
		options.setOption("basetimeunit", ((AbstractLogicalOperator) sink).getBaseTimeUnit().toString());

		access.setOptionMap(options);
		access.setAttributes(sourceOp.getOutputSchema().getAttributes());
		access.setLocalMetaAttribute(MetadataRegistry.getMetadataType(sourceOp.getOutputSchema().getMetaAttributeNames()));
		access.setReadMetaData(true);
		access.setOverWriteSchemaSourceName(false);

		access.setAccessAOName(new Resource("System.OdysseusNetAccess_" + sourcePort));

		return access;
	}

	@Override
	public ILogicalOperator createSinkOfSource(ILogicalQueryPart sourceQueryPart, ILogicalOperator source, IOdysseusNode sourceNode) {
		SenderAO senderAO = new SenderAO();
		senderAO.setTransportHandler("tcpserver");
		senderAO.setDataHandler("tuple");
		senderAO.setWrapper("GenericPush");
		senderAO.setProtocolHandler("Odysseus");
		senderAO.setWriteMetaData(true);

		OptionMap options = new OptionMap();
		options.setOption("port", sourcePort + "");

		senderAO.setOptionMap(options);
		senderAO.setSink(new Resource("System.OdysseusNetSender_" + sourcePort));

		return senderAO;
	}

	@Override
	public void endDisconnect() {
		sourceOp = null;
		sourceNode = null;
		sourcePort = -1;
	}

}

//#addquery
//out = SENDER({
//          transport = 'tcpserver',
//          datahandler = 'tuple',
//          sink = 'sink_person',
//          wrapper = 'GenericPush',
//          protocol = 'Odysseus',
//          writeMetadata = true,
//          OPTIONS = [['port','6666']]
//        },
//        sel
//      )
//
//
//#addquery
//in = ACCESS({
//          transport = 'tcpclient',
//          source = 'source_person',
//          datahandler = 'tuple',
//          wrapper = 'GenericPush',
//          protocol = 'Odysseus',
//          options = [['host','localhost'],['port','6666']],
//          schema=[
//            ['timestamp', 'LONG'],
//            ['id', 'INTEGER'],
//            ['name', 'STRING'],
//            ['email', 'STRING'],
//            ['creditcard', 'STRING'],
//            ['city', 'STRING'],
//            ['state', 'STRING']
//          ],
//          METAATTRIBUTE = 'TimeInterval',
//          readMetaData = true
//        }
//      )
//
