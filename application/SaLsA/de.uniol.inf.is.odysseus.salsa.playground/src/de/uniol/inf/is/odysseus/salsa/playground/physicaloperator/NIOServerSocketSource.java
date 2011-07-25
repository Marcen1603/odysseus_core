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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;


public class NIOServerSocketSource<T extends IMetaAttribute> extends AbstractSource<RelationalTuple<ITimeInterval>>{

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
		try {
			this.key.channel().close();
			this.key.cancel();
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public AbstractSource<RelationalTuple<ITimeInterval>> clone() {
		return new NIOServerSocketSource(this);
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
		if (this.isOpen() && !request.contains("$") && request.startsWith("S")) {
			RelationalTuple tuple = new RelationalTuple<ITimeInterval>(this.getOutputSchema().getAttributeCount());
			ITimeInterval metadata = null;
			
			String[] values = request.split(";");
			boolean transferTuple = true;
			
			//getLogger().debug(request);
			
			
			if(values.length == this.getOutputSchema().getAttributeCount()){
				for(int i = 0; i < this.getOutputSchema().getAttributeCount(); i++){
						//getLogger().debug(i + ":" + values[i] + " " + this.getOutputSchema().get(i).getDatatype().getQualName());
						if(this.getOutputSchema().get(i).getDatatype().getQualName().equalsIgnoreCase("String")){
							tuple.addAttributeValue(i, values[i].toString());
						}
						if(this.getOutputSchema().get(i).getDatatype().getQualName().equalsIgnoreCase("Double")){
							tuple.addAttributeValue(i, Double.parseDouble(values[i]));
						}
						if(this.getOutputSchema().get(i).getDatatype().getQualName().equalsIgnoreCase("Integer")){
							tuple.setAttribute(i, Integer.parseInt(values[i]));
						}
						if(this.getOutputSchema().get(i).getDatatype().getQualName().equalsIgnoreCase("Long")){
							tuple.setAttribute(i, Long.parseLong(values[i]));
							//Simple Hack, only for SaLsA Sensor Cluster!(Like the whole class)
							if(this.getOutputSchema().get(i).getAttributeName().equalsIgnoreCase("timestamp")){
								metadata = new TimeInterval(new PointInTime(Long.parseLong(values[i])));	
							}
						}
						if(this.getOutputSchema().get(i).getDatatype().getQualName().equalsIgnoreCase("Object")){
							
							
							String[] coordinateValues = ((String) values[i].subSequence(1, values[i].length()-1)).split(",");
							Coordinate coordinates[] = new Coordinate[coordinateValues.length];
							String[] coordinateValue;
							
							for(int c = 0; c < coordinateValues.length; c++){
								coordinateValue = ((String)coordinateValues[c].subSequence(1, coordinateValues[c].length()-1)).split(":");
								coordinates[c] = new Coordinate(Double.parseDouble(coordinateValue[0]),Double.parseDouble(coordinateValue[1]));
							}
							
							
							GeometryFactory geoFactory = new GeometryFactory();
							Geometry geometry = null;

//							if (coordinates.length == 1) {
//								geometry = geoFactory.createPoint(coordinates[0]);
//							}
							
							if (coordinates.length > 1) {
								geometry = geoFactory.createLineString(coordinates);
								tuple.addAttributeValue(i, geometry);
							}
							else{
								transferTuple = false;
							}
							
							
						}
				}	
				if(metadata == null){
					getLogger().error("no timestamp in Stream");
					metadata = new TimeInterval(new PointInTime(System.currentTimeMillis()));	
				}
				
				//transferTuple = false;
				if(transferTuple){
					tuple.setMetadata(metadata);
					transfer((RelationalTuple<ITimeInterval>)tuple);
				}

			}
			else{
				getLogger().debug(values.length + " != " + this.getOutputSchema().getAttributeCount());
			}
		}
	}
	
}
