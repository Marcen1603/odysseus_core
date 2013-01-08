package de.offis;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.special.Erf;

import umontreal.iro.lecuyer.util.Num;


public class Matlab {

	private static final double pi = Math.PI;

	public static void main(String[] args) {
//		>> r = [4 3 2 1;3 5 -1 1;2 -1 4 2;1 1 2 5];
		//%     >> a = -inf*[1 1 1 1 ]'; b = [ 1 2 3 4 ]';
		//%     >> [ p e ] = qsimvn( 5000, r, a, b ); disp([ p e ])
		
		Matrix R = new Matrix(new double[][]{
				{15,3,2,1},
				{3,5,-1,1},
				{2,-1,4,2},
				{1,1,2,5}});		
		
		Matrix a = new Matrix(new double[][]{
				{Double.NEGATIVE_INFINITY},
				{Double.NEGATIVE_INFINITY},
				{Double.NEGATIVE_INFINITY},
				{Double.NEGATIVE_INFINITY}});
		
		Matrix b = new Matrix(new double[][]{{1},{2},{3},{4}});
		
		QSIMVNReturn ret = QSIMVN(5000, R, a, b);
		System.out.println("p = " + ret.p + "   e = " + ret.e);
	}
	
	public static QSIMVNReturn QSIMVN(int m, Matrix r, Matrix a, Matrix b){
		//[n, n] = size(r);
		// size of r must be quadratic i guess ...
		int n = r.getRowDimension();
		
		//[ ch as bs ] = chlrdr( r, a, b );
		chlrdrReturn r2 = chlrdr( r, a, b );
		double ct = r2.ch.get(1, 1); 
		double ai = r2.as.get(1); 
		double bi = r2.bs.get(1); 
		double cn = 37.5; 
		double c = 0;
		double d = 0;
		if (Math.abs(ai) < cn*ct){ c = phi(ai/ct);} else{c = ( 1 + sign(ai) )/2;}
		if (Math.abs(bi) < cn*ct){ d = phi(bi/ct);} else{d = ( 1 + sign(bi) )/2;}
		double ci = c; 
		double dci = d - ci; 
		double p = 0; 
		double e = 0;
		double ns = 12;
		
		double nv = max(new double[]{m/ns, 1}); //double nv = max( [ m/ns 1 ] ); 
		//%q = 2.^( [1:n-1]'/n) ; % Niederreiter point set generators
		Matrix ps = primes((int)(5*n*log(n+1)/4)).sqrt(); 
		Matrix q = ps.getSubVector(1, n-1).trans();//(1:n-1)'; //% Richtmyer generators
		
		
		
		//%
		//% Randomization loop for ns samples
		//%
		for(int i = 1 ; i <= ns ; i++){ //for i = 1 : ns
			double vi = 0; 
			Matrix xr = rand(n-1);
//			Matrix xr = Matrix.zeros(n-1); // TODO
			for(int j = 1 ; j <= nv ; j++){
				// Loop for nv quasirandom points
				
//				// TODO
//				Matrix temp = q.matlabMultiply(j);
//				temp = temp.add(xr);
//				temp = temp.mod(1);
//				temp = temp.matlabMultiply(2).substract(1);
//				temp = temp.abs();
//				Matrix x = temp;
				Matrix x = q.matlabMultiply(j).add(xr).mod(1).matlabMultiply(2).substract(1).abs(); //% periodizing transformation
	
				    
				    double vp =   mvndns( n, r2.ch, ci, dci,  x, r2.as, r2.bs ); 
				    vi = vi + ( vp - vi )/j; 
			}
				d = ( vi - p )/i; 
				p = p + d; 
				if (Math.abs(d) > 0){ 
					e = Math.abs(d)*Math.sqrt(1 + Math.pow(( e/d ),2*( i - 2 )/i ));
				}  else{
				    if (i > 1){
				    	e = e*Math.sqrt( ( i - 2 )/i );
				    }
				}
		}
		
		e = 3*e; //% error estimate is 3 x standard error with ns samples.

		return new QSIMVNReturn(p, e);
	}
	


