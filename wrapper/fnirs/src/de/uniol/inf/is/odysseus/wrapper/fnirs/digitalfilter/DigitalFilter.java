package de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter;

public class DigitalFilter 
{
	public static final double EPS = 1e-10;
	
	static Complex bessel_poles[] =
		  { /* table produced by /usr/fisher/bessel --	N.B. only one member of each C.Conj. pair is listed */
		    new Complex(-1.000000e+00,  0.000000e+00),	 new Complex( -8.660254e-01, -5.000000e-01 ),    new Complex( -9.416000e-01,  0.000000e+00 ),
		    new Complex( -7.456404e-01, -7.113666e-01 ), new Complex( -9.047588e-01, -2.709187e-01 ),    new Complex( -6.572112e-01, -8.301614e-01 ),
		    new Complex( -9.264421e-01,  0.000000e+00 ), new Complex( -8.515536e-01, -4.427175e-01 ),    new Complex( -5.905759e-01, -9.072068e-01 ),
		    new Complex( -9.093907e-01, -1.856964e-01 ), new Complex( -7.996542e-01, -5.621717e-01 ),    new Complex( -5.385527e-01, -9.616877e-01 ),
		    new Complex( -9.194872e-01,  0.000000e+00 ), new Complex( -8.800029e-01, -3.216653e-01 ),    new Complex( -7.527355e-01, -6.504696e-01 ),
		    new Complex( -4.966917e-01, -1.002509e+00 ), new Complex( -9.096832e-01, -1.412438e-01 ),    new Complex( -8.473251e-01, -4.259018e-01 ),
		    new Complex( -7.111382e-01, -7.186517e-01 ), new Complex( -4.621740e-01, -1.034389e+00 ),    new Complex( -9.154958e-01,  0.000000e+00 ),
		    new Complex( -8.911217e-01, -2.526581e-01 ), new Complex( -8.148021e-01, -5.085816e-01 ),    new Complex( -6.743623e-01, -7.730546e-01 ),
		    new Complex( -4.331416e-01, -1.060074e+00 ), new Complex( -9.091347e-01, -1.139583e-01 ),    new Complex( -8.688460e-01, -3.430008e-01 ),
		    new Complex( -7.837694e-01, -5.759148e-01 ), new Complex( -6.417514e-01, -8.175836e-01 ),    new Complex( -4.083221e-01, -1.081275e+00 ),
		  };	
	
	public enum PassType
	{
		LOWPASS, HIGHPASS, BANDPASS
	};
	
	public enum FilterType
	{
		BUTTERWORTH, BESSEL, CHEBYCHEV
	};
	
	public final FilterType filterType;
	public final PassType passType;
	public final int order;
	public final double raw_alpha1, raw_alpha2, ripple;
	
	private Complex[] sPoles;
	private int numPoles;
	private double warped_alpha1;
	private double warped_alpha2;
	private Complex[] zPoles;
	private Complex[] zZeros;
	private Complex dc_gain;
	private Complex fc_gain;
	private Complex hf_gain;
	private double[] xcoeffs;
	private double[] ycoeffs;
	
	public DigitalFilter(FilterType filterType, PassType passType, int order, double alpha1, double alpha2, double chebychevRipple, boolean noPreWarp)
	{
		this.filterType = filterType;
		this.passType = passType;
		this.order = order;
		
		this.raw_alpha1 = alpha1;
		
		if (passType != PassType.BANDPASS)
			this.raw_alpha2 = alpha1;
		else
			this.raw_alpha2 = alpha2;
			
		if (filterType == FilterType.CHEBYCHEV && chebychevRipple >= 0.0)
			throw new IllegalArgumentException("Chebychev ripple attenuation must be lower than 0!");
	
		this.ripple = chebychevRipple;
		
		compute_s();
		normalize(noPreWarp);
	    compute_z();
	    expandpoly();		
	}
	
	public Complex getDCGain()
	{
		return dc_gain;
	}
	
	public Complex getCenterGain()
	{
		return fc_gain;
	}
	
