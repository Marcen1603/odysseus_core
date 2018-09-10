package de.uniol.inf.is.odysseus.complexnumber;

import de.uniol.inf.is.odysseus.core.IClone;

public class ComplexNumber implements IClone, Cloneable{

	final private double r;
	final private double i;

	public ComplexNumber(double real, double imaginary){
		this.r = real;
		this.i = imaginary;
		
	}	
	
	public ComplexNumber(ComplexNumber complexNumber) {
		this.r = complexNumber.r;
		this.i = complexNumber.i;
	}

	public double getReal(){
		return r;
	}
	
	public double getImaginary(){
		return i;
	}
	
	public ComplexNumber plus(ComplexNumber other){
		return new ComplexNumber(this.r+other.r,this.i+other.i);
	}

	public ComplexNumber minus(ComplexNumber other){
		return new ComplexNumber(this.r-other.r,this.i-other.i);
	}
	
	public ComplexNumber multiply(ComplexNumber other){
		return new ComplexNumber(this.r*other.r-this.i*other.i,this.r*other.i+this.i*other.r );
	}
	
	public ComplexNumber devide(ComplexNumber other){
		double denominator = (other.r*other.r+other.i*other.i);
		double r = (this.r*other.r+this.i*other.i)/denominator;
		double i = (this.i*other.r-this.r*other.i)/denominator;
		return new ComplexNumber(r,i);
	}
	
	public double abs(){
		return Math.sqrt(r*r+i*i);
	}
	
	@Override
	public ComplexNumber clone() {
		final ComplexNumber number = new ComplexNumber(this);
		return number;
	}

	public static ComplexNumber parseComplexNumber(String string) {
		String[] split = string.split("\\+");
		if (split.length == 2){
			double r = Double.parseDouble(split[0]);
			double i = Double.parseDouble(split[1].substring(0, split[1].indexOf("i")));
			return new ComplexNumber(r,i);
		}
		throw new NumberFormatException("Cannot parse "+string);
	}
	
	@Override
	public String toString() {
		return r+"+"+i+"i";
	}
	
	public static void main(String[] args) {
		ComplexNumber a = new ComplexNumber(3,2);
		ComplexNumber b = new ComplexNumber(5,5);
		System.out.println(a.plus(b));
		System.out.println(a.minus(b));
		ComplexNumber c = new ComplexNumber(2,5);
		ComplexNumber d = new ComplexNumber(3,7);
		System.out.println(c.multiply(d));
		System.out.println(c.devide(d));
		System.out.println(ComplexNumber.parseComplexNumber(c.devide(d).toString()));

		System.out.println(a.abs());
		System.out.println(b.abs());
		System.out.println(c.abs());
		System.out.println(d.abs());
	}


}
