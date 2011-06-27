package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class NIOServerSocketSource<T> extends AbstractSource<T>{

	private InetSocketAddress socketAddress;
	private ServerSocketChannel server;
	private SelectionKey key;
	private Selector selector;
	private Thread worker;
	
	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(NIOServerSocketSource.class);
		}
		return _logger;
	}

	public NIOServerSocketSource(String host, int port) throws IOException {
		InetAddress inetAddress = InetAddress.getByName(host);
		this.socketAddress = new InetSocketAddress(inetAddress, port);
	}

	public NIOServerSocketSource(NIOServerSocketSource<T> nioServerSocketSource) {
		super();
		this.socketAddress = nioServerSocketSource.getSocketAddress();
	}

	public void setSchema(SDFAttributeList schema) {
		getLogger().debug("SetSchema: " + schema.toString());
		super.setOutputSchema(schema);
	}
	
	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		getLogger().debug("Process Open");
		getLogger().debug(	"Socket Listen: " + this.socketAddress.getHostName() + " "+ this.socketAddress.getPort());
		
		try {
			this.selector = Selector.open();
			server = ServerSocketChannel.open();
			server.socket().bind(socketAddress);
			server.configureBlocking(false);
			key = server.register(selector, SelectionKey.OP_ACCEPT);
			
			worker = new NIOServerSocketWorker<T>(this);
			worker.start();
		} catch (IOException e) {
			getLogger().error(e.getMessage());
			new OpenFailedException(e);
		}
	}

	@Override
	protected void process_close() {
		worker.interrupt();
	}
	
	@Override
	public AbstractSource<T> clone() {
		return new NIOServerSocketSource<T>(this);
	}

	public Selector getSelector() {
		return this.selector;
	}

	public SelectionKey getKey(){
		return this.key;
	}
	
	public ServerSocketChannel getServerSocketChannel(){
		return this.server;
	}
	
	public void request(String request){
		getLogger().debug("Request: " + request);
		getLogger().debug("Attribute: " + this.getOutputSchema());
		
		if(this.getOutputSchema() != null){
			
		}
		
		RelationalTuple tuple = new RelationalTuple<ITimeInterval>(this.getOutputSchema().getAttributeCount());
		
		//tuple.setMetadata()
		
		String[] values = request.split(";");
		for(int i = 0; i < values.length; i++){
			if(this.getOutputSchema().get(i).getDatatype().getQualName().equals("String")){
				tuple.addAttributeValue(i, values[i]);
			}
			if(this.getOutputSchema().get(i).getDatatype().getQualName().equals("Double")){
				tuple.addAttributeValue(i, Double.parseDouble(values[i]));
			}
			if(this.getOutputSchema().get(i).getDatatype().getQualName().equals("Integer")){
				//tuple.addAttributeValue(i, Integer.parseInt(values[i]));
				tuple.setAttribute(i, Integer.parseInt(values[i]));
			}
			if(this.getOutputSchema().get(i).getDatatype().getQualName().equals("Geometry")){
				
			}	
			
		}			
		transfer((T)tuple);
	}

	
}
