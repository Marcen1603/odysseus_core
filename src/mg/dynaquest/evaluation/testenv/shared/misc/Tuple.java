/*
 * Created on 19.10.2005
 *
 */
package mg.dynaquest.evaluation.testenv.shared.misc;

import java.io.Serializable;


/**
 * Stellt ein Tupel, dar, wie es von einer Datenquelle (<code>DataSource</code>) geliefert wird. Die einzelnen Werte werden als Feld von  {@link Value  Value} -Objekten gespeichert. Indizes beginnen nach <i>Java</i>-Art mit <code>0</code>.
 * @author  Tobias Mueller (IP, MG) <tobias.mueller@polaris-neu.offis.uni-oldenburg.de>
 */
public class Tuple implements Serializable {
   
	/**
	 * Erhöht die Typsicherheit beim Serialisieren
	 */
	public static final long serialVersionUID = -3196198971078946293L;
	// die einzelnen Werte vom Typ Value
	/**
	 * @uml.property  name="data"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Value[] data;
	
	/**
	 * Erzeugt neues Tupel mit der entsprechenden Anzahl von Werten.
	 * 
	 * @param columns die Anzahl der Werte
	 */
	public Tuple(int columns)  {
		data = new Value[columns]; 
	}
	
	/**
	 * Setzt einen Wert in diesem Tupel.
	 * 
	 * @param index der index des zu setzenden Wertes, beginnend mit <code>0</code>
	 * @param val der neue Wert
	 */
	public void setValue(int index, Value val)  {
		data[index] = val;
	}
	
	/**
	 * Liefert einen Wert dieses Tupels.
	 * 
	 * @param index  der index des Wertes, beginnend mit <code>0</code>
	 * @return der Wert an der Stelle <code>index</code>
	 */
	public Value getValue(int index)  {
		return data[index];
	}
	
	/**
	 * Gibt die Anzahl der Werte dieses Tupels an.
	 * 
	 * @return die Anzahl der Werte
	 */
	public int columns()  {
		return data.length;
	}
	
	public String toString()  {
		String s = "Tuple[";
		for (int i = 0; i < data.length; i ++)  {
			s += data[i].toString();
			if (i < data.length - 1)
				s += ",";
		}
		s += "]";
		return s;
	}

    public String getValueString(char c, int[] expectedAttributeOrder) {
        StringBuffer b = new StringBuffer();
        if (expectedAttributeOrder != null){
	        for(int i: expectedAttributeOrder){
	        	b.append(data[i].getAsStringValue());
	        	b.append(c);
	        }
        }else{
	        for (Value v: data){
	            b.append(v.getAsStringValue());
	            b.append(c);
	        }
        }
        // Ohne das letzte Trennzeichen zurückliefern
        return b.substring(0,b.length()-1) .toString();
    }
}
