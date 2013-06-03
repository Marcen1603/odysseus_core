package de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi;

import java.io.IOException;

import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.util.JxtaBiDiPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.util.connect.AbstractJxtaConnection;

class JxtaBiDiConnection extends AbstractJxtaConnection implements PipeMsgListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiConnection.class);
	
	private final JxtaBiDiPipe pipe;
	
	JxtaBiDiConnection( JxtaBiDiPipe pipe ) {
		Preconditions.checkNotNull(pipe, "Pipe for jxta bidi connection must not be null!");
		
		this.pipe = pipe;
		this.pipe.setMessageListener(this);
	}

	@Override
	public void disconnect() {
		try {
			pipe.setMessageListener(null);
			pipe.close();
			
		} catch (IOException e) {
			LOG.error("Co uld not close JxtaBiDiPipe", e);
		} finally {
			super.disconnect();
		}
	}
	
	@Override
	public void send(byte[] data) throws IOException {
		waitForConnect();
		
		Message msg = new Message();
		msg.addMessageElement(new ByteArrayMessageElement("bytes", null, data, null));
		
		pipe.sendMessage(msg);
	}

	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		Message msg = event.getMessage();
		
		ByteArrayMessageElement bytes = (ByteArrayMessageElement) msg.getMessageElement("bytes");
		fireMessageReceiveEvent(bytes.getBytes());
	}

	protected final JxtaBiDiPipe getPipe() {
		return pipe;
	}
}
