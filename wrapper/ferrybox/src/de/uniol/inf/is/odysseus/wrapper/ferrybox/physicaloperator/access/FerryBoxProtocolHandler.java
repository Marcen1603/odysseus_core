/*******************************************************************************
 * LMS1xx protocol handler for the Odysseus data stream management system
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.ferrybox.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Protocol Handler for the SICK protocol supporting LMS100 and LMS151 laser
 * scanner
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class FerryBoxProtocolHandler extends LineProtocolHandler<KeyValueObject<IMetaAttribute>> {
	private static final Logger LOG = LoggerFactory.getLogger(FerryBoxProtocolHandler.class);
	public static final String NAME = "FerryBox";

	/** Commands */
	public static final String START = "header\r\n";
	public static final String DATA = "daten\r\n";
	public static final String STOP = "stop\r\n";
	
	private int interval;
	private Timer timer;

	@Override public String getName() { return FerryBoxProtocolHandler.NAME; }
	
	public FerryBoxProtocolHandler() {
		super();
	}

	public FerryBoxProtocolHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		
		interval = optionsMap.getInt("interval", 1000);
	}	

	@Override
	public IProtocolHandler<KeyValueObject<IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options, IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler) {
		return new FerryBoxProtocolHandler(direction, access, dataHandler, options);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	@Override
	public void onConnect(final ITransportHandler caller) {
		send(START);
	}

	private void send(String message)
	{
		try {
			getTransportHandler().send(message.getBytes(charset));
		} catch (IOException e) {
			FerryBoxProtocolHandler.LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void onDisonnect(ITransportHandler caller)
	{
		timer.cancel();
		timer.purge();
		timer = null;
		
		send(STOP);
	}
	
	@Override
	public void process(long callerId, final ByteBuffer message) 
	{
		String line = new String(message.array(), charset);
		System.out.println(line);

		if (timer == null)
		{
			// The first message received is the header
			// Now start the data timer
			timer = new Timer();
			final TimerTask task = new TimerTask() {
				@Override
				public void run() {
					send(DATA);
				}
			};        
			timer.schedule(task, 0, interval);						
		}
		else
		{		
			// Split received nmea string and fill map
			final String[] lines = line.substring(0, line.length()-4).split(",");
			Map<String, Object> event = new HashMap<>();
			int i=0;
			for (String entry : lines)
			{
				event.put("entry" + i, entry);
			}
			
			KeyValueObject<IMetaAttribute> kvObject = new KeyValueObject<>(event);
			getTransfer().transfer(kvObject);
		}
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return ITransportExchangePattern.InOnly;
	}
	
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> other) {
		if (!(other instanceof FerryBoxProtocolHandler)) return false;
		
		FerryBoxProtocolHandler o = (FerryBoxProtocolHandler) other;
		if (interval != o.interval) return false;
		
		return true;
	}
}
