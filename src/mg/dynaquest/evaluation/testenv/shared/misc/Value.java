package mg.dynaquest.evaluation.testenv.shared.misc;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Speichert verschiedene Datentypen, auf die dann schnell zugegriffen werden kann. Welcher Datentyp gerade gehalten wird, ist durch  {@link #getType()  getType()}  abfragbar. Dient dazu, Werte aus einer Datenbankanfrage, deren Typ unterschiedlich sein kann, einheitlich zu behandeln.
 * @author  <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 */
public class Value  implements Serializable {
	
	/**
	 * Erhöht die Typsicherheit beim Serialisieren.
	 */
	public static final long serialVersionUID = -6701683418704732033L;
	
	/**
	 * Der gehaltene Wert ist vom Typ <code>String</code>
	 */
	public static final int STRING = 0;
	/**
	 * Der gehaltene Wert ist vom Typ <code>int</code>
	 */
	public static final int INT = 1;
	/**
	 * Der gehaltene Wert ist vom Typ <code>long</code>
	 */
	public static final int LONG = 2;
	/**
	 * Der gehaltene Wert ist vom Typ <code>float</code>
	 */
	public static final int FLOAT = 3;
	/**
	 * Der gehaltene Wert ist vom Typ <code>double</code>
	 */
	public static final int DOUBLE = 4;
	/**
	 * Der gehaltene Wert ist vom Typ <code>BigDecimal</code>
	 */
	public static final int BIGDEC = 5;
	
	// enthält eine der obigen Konstanten
	/**
	 * @uml.property  name="type"
	 */
	private int type;
	
	/**
	 * @uml.property  name="sVal"
	 */
	private String sVal = null;
	/**
	 * @uml.property  name="lVal"
	 */
	private long lVal = 0;
	/**
	 * @uml.property  name="iVal"
	 */
	private int iVal = 0;
	/**
	 * @uml.property  name="fVal"
	 */
	private float fVal = 0;
	/**
	 * @uml.property  name="dVal"
	 */
	private double dVal = 0;
	/**
	 * @uml.property  name="bVal"
	 */
	private BigDecimal bVal = null;
	
	/**
	 * @uml.property  name="attr"
	 */
	private String attr;
	
	private String operator;
	
	/**
	 * Erzeugt einen neues <code>Value</code>-Objekt, welches einen 
	 * <code>String</code> hält.
	 * 
	 * @param val der gehaltene Wert
	 */
	public Value(String val, String attr, String operator)  {
		this.type = STRING;
		this.sVal = val;
		this.attr = attr;
		this.operator = operator;
	}
	
	/**
	 * Erzeugt einen neues <code>Value</code>-Objekt, welches einen 
	 * <code>long</code>-Wert hält.
	 * 
	 * @param val der gehaltene Wert
	 */
	public Value(long val, String attr, String operator)  {
		this.type = LONG;
		this.lVal = val;
		this.attr = attr;
		this.operator = operator;
	}
	
	/**
	 * Erzeugt einen neues <code>Value</code>-Objekt, welches einen 
	 * <code>int</code>-Wert hält.
	 * 
	 * @param val der gehaltene Wert
	 */
	public Value(int val, String attr, String operator)  {
		this.type = INT;
		this.iVal = val;
		this.attr = attr;
		this.operator = operator;
	}
	
	/**
	 * Erzeugt einen neues <code>Value</code>-Objekt, welches einen 
	 * <code>float</code>-Wert hält.
	 * 
	 * @param val der gehaltene Wert
	 */
	public Value(float val, String attr, String operator)  {
		this.type = FLOAT;
		this.fVal = val;
		this.attr = attr;
		this.operator = operator;
	}
	
	/**
	 * Erzeugt einen neues <code>Value</code>-Objekt, welches einen 
	 * <code>double</code>-Wert hält.
	 * 
	 * @param val der gehaltene Wert
	 */
	public Value(double val, String attr, String operator)  {
		this.type = DOUBLE;
		this.dVal = val;
		this.attr = attr;
		this.operator = operator;
	}
	
	/**
	 * Erzeugt einen neues <code>Value</code>-Objekt, welches ein
	 * <code>BigDecimal</code>-Objekt hält.
	 * 
	 * @param val der gehaltene Wert
	 */
	public Value(BigDecimal val, String attr, String operator)  {
		this.type = BIGDEC;
		this.bVal = val;
		this.attr = attr;
		this.operator = operator;
	}
	
	/**
	 * Liefert den <code>double</code>-Wert.  Nur sinnvoll wenn  {@link #getType()  getType()}  =  {@link #DOUBLE  DOUBLE} .  
	 * @return  den double-Wert oder <code>0</code>, falls <code>getType() != DOUBLE</code>
	 * @uml.property  name="dVal"
	 */
	public double getDVal() {
		return dVal;
	}
	
	/**
	 * Liefert den <code>float</code>-Wert.  Nur sinnvoll wenn  {@link #getType()  getType()}  =  {@link #FLOAT  FLOAT} .  
	 * @return  den FLOAT-Wert oder <code>0</code>, falls <code>getType() != FLOAT</code>
	 * @uml.property  name="fVal"
	 */
	public float getFVal() {
		return fVal;
	}
	
	/**
	 * Liefert den <code>int</code>-Wert.  Nur sinnvoll wenn  {@link #getType()  getType()}  =  {@link #INT  INT} .  
	 * @return  den double-Wert oder <code>0</code>, falls <code>getType() != INT</code>
	 * @uml.property  name="iVal"
	 */
	public int getIVal() {
		return iVal;
	}
	
	/**
	 * Liefert den <code>long</code>-Wert.  Nur sinnvoll wenn  {@link #getType()  getType()}  =  {@link #LONG  LONG} .  
	 * @return  den double-Wert oder <code>0</code>, falls <code>getType() != LONG</code>
	 * @uml.property  name="lVal"
	 */
	public long getLVal() {
		return lVal;
	}
	
	/**
	 * Liefert den <code>String</code>-Wert.  Nur sinnvoll wenn  {@link #getType()  getType()}  =  {@link #STRING  STRING} .  
	 * @return  den String-Wert oder <code>null</code>, falls <code>getType() != STRING</code>
	 * @uml.property  name="sVal"
	 */
	public String getSVal() {
		return sVal;
	}
	
	/**
	 * Liefert den <code>BigDecimal</code>-Wert.  Nur sinnvoll wenn  {@link #getType()  getType()}  =  {@link #BIGDEC  BIGDEC} .  
	 * @return  den double-Wert oder <code>null</code>, falls <code>getType() != DOUBLE</code>
	 * @uml.property  name="bVal"
	 */
	public BigDecimal getBVal()  {
		return bVal;
	}
	
	/**
	 * Liefert den gehaltenen Datentyp. Sollte vor jeder getter-Methode aufgerufen werden, damit diese sinnvoll verwendet werden können.
	 * @return  den Typ des gehaltenen Datums. Mögliche Werte: {@link #STRING  STRING}  ,  {@link #INT  INT}  ,  {@link #LONG  LONG}  ,  {@link #FLOAT  FLOAT}  ,  {@link #DOUBLE  DOUBLE}  ,  {@link #BIGDEC  BIGDEC}
	 * @uml.property  name="type"
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @return  the attr
	 * @uml.property  name="attr"
	 */
	public String getAttr(){
		return attr;
	}
	
	public String getOperator(){
		return operator;
	}
	
	public String toString()  {
		StringBuffer ret = new StringBuffer("");
		switch (type)  {
		case STRING: 
			ret.append(sVal).append("(s)");
			break;
		case INT:
			ret.append(iVal).append("(i)");
			break;
		case LONG:
			ret.append(lVal).append("(l)");
			break;
		case FLOAT:
			ret.append(fVal).append("(f)");
			break;
		case DOUBLE:
			ret.append(dVal).append("(d)");
			break;
		case BIGDEC:
			ret.append(bVal.toString()).append("b");
			break;
		}
		if (attr != null){
			ret.append("[").append(attr).append("]");
		}
		if (operator != null){
			ret.append("[").append(operator).append("]");
		}
		return ret.toString();
	}

    public Object getAsStringValue() {
        switch (type)  {
        case STRING: 
            return clearPipeSymbol(sVal);
        case INT:
            return iVal + "";
        case LONG:
            return lVal + "";
        case FLOAT:
            return fVal + "";
        case DOUBLE:
            return dVal + "";
        case BIGDEC:
            return bVal.toString() + "";
        default:
            return "";
        }
    }

	private String clearPipeSymbol(String val) {
		return val.replace('|', '_');
	}

	public void setAttribute(String attribute) {
		this.attr = attribute;
		
	}
}
