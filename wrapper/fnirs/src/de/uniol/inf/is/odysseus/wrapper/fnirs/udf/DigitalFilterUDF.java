package de.uniol.inf.is.odysseus.wrapper.fnirs.udf;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

/**
 * Digital filter operator.
 * 
 * @author Henrik Surm
 */

interface IFilter
{
	public double doFilter(double input);
}

class Filter2 implements IFilter
{
	// Order = 6, Sampling rate = 6.??Hz, Cutoff Freq = 0.08Hz
	private final int 	 NZEROS = 6;
	private final int 	 NPOLES = 6;
	private final double GAIN   = 9.070781465e+06;

	private final double[] xv = new double[NZEROS+1];
	private final double[] yv = new double[NPOLES+1];

	@Override public double doFilter(double input)
	{ 
		xv[0] = xv[1]; xv[1] = xv[2]; xv[2] = xv[3]; xv[3] = xv[4]; xv[4] = xv[5]; xv[5] = xv[6]; 
	    xv[6] = input / GAIN;
	    yv[0] = yv[1]; yv[1] = yv[2]; yv[2] = yv[3]; yv[3] = yv[4]; yv[4] = yv[5]; yv[5] = yv[6]; 
	    yv[6] =   (xv[0] + xv[6]) + 6 * (xv[1] + xv[5]) + 15 * (xv[2] + xv[4])
	    		+ 20 * xv[3]
	    		+ ( -0.5710336809 * yv[0]) + (  3.7457469236 * yv[1])
	    		+ (-10.2538608560 * yv[2]) + ( 14.9952062960 * yv[3])
	            + (-12.3564755620 * yv[4]) + (  5.4404098241 * yv[5]);
	    return yv[6];
	}	
}

class Filter implements IFilter
{
	// Order = 6, Sampling rate = 20.83Hz, Cutoff Freq = 0.08Hz
	private final int 	 NZEROS = 6;
	private final int 	 NPOLES = 6;
	private final double GAIN   = 3.394859675e+11;

	private final double[] xv = new double[NZEROS+1];
	private final double[] yv = new double[NPOLES+1];

	@Override public double doFilter(double input)
	{ 
		xv[0] = xv[1]; xv[1] = xv[2]; xv[2] = xv[3]; xv[3] = xv[4]; xv[4] = xv[5]; xv[5] = xv[6]; 
        xv[6] = input / GAIN;
        yv[0] = yv[1]; yv[1] = yv[2]; yv[2] = yv[3]; yv[3] = yv[4]; yv[4] = yv[5]; yv[5] = yv[6]; 
        yv[6] =   (xv[0] + xv[6]) + 6 * (xv[1] + xv[5]) + 15 * (xv[2] + xv[4])
                     + 20 * xv[3]
                     + ( -0.9109757297 * yv[0]) + (  5.5507898485 * yv[1])
                     + (-14.0932818570 * yv[2]) + ( 19.0848614060 * yv[3])
                     + (-14.5381579570 * yv[4]) + (  5.9067642887 * yv[5]);
	    return yv[6];
	}	
}


@UserDefinedFunction(name = "DigitalFilter")
public class DigitalFilterUDF implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>>, Serializable
{
	private static final long serialVersionUID = 4077769822100028025L;
	private int index;
	private IFilter[] filters;
	
	public DigitalFilterUDF() 
	{		 
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(String initString) 
	{
		if (initString != null)
			index = Integer.parseInt(initString);
		else
			index = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) 
	{
		Object attribute = in.getAttribute(index);
		
		if (attribute instanceof double[])
		{
			double[] values = (double[]) attribute;
			
			int num = values.length;
			
			if (filters == null)
			{
				filters = new IFilter[num];
				for (int i=0;i<num;i++)
					filters[i] = new Filter2();
			}
			
			for (int i=0;i<num;i++)
			{
				values[i] = filters[i].doFilter(values[i]);
			}
		}
		
		if (attribute instanceof Double)
		{
			if (filters == null)
			{
				filters = new IFilter[1];
				filters[0] = new Filter2();
			}
			
			in.setAttribute(index, filters[0].doFilter((Double)attribute));
		}
		
		return in;
	}
	
	@Override public OutputMode getOutputMode() 
	{
        return OutputMode.MODIFIED_INPUT;
    }
}
