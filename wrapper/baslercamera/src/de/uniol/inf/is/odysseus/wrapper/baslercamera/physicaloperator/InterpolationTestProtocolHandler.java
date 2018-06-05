package de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class InterpolationTestProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {

	public static final String NAME = "InterpolationTest";
	
	private BufferedReader reader;
	private int interval;
	private AtomicLong currentTimeStamp;

	private Timer timer;
	
	public InterpolationTestProtocolHandler(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		super(direction, access, dataHandler, options);
		
//		options.checkRequiredException("interval");
		
		interval = options.getInt("interval", 100);
	}
	
	public InterpolationTestProtocolHandler() {
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		return new InterpolationTestProtocolHandler<>(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return true;
	}
	
	@Override
	public void open()
	{
		currentTimeStamp = null;
	}
	
	@Override
	public void close()
	{
		stopTimer();
		currentTimeStamp = null;
	}
	
	private void stopTimer() 
	{
        timer.cancel();
        timer.purge();
        timer = null;
	}

	private void startTimer()
	{
        timer = new Timer();
        TimerTask task = new TimerTask() 
        {
            @Override
            public void run() 
            {
                long time = currentTimeStamp.get();
                InterpolationTestProtocolHandler.this.process(Long.toString(time));
            }
        };
        timer.schedule(task, 0, interval);
		
	}
	
	public void process(String token)
	{
		T retValue = getDataHandler().readData(token);
		getTransfer().transfer(retValue);		
	}
	
	@Override
	public boolean hasNext() throws IOException 
	{
		if (getTransportHandler().getInputStream().available() > 0)
		{
			reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));		
			return true;
		}
		else
		{
			reader = null;
			return false;
		}		 
	}
	
	@Override
	public T getNext() throws IOException 
	{
		String s = reader.readLine();
		
		if (currentTimeStamp == null)
		{
			currentTimeStamp = new AtomicLong(0);
			currentTimeStamp.set(Long.parseLong(s));
			startTimer();
		}
		else
			currentTimeStamp.set(Long.parseLong(s));
		
		return null;
	}
}
