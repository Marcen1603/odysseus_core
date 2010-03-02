package de.uniol.inf.is.odysseus.action.dataSources.ideaal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.benchmark.BenchmarkData;
import de.uniol.inf.is.odysseus.action.benchmark.IActuatorBenchmark;
import de.uniol.inf.is.odysseus.action.dataSources.ISourceClient;
import de.uniol.inf.is.odysseus.action.dataSources.StreamClient;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * Client responsible for fetching values 
 * @author Simon Flandergan
 *
 */
public class SocketSensorClient extends ISourceClient {
	private static IActuatorBenchmark benchmark;
	private long interval;
	private Socket socket;	
	private int retries = 5;
	
	private List<String> messages;
	private SDFAttributeList schema;
	private Sensor sensor;
	
	private Logger logger;
	
	private boolean benchmarking = false;
	

	public SocketSensorClient(Sensor sensor) {
		this.interval = sensor.getInterval();
		this.messages = sensor.getMessages();
		this.schema = Sensor.getSchema(sensor);
		this.sensor = sensor;
				
		this.logger = LoggerFactory.getLogger(SocketSensorClient.class);
	}	
	
	public void bindBenchmark(IActuatorBenchmark benchmark){
		SocketSensorClient.benchmark = benchmark;
		this.benchmarking = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean processData()  {
		try {
			String debugInfo = "";
			if (this.socket == null){
				//create socket if it isnt opened yet
				this.socket = new Socket(sensor.getIp(), sensor.getPort());
			}
			
			RelationalTuple tuple = null;
			
			//send notification if benchmarking is avaiable
			if (this.benchmarking){
				tuple = new RelationalTuple<BenchmarkData>(this.schema.size());		
				BenchmarkData data = new BenchmarkData(benchmark.getNextID(this.sensor.name()+"_Odysseus"));
				tuple.setMetadata(data);
			}else {
				tuple = new	RelationalTuple<ITimeInterval>(this.schema.size());
			}
			
			tuple.setAttribute(0, System.currentTimeMillis());
			
			//send request to sensor
			OutputStream outputStream = this.socket.getOutputStream();
			int index = 1;
			for (String message : messages){
			
				outputStream.flush();
				outputStream.write(message.getBytes());
				outputStream.flush();
			
				//receive result
				String input = readFromSensor(this.socket.getInputStream());
				if (input != null & input.length() >0){
					//add functions to val if necessary
					input = Sensor.calcRealValue(this.sensor, message, input);
					
					SDFAttribute attribute = schema.getAttribute(index);
					Object val = this.extractFromInputString(
							attribute.getDatatype(), input);
					if (val != null){
						tuple.setAttribute(index, val);
						debugInfo += val+" ";
						index++;
					}else {
						throw new InternalException("Irregular value from sensor: "+input);
					}
				}else {
					throw new InternalException("Value from Sensor is empty");
				}
				
				//wait shortly until next val is fetched
				try {
					sleep(this.interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (this.benchmarking){
				((BenchmarkData)tuple.getMetadata()).addOutputTime(IActuatorBenchmark.Operation.DATAEXTRACTION.name());
			}
			
			//send tuple to clients
			synchronized (clients) {
				for (StreamClient client : this.clients){
					client.writeObject(tuple);
				}
			}
			
			this.logger.debug("Sent tuple: "+debugInfo);
		} catch (IOException e) {
			//socket not avaiable retry or terminate
			this.retries--;
			if (this.retries<1){
				this.logger.error("Sensor or Client not avaiable. Stopping server");
				return false;
			}else {
				this.logger.error("Sensor or Client not avaiable. Retrying ...("+this.retries+" retries left)");
			}
		} catch (InternalException e) {
			this.logger.error(e.getMessage());
			this.logger.error("Skipping tuple");

			//wait till next fetch
			try {
				sleep(this.interval);
			} catch (InterruptedException e2) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private Object extractFromInputString(SDFDatatype datatype, String val) {
		String qualName = datatype.getQualName();
		qualName = qualName.toLowerCase();
		if (qualName.equals("double")){
			return Double.valueOf(val);
		}else if (qualName.equals("float")){
			return Float.valueOf(val);
		}else if (qualName.equals("long")){
			return Long.valueOf(val);
		}else if (qualName.equals("int")){
			return Integer.valueOf(val);
		}else if (qualName.equals("short")){
			return Short.valueOf(val);
		}else if (qualName.equals("byte")){
			return Byte.valueOf(val);
		}else if (qualName.equals("char")){
			return val.charAt(0);
		}else if (qualName.equals("boolean")){
			return Boolean.valueOf(val);
		}else if (qualName.equals("string")){
			return val;
		}
		return null;
			
	}

	private String readFromSensor(InputStream stream) {
		String message ="";
		char[] buffer = new char[255];
		
		InputStreamReader reader = new InputStreamReader(stream);
		try {
			if (reader.ready()){
				reader.read(buffer, 0, stream.available());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
		
		message = new String(buffer);
		
		return message.trim();
	}

	@Override
	public void cleanUp() {
		if (this.socket != null && !this.socket.isClosed()){
			try {
				this.retries = 0;
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (StreamClient client : clients){
			client.closeSocket();
		}
		this.clients.clear();
	}
	
	public SDFAttributeList getSchema() {
		return schema;
	}
	
	/**
	 * Internal exception
	 * @author Simon Flandergan
	 *
	 */
	class InternalException extends Exception{

		public InternalException(String string) {
			super(string);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 3281636761706013319L;
		
		
	}

}
