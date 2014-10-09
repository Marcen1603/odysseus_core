package de.uniol.inf.is.odysseus.wrapper.rpi.gpio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class RPiGPIOTransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>> {

	private static final Logger LOG = LoggerFactory.getLogger(RPiGPIOTransportHandler.class);
	
	private static final String NAME = "RPiGPIO";
	
	private final static String PIN = "pin"; // Pull: down or up
	private final static String MODE = "mode"; // Tranport mode: in or out
	private final static String PULL_STATE = "pull"; // Pull: down or up (high or low)

	//private String pin = "";
	private String mode = "in";
	private String pullState = "low";

	private static GpioController gpioController;
	private Pin _pin = RaspiPin.GPIO_07;

	private long lastErrorTime = 0;
	
	static{
		myinitGPIO();
	}
	
	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		RPiGPIOTransportHandler tHandler = new RPiGPIOTransportHandler();
		protocolHandler.setTransportHandler(tHandler);
		this.init(options);
		
		return tHandler;
	}
	
	protected void init(final OptionMap options) {
		if (options.containsKey(RPiGPIOTransportHandler.PIN)) {
            //this.pin = options.get(RPiGPIOTransportHandler.PIN);
        }
		
		if (options.containsKey(RPiGPIOTransportHandler.MODE)) {
            this.mode = options.get(RPiGPIOTransportHandler.MODE);
        }
		
		if (options.containsKey(RPiGPIOTransportHandler.PULL_STATE)) {
            this.pullState = options.get(RPiGPIOTransportHandler.PULL_STATE);
        }
    }
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}
	
	private static GpioPinDigitalInput myButton;
	
	@Override
	public Tuple<?> getNext() {
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(2, false);
		
		try{
            boolean buttonPressed = myButton.isHigh();
            
        	String buttonState = "";
            if(buttonPressed){
            	buttonState = "1";
            }else{
            	buttonState = "0";
            }
            
        	tuple.setAttribute(0, "");
        	tuple.setAttribute(1, buttonState);
        	
		}catch(Exception ex){
			long deltaErrorTime = System.currentTimeMillis() - lastErrorTime;
			
			if(deltaErrorTime>1000 || lastErrorTime==0){
				LOG.debug("rpi gpio getNext error: ", ex);
				
				LOG.debug("Stack trace: ");
				ex.printStackTrace();
				lastErrorTime=System.currentTimeMillis();
			}
			
			tuple.setAttribute(0, "error");
        	tuple.setAttribute(1, "On Raspberry Pi? pi4j installed? pin:"+_pin.toString()+" mode:"+mode+" pullState:"+pullState + " message:"+ex.getMessage()+ " stacktrace:"+ex.getStackTrace().toString());
		}
		
		return tuple;
	}
	
	private static void myinitGPIO() {
		LOG.error("initGPIO() is called.");
		
		try{
			if(gpioController==null){
				gpioController = GpioFactory.getInstance();
				LOG.error("initGPIOController() without exception :-)");
			}else{
				LOG.error("initGPIOController() ELSE already instantiated!?");
			}
			
			
			if(myButton==null && gpioController!=null){
				myButton = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_07,"MyButton");
				LOG.error("initGPIOPin() without exception :-)");
			}else{
				LOG.error("initGPIOPin() ELSE already instantiated!?");
			}
			
		}catch(NullPointerException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}catch (UnsatisfiedLinkError ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}
}
