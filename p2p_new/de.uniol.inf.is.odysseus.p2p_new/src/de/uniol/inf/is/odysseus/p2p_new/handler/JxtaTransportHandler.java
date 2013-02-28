package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.OutputPipeEvent;
import net.jxta.pipe.OutputPipeListener;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class JxtaTransportHandler extends AbstractTransportHandler implements PipeMsgListener, OutputPipeListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaTransportHandler.class);

	private static final String PIPE_NAME = "Odysseus Pipe";
	private static final String PIPEID_TAG = "pipeid";

	private InputPipe inputPipe;
	private OutputPipe outputPipe;
	
	private PipeID pipeID;

	// for transportFactory
	public JxtaTransportHandler() {
		super();
	}

	public JxtaTransportHandler(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		super(protocolHandler);
		processOptions(options);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		return new JxtaTransportHandler(protocolHandler, options);
	}

	@Override
	public void send(byte[] message) throws IOException {
		if( outputPipe != null ) {
			LOG.info("Sending message");
			
			Message msg = new Message();
			msg.addMessageElement(new ByteArrayMessageElement("DATA", null, message, null));
			outputPipe.send(msg);
		}
	}

	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return "JXTA";
	}

	@Override
	public void processInOpen() throws IOException {
		LOG.info("Process In Open");
		PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);
		P2PNewPlugIn.getDiscoveryService().publish(pipeAdvertisement); // needed?
		
		inputPipe = P2PNewPlugIn.getPipeService().createInputPipe(pipeAdvertisement, this);
		LOG.info("InputPipe is {}", inputPipe);
	}

	@Override
	public void processOutOpen() throws IOException {
		LOG.info("Process Out Open");
		
		PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);
		P2PNewPlugIn.getPipeService().createOutputPipe(pipeAdvertisement, this);
		
	}

	@Override
	public void processInClose() throws IOException {
		if( inputPipe != null ) {
			inputPipe.close();
			LOG.info("InputPipe closed");
		}
	}

	@Override
	public void processOutClose() throws IOException {
		if( outputPipe != null ) {
			outputPipe.close();
			LOG.info("OutputPipe closed");
		}
	}

	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		LOG.info("Got pipe event");
		
		MessageElement messageElement = event.getMessage().getMessageElement("DATA");
		byte[] data = messageElement.getBytes(false);
		fireProcess(ByteBuffer.wrap(data));
	}

	@Override
	public void outputPipeEvent(OutputPipeEvent event) {
		outputPipe = event.getOutputPipe();
		
		LOG.info("Output pipe is {}", outputPipe);
	}
	
	protected void processOptions(Map<String, String> options) {
		String id = options.get(PIPEID_TAG);
		if (!Strings.isNullOrEmpty(id)) {
			pipeID = convertToPipeID(id);
		}
	}

	private static PipeAdvertisement createPipeAdvertisement(PipeID pipeID) {
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setName(PIPE_NAME);
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastSecureType);
		LOG.info("Pipe Advertisement with id = {}", pipeID);
		return advertisement;
	}

//	private static PipeAdvertisement getPipeAdvertisement(PipeID pipeID) {
//		try {
//			Enumeration<Advertisement> advs = P2PNewPlugIn.getDiscoveryService().getLocalAdvertisements(DiscoveryService.ADV, null, null);
//			while( advs.hasMoreElements() ) {
//				Advertisement adv = advs.nextElement();
//				if( adv instanceof PipeAdvertisement ) {
//					PipeAdvertisement padv = (PipeAdvertisement) adv;
//					if( padv.getPipeID().equals(pipeID)) {
//						return padv;
//					}
//				}
//			}
//			LOG.error("Desired pipe advertisement with id {} not found", pipeID);
//			return null;
//			
//		} catch (IOException ex) {
//			LOG.error("Could not find pipe advertisement for pipeid {}", pipeID, ex);
//			return null;
//		}
//	}

	private static PipeID convertToPipeID(String text) {
		try {
			URI id = new URI(text);
			return PipeID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}
}
