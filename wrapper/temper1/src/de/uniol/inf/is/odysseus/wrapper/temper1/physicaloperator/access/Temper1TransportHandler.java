package de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator.access;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFTimeUnit;


public class Temper1TransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>>{
	private static Logger LOG = LoggerFactory.getLogger(Temper1TransportHandler.class);
	
	public static final String NAME = "Temper1";
	private static final int VENDOR_ID = 3141;
    private static final int PRODUCT_ID = 29697;
    private static final int BUFSIZE = 2048;
    private static ArrayList<HIDDeviceInfo> temperaturSensors = new ArrayList<HIDDeviceInfo>();
    public static final String TEMPNUMBER = "tempnumber";
    
	static {
    	ClassPathLibraryLoader.loadNativeHIDLibrary();
	}
	
	private int currentTempNumber;
	private long lastUpdateConnectedTemperatureSensorsTime;
	private long DELTATIME_FOR_DEVICE_UPDATE_MS = 10000;

	public Temper1TransportHandler() {
		// TODO Auto-generated constructor stub
	}

	public Temper1TransportHandler(Temper1TransportHandler other) {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		updateConnectedTemperatureSensors();
		
		Temper1TransportHandler tHandler = new Temper1TransportHandler();
		
		if (options.containsKey(TEMPNUMBER)) {
			tHandler.setTempNumber(options.get(TEMPNUMBER));
	    }
		
		protocolHandler.setTransportHandler(tHandler);
		
		return tHandler;
	}

	private void updateConnectedTemperatureSensors() {
		long delta = System.currentTimeMillis() - lastUpdateConnectedTemperatureSensorsTime;
		
		if(delta >= DELTATIME_FOR_DEVICE_UPDATE_MS ){
			//System.out.println("updateConnectedTemperatureSensors");
			
			temperaturSensors.clear();
			try {
	            //com.codeminders.hidapi.ClassPathLibraryLoader.loadNativeHIDLibrary();
	            HIDDeviceInfo[] infos = HIDManager.getInstance().listDevices();
	            
	            for (HIDDeviceInfo info : infos) {
	                if(info.getVendor_id()==VENDOR_ID && info.getProduct_id()==PRODUCT_ID && info.getUsage()==1){
	                	if(!temperaturSensors.contains(info)){
	                		temperaturSensors.add(info);
	                	}
	                }
	            }
	            
	            //System.out.println("size:"+temperaturSensors.size());
			}catch(IOException ex){
				//ex.printStackTrace();
				LOG.error(ex.getMessage(), ex);
				
				LOG.debug("try to load other lib for rpi...");
				
				
				echoTest();
				
				
	        } catch (Exception ex) {
	            //ex.printStackTrace();
	            LOG.error(ex.getMessage(), ex);
	        }
			
			lastUpdateConnectedTemperatureSensorsTime = System.currentTimeMillis();
		}
	}

	private static void echoTest() {
		try {
			final Process p = Runtime.getRuntime().exec("lib/pcsensor");
			
			new Thread(new Runnable() {
			    public void run() {
			        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			        String line = null; 
			        
			        try {
			            while ((line = input.readLine()) != null) {
			                System.out.println(line);
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}).start();
			
			p.waitFor();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch(Exception e2){
			e2.printStackTrace();
		}
	}
	
	private void setTempNumber(String tempNumber) {
		Integer tempNumberInteger = Integer.parseInt(tempNumber);
		
		this.currentTempNumber = tempNumberInteger.intValue();
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Nach
	 * Quelle:
	 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on-os-x-to-read-temperature-from-the-temper1-sensor/
	 * @param hidDeviceInfo 
	 * @throws IOException 
	 * 
	 */
    @SuppressWarnings("unused")
	private static float readDevice(HIDDeviceInfo device) throws IOException {
    	HIDDevice dev;
    	
        HIDManager hid_mgr = HIDManager.getInstance();
        //dev = hid_mgr.openById(VENDOR_ID, PRODUCT_ID, null);
        dev = hid_mgr.openByPath(device.getPath());
        
        byte[] temp = new byte[] {
            (byte)0x01, (byte)0x80, (byte)0x33, (byte)0x01, 
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
        };
        
        try
        {
        	int res = dev.write(temp);
        	
            byte[] buf = new byte[BUFSIZE];
            int n = dev.read(buf);
                
            int rawtemp = (buf[3] & (byte)0xFF) + (buf[2] << 8);
            if ((buf[2] & 0x80) != 0) {
                /* return the negative of magnitude of the temperature */
                rawtemp = -((rawtemp ^ 0xffff) + 1);
            }
            
            return c_to_u(raw_to_c(rawtemp), 'C');
        }
        finally
        {
            dev.close();
            hid_mgr.release();    
        }
    }
    
    /**
	 * Quelle: 
	 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on-os-x-to-read-temperature-from-the-temper1-sensor/
	 * @param rawtemp
	 * @return
	 */
	private static float raw_to_c(int rawtemp)
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
    private static float c_to_u(float deg_c, char unit)
    {
        if (unit == 'F')
            return (deg_c * 1.8f) + 32.f;
        else if (unit == 'K')
            return (deg_c + 273.15f);
        else
            return deg_c;
    }

	@Override
	public boolean hasNext() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getNext() {
		Tuple<?> tuple = new Tuple(2, false);
		
		int deviceNumber = this.currentTempNumber < temperaturSensors.size() ? this.currentTempNumber : 0;
		
		try {
			tuple.setAttribute(0, readDevice(temperaturSensors.get(deviceNumber)));
			
			/*
			tuple.setAttribute(0, readDevice(temperaturSensors.get(--i)));
			if(i==0){
				i=temperaturSensors.size();
			}*/
		} catch (IOException e) {
			tuple.setAttribute(0, getSimulatedTemperature());
			updateConnectedTemperatureSensors();
		} catch(Exception e){
			tuple.setAttribute(0, getSimulatedTemperature());
			updateConnectedTemperatureSensors();
		}
		
		tuple.setAttribute(1, new SDFTimeUnit("YYYY-MM-dd HH-mm-ss"));
		
		return tuple;
	}
	
	private static float getSimulatedTemperature(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date(System.currentTimeMillis()));
    	float seconds = calendar.get(Calendar.SECOND);
    	
    	float y=0;
		if(seconds>=30){
			y = ((seconds*-1)+60)/60 ; // 30...0
		}else{
			y = seconds/60 ;//0...30
		}
		
    	return 10 + y*30; //10..15..20..25..20..15..10
    }
	
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}
}
