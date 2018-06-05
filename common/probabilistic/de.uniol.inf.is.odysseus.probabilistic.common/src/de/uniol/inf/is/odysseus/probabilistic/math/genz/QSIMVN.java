/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.probabilistic.math.genz;

import java.text.DecimalFormat;

import org.apache.commons.math3.util.FastMath;

/**
 * Based on the Matlab function for the numerical computation of multivariate
 * normal distribution values by Alan Genz.
 * 
 * http://www.sci.wsu.edu/math/faculty/genz/homepage
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @author Alexander Funk
 * 
 *         FIXME 20140319 christian@kuka.cc Use apache math matrix
 */
public final class QSIMVN {
    /**
     * Compute the cumulative probability of the given distribution.
     * 
     * @param m
     * @param r
     * @param a
     * @param b
     * @return
     */
    public static QSIMVNResult cumulativeProbability(final int m, final Matrix r, final Matrix a, final Matrix b) {
        // [n, n] = size(r);
        // size of r must be quadratic i guess ...
        final int n = r.getRowDimension();

        // [ ch as bs ] = chlrdr( r, a, b );
        final ChlrdrResult r2 = QSIMVN.chlrdr(r, a, b);
        final double ct = r2.ch.get(1, 1);
        final double ai = r2.as.get(1);
        final double bi = r2.bs.get(1);
        final double cn = 37.5;
        double c = 0.0;
        double d = 0.0;
        if (FastMath.abs(ai) < (cn * ct)) {
            c = Util.phi(ai / ct);
        }
        else {
            c = (1.0 + FastMath.signum(ai)) / 2.0;
        }
        if (FastMath.abs(bi) < (cn * ct)) {
            d = Util.phi(bi / ct);
        }
        else {
            d = (1.0 + FastMath.signum(bi)) / 2.0;
        }
        final double ci = c;
        final double dci = d - ci;
        double p = 0.0;
        double e = 0.0;
        final double ns = 12.0;

        final double nv = Util.max(new double[] { m / ns, 1.0 }); // double nv =
                                                                  // max( [
        // m/ns 1 ] );
        // %q = 2.^( [1:n-1]'/n) ; % Niederreiter point set generators
        final Matrix ps = Matrix.primes((int) ((5.0 * n * FastMath.log(n + 1.0)) / 4.0)).sqrt();
        final Matrix q = ps.getSubVector(1, n - 1).trans();// (1:n-1)'; //%
                                                           // Richtmyer
                                                           // generators

        // %
        // % Randomization loop for ns samples
        // %
        for (int i = 1; i <= ns; i++) { // for i = 1 : ns
            double vi = 0.0;
            final Matrix xr = Matrix.rand(n - 1);
            for (int j = 1; j <= nv; j++) {
                // Loop for nv quasirandom points
                final Matrix x = q.matlabMultiply(j).add(xr).mod(1).matlabMultiply(2.0).substract(1.0).abs(); // %
                                                                                                              // periodizing
                                                                                                              // transformation
                final double vp = QSIMVN.mvndns(n, r2.ch, ci, dci, x, r2.as, r2.bs);
                vi = vi + ((vp - vi) / j);
            }
            d = (vi - p) / i;
            p = p + d;

            if (FastMath.abs(d) > 0.0) {
                e = FastMath.abs(d) * FastMath.sqrt(1.0 + ((FastMath.pow(e / d, 2.0) * (i - 2.0)) / i));

                // final double a1 = (e / d);
                // final double b2 = 2.0 * (i - 2.0) / i;
                // double v1 = FastMath.pow(a1, b2);
                // if (Double.isNaN(v1)) {
                // v1=0.0;
                // }
                // double v2 = FastMath.pow(1.0 + v1, 0.5);
                //
                // if (Double.isNaN(v2)) {
                // // sometimes wants to compute sqrt of negative number
                // v2 = 0.0;
                // }
                // final double val = FastMath.abs(d) * v2;
                // e = val;
            }
            else {
                if (i > 1.0) {
                    final double val = FastMath.sqrt((i - 2.0) / i);
                    if (val == 0.0) {
                        e = 0.0;
                    }
                    else {
                        e = e * val;
                    }
                }
            }
        }

        e = e * 3.0; // % error estimate is 3 x standard error with ns samples.

        return new QSIMVNResult(p, e);
    }

