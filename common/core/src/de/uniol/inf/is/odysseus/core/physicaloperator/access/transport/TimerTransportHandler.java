/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.command.ICommandProvider;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimerTransportHandler extends AbstractPushTransportHandler implements ICommandProvider {
    public static final String NAME = "Timer";
	public static final String PERIOD = "period";
    public static final String TIMEFROMSTART = "timefromstart";
    private Timer timer = null;
    private long period = 1000l;
    private boolean timeFromStart = true;
    private long start = 0;

    /**
 * 
 */
    public TimerTransportHandler() {
        super();
    }

    public TimerTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
        super(protocolHandler, options);
        
        options.checkRequiredException(PERIOD);
        
        period = options.getLong(PERIOD, 0);
        timeFromStart = options.getBoolean(TIMEFROMSTART, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final TimerTransportHandler handler = new TimerTransportHandler(protocolHandler, options);
        return handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
    	if (timeFromStart)
    		start = System.currentTimeMillis();
    	else
    		start = 0;
    	
        fireOnConnect();
        startTimer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
    	stopTimer();
        fireOnDisconnect();
    }

    private void startTimer() {
        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                final ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / 8);
                buffer.putLong(System.currentTimeMillis() - start);
                TimerTransportHandler.this.fireProcess(buffer);
            }
        };        
        timer.schedule(task, 0, this.period);    	
    }
    
    private void stopTimer() {
        timer.cancel();
        timer.purge();
        timer = null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof TimerTransportHandler)) {
            return false;
        }
        final TimerTransportHandler other = (TimerTransportHandler) o;
        if (this.period != other.period) {
            return false;
        }

        return true;
    }
    
	@Override
	public Command getCommandByName(String commandName, SDFSchema schema) 
	{
		switch (commandName)
		{
			case "setperiod":
			{
				final int periodAttribute = schema.findAttributeIndexException("Period");
				
				return new Command()
				{
					@Override public void run(IStreamObject<?> input) 
					{
						long period;

						if (input instanceof Tuple<?>)
						{
							Tuple<?> tuple = (Tuple<?>) input;								 			
							period = ((Number) tuple.getAttribute(periodAttribute)).longValue();
						}
						else
						if (input instanceof KeyValueObject)
						{
							KeyValueObject<?> kv = (KeyValueObject<?>)input;
							period = ((Number) kv.getAttribute("period")).longValue();
						}
						else
							throw new IllegalArgumentException("Cannot execute command on input type " + input.getClass().getName());

						TimerTransportHandler.this.setPeriod(period);
					}
				};
			}
			
 		default: return null;
		}
	}

	public void setPeriod(long period) {
		this.period = period;
		
		if (timer != null)
		{
			stopTimer();
			startTimer();
		}
	}

}
