/*
 * Created on 17.05.2006
 *
 */
package mg.dynaquest.support;

import java.util.ArrayList;
import java.util.List;

/** Klasse zum Bilden von Potenzmengen
 *  
 *  Potenzmengen lassen sich relativ einfach bilden in dem eine
 *  Zahl hochgezaehlt wird und die Zahl dabei als Bitvektor interpretiert 
 *  wird. Jede Teilmenge wird dann durch die Elemente gebildet, an deren
 *  Index eine 1 steht.
 *  
 *  Ansatz: Wandle int-Zahl in String in Bitdarstellung um, gehe durch den String und
 *  immer dann wenn eine 1 vorkommt, den enstprechenden Index aus der ursprünglichen
 *  Menge in die neue Menge aufnehmen.
 * 
 * @author Marco Grawunder
 *
 */

public class PowerSet {

    private PowerSet() {
    }
    
    static private String toIntString(int i, String fillString){
        String tmp = (fillString+Integer.toBinaryString(i));
        return tmp.substring(tmp.length()-fillString.length());
    }
    
    /** Erzeugen der Potenzmenge aus der Menge input 
     *  
     * @param input Die Eingabeobjekte. Hier wird eine Liste verwendet (Sonst muss Ordnung vorhanden sein)
     * @return die Potenzmenge, d.h. die Liste aller Teilmengen (inkl. der leeren Menge!)
     * Es ist garantiert, dass die Elemente in der Rückgabe entsprechend der Binärposition enthalten sind
     * d.h. 0 ist immer die leere Menge, 1 ist immer das erste Element etc.
     */
    
    @SuppressWarnings("unchecked")
    static public List<List> buildPowerSet(List input){        
        List ret = new ArrayList();
        // Damit die Zahlen alle gleich lang sind ...
        StringBuffer fillStringB = new StringBuffer();
        int bitStringLength = input.size();
        for (int i=0;i<bitStringLength;i++) fillStringB.append('0');
        String fillString = fillStringB.toString();
        
        for (int i=0;i<Math.pow(2,input.size());i++){
            List subSet = new ArrayList();
            String bitVector = toIntString(i,fillString);
            for (int j=0;j<bitVector.length();j++){
                if (bitVector.charAt(j)=='1'){
                    subSet.add(input.get(j));
                }
            }
            ret.add(subSet);
        }
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    public static List<List> hurz(List input, int length){
    	List ret = new ArrayList();
    	int base = input.size();
    	int[] elem = new int[Math.max(base,length)+1];
    	for (int j=0;j< Math.pow(base, length);j++){
    		int digits = (int) Math.ceil(Math.log(j+1)/Math.log(base));
    		int i=j;
	    	for (int d=digits;d>0;d--){
	    		elem[d-1] = (int) Math.round(Math.floor(i / Math.pow(base, d-1)));
	    		i -=  Math.floor(i/Math.pow(base, d-1))*Math.pow(base, d-1);
	    	}
	    	// Jetzt nicht die ganze Zahl betrachten, sondern nur die hintersten
	    	// length Stellen
	    	List subElem = new ArrayList();
	    	for (int z=0;z<length;z++){
	    		subElem.add(input.get(elem[z]));
	    	}
	    	ret.add(subElem);
    	}  	    	    	
    	return ret;
    }
    
    public static void main(String[] args) {
        List<Object> input = new ArrayList<Object>();
        input.add("A");
        input.add("B");
        input.add("C");
        input.add("D");
        input.add("E");
        
        //System.out.println(PowerSet.buildPowerSet(input));
        System.out.println(PowerSet.hurz(input,2));
    }





    
   
    
}
