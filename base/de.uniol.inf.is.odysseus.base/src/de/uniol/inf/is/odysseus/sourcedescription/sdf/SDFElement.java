package de.uniol.inf.is.odysseus.sourcedescription.sdf;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDF;

/**
 * @author  Marco Grawunder
 */
public abstract class SDFElement implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6076421686616330199L;
	/**
	 * @uml.property  name="uRI"
	 */
    private String URI = "null";
    /**
	 * @uml.property  name="qualName"
	 */
    private String qualName = "null";
    
    private String URIWithoutQualName;


	public SDFElement(String URI) {
		setURI(URI);
	}

    public SDFElement(SDFElement copy) {
    	this.URI = copy.URI;
    	this.qualName = copy.qualName;
    	this.URIWithoutQualName = copy.URIWithoutQualName;
	}

	/**
     * Nachtraegliches Festlegen der URI dieses
     * Elements. Sollte in der Regel nicht geschehen. Eventuell
     * wird diese Methode spaeter protected oder private (Factorys benoetigen
     * diese Eigenschaft!)
     * @uml.property name="uRI"
     * 
     */
    public void setURI(String URI) {
        this.URI = URI;
        if (URI != null){
	        int sharpPos = URI.indexOf("#");
	        if (sharpPos > 0){
	            this.qualName = URI.substring(sharpPos+1);
	            this.URIWithoutQualName = URI.substring(0,sharpPos);
	        }
        }else{
        	this.URI = "tmp:"+System.currentTimeMillis();
        }
    }

	public String getURI(boolean prettyPrint) {
		if (prettyPrint) {
			return SDF.prettyPrintURI(URI);
		} else {
			return URI;
		}
	}
	
	/**
	 * Liefert den qualifizierden Namen des Elements, d.h. der Teil der Uri nach dem ersten (!) Auftreten von #
	 * @return  String
	 * @uml.property  name="qualName"
	 */
	public String getQualName(){
	    return qualName;
	}

    // TODO: Warum ist das so??? ueberpruefen
	public String toString() {
		return getURI(true);
		//return getQualName();
	}
	
	public String getURI() {
		return URI;
	}
    
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * <p>
	 * The <code>equals</code> method implements an equivalence relation:
	 * <ul>
	 * <li>It is <i>reflexive </i>: for any reference value <code>x</code>,
	 * <code>x.equals(x)</code> should return <code>true</code>.
	 * <li>It is <i>symmetric </i>: for any reference values <code>x</code>
	 * and <code>y</code>,<code>x.equals(y)</code> should return
	 * <code>true</code> if and only if <code>y.equals(x)</code> returns
	 * <code>true</code>.
	 * <li>It is <i>transitive </i>: for any reference values <code>x</code>,
	 * <code>y</code>, and <code>z</code>, if <code>x.equals(y)</code>
	 * returns <code>true</code> and <code>y.equals(z)</code> returns
	 * <code>true</code>, then <code>x.equals(z)</code> should return
	 * <code>true</code>.
	 * <li>It is <i>consistent </i>: for any reference values <code>x</code>
	 * and <code>y</code>, multiple invocations of <tt>x.equals(y)</tt>
	 * consistently return <code>true</code> or consistently return
	 * <code>false</code>, provided no information used in
	 * <code>equals</code> comparisons on the object is modified.
	 * <li>For any non-null reference value <code>x</code>,
	 * <code>x.equals(null)</code> should return <code>false</code>.
	 * </ul>
	 * <p>
	 * The <tt>equals</tt> method for class <code>Object</code> implements
	 * the most discriminating possible equivalence relation on objects; that
	 * is, for any reference values <code>x</code> and <code>y</code>, this
	 * method returns <code>true</code> if and only if <code>x</code> and
	 * <code>y</code> refer to the same object (<code>x==y</code> has the
	 * value <code>true</code>).
	 * <p>
	 * Note that it is generally necessary to override the <tt>hashCode</tt>
	 * method whenever this method is overridden, so as to maintain the general
	 * contract for the <tt>hashCode</tt> method, which states that equal
	 * objects must have equal hash codes.
	 * 
	 * @param obj
	 *            the reference object with which to compare.
	 * @return <code>true</code> if this object is the same as the obj
	 *         argument; <code>false</code> otherwise.
	 * @see #hashCode()
	 * @see java.util.Hashtable
	 */
	public boolean equals(Object obj) {     
	    if (obj instanceof SDFElement){
	        return URI.equals(((SDFElement) obj).getURI(false));
	    }else if (obj instanceof String){
	        return URI.equals(obj);
	    }else{
	        return false;
	    }
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * <p>
	 * The general contract of <code>hashCode</code> is:
	 * <ul>
	 * <li>Whenever it is invoked on the same object more than once during an
	 * execution of a Java application, the <tt>hashCode</tt> method must
	 * consistently return the same integer, provided no information used in
	 * <tt>equals</tt> comparisons on the object is modified. This integer
	 * need not remain consistent from one execution of an application to
	 * another execution of the same application.
	 * <li>If two objects are equal according to the <tt>equals(Object)</tt>
	 * method, then calling the <code>hashCode</code> method on each of the
	 * two objects must produce the same integer result.
	 * <li>It is <em>not</em> required that if two objects are unequal
	 * according to the {@link java.lang.Object#equals(java.lang.Object)}
	 * method, then calling the <tt>hashCode</tt> method on each of the two
	 * objects must produce distinct integer results. However, the programmer
	 * should be aware that producing distinct integer results for unequal
	 * objects may improve the performance of hashtables.
	 * </ul>
	 * <p>
	 * As much as is reasonably practical, the hashCode method defined by class
	 * <tt>Object</tt> does return distinct integers for distinct objects.
	 * (This is typically implemented by converting the internal address of the
	 * object into an integer, but this implementation technique is not required
	 * by the Java <font size="-2"> <sup>TM </sup> </font> programming
	 * language.)
	 * 
	 * @return a hash code value for this object.
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @see java.util.Hashtable
	 */
	public int hashCode() {
		return URI.hashCode();
	}

	public String getURIWithoutQualName() {
		return URIWithoutQualName;
	}
		
	
}