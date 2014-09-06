/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.util;

public class Complex {

	// MEMBER VARIABLES

	private double real = 0;
	private double imag = 0;

	// CONSTRUCTOR

	public Complex() {
		super();
		this.real = 0;
		this.imag = 0;
	}

	public Complex(double newReal, double newImag) {
		super();
		this.real = newReal;
		this.imag = newImag;
	}

	// PUBLIC METHODS

	public Complex(Complex complex) {
		// TODO Auto-generated constructor stub
		this.real = complex.real;
		this.imag = complex.imag;
	}

	public double getReal() {
		return this.real;
	}

	public double getImag() {
		return this.imag;
	}

	public double getAbs() {
		return java.lang.Math.sqrt(java.lang.Math.pow(this.real, 2)
				+ java.lang.Math.pow(this.imag, 2));
	}

	public double getArg() {
		return java.lang.Math.atan2(this.imag, this.real);
	}

	public Complex setReal(double newReal) {
		this.real = newReal;
		return this;
	}

	public Complex setImag(double newImag) {
		this.imag = newImag;
		return this;
	}

	public Complex setComplexRect(double newReal, double newImag) {
		this.real = newReal;
		this.imag = newImag;
		return this;
	}

	public Complex setAbs(double newAbs) {
		double tempArg = java.lang.Math.atan2(this.imag, this.real);
		this.imag = newAbs * java.lang.Math.sin(tempArg);
		this.real = newAbs * java.lang.Math.cos(tempArg);
		return this;
	}

	public Complex setArg(double newArg) {
		double tempAbs = java.lang.Math.sqrt(java.lang.Math.pow(this.real, 2)
				+ java.lang.Math.pow(this.imag, 2));
		this.real = tempAbs * java.lang.Math.cos(newArg);
		this.imag = tempAbs * java.lang.Math.sin(newArg);
		return this;
	}

	public Complex setComplexPol(double newAbs, double newArg) {
		this.real = newAbs * java.lang.Math.cos(newArg);
		this.imag = newAbs * java.lang.Math.sin(newArg);
		return this;
	}

	public Complex add(Complex second) {
		this.real += second.real;
		this.imag += second.imag;
		return this;
	}

	public Complex negate() {
		this.real = -this.real;
		this.imag = -this.imag;
		return this;
	}

	public Complex sustract(Complex second) {
		this.real = this.real - second.real;
		this.imag = this.imag - second.imag;
		return this;
	}

	public Complex invert() throws ArithmeticException {
		double tempReal = this.real;
		double tempImag = this.imag;
		double divider = (java.lang.Math.pow(tempReal, 2) + java.lang.Math.pow(
				tempImag, 2));
		if (divider == 0) {
			throw new ArithmeticException(
					"Intent to invert a zero complex number");
		} else {
			this.real = tempReal / divider;
			this.imag = -tempImag / divider;
			return this;
		}
	}

	public Complex multiply(Complex second) {
		double tempReal = this.real;
		double tempImag = this.imag;
		this.real = tempReal * second.real - tempImag * second.imag;
		this.imag = tempImag * second.real + tempReal * second.imag;
		return this;
	}

	public Complex multiply(double second) {
		this.imag = this.imag * second;
		this.real = this.real * second;
		return this;
	}

	public Complex divide(Complex second) throws ArithmeticException {
		double tempReal = this.real;
		double tempImag = this.imag;
		double divider = java.lang.Math.pow(second.real, 2)
				+ java.lang.Math.pow(second.imag, 2);
		if (divider == 0) {
			throw new ArithmeticException(
					"Intent to divide by a zero complex number");
		} else {
			this.real = ((tempReal * second.real) + (tempImag * second.imag))
					/ divider;
			this.imag = ((tempImag * second.real) - (tempReal * second.imag))
					/ divider;
			return this;
		}
	}

	public Complex divide(double second) throws ArithmeticException {
		if (second != 0) {
			this.imag = this.imag / second;
			this.real = this.real / second;
			return this;
		} else {
			throw new ArithmeticException(
					"Intent to divide by a zero real number");
		}
	}

	public String toString() {
		return (this.real + "+i" + this.imag);
	}

	public Complex clone() {
		return new Complex(this.real, this.imag);
	}

	public Complex conjugate() {
		this.imag = -this.imag;
		return this;
	}
}
