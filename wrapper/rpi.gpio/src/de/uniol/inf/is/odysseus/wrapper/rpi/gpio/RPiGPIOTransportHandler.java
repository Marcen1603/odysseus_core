package de.uniol.inf.is.odysseus.wrapper.rpi.gpio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
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

	private String pin = "";
	private String mode = "in";
	private String pullState = "low";

	private GpioController gpioController;
	
	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		RPiGPIOTransportHandler tHandler = new RPiGPIOTransportHandler();
		protocolHandler.setTransportHandler(tHandler);
		this.init(options);
		
		initGPIO();
		
		return tHandler;
	}
	
	protected void init(final OptionMap options) {
		if (options.containsKey(RPiGPIOTransportHandler.PIN)) {
            this.pin = options.get(RPiGPIOTransportHandler.PIN);
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
	
	@Override
	public Tuple<?> getNext() {
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(2, false);
		
		boolean value=false;
		
		if(this.gpioController!=null){
			try{
	            GpioPinDigitalInput myButton = this.gpioController.provisionDigitalInputPin(RaspiPin.GPIO_07,
	                                                                         "MyButton");
	            //PinState ps = myButton.getState();
	            boolean buttonPressed = myButton.isHigh();
	            
	        	String buttonState = "";
	            if(buttonPressed){
	            	buttonState = "1";
	            }else{
	            	buttonState = "0";
	            }
	            
	        	tuple.setAttribute(0, pin);
	        	tuple.setAttribute(1, buttonState);
	        	
	        	value=true;
			}catch(Exception ex){
				tuple.setAttribute(0, "error");
	        	tuple.setAttribute(1, "On Raspberry Pi? pi4j installed? pin:"+pin+" mode:"+mode+" pullState:"+pullState);
	        	value=true;
			}
		}else{
			initGPIO();
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
	
	private void initGPIO() {
		try{
			this.gpioController = GpioFactory.getInstance();
		}catch(NullPointerException ex){
			if(!initNullPointerException){
				ex.printStackTrace();
				initNullPointerException=true;
			}
		}catch(Exception ex){
			if(!initException){
				ex.printStackTrace();
				initException=true;
			}
		}catch (UnsatisfiedLinkError ex) {
			if(!initUnsatisfiedLinkError){
				ex.printStackTrace();
				initUnsatisfiedLinkError=true;
			}
		}
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}
}
