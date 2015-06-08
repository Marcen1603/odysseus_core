package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;

public class SetSystemTimePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> 
{
	static Logger LOG = LoggerFactory.getLogger(SenderPO.class);
	private int threshold;

	public SetSystemTimePO(int threshold) 
	{
		this.threshold = threshold;
	}

	public SetSystemTimePO(SetSystemTimePO<T> other) 
	{
		super(other);
		this.threshold = other.threshold;
	}

	@Override
	protected void process_next(T object, int port) 
	{
        TimeInterval timeStamp = (TimeInterval)object.getMetadata();
        if (timeStamp != null)
        {
        	long elementTime = timeStamp.getStart().getMainPoint();
        	long systemTime = System.currentTimeMillis();

        	long diff = elementTime - systemTime;
        	if (Math.abs(diff) > threshold)
        	{
        		System.out.println("Time diff too big! " + diff + "ms. TODO: Set system time");

/*        		Date d = new Date();
        		
        		String dateToSet = new SimpleDateFormat("dd-MM-yy").format(d);
        		String timeToSet = new SimpleDateFormat("hh:mm:ss").format(d);
        		
        		try 
        		{
					Runtime.getRuntime().exec("cmd /C date " + dateToSet); // dd-MM-yy
					Runtime.getRuntime().exec("cmd /C time " + timeToSet); // hh:mm:ss
				} catch (IOException e) {
					e.printStackTrace();
				}*/ 
        		        		
        	}
        	else
        		System.out.println("Time diff ok. " + diff + "ms.");
        }
		
		transfer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) 
	{
		sendPunctuation(punctuation);
	}

	@Override
	protected void process_open() throws OpenFailedException 
	{
		if (!isOpen()) 
		{
			super.process_open();
		}
	}

	@Override
	protected void process_close() 
	{
		if (isOpen()) 
		{
			super.process_close();
		}
	}

	@Override
	public OutputMode getOutputMode() 
	{
		return OutputMode.INPUT;
	}
	
}

