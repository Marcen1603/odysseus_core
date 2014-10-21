package de.uniol.inf.is.odysseus.wrapper.fnirs.udf;

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
@UserDefinedFunction(name = "DigitalFilter")
public class DigitalFilterUDF implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> 
{
	private static final long serialVersionUID = 4077769822100028025L;
	private int pos;
	
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
			pos = Integer.parseInt(initString);
	}
	
	private final int 	 NZEROS = 4;
	private final int 	 NPOLES = 4;
	private final double GAIN   = 1.177840024e+00;

	private final double[] xv = new double[NZEROS+1];
	private final double[] yv = new double[NPOLES+1];

	private double doFilter(double input)
	{ 
		xv[0] = xv[1]; xv[1] = xv[2]; xv[2] = xv[3]; xv[3] = xv[4]; 
		xv[4] = input / GAIN;
		yv[0] = yv[1]; yv[1] = yv[2]; yv[2] = yv[3]; yv[3] = yv[4]; 
		yv[4] =   (xv[0] + xv[4]) + 4 * (xv[1] + xv[3]) + 6 * xv[2]
	                     + ( -0.7208209234 * yv[0]) + ( -3.1190000182 * yv[1])
	                     + ( -5.0713777302 * yv[2]) + ( -3.6729890606 * yv[3]);
		return yv[4];
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) 
	{
		double result = doFilter((Double)in.getAttribute(pos));
		
		in.setAttribute(pos, result);
		
		return in;
	}
	
	/**
	 * Returns output mode of this class.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return OutputMode Output mode.
	 */
	@Override public OutputMode getOutputMode() 
	{
        return OutputMode.MODIFIED_INPUT;
    }
}