	private static double sqrt(double d) {
		return Math.sqrt(d);
	}

	private static Matrix rand(int n) {
//		r = rand(n) returns an n-by-n matrix containing pseudorandom 
//				values drawn from the standard uniform distribution on the 
//				open interval (0,1). r = rand(m,n) or r = rand([m,n]) returns 
//				an m-by-n matrix. r = rand(m,n,p,...) or r = rand([m,n,p,...]) 
//				returns an m-by-n-by-p-by-... array. r = rand returns a scalar. r = rand(size(A)) 
//				returns an array the same size as A.
		
		// TODO schauen ob diese methode äquivalent zu mathlabs rand(n) ist.
		double[] temp = new double[n];
		Random r = new Random();
		for(int i = 0 ; i < n ; i++){
				temp[i] = r.nextDouble();			
		}

		return  new Matrix(temp, false);
	}

	private static Matrix primes(int n) {
//		Math.
//		p = primes(n) returns a row vector of the prime numbers 
//				less than or equal to n. A prime number is one that 
//				has no factors other than 1 and itself.
		ArrayList<Integer> prim = generatePrimes(n);
		double[] primes = new double[prim.size()];
		for(int i = 0 ; i < prim.size() ; i++){
			primes[i] = prim.get(i);
		}
		
		return new Matrix(primes, true);
	}
	
	// Return primes less than limit
	static ArrayList<Integer> generatePrimes(int limit) {
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
	    return primes;
	}
	
	static int countPrimesUpperBound(int max) {
	    return max > 1 ? (int)(1.25506 * max / Math.log((double)max)) : 0;
	}

