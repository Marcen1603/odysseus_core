package de.uniol.inf.is.odysseus.probabilistic.math.genz;

import java.util.ArrayList;

import org.apache.commons.math3.special.Erf;

import umontreal.iro.lecuyer.util.Num;

public class Util {



	public static double sqrt(double d) {
		return Math.sqrt(d);
	}


	public static double max(double[] ds) {
//		C = max(A) returns the largest elements along different dimensions of an array.
//
//				If A is a vector, max(A) returns the largest element in A.
//
//				If A is a matrix, max(A) treats the columns of A as vectors, 
//				returning a row vector containing the maximum element from each column.
//
//				If A is a multidimensional array, max(A) treats the values along 
//				the first non-singleton dimension as vectors, returning the 
//				maximum value of each vector.
//
//				C = max(A,B) returns an array the same size as A and B with the 
//				largest elements taken from A or B. The dimensions of A and B must 
//				match, or they may be scalar.
//
//				C = max(A,[],dim) returns the largest elements along the dimension 
//				of A specified by scalar dim. For example, max(A,[],1) produces 
//				the maximum values along the first dimension of A.
//
//				[C,I] = max(...) finds the indices of the maximum values of A, 
//				and returns them in output vector I. If there are several 
//				identical maximum values, the index of the first one found is returned.
		
		
		
		double max = 0;
		for(double d : ds){
			if(d > max){
				max = d;
			}
		}
		
		return max;
	}

	public static double log(int i) {
		return Math.log(i);
	}

	public static double phi(double z) {
		return Erf.erfc(-z/sqrt(2))/2;
	}

	public static double sign(double ai) {
		return Math.signum(ai);
	}
	


	public static double phinv(double p) {
		//%
				//%  Standard statistical normal distribution functions
				//%
				//function p =   phi(z), p =  erfc( -z/sqrt(2) )/2;
				//function z = phinv(p), z = norminv( p );
				//% function z = phinv(p), z = -sqrt(2)*erfcinv( 2*p ); % use if no norminv
				//%
				//
		return -Util.sqrt(2)*Num.erfcInv(2*p);
	}

	public static double exp(double v) {
//		Y = exp(X) returns the exponential for each element 
//				of X. exp operates element-wise on arrays. For 
//				complex x + i * y, exp returns the complex 
//				exponential ez = ex(cos y + i sin y). Use expm for matrix exponentials.
		return Math.exp(v);
//		return 0;
	}	
	
	// Return primes less than limit
	public static ArrayList<Integer> generatePrimes(int limit) {
	    final int numPrimes = countPrimesUpperBound(limit);
	    ArrayList<Integer> primes = new ArrayList<Integer>(numPrimes);
	    boolean [] isComposite    = new boolean [limit];   // all false
	    final int sqrtLimit       = (int)Math.sqrt(limit); // floor
	    for (int i = 2; i <= sqrtLimit; i++) {
	        if (!isComposite [i]) {
	            primes.add(i);
	            for (int j = i*i; j < limit; j += i) // `j+=i` can overflow
	                isComposite [j] = true;
	        }
	    }
	    for (int i = sqrtLimit + 1; i < limit; i++)
	        if (!isComposite [i])
	            primes.add(i);
	    
	    // check limit for prime
	    if(isPrime(limit))
	    	primes.add(limit);
	    
	    return primes;
	}
	
	private static boolean isPrime(long n) {
	    if(n < 2) return false;
	    if(n == 2 || n == 3) return true;
	    if(n%2 == 0 || n%3 == 0) return false;
	    long sqrtN = (long)Math.sqrt(n)+1;
	    for(long i = 6L; i <= sqrtN; i += 6) {
	        if(n%(i-1) == 0 || n%(i+1) == 0) return false;
	    }
	    return true;
	}
	
	private static int countPrimesUpperBound(int max) {
	    return max > 1 ? (int)(1.25506 * max / Math.log((double)max)) : 0;
	}
}
