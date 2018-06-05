package de.uniol.inf.is.odysseus.wrapper.snet.deprecated;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.NioTcpConnection;

public class TcpOutputStream  extends OutputStream implements Runnable{
	
	protected final static Logger LOG = LoggerFactory.getLogger(TcpOutputStream.class);
	protected Thread t;
	
	private NioTcpConnection connection;
		
	@Override
	public void run() {
		
	}
	
	private ByteBuffer buffer = ByteBuffer.allocate(1024);

	public TcpOutputStream(NioTcpConnection con) {
		this.connection = con;
		start();
	}

	@Override
	public void flush() throws IOException {
		this.buffer.flip();
		if (connection != null) {
			connection.write(this.buffer);
		}
		this.buffer.clear();
	}

	@Override
	public void write(final int b) throws IOException {
		if ((1 + this.buffer.position()) >= this.buffer.capacity()) {
			final ByteBuffer newBuffer = ByteBuffer
					.allocate((1 + this.buffer.position()) * 2);
			final int pos = this.buffer.position();
			this.buffer.flip();
			newBuffer.put(this.buffer);
			this.buffer = newBuffer;
			this.buffer.position(pos);
			TcpOutputStream.LOG.debug("Extending buffer to "
					+ this.buffer.capacity());
		}
		this.buffer.put((byte) b);
	}
	
	public void start(){
		if(t == null){
			t = new Thread(this);
			t.setName("TcpOutputStream");
			t.start();
			LOG.debug(this.getClass().toString() + " started.");
		} else {
			LOG.debug("Trying to start TcpOutputStream - but it was already running");
		}
	}
	
	@Override
	public void close(){
		this.buffer.clear();
		if(t != null){
			t.interrupt();
		}
		t = null;
		LOG.debug(this.getClass().toString() + " closed.");
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}
}