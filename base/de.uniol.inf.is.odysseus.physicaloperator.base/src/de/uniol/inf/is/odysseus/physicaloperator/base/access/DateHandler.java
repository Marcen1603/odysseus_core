package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Used for getting DATE attributes out of a streams.
 * Handles these attributes as simple long values, because of
 * faster processing.
 * 
 * @author Andre Bolles
 *
 */
public class DateHandler extends AbstractAtomicDataHandler {

	@Override
	public final Object readData() throws IOException {
		return getStream().readLong();
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		long l = buffer.getLong();
		//System.out.println("read Long Data: "+l);
		return l;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Long Data "+((Number)data).longValue());
		buffer.putLong(((Number)data).longValue());
	}
	
	

}