	private static double max(double[] ds) {
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

	private static double log(int i) {
		return Math.log(i);
	}

	private static double phi(double z) {
		return Erf.erfc(-z/sqrt(2))/2;
	}

	private static double sign(double ai) {
		return Math.signum(ai);
	}
	
	/**
	 *  Transformed integrand for computation of MVN probabilities. 
	 * 
	 * @param n
	 * @param ch
	 * @param ci
	 * @param dci
	 * @param x
	 * @param a
	 * @param b
	 */
	public static double mvndns(int n, Matrix ch, double ci, double dci, Matrix x, Matrix a, Matrix b){
		//function p = mvndns( n, ch, ci, dci, x, a, b )
		Matrix y = Matrix.zeros(n-1); 
		double cn = 37.5; 
		double s = 0; 
		double c = ci; 
		double dc = dci; 
		double p = dc;
		double d = 0;
		
		for(int i = 2 ; i <= n ; i++){		
		
			double xxx = c + x.get(i-1)*dc; // TODO
			y.set(i-1, phinv( xxx));
			s = ch.getSubRow(i, 1, i-1).matlabMultiply(y.getSubVector(1, i-1));
			
			double ct = ch.get(i, i);
			double ai = a.get(i) - s;
			double bi = b.get(i) - s;
			if (Math.abs(ai) < cn * ct) {
				c = phi(ai / ct);
			} else {
				c = (1 + sign(ai)) / 2;
			}
			if (Math.abs(bi) < cn * ct) {
				d = phi(bi / ct);
			} else {
				d = (1 + sign(bi)) / 2;
			}
			dc = d - c;
			p = p * dc;
		}

		return p;
	}

	private static double phinv(double p) {
		//%
				//%  Standard statistical normal distribution functions
				//%
				//function p =   phi(z), p =  erfc( -z/sqrt(2) )/2;
				//function z = phinv(p), z = norminv( p );
				//% function z = phinv(p), z = -sqrt(2)*erfcinv( 2*p ); % use if no norminv
				//%
				//
		return -sqrt(2)*Num.erfcInv(2*p);
	}
	
	public static chlrdrReturn chlrdr(Matrix r, Matrix a, Matrix b){
		//function [ c, ap, bp ] = chlrdr( R, a, b )
				//%
				//%  Computes permuted lower Cholesky factor c for R which may be singular, 
				//%   also permuting integration limit vectors a and b.
				//%
				double ep = 1e-10; //% singularity tolerance;
				//%
				//[n, n] = size(R);
				// size of R must be quadratic i guess ...
				int n = r.getRowDimension(); 
				Matrix c = r; 
				Matrix ap = a;
				Matrix bp = b; 
				Matrix d = c.diag().max(0).sqrt();
				for(int i = 1 ; i <= n ; i++){
				  if (d.get(i) > 0){
					  
//					c(:,i) = c(:,i)/d(i);
					c.divideColumn(i, d.get(i));
					
//				    c(i,:) = c(i,:)/d(i); 
				    c.divideRow(i, d.get(i));
				    
				    ap.set(i, ap.get(i)/d.get(i)); 
				    bp.set(i, bp.get(i)/d.get(i));
				  }
				}
				Matrix y = Matrix.zeros(n,1); 
				double sqtp = sqrt(2*pi);
				for(int k = 1 ; k <= n ; k++){
				   int im = k; 
				   double ckk = 0; 
				   double dem = 1; 
				   double s = 0; 
				   
				   double cii;
				   double ai;
				   double bi;
				   double de;
				   double am = 0;
				   double bm = 0;
				   double tv;
				   Matrix t;
				   
					for(int i = k ; i <= n ; i++){
//				       if (c.get(i,i) > eps(1)){
				          cii = Math.sqrt( max(new double[]{c.get(i, i), 0}) );
				          if (i > 1){
				        	  if(k <= 1){				        		
				        		// FIXME added at java convcersion
				        		  s = 0;
				        	  } else {
				        			  s = c.getSubRow(i, 1, k-1).matlabMultiply(y.getSubVector(1, k-1));  
						        	  
				        	  }
				          }
				          
				          ai = ( ap.get(i)-s )/cii; 
				          bi = ( bp.get(i)-s )/cii; 
				          de = phi(bi) - phi(ai);
				          if (de <= dem){
					          ckk = cii;
					          dem = de;
					          am = ai;
					          bm = bi;
					          im = i;
				          }
//				       }
					}
				   if (im > k){
				      tv = ap.get(im); 
				      ap.set(im, ap.get(k)); 
				      ap.set(k, tv);
				      tv = bp.get(im); 
				      bp.set(im, bp.get(k)); 
				      bp.set(k, tv);
				      c.setElement(im, im, c.get(k,k)); 
				      
				      t = c.getSubRow(im, 1, k-1); 
				      c.setSubRow(im,1,c.getSubRow(k,1,k-1)); 
				      c.setSubRow(k,1,t); 
				      
				      t = c.getSubColumn(im, im+1, n); 
				      c.setSubCol(im, im+1, c.getSubColumn(k, im+1, n)); 
				      c.setSubCol(k, im+1, t); 
				      
				      t = c.getSubColumn(k, k+1, im-1); 
				      c.setSubCol(k, k+1, c.getSubRow(im, k+1, im-1).trans());
				      c.setSubRow(im, k+1, t.trans()); 
				   }
				   if (ckk > ep*k){
				      c.setElement(k, k, ckk);
				      c.setSubRow(k, k+1, n, 0);
				      for (int i = k+1 ; i <= n ; i++){
				         c.setElement(i, k, c.get(i, k)/ckk); 
//				         Matrix temp = c.getSubColumn(k, k+1, i);
//				         temp = temp.trans();
//				         temp = temp.matlabMultiply(c.get(i,k));
				         c.setSubRow(i, k+1, c.getSubRow(i, k+1, i).substract(c.getSubColumn(k, k+1, i).trans().matlabMultiply(c.get(i,k))));
				      }
				      if (Math.abs(dem) > ep){
					y.set(k,  ( exp( Math.pow(-am,2)/2 ) - exp( Math.pow(-bm, 2)/2 ) )/( sqtp*dem )); // TODO Math.pow(-bm, 2/2) ODER Math.pow(-bm, 2/2)
				     } else {
					if (am < -10){
					  y.set(k, bm);
					}else if (bm > 10){
						y.set(k, am);
					}else{
					  y.set(k,( am + bm )/2);
					}
				      }
				   } else {
				      c.setSubCol(k, k, n, 0); 
				      y.set(k, 0);
				   }
				}
		return new chlrdrReturn(c, ap, bp);
	}
	

	


	private static double eps(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int exp(double pow) {
		// TODO Auto-generated method stub
//		Y = exp(X) returns the exponential for each element 
//				of X. exp operates element-wise on arrays. For 
//				complex x + i * y, exp returns the complex 
//				exponential ez = ex(cos y + i sin y). Use expm for matrix exponentials.
		return 0;
	}	
}


//function [ p, e ] = qsimvn( m, r, a, b )
//%
//%  [ P E ] = QSIMVN( M, R, A, B )
//%    uses a randomized quasi-random rule with m points to estimate an
//%    MVN probability for positive definite covariance matrix r,
//%    with lower integration limits a and upper integration limits b. 
//%   Probability p is output with error estimate e.
//%    Example usage:
//%     >> r = [4 3 2 1;3 5 -1 1;2 -1 4 2;1 1 2 5];
//%     >> a = -inf*[1 1 1 1 ]'; b = [ 1 2 3 4 ]';
//%     >> [ p e ] = qsimvn( 5000, r, a, b ); disp([ p e ])
//%
//%   This function uses an algorithm given in the paper
//%      "Numerical Computation of Multivariate Normal Probabilities", in
//%      J. of Computational and Graphical Stat., 1(1992), pp. 141-149, by
//%          Alan Genz, WSU Math, PO Box 643113, Pullman, WA 99164-3113
//%          Email : AlanGenz@wsu.edu
//%  The primary references for the numerical integration are 
//%   "On a Number-Theoretical Integration Method"
//%   H. Niederreiter, Aequationes Mathematicae, 8(1972), pp. 304-11, and
//%   "Randomization of Number Theoretic Methods for Multiple Integration"
//%    R. Cranley and T.N.L. Patterson, SIAM J Numer Anal, 13(1976), pp. 904-14.
//%
//%   Alan Genz is the author of this function and following Matlab functions.
//%
//% Initialization
//%
//[n, n] = size(r); [ ch as bs ] = chlrdr( r, a, b );
//ct = ch(1,1); ai = as(1); bi = bs(1); cn = 37.5; 
//if abs(ai) < cn*ct, c = phi(ai/ct); else, c = ( 1 + sign(ai) )/2; end
//if abs(bi) < cn*ct, d = phi(bi/ct); else, d = ( 1 + sign(bi) )/2; end
//ci = c; dci = d - ci; p = 0; e = 0;
//ns = 12; nv = max( [ m/ns 1 ] ); 
//%q = 2.^( [1:n-1]'/n) ; % Niederreiter point set generators
//ps = sqrt(primes(5*n*log(n+1)/4)); q = ps(1:n-1)'; % Richtmyer generators
//%
//% Randomization loop for ns samples
//%
//for i = 1 : ns
//  vi = 0; xr = rand( n-1, 1 ); 
//  %
//  % Loop for nv quasirandom points
//  %
//  for  j = 1 : nv
//    x = abs( 2*mod( j*q + xr, 1 ) - 1 ); % periodizing transformation
//    vp =   mvndns( n, ch, ci, dci,  x, as, bs ); 
//    vi = vi + ( vp - vi )/j; 
//  end   
//  %
//  d = ( vi - p )/i; p = p + d; 
//  if abs(d) > 0 
//    e = abs(d)*sqrt( 1 + ( e/d )^2*( i - 2 )/i );
//  else
//    if i > 1, e = e*sqrt( ( i - 2 )/i ); end
//  end
//end
//%
//e = 3*e; % error estimate is 3 x standard error with ns samples.
//return
//%
//% end qsimvn
//%
//function p = mvndns( n, ch, ci, dci, x, a, b )
//%
//%  Transformed integrand for computation of MVN probabilities. 
//%
//y = zeros(n-1,1); cn = 37.5; s = 0; c = ci; dc = dci; p = dc; 
//for i = 2 : n
//  y(i-1) = phinv( c + x(i-1)*dc ); s = ch(i,1:i-1)*y(1:i-1); 
//  ct = ch(i,i); ai = a(i) - s; bi = b(i) - s;
//  if abs(ai) < cn*ct, c = phi(ai/ct); else, c = ( 1 + sign(ai) )/2; end
//  if abs(bi) < cn*ct, d = phi(bi/ct); else, d = ( 1 + sign(bi) )/2; end
//  dc = d - c; p = p*dc; 
//end 
//return
//%
//% end mvndns
//%
//function [ c, ap, bp ] = chlrdr( R, a, b )
//%
//%  Computes permuted lower Cholesky factor c for R which may be singular, 
//%   also permuting integration limit vectors a and b.
//%
//ep = 1e-10; % singularity tolerance;
//%
//[n,n] = size(R); c = R; ap = a; bp = b; d = sqrt(max(diag(c),0));
//for i = 1 :  n
//  if d(i) > 0
//    c(:,i) = c(:,i)/d(i); c(i,:) = c(i,:)/d(i); 
//    ap(i) = ap(i)/d(i); bp(i) = bp(i)/d(i);
//  end
//end
//y = zeros(n,1); sqtp = sqrt(2*pi);
//for k = 1 : n
//   im = k; ckk = 0; dem = 1; s = 0; 
//   for i = k : n 
//       if c(i,i) > eps
//          cii = sqrt( max( [c(i,i) 0] ) ); 
//          if i > 1, s = c(i,1:k-1)*y(1:k-1); end
//          ai = ( ap(i)-s )/cii; bi = ( bp(i)-s )/cii; de = phi(bi) - phi(ai);
//          if de <= dem, ckk = cii; dem = de; am = ai; bm = bi; im = i; end
//       end
//   end
//   if im > k
//      tv = ap(im); ap(im) = ap(k); ap(k) = tv;
//      tv = bp(im); bp(im) = bp(k); bp(k) = tv;
//      c(im,im) = c(k,k); 
//      t = c(im,1:k-1); c(im,1:k-1) = c(k,1:k-1); c(k,1:k-1) = t; 
//      t = c(im+1:n,im); c(im+1:n,im) = c(im+1:n,k); c(im+1:n,k) = t; 
//      t = c(k+1:im-1,k); c(k+1:im-1,k) = c(im,k+1:im-1)'; c(im,k+1:im-1) = t'; 
//   end
//   if ckk > ep*k
//      c(k,k) = ckk; c(k,k+1:n) = 0;
//      for i = k+1 : n
//         c(i,k) = c(i,k)/ckk; c(i,k+1:i) = c(i,k+1:i) - c(i,k)*c(k+1:i,k)';
//      end
//      if abs(dem) > ep 
//	y(k) = ( exp( -am^2/2 ) - exp( -bm^2/2 ) )/( sqtp*dem ); 
//      else
//	if am < -10
//	  y(k) = bm;
//	elseif bm > 10
//	  y(k) = am;
//	else
//	  y(k) = ( am + bm )/2;
//	end
//      end
//   else
//      c(k:n,k) = 0; y(k) = 0;
//   end
//end
//return
//%
//% end chlrdr
//%
//%
//%  Standard statistical normal distribution functions
//%
//function p =   phi(z), p =  erfc( -z/sqrt(2) )/2;
//function z = phinv(p), z = norminv( p );
//% function z = phinv(p), z = -sqrt(2)*erfcinv( 2*p ); % use if no norminv
//%
//