    /**
     * Transformed integrand for computation of MVN probabilities.
     * 
     * @param n
     * @param ch
     * @param ci
     * @param dci
     * @param x
     * @param a
     * @param b
     */
    public static double mvndns(final int n, final Matrix ch, final double ci, final double dci, final Matrix x, final Matrix a, final Matrix b) {
        // function p = mvndns( n, ch, ci, dci, x, a, b )
        final Matrix y = Matrix.zeros(n - 1);
        final double cn = 37.5;
        double s = 0;
        double c = ci;
        double dc = dci;
        double p = dc;
        double d = 0;

        for (int i = 2; i <= n; i++) {

            final double xxx = c + (x.get(i - 1) * dc);
            y.set(i - 1, Util.phinv(xxx));
            s = ch.getSubRow(i, 1, i - 1).matlabMultiply(y.getSubVector(1, i - 1));

            final double ct = ch.get(i, i);
            final double ai = a.get(i) - s;
            final double bi = b.get(i) - s;
            if (FastMath.abs(ai) < (cn * ct)) {
                c = Util.phi(ai / ct);
            }
            else {
                c = (1.0 + FastMath.signum(ai)) / 2.0;
            }
            if (FastMath.abs(bi) < (cn * ct)) {
                d = Util.phi(bi / ct);
            }
            else {
                d = (1.0 + FastMath.signum(bi)) / 2.0;
            }
            dc = d - c;
            p = p * dc;
        }

        return p;
    }

    /**
     * 
     * @param r
     * @param a
     * @param b
     * @return
     */
    public static ChlrdrResult chlrdr(final Matrix r, final Matrix a, final Matrix b) {
        // function [ c, ap, bp ] = chlrdr( R, a, b )
        // %
        // % Computes permuted lower Cholesky factor c for R which may be
        // singular,
        // % also permuting integration limit vectors a and b.
        // %
        final double ep = 1e-10; // % singularity tolerance;
        final double eps = FastMath.pow(2, -52.0);
        // %
        // [n, n] = size(R);
        // size of R must be quadratic i guess ...
        final int n = r.getRowDimension();
        final Matrix c = r;
        final Matrix ap = a;
        final Matrix bp = b;
        final Matrix d = c.diag().max(0).sqrt();
        for (int i = 1; i <= n; i++) {
            if (d.get(i) > 0.0) {

                c.divideColumn(i, d.get(i));
                c.divideRow(i, d.get(i));

                ap.set(i, ap.get(i) / d.get(i));
                bp.set(i, bp.get(i) / d.get(i));
            }
        }
        final Matrix y = Matrix.zeros(n, 1);
        final double sqtp = FastMath.sqrt(2.0 * FastMath.PI);
        for (int k = 1; k <= n; k++) {
            int im = k;
            double ckk = 0.0;
            double dem = 1.0;
            double s = 0.0;

            double cii;
            double ai;
            double bi;
            double de;
            double am = 0.0;
            double bm = 0.0;
            double tv;
            Matrix t;
            for (int i = k; i <= n; i++) {
                if (c.get(i, i) > eps) {
                    cii = FastMath.sqrt(Util.max(new double[] { c.get(i, i), 0.0 }));
                    if (i > 1) {
                        if (k <= 1) {
                            // added at java convcersion
                            s = 0.0;
                        }
                        else {
                            s = c.getSubRow(i, 1, k - 1).matlabMultiply(y.getSubVector(1, k - 1));

                        }
                    }

                    ai = (ap.get(i) - s) / cii;
                    bi = (bp.get(i) - s) / cii;
                    de = Util.phi(bi) - Util.phi(ai);
                    if (de <= dem) {
                        ckk = cii;
                        dem = de;
                        am = ai;
                        bm = bi;
                        im = i;
                    }
                }
            }
            if (im > k) {
                tv = ap.get(im);
                ap.set(im, ap.get(k));
                ap.set(k, tv);
                tv = bp.get(im);
                bp.set(im, bp.get(k));
                bp.set(k, tv);
                c.setElement(im, im, c.get(k, k));

                t = c.getSubRow(im, 1, k - 1);
                c.setSubRow(im, 1, c.getSubRow(k, 1, k - 1));
                c.setSubRow(k, 1, t);

                t = c.getSubColumn(im, im + 1, n);
                c.setSubCol(im, im + 1, c.getSubColumn(k, im + 1, n));
                c.setSubCol(k, im + 1, t);

                t = c.getSubColumn(k, k + 1, im - 1);
                c.setSubCol(k, k + 1, c.getSubRow(im, k + 1, im - 1).trans());
                c.setSubRow(im, k + 1, t.trans());
            }
            if (ckk > (ep * k)) {
                c.setElement(k, k, ckk);
                c.setSubRow(k, k + 1, n, 0.0);
                for (int i = k + 1; i <= n; i++) {
                    c.setElement(i, k, c.get(i, k) / ckk);
                    c.setSubRow(i, k + 1, c.getSubRow(i, k + 1, i).substract(c.getSubColumn(k, k + 1, i).trans().matlabMultiply(c.get(i, k))));
                }
                if (FastMath.abs(dem) > ep) {
                    y.set(k, (FastMath.exp(FastMath.pow(-am, 2.0) / 2.0) - FastMath.exp(FastMath.pow(-bm, 2.0) / 2.0)) / (sqtp * dem));
                }
                else {
                    if (am < -10.0) {
                        y.set(k, bm);
                    }
                    else if (bm > 10.0) {
                        y.set(k, am);
                    }
                    else {
                        y.set(k, (am + bm) / 2.0);
                    }
                }
            }
            else {
                c.setSubCol(k, k, n, 0.0);
                y.set(k, 0.0);
            }
        }
        return new ChlrdrResult(c, ap, bp);
    }

