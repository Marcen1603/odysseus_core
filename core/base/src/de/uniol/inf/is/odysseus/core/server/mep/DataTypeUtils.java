package de.uniol.inf.is.odysseus.core.server.mep;

public class DataTypeUtils{

	public static Class<?> min(Class<?> left, Class<?> right){
		
		if(left == Object.class || left == String.class && right != Object.class){
			return right; // everything or more specific than string
		}
		if(left == Number.class){
			if(right == String.class || right == Object.class){
				return left;
			}
			return right; // Number, Integer, Float, Double 
		}
		if(left == Integer.class && !(right == Float.class || right == Double.class)){
			return left;
		}
		if(left == Integer.class && (right == Float.class || right == Double.class)){
			return right;
		}
		if(left == Float.class && !(right == Double.class)){
			return left;
		}
		
		if(left == Double.class){
			return left;
		}
		throw new IllegalArgumentException("Datatypes are not comparable: left=" + left + " | right=" +right);
	}
	
	public static boolean compatible(Class<?> left, Class<?> right){
		if(left == right){
			return true;
		}
		// String and Object are only compatible to themselfes
		else if(left == String.class || left == Object.class ){
			return false;
		}
		else if(left == Number.class && right == Number.class ||
				left == Number.class && right == Double.class ||
				left == Number.class && right == Integer.class ||
				left == Number.class && right == Float.class){
			return true;
		}
		else if(left == Float.class && right == Number.class ||
				left == Float.class && right == Float.class ||
				left == Float.class && right == Integer.class ||
				left == Float.class && right == Double.class){
			return true;
		}
		else if(left == Double.class && right == Number.class ||
				left == Double.class && right == Float.class ||
				left == Double.class && right == Integer.class ||
				left == Double.class && right == Double.class){
			return true;
		}
		
		throw new IllegalArgumentException("Datatypes are not comparable: left=" + left + " | right=" +right);
	}
}
