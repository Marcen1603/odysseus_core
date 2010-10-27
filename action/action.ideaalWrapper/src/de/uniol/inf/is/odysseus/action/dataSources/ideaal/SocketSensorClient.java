package de.uniol.inf.is.odysseus.action.dataSources.ideaal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.dataSources.ISourceClient;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
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
	private long interval;
	private Socket socket;	
	private int retries = 5;
	
	private List<String> messages;
	private SDFAttributeList schema;
	private Sensor sensor;
	
	private int tupleID = 0;
	

	public SocketSensorClient(Sensor sensor) {
		this.interval = sensor.getInterval();
		this.messages = sensor.getMessages();
		this.schema = Sensor.getSchema(sensor);
		this.sensor = sensor;
				
		super.logger = LoggerFactory.getLogger(SocketSensorClient.class);
	}	
	
	@Override
	public boolean processData()  {
		try {
			if (this.socket == null){
				//create socket if it isnt opened yet
				this.socket = new Socket(sensor.getIp(), sensor.getPort());
			}
			
			RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());	
			tuple.setAttribute(0, System.currentTimeMillis());
			
			tuple.setAttribute(1, tupleID);
			this.tupleID++;
			
			//send request to sensor
			OutputStream outputStream = this.socket.getOutputStream();
			int index = 2;
			for (String message : messages){
			
				outputStream.flush();
				outputStream.write(message.getBytes());
				outputStream.flush();
			
				//receive result
				String input = readFromSensor(this.socket.getInputStream());
				if (input != null & input.length() >0){
					//add functions to val if necessary
					try {
						input = Sensor.calcRealValue(this.sensor, message, input);
					}catch (Exception e){
						throw new InternalException(e.getMessage());
					}
					
					SDFAttribute attribute = schema.getAttribute(index);
					Object val = this.extractFromInputString(
							attribute.getDatatype(), input);
					if (val != null){
						tuple.setAttribute(index, val);
						index++;
					}else {
						throw new InternalException("Irregular value from sensor: "+input);
					}
				}else {
					throw new InternalException("Value from Sensor is empty");
				}
				
				//wait shortly until next val is fetched
				try {
					Thread.sleep(this.interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			super.sendTupleToClients(tuple);
			
		} catch (IOException e) {
			//socket not avaiable retry or terminate
			this.retries--;
			if (this.retries<1){
				this.logger.error("Sensor not avaiable. Stopping server");
				return false;
			}else {
				this.logger.error("Sensor not avaiable. Retrying ...("+this.retries+" retries left)");
			}
		} catch (InternalException e) {
			super.logger.error(e.getMessage());
			super.logger.error("Skipping tuple");

			//wait till next fetch
			try {
				Thread.sleep(this.interval);
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
				this.socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
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