	public Complex getHFGain()
	{
		return hf_gain;
	}
	
	public int getNumPoles()
	{
		return numPoles;
	}
	
	public double[] getXCoefficients()
	{
		return xcoeffs;
	}
	
	public double[] getYCoefficients()
	{
		return ycoeffs;
	}
	
	private static double asinh(double x) 
	{ 
		return Math.log(x + Math.sqrt(x*x + 1.0)); 
	} 		
	
	private void choosepole(Complex z)
	{ 
		if (z.re < 0.0)
			sPoles[numPoles++] = z;
	}	
	
	private void compute_s()
	{
		int polesFactor = passType == PassType.BANDPASS ? 2 : 1;
		
		numPoles = 0;
		
		switch (filterType)
		{
			case BESSEL:
			{
				sPoles = new Complex[order * polesFactor];
				
		    	int i;
		    	int p = (order*order)/4; /* ptr into table */
		    	if ((order & 1) != 0) 
		    		choosepole(bessel_poles[p++]);
		    	
		    	for (i=0; i < order/2; i++)
		    	{ 
		    		choosepole(bessel_poles[p]);
		    		choosepole(bessel_poles[p].conj());
		    		p++;
		    	}				
		    	break;
			}
			
			case BUTTERWORTH:
			case CHEBYCHEV:
			{
				sPoles = new Complex[2*order*polesFactor];
				
		    	for (int i=0; i < 2*order; i++)
		    	{ 
		    		Complex s = new Complex();
		    		s.re = 0.0; s.im = ((order & 1) != 0) ? (i*Math.PI) / order : ((i+0.5)*Math.PI) / order;
		    		choosepole(s.exp());
		    	}
		    	
		    	if (filterType == FilterType.CHEBYCHEV)
		    	{
		    		/* modify for Chebyshev (p. 136 DeFatta et al.) */
			    	double rip = Math.pow(10.0, -ripple / 10.0);
			    	double eps = Math.sqrt(rip - 1.0);
			    	double y = asinh(1.0 / eps) / order;
			    	if (y <= 0.0)
			    		throw new IllegalArgumentException("Chebyshev y = " + y + ", must be greter than 0");
			    	
			    	for (int i=0; i < numPoles; i++)
			    	{ 
			    		sPoles[i].re *= Math.sinh(y);
			    		sPoles[i].im *= Math.cosh(y);
			    	}		    		
		    	}		    	
		    	break;
			}
		}
	}
	
	private void normalize(boolean noPreWarp)
	{ 
		int i;
	    /* for bilinear transform, perform pre-warp on alpha values */
	    if (noPreWarp)
	    { 
	    	warped_alpha1 = raw_alpha1;
	    	warped_alpha2 = raw_alpha2;
	    }
	    else
	    { 
	    	warped_alpha1 = Math.tan(Math.PI * raw_alpha1) / Math.PI;
	    	warped_alpha2 = Math.tan(Math.PI * raw_alpha2) / Math.PI;
	    }
	    
		Complex w1 = new Complex(2*Math.PI * warped_alpha1, 0.0);
		Complex w2 = new Complex(2*Math.PI * warped_alpha2, 0.0); 	    
	    
	    /* transform prototype into appropriate filter type (lp/hp/bp) */
	    switch (passType)
	    { 
		    case LOWPASS:
		    {
			    for (i=0; i < numPoles; i++) sPoles[i] = Complex.mul(sPoles[i], w1);
			    break;
		    }
	
			case HIGHPASS:
			{
			    for (i=0; i < numPoles; i++) sPoles[i] = Complex.div(w1, sPoles[i]);
			    /* also N zeros at (0,0) */
			    break;
			}
	
			case BANDPASS:
			{ 
			    Complex w0 = Complex.mul(w1, w2).sqrt();
			    Complex bw = Complex.sub(w2, w1);
			    for (i=0; i < numPoles; i++)
			    { 
			    	Complex hba = Complex.mul(Complex.HALF, Complex.mul(sPoles[i], bw));
			    	Complex temp = Complex.div(w0, hba);
			    	temp = Complex.sub(Complex.ONE, Complex.mul(temp, temp)).sqrt();
			    	sPoles[i] = Complex.mul(hba, Complex.add(Complex.ONE, temp));
			    	sPoles[numPoles+i] = Complex.mul(hba, Complex.sub(Complex.ONE, temp));
			    }
			    /* also N zeros at (0,0) */
			    numPoles *= 2;
			    break;
			}
	    }
	}	
	
