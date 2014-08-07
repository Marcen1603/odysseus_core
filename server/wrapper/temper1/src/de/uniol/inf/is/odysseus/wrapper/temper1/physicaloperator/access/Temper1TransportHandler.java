package de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator.access;


import java.io.IOException;
import java.util.Map;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class Temper1TransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>>{

	private static final String NAME = "Temper1";
	private static final int VENDOR_ID = 3141;
    private static final int PRODUCT_ID = 29697;
    private static final int BUFSIZE = 2048;
	
	static {
    	ClassPathLibraryLoader.loadNativeHIDLibrary();
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		Temper1TransportHandler tHandler = new Temper1TransportHandler();
		
		protocolHandler.setTransportHandler(tHandler);
		
		return tHandler;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Nach
	 * Quelle:
	 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on-os-x-to-read-temperature-from-the-temper1-sensor/
	 * @throws IOException 
	 * 
	 */
    @SuppressWarnings("unused")
	private static float readDevice() throws IOException {
    	HIDDevice dev;
    	
        HIDManager hid_mgr = HIDManager.getInstance();
        dev = hid_mgr.openById(VENDOR_ID, PRODUCT_ID, null);

        byte[] temp = new byte[] {
            (byte)0x01, (byte)0x80, (byte)0x33, (byte)0x01, 
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
        };
        
        int res = dev.write(temp);
        
        try
        {
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
		Tuple<?> tuple = new Tuple(1, false);
		try {
			tuple.setAttribute(0, readDevice());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tuple;
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
