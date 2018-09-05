package de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter;

/* mkfilter -- given n, compute recurrence relation
to implement Butterworth, Bessel or Chebyshev filter of order n
A.J. Fisher, University of York   <fisher@minster.york.ac.uk>
September 1992 */

public class Complex 
{
	public double re;
	public double im;
	
	public Complex()
	{
		this(0.0, 0.0);
	}
	
	public Complex(double re, double im)
	{
		this.re = re;
		this.im = im;
	}
	
	public static Complex evaluate(Complex topco[], Complex botco[], int np, Complex z)
	{ 
		// evaluate response, substituting for z
	    return div(eval(topco, np, z), eval(botco, np, z));
	}

	public static Complex eval(Complex coeffs[], int np, Complex z)
	{ 
		// evaluate polynomial in z, substituting for z
		Complex sum = new Complex();
	    int i;
	    sum = new Complex();
	    for (i=np; i >= 0; i--) sum = add(mul(sum, z), coeffs[i]);
	    return sum;
	}

	private static double mySqrt(double x)
	{ /* because of deficiencies in hypot on Sparc, it's possible for arg of Xsqrt to be small and -ve,
	       which logically it can't be (since r >= |x.re|).	 Take it as 0. */
		return (x >= 0.0) ? Math.sqrt(x) : 0.0;
	}	
	
	public Complex sqrt()
	{ 
	    double r = magnitude();
	    return new Complex(mySqrt(0.5 * (r + re)), Math.abs(mySqrt(0.5 * (r - re))));
	}
	
	public double magnitude()
	{
		return Math.hypot(im, re);
	}

	public Complex exp()
	{ 
		double r = Math.exp(re);
	    return new Complex(r * Math.cos(im), r * Math.sin(im));
	}

	public Complex conj()
	{ 
		return new Complex(re, -im);
	}

	public static Complex add(Complex x, Complex y)
	{ 
		Complex z = new Complex();
	    z.re = x.re + y.re;
	    z.im = x.im + y.im;
	    return z;
	  }

	public static Complex sub(Complex x, Complex y)
	{ 
		Complex z = new Complex();
	    z.re = x.re - y.re;
	    z.im = x.im - y.im;
	    return z;
	}

	public static Complex mul(Complex x, Complex y)
	{ 
		Complex z = new Complex();
	    z.re = (x.re*y.re - x.im*y.im);
	    z.im = (x.im*y.re + x.re*y.im);
	    return z;
	}

	public static Complex div(Complex x, Complex y)
	{ 
		Complex z = new Complex();
	    double mag = y.re*y.re + y.im*y.im;
	    z.re = (x.re*y.re + x.im*y.im) / mag;
	    z.im = (x.im*y.re - x.re*y.im) / mag;
	    return z;
	}

	public Complex neg()
	{
		return new Complex(-re, -im);
	}
	
	static public final Complex MINUS_ONE = new Complex(-1.0, 0.0);
	static public final Complex ZERO = new Complex( 0.0, 0.0);
	static public final Complex ONE  = new Complex( 1.0, 0.0);
	static public final Complex TWO  = new Complex( 2.0, 0.0);
	static public final Complex HALF = new Complex( 0.5, 0.0);	
}