	private void compute_z() /* given S-plane poles, compute Z-plane poles */
	{ 		
		zPoles = new Complex[numPoles];
		zZeros = new Complex[numPoles];
	    for (int i=0; i < numPoles; i++)
	    { /* use bilinear transform */
	    	Complex top = Complex.add(Complex.TWO, sPoles[i]);
	    	Complex bot = Complex.sub(Complex.TWO, sPoles[i]);
	    	zPoles[i] = Complex.div(top, bot);
	    	switch (passType)
	    	{ 
	    	case LOWPASS:	zZeros[i] = Complex.MINUS_ONE; break;
		    case HIGHPASS:	zZeros[i] = Complex.ONE; break;
		    case BANDPASS:	zZeros[i] = ((i & 1) != 0) ? Complex.ONE : Complex.MINUS_ONE; break;
	    	}
	    }
	}
	
	private void expandpoly() /* given Z-plane poles & zeros, compute top & bot polynomials in Z, and then recurrence relation */
	{ 
		Complex[] topcoeffs = new Complex[numPoles+1];
		Complex[] botcoeffs = new Complex[numPoles+1];
	    expand(zZeros, topcoeffs);
	    expand(zPoles, botcoeffs);
	    
	    dc_gain = Complex.evaluate(topcoeffs, botcoeffs, numPoles, Complex.ONE);
	    Complex st = new Complex(0.0, 2*Math.PI * 0.5 * (raw_alpha1 + raw_alpha2)); /* "jwT" for centre freq. */
	    fc_gain = Complex.evaluate(topcoeffs, botcoeffs, numPoles, st.exp());
	    hf_gain = Complex.evaluate(topcoeffs, botcoeffs, numPoles, Complex.MINUS_ONE);
	    
	    xcoeffs = new double[numPoles+1];
	    ycoeffs = new double[numPoles+1];
	    for (int i=0; i <= numPoles; i++)
	    { 
	    	xcoeffs[i] = topcoeffs[i].re / botcoeffs[numPoles].re;
	    	ycoeffs[i] = -(botcoeffs[i].re / botcoeffs[numPoles].re);
	    }
	}

	private void expand(Complex[] pz, Complex[] coeffs)
	{ /* compute product of poles or zeros as a polynomial of z */
		int i;
	    coeffs[0] = Complex.ONE;
	    for (i=0; i < numPoles; i++) coeffs[i+1] = Complex.ZERO;
	    for (i=0; i < numPoles; i++) multin(pz[i], coeffs);
	    /* check computed coeffs of z^k are all real */
	    for (i=0; i < numPoles+1; i++)
	    { 
	    	if (Math.abs(coeffs[i].im) > EPS)
	    		throw new ArithmeticException(String.format("Coefficient of z^%d is not real. Poles are not complex conjugates", i));
	    }
	}

	private void multin(Complex w, Complex[] coeffs)
	{ /* multiply factor (z-w) into coeffs */
	    Complex nw = w.neg();
	    for (int i=numPoles; i >= 1; i--)
	    	coeffs[i] = Complex.add(Complex.mul(nw, coeffs[i]), coeffs[i-1]);
	    coeffs[0] = Complex.mul(nw, coeffs[0]);
	}
	
	@Override
	public String toString()
	{
		return String.format("DigitalFilter{filter=%s, pass=%s, order=%d, raw_alpha1=%f, raw_alpha2=%f, ripple=%f}", filterType.toString(), passType.toString(), order, raw_alpha1, raw_alpha2, ripple);
		
	}
}
