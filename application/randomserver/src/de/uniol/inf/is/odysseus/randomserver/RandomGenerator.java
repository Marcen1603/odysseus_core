package de.uniol.inf.is.odysseus.randomserver;

import java.util.Random;

public class RandomGenerator {

	final static Random RANDOMIZER = new Random();
	 
    public static double randomDouble(double lowerBound, double upperBound) {
    	if(upperBound<lowerBound || lowerBound < 0){
    		throw new IllegalArgumentException();
    	}
        return lowerBound + (upperBound - lowerBound) * RANDOMIZER.nextDouble();
      
    }
 
    public static int randomInt(int minInclusive, int maxExclusive) {
    	if(maxExclusive<minInclusive || minInclusive<0){
    		throw new IllegalArgumentException();
    	}
        return minInclusive + RANDOMIZER.nextInt(maxExclusive - minInclusive);
    }
    
    
    public static long randomLong(long lowerBound, long upperBound){
    	if(upperBound<lowerBound || lowerBound<0){
    		throw new IllegalArgumentException();
    	}
    	return lowerBound + (upperBound - lowerBound) * RANDOMIZER.nextLong();
    }
    
    public static String randomString(int minLength, int maxLength){
    	if(maxLength<minLength || minLength<0){
    		throw new IllegalArgumentException();
    	}
    	// some words...
    	String[] parts = {"", "W", "LD", "TUR", "WROT", "SPEED", "GERMAN", "ILLEGAL", "ROTATION", "ARGUMENTS", "PARAMETERS"};
    	int length = randomInt(minLength, maxLength);
    	if(length<=10){
    		return parts[length];
    	}else{
    		int currentLength = 0;
    		String value = "";
    		while(currentLength<length){
    			int nextMax = 11;
    			if(nextMax + currentLength > length){
    				nextMax = length - currentLength;
    			}
    			int nextRand = randomInt(0, nextMax);
    			value = value + " " + parts[nextRand];
    			currentLength = currentLength+nextRand+1;    			
    		}
    		return value;
    	}        
    }
    
   
    
    
}
