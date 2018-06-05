package de.uniol.inf.is.odysseus.wrapper.fnirs.physicaloperator;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter.DigitalFilter;
import de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter.FilterLoop;

abstract class TupleAttributeFilter
{
	public abstract Object filter(Object obj);
}

class DoubleAttributeFilter extends TupleAttributeFilter
{
	FilterLoop loop;
	
	public DoubleAttributeFilter(DigitalFilter filter)
	{
		loop = new FilterLoop(filter);
	}
	
	@Override
	public Object filter(Object obj)
	{
		return loop.filterStep((Double) obj);
	}
}

class ByteBufferAttributeFilter extends TupleAttributeFilter
{
	private final int bytesPerSample;
	private FilterLoop loop;
	
	public ByteBufferAttributeFilter(DigitalFilter filter, int bytesPerSample)
	{
		loop = new FilterLoop(filter);
		
		if (bytesPerSample != 2 || bytesPerSample != 4)
			throw new IllegalArgumentException("Only 2 or 4 bytes per sample allowed!");
		
		this.bytesPerSample = bytesPerSample;
	}
	
	@Override
	public Object filter(Object obj)
	{
		ByteBuffer buf = (ByteBuffer) obj;
		
		int len = buf.limit(), pos = 0;
		
		switch (bytesPerSample)
		{
			case 2:
			{
				while (pos <= len-2)
				{
					buf.putShort(pos, (short) loop.filterStep(buf.getShort(pos)));
					pos += 2;
				}
				break;
			}
			
			case 3:
			{
				break;
			}
			
			case 4:
			{
				while (pos <= len-4)
				{
					buf.putInt(pos, (int) loop.filterStep(buf.getInt(pos)));
					pos += 4;
				}
				break;
			}			
		}
		
		return buf;
	}
}

class VectorAttributeFilter extends TupleAttributeFilter
{
	DigitalFilter filter;
	FilterLoop[] loops;
	
	public VectorAttributeFilter(DigitalFilter filter)
	{
		this.filter = filter;
	}
	
	@Override
	public Object filter(Object obj)
	{
		double[] vector = (double[]) obj;
		
		if (loops == null)
		{
			loops = new FilterLoop[vector.length];
			for (int i=0;i<vector.length;i++)
				loops[i] = new FilterLoop(filter);
		}
		else
			if (loops.length != vector.length)
				throw new IllegalArgumentException("Vector length may not change!");
			
		for (int i=0;i<loops.length;i++)
			vector[i] = loops[i].filterStep(vector[i]);
		
		return vector;
	}
}

public class RelationalDigitalFilterPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> 
{
	static Logger LOG = LoggerFactory.getLogger(SenderPO.class);

	private DigitalFilter filter;
	
	private Map<Integer, TupleAttributeFilter> attributeFilters = new HashMap<>();
	private int[] attributes;
	private int byteBufferSampleDepth;	
	
	public RelationalDigitalFilterPO() 
	{
	}

	public RelationalDigitalFilterPO(RelationalDigitalFilterPO<T> other) 
	{
		super(other);
		filter = other.filter;
		attributeFilters = other.attributeFilters;
		attributes = other.attributes;
		byteBufferSampleDepth = other.byteBufferSampleDepth;
	}
	
	public void setByteBufferSampleDepth(int byteBufferSampleDepth)
	{
		this.byteBufferSampleDepth = byteBufferSampleDepth;
	}

	public void setFilter(DigitalFilter filter, int[] attributes, SDFSchema inputSchema)
	{
		this.filter = filter;
		
		if (attributes == null)
		{
			attributes = new int[inputSchema.size()];
			for (int i=0;i<attributes.length;i++)
				attributes[i] = i;
		}
		
		this.attributes = attributes;
		
		for (int idx : attributes)
		{
			if (inputSchema.get(idx).getDatatype() == SDFDatatype.DOUBLE)
				attributeFilters.put(idx, new DoubleAttributeFilter(filter));
			else
			if (inputSchema.get(idx).getDatatype() == SDFDatatype.BYTEBUFFER)
				attributeFilters.put(idx, new ByteBufferAttributeFilter(filter, byteBufferSampleDepth / 8));
			else				
			if (inputSchema.get(idx).getDatatype() == SDFDatatype.VECTOR_DOUBLE)
				attributeFilters.put(idx, new VectorAttributeFilter(filter));
			else
				throw new RuntimeException("Data type " + inputSchema.get(idx).getDatatype().getQualName() + " not supported for digital filter operator");
		}
	}
	
	public DigitalFilter getFilter()
	{
		return filter;
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
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator operator) {
		if (!(operator instanceof RelationalDigitalFilterPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		RelationalDigitalFilterPO<T> filterPO = (RelationalDigitalFilterPO<T>) operator;

		if (!filter.equals(filterPO.filter)) return false;
		if (!attributes.equals(filterPO.attributes)) return false;
		
		return true;
	}

	@Override
	protected void process_next(Tuple<T> object, int port) 
	{
		for (int idx : attributes)
		{
			object.setAttribute(idx, attributeFilters.get(idx).filter(object.getAttribute(idx)));
		}
		
		transfer(object);
	}	
}