    /**
     * Test code.
     * 
     * @param args
     *            Default args
     */
    public static void main(final String[] args) {
        // System.out.println(Util.generatePrimes(8));
        // >> r = [4 3 2 1;3 5 -1 1;2 -1 4 2;1 1 2 5];
        // % >> a = -inf*[1 1 1 1 ]'; b = [ 1 2 3 4 ]';
        // % >> [ p e ] = qsimvn( 5000, r, a, b ); disp([ p e ])

        final Matrix r = new Matrix(new double[][] { { 4.0, 3.0, 2.0, 1.0 }, { 3.0, 5.0, -1.0, 1.0 }, { 2.0, -1.0, 4.0, 2.0 }, { 1.0, 1.0, 2.0, 5.0 } });

        final Matrix a = new Matrix(new double[][] { { Double.NEGATIVE_INFINITY }, { Double.NEGATIVE_INFINITY }, { Double.NEGATIVE_INFINITY }, { Double.NEGATIVE_INFINITY } });

        final Matrix b = new Matrix(new double[][] { { 1.0 }, { 2.0 }, { 3.0 }, { 4.0 } });

        final QSIMVNResult ret = QSIMVN.cumulativeProbability(500, r, a, b);
        System.out.println("p = " + ret.getProbability() + "   e = " + new DecimalFormat("#.###############").format(ret.getError()));
    }

    /**
     * Hidden utility constructor.
     */
    private QSIMVN() {
        throw new UnsupportedOperationException();
    }
}

