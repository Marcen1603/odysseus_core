package de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator.access;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeminders.hidapi.ClassPathLibraryLoader;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandlerDelegate;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandlerListener;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class CopyOfTemper1TransportHandler implements ITransportHandler {
	public static final String NAME = "Temper1";
	
	AbstractTransportHandlerDelegate<?> delegate;
	
	private InputStream input;
	private OutputStream output;
	
	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		return new CopyOfTemper1TransportHandler(protocolHandler, options);
	}
	
	public CopyOfTemper1TransportHandler() {
        super();
        delegate = new AbstractTransportHandlerDelegate<>(null, this,null);
    }
    public CopyOfTemper1TransportHandler(IProtocolHandler<?> protocolHandler) {
        //super(protocolHandler, null);
    	
    	
    }
    
    public CopyOfTemper1TransportHandler(final IProtocolHandler<?> protocolHandler, final Map<String, String> optionsMap) {
    	delegate = new AbstractTransportHandlerDelegate<>(protocolHandler.getExchangePattern(), this, optionsMap);
		protocolHandler.setTransportHandler(this);
		
        this.init(optionsMap);
    }
    
    protected void init(final Map<String, String> options) {
    	System.out.println("init");
    	
    	ClassPathLibraryLoader.loadNativeHIDLibrary();
    }
    
	@Override
	public String getName() {
		return NAME;
	}
	@Override
	public void open() throws UnknownHostException, IOException {
		System.out.println("open");
		
		delegate.open();
	}

	@Override
	public void close() throws IOException {
		System.out.println("close");
		delegate.close();
	}
	
	@Override
	public boolean isDone() {
		System.out.println("isDone");
		
		return false;
	}
	
	@Override
	public void processInOpen() throws IOException {
		System.out.println("processInOpen");
		
		this.input = new Temper1InputStream();
	}

	@Override
	public void processOutOpen() throws IOException {
		System.out.println("processOutOpen");
		
		try{
			URL u = new URL("http://www.google.de");
	        URLConnection uc = u.openConnection();
	        uc.connect();
	        this.output = uc.getOutputStream();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	

	@Override
	public void processInClose() throws IOException {
		System.out.println("processInClose");
		this.input = null;
	}

	@Override
	public void processOutClose() throws IOException {
		System.out.println("processOutClose");
	}

	@Override
	public void send(byte[] message) throws IOException {
		System.out.println("send");
	}

	@Override
	public InputStream getInputStream() {
		System.out.println("getInputStream");
		
		return this.input;
	}

	@Override
	public OutputStream getOutputStream() {
		System.out.println("getOutputStream");
		
		return this.output;
	}
	
	
	@Override
	public void setSchema(SDFSchema schema) {
		System.out.println("setSchema");
		
		delegate.setSchema(schema);
	}

	public SDFSchema getSchema() {
		return delegate.getSchema();
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		System.out.println("getExchangePattern");
		
		return delegate.getExchangePattern();
	}
	@Override
	public boolean isSemanticallyEqual(ITransportHandler other) {
		System.out.println("isSemanticallyEqual");
		
		return false;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addListener(ITransportHandlerListener listener) {
		delegate.addListener(listener);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void removeListener(ITransportHandlerListener listener) {
		delegate.removeListener(listener);
	}
	
	private class Temper1InputStream extends InputStream {
		private InputStream inputStream;
		
		static final int VENDOR_ID = 3141;
	    static final int PRODUCT_ID = 29697;
	    static final int BUFSIZE = 2048;
		private static final long READ_UPDATE_DELAY_MS = 10;
		
		@Override
		public synchronized int read() throws IOException {
			if (this.isStreamEmpty()) {
				this.call();
			}
			return this.inputStream.read();
		}
		
		@Override
		public int available() throws IOException {
			if (this.isStreamEmpty()) {
				this.call();
			}
			return this.inputStream.available();
		}
		
		private boolean isStreamEmpty() throws IOException {
			return (this.inputStream == null) || (this.inputStream.available() == 0);
		}
		
		private synchronized void call() throws IOException {
			if (this.inputStream != null) {
				this.inputStream.close();
			}
			
			System.out.println("Call()");
	        
			
			/**
			 * Temperatur auslesen von TEMPer1 nach:
			 * Quelle: http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on-os-x-to-read-temperature-from-the-temper1-sensor/
			 */
			//ClassPathLibraryLoader.loadNativeHIDLibrary();
	    	
			//System.out.println("readDevice start - "+System.currentTimeMillis());
	        float tempValue = readDevice();
	        String tempValueString = "";
	        tempValueString += tempValue;
	        //tempValueString += 15.678;
	        tempValueString += ",";
	        
	        
	        this.inputStream = new ByteArrayInputStream(tempValueString.getBytes());
	        
	        System.out.println(tempValueString);
	        
	        
	        
	        //String str ="Hallo Welt!,";
			//this.inputStream = new ByteArrayInputStream(str.getBytes());
		}
		
		/**
		 * Quelle: 
		 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on-os-x-to-read-temperature-from-the-temper1-sensor/
		 * @param rawtemp
		 * @return
		 */
		float raw_to_c(int rawtemp)
	    {
	        float temp_c = rawtemp * (125.f / 32000.f);
	        return temp_c;
	    }
	    
		/**
		 * Quelle:
		 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on-os-x-to-read-temperature-from-the-temper1-sensor/
		 * @param deg_c
		 * @param unit
		 * @return
		 */
	    float c_to_u(float deg_c, char unit)
	    {
	        if (unit == 'F')
	            return (deg_c * 1.8f) + 32.f;
	        else if (unit == 'K')
	            return (deg_c + 273.15f);
	        else
	            return deg_c;
	    }
	    
	    /**
	     * Nach
	     * Quelle:
	     * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on-os-x-to-read-temperature-from-the-temper1-sensor/
	     * @return
	     * @throws IOException
	     */
	    
	    Random rand = new Random();
	    
	    private float readDevice() throws IOException {
	    	return 20f + ( 10 * rand.nextFloat());
//	    {
//	        HIDDevice dev;
//	        int rawtemp = -1000;
//	        
//	        try
//	        {
//	            HIDManager hid_mgr = HIDManager.getInstance();
//	            dev = hid_mgr.openById(VENDOR_ID, PRODUCT_ID, null);
//
//	            byte[] temp = new byte[] {
//	                (byte)0x01, (byte)0x80, (byte)0x33, (byte)0x01, 
//	                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
//	            };
//	            
//	            int res = dev.write(temp);
//	            
//	            
//	            try
//	            {
//	                byte[] buf = new byte[BUFSIZE];
//	                int n = dev.read(buf);
//	                    
//	                rawtemp = (buf[3] & (byte)0xFF) + (buf[2] << 8);
//	                if ((buf[2] & 0x80) != 0) {
//	                    /* return the negative of magnitude of the temperature */
//	                    rawtemp = -((rawtemp ^ 0xffff) + 1);
//	                }
//
//	                System.out.println("temp = " + c_to_u(raw_to_c(rawtemp), 'C'));
//	                
//	                /*
//	                try {
//	    				Thread.sleep(READ_UPDATE_DELAY_MS);
//	    			} catch (InterruptedException e) {
//	    			}
//	                */
//	                
//	            }
//	            finally
//	            {
//	                dev.close();
//	                hid_mgr.release();    
//	            }
//	        }
//	        catch(IOException e)
//	        {
//	            e.printStackTrace();
//	        }
//	        
//	        if(rawtemp == -1000){
//	        	throw new IOException();
//	        }
//	        
//	        return c_to_u(raw_to_c(rawtemp), 'C');
	    }
	}
}
