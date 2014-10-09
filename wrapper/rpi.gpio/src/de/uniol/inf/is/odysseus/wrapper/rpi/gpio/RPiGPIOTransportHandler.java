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

	private GpioController gpioController;
	private Pin _pin = RaspiPin.GPIO_07;
	
	
	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		RPiGPIOTransportHandler tHandler = new RPiGPIOTransportHandler();
		protocolHandler.setTransportHandler(tHandler);
		this.init(options);
		
		LOG.debug("RPiGPIOTransportHandler createInstance");
		
		myinitGPIO();
		
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
	
	private boolean errorPi4J = false;
	private boolean initNullPointerException = false;
	private boolean initException = false;
	private boolean initUnsatisfiedLinkError = false;

	private boolean getStateException = false;

	private GpioPinDigitalInput myButton = null;

	private boolean initMustCalled = false;
	
	@Override
	public Tuple<?> getNext() {
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(2, false);
		
		boolean value=false;
		
		if(this.gpioController!=null && this.myButton!=null){
			try{
	            boolean buttonPressed = this.myButton.isHigh();
	            
	        	String buttonState = "";
	            if(buttonPressed){
	            	buttonState = "1";
	            }else{
	            	buttonState = "0";
	            }
	            
	        	tuple.setAttribute(0, _pin.getName());
	        	tuple.setAttribute(1, buttonState);
	        	
	        	value=true;
			}catch(Exception ex){
				if(!getStateException){
					LOG.debug("rpi gpio getNext error: ", ex);
					
					LOG.debug("Stack trace: ");
					ex.printStackTrace();
					
					getStateException=true;
				}
				LOG.debug("rpi gpio getNext error: ", ex);
				
				LOG.debug("Stack trace: ");
				ex.printStackTrace();
				
				
				System.out.println("--------------");
				System.out.println("exception: "+ex);
				System.out.println("exception message: "+ex.getMessage());
				
				System.out.println("exception stacktrace: ");
				for(int i=ex.getStackTrace().length;i<ex.getStackTrace().length;i++){
					System.out.println(""+ex.getStackTrace()[i]);
				}
				
				tuple.setAttribute(0, "error");
	        	tuple.setAttribute(1, "On Raspberry Pi? pi4j installed? pin:"+_pin.toString()+" mode:"+mode+" pullState:"+pullState + " message:"+ex.getMessage()+ " stacktrace:"+ex.getStackTrace().toString());
	        	
	        	value=true;
			}
		}else{
			
			tuple.setAttribute(0, "errorINIT");
        	tuple.setAttribute(1, "initGPIO must called");
        	
        	if(!initMustCalled){
        		LOG.error("initGPIO must called");
        		initMustCalled=true;
        	}
        	
        	myinitGPIO();
			
        	
        	return tuple;
		}
        
        if(!value && !errorPi4J){
        	if(!errorPi4J){
        		LOG.error("RPi GPIO TransportHandler runs on Raspberry Pi only. Do you installed pi4j?");
        		errorPi4J = true;
        	}
        	//return null;
        	tuple.setAttribute(0, "error3");
        	tuple.setAttribute(1, "On Raspberry Pi? pi4j installed?");
        }
        
		return tuple;
	}
	
	private void myinitGPIO() {
		LOG.error("initGPIO() is called.");
		
		try{
			initGPIOController();
			LOG.error("initGPIOController() without exception :-)");
			
			
			initGPIOPin();
			LOG.error("initGPIOPin() without exception :-)");
			
			
		}catch(NullPointerException ex){
			if(!initNullPointerException){
				ex.printStackTrace();
				LOG.error("", ex);
				initNullPointerException=true;
			}
		}catch(Exception ex){
			if(!initException){
				ex.printStackTrace();
				LOG.error("", ex);
				initException=true;
			}
		}catch (UnsatisfiedLinkError ex) {
			if(!initUnsatisfiedLinkError){
				ex.printStackTrace();
				LOG.error("", ex);
				initUnsatisfiedLinkError=true;
			}
		}
	}

	private void initGPIOPin() {
		if(this.myButton==null && this.gpioController!=null){
			this.myButton = this.gpioController.provisionDigitalInputPin(_pin,"MyButton");
		}
	}

	private void initGPIOController() {
		if(this.gpioController==null){
			this.gpioController = GpioFactory.getInstance();
		}
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}
}
