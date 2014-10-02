package de.uniol.inf.is.odysseus.wrapper.rpi.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class RPiGPIOTransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>> {

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
	
	@Override
	public Tuple<?> getNext() {
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(2, false);
		
		boolean value=false;
		
		if(this.gpioController!=null){
			try{	
	        	// provision gpio pin #02 as an input pin with its internal pull down resistor enabled
	            // (configure pin edge to both rising and falling to get notified for HIGH and LOW state
	            // changes)
	            GpioPinDigitalInput myButton = this.gpioController.provisionDigitalInputPin(RaspiPin.GPIO_07,             // PIN NUMBER
	                                                                         "MyButton",                   // PIN FRIENDLY NAME (optional)
	                                                                         PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)
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
	        	tuple.setAttribute(1, "On Raspberry? pi4j installed? pin:"+pin+" mode:"+mode+" pullState:"+pullState);
	        	value=true;
			}
		}else{
			//initGPIO();
		}
        
        if(!value){
        	tuple.setAttribute(0, "error2");
        	tuple.setAttribute(1, "On Raspberry? pi4j installed? pin:"+pin+" mode:"+mode+" pullState:"+pullState);
        }
        
		return tuple;
	}
	
	private void initGPIO() {
		if(this.gpioController==null){
			try{
				this.gpioController = GpioFactory.getInstance();
			}catch(NullPointerException ex){
				ex.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}catch (UnsatisfiedLinkError ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