// function [ p, e ] = qsimvn( m, r, a, b )
// %
// % [ P E ] = QSIMVN( M, R, A, B )
// % uses a randomized quasi-random rule with m points to estimate an
// % MVN probability for positive definite covariance matrix r,
// % with lower integration limits a and upper integration limits b.
// % Probability p is output with error estimate e.
// % Example usage:
// % >> r = [4 3 2 1;3 5 -1 1;2 -1 4 2;1 1 2 5];
// % >> a = -inf*[1 1 1 1 ]'; b = [ 1 2 3 4 ]';
// % >> [ p e ] = qsimvn( 5000, r, a, b ); disp([ p e ])
// %
// % This function uses an algorithm given in the paper
// % "Numerical Computation of Multivariate Normal Probabilities", in
// % J. of Computational and Graphical Stat., 1(1992), pp. 141-149, by
// % Alan Genz, WSU Math, PO Box 643113, Pullman, WA 99164-3113
// % Email : AlanGenz@wsu.edu
// % The primary references for the numerical integration are
// % "On a Number-Theoretical Integration Method"
// % H. Niederreiter, Aequationes Mathematicae, 8(1972), pp. 304-11, and
// % "Randomization of Number Theoretic Methods for Multiple Integration"
// % R. Cranley and T.N.L. Patterson, SIAM J Numer Anal, 13(1976), pp. 904-14.
// %
// % Alan Genz is the author of this function and following Matlab functions.
// %
// % Initialization
// %
// [n, n] = size(r); [ ch as bs ] = chlrdr( r, a, b );
// ct = ch(1,1); ai = as(1); bi = bs(1); cn = 37.5;
// if abs(ai) < cn*ct, c = phi(ai/ct); else, c = ( 1 + sign(ai) )/2; end
// if abs(bi) < cn*ct, d = phi(bi/ct); else, d = ( 1 + sign(bi) )/2; end
// ci = c; dci = d - ci; p = 0; e = 0;
// ns = 12; nv = max( [ m/ns 1 ] );
// %q = 2.^( [1:n-1]'/n) ; % Niederreiter point set generators
// ps = sqrt(primes(5*n*log(n+1)/4)); q = ps(1:n-1)'; % Richtmyer generators
// %
// % Randomization loop for ns samples
// %
// for i = 1 : ns
// vi = 0; xr = rand( n-1, 1 );
// %
// % Loop for nv quasirandom points
// %
// for j = 1 : nv
// x = abs( 2*mod( j*q + xr, 1 ) - 1 ); % periodizing transformation
// vp = mvndns( n, ch, ci, dci, x, as, bs );
// vi = vi + ( vp - vi )/j;
// end
// %
// d = ( vi - p )/i; p = p + d;
// if abs(d) > 0
// e = abs(d)*sqrt( 1 + ( e/d )^2*( i - 2 )/i );
// else
// if i > 1, e = e*sqrt( ( i - 2 )/i ); end
// end
// end
// %
// e = 3*e; % error estimate is 3 x standard error with ns samples.
// return
// %
// % end qsimvn
// %
// function p = mvndns( n, ch, ci, dci, x, a, b )
// %
// % Transformed integrand for computation of MVN probabilities.
// %
// y = zeros(n-1,1); cn = 37.5; s = 0; c = ci; dc = dci; p = dc;
// for i = 2 : n
// y(i-1) = phinv( c + x(i-1)*dc ); s = ch(i,1:i-1)*y(1:i-1);
// ct = ch(i,i); ai = a(i) - s; bi = b(i) - s;
// if abs(ai) < cn*ct, c = phi(ai/ct); else, c = ( 1 + sign(ai) )/2; end
// if abs(bi) < cn*ct, d = phi(bi/ct); else, d = ( 1 + sign(bi) )/2; end
// dc = d - c; p = p*dc;
// end
// return
// %
// % end mvndns
// %
// function [ c, ap, bp ] = chlrdr( R, a, b )
// %
// % Computes permuted lower Cholesky factor c for R which may be singular,
// % also permuting integration limit vectors a and b.
// %
// ep = 1e-10; % singularity tolerance;
// %
// [n,n] = size(R); c = R; ap = a; bp = b; d = sqrt(max(diag(c),0));
// for i = 1 : n
// if d(i) > 0
// c(:,i) = c(:,i)/d(i); c(i,:) = c(i,:)/d(i);
// ap(i) = ap(i)/d(i); bp(i) = bp(i)/d(i);
// end
// end
// y = zeros(n,1); sqtp = sqrt(2*pi);
// for k = 1 : n
// im = k; ckk = 0; dem = 1; s = 0;
// for i = k : n
// if c(i,i) > eps
// cii = sqrt( max( [c(i,i) 0] ) );
// if i > 1, s = c(i,1:k-1)*y(1:k-1); end
// ai = ( ap(i)-s )/cii; bi = ( bp(i)-s )/cii; de = phi(bi) - phi(ai);
// if de <= dem, ckk = cii; dem = de; am = ai; bm = bi; im = i; end
// end
// end
// if im > k
// tv = ap(im); ap(im) = ap(k); ap(k) = tv;
// tv = bp(im); bp(im) = bp(k); bp(k) = tv;
// c(im,im) = c(k,k);
// t = c(im,1:k-1); c(im,1:k-1) = c(k,1:k-1); c(k,1:k-1) = t;
// t = c(im+1:n,im); c(im+1:n,im) = c(im+1:n,k); c(im+1:n,k) = t;
// t = c(k+1:im-1,k); c(k+1:im-1,k) = c(im,k+1:im-1)'; c(im,k+1:im-1) = t';
// end
// if ckk > ep*k
// c(k,k) = ckk; c(k,k+1:n) = 0;
// for i = k+1 : n
// c(i,k) = c(i,k)/ckk; c(i,k+1:i) = c(i,k+1:i) - c(i,k)*c(k+1:i,k)';
// end
// if abs(dem) > ep
// y(k) = ( exp( -am^2/2 ) - exp( -bm^2/2 ) )/( sqtp*dem );
// else
// if am < -10
// y(k) = bm;
// elseif bm > 10
// y(k) = am;
// else
// y(k) = ( am + bm )/2;
// end
// end
// else
// c(k:n,k) = 0; y(k) = 0;
// end
// end
// return
// %
// % end chlrdr
// %
// %
// % Standard statistical normal distribution functions
// %
// function p = phi(z), p = erfc( -z/sqrt(2) )/2;
// function z = phinv(p), z = norminv( p );
// % function z = phinv(p), z = -sqrt(2)*erfcinv( 2*p ); % use if no norminv
// %
//
