package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class AttributeMapping {

	/**
	 * @uml.property  name="inputPositions" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] inputPositions = null;

	/**
	 * @uml.property  name="outputPositions" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] outputPositions = null;

	/**
	 * @uml.property  name="conversion"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ConversionFunction conversion = null;

	public AttributeMapping(int[] inputPositions,
			ConversionFunction conversion, int[] outputPositions) {
		this.inputPositions = (int[]) inputPositions.clone();
		this.conversion = conversion;
		this.outputPositions = (int[]) outputPositions.clone();
	}

	
	public ConversionFunction getConversion() {
		return conversion;
	}


	public int[] getInputPositions() {
		return inputPositions;
	}


	public int[] getOutputPositions() {
		return outputPositions;
	}


	/**
	 * Wendet die definierte Konvertierungsfunktion auf die Positionen im
	 * Eingabevektor an, die vorher definiert wurden und schreibt das Ergebnis
	 * in den Ausgabevektor (muss bereits initial gefüllt sein!)
	 */
	public void process(List<Object> inputValues, List<Object> outputValues) {
		ArrayList<Object> in = new ArrayList<Object>();
		for (int i = 0; i < inputPositions.length; i++) {
			in.add(inputValues.get(inputPositions[i]));
		}
		List ret = conversion.process(in);
		Object[] retObj = ret.toArray();
		for (int i = 0; i < outputPositions.length; i++) {
			outputValues.set(outputPositions[i], retObj[i]);
		}
	}

	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		ret.append("\t "+printArray(this.inputPositions) + " " + this.conversion.getClass() + " "
				+ printArray(this.outputPositions));
		return ret.toString();
	}

	/**
     * @param is
     * @return
     */
    private String printArray(int[] is) {
        StringBuffer ret = new StringBuffer();
        for (int i=0;i<is.length;i++) ret.append(is[i]+" ");
        return ret.toString();
    }
    
    public boolean isOneToOne(){
    	return inputPositions.length == outputPositions.length;
    }
    
    public boolean isManyToOne(){
    	return (inputPositions.length > 1) &&  (outputPositions.length == 1);
    }

    public boolean isOneToMany(){
    	return (inputPositions.length == 1) &&  (outputPositions.length > 1);
    }

    public static void main(String[] args) {
		ArrayList<Object> globalAttributes = new ArrayList<Object>();
		// Die beiden Zusammenbauen
		globalAttributes.add(new String("Marco"));
		globalAttributes.add(new String("Grawunder"));
		globalAttributes.add(new Double(1000.0)); // Euro
		globalAttributes.add(new String("Dieser soll gleich bleiben"));
		globalAttributes.add(new String("Vorne Hinten"));

		ArrayList<Object> localAttributes = new ArrayList<Object>();
		localAttributes.add(new Boolean(false));
		localAttributes.add(new Boolean(false));
		localAttributes.add(new Boolean(false));
		localAttributes.add(new Boolean(false));
		localAttributes.add(new Boolean(false));

		AttributeMapping[] mappings = new AttributeMapping[4];
		int[] inputPos = { 0, 1 };
		int[] outputPos = { 0 };
		ConversionFunction conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#StringConcat");
		mappings[0] = new AttributeMapping(inputPos, conv, outputPos);
		int[] inputPos2 = { 2 };
		int[] outputPos2 = { 1 };
		conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_unit_mappings.sdf#EuroDollarTrafo");
		mappings[1] = new AttributeMapping(inputPos2, conv, outputPos2);
		int[] inputPos3 = { 3 };
		int[] outputPos3 = { 2 };
		conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#Identity");
		mappings[2] = new AttributeMapping(inputPos3, conv, outputPos3);

		int[] inputPos4 = { 4 };
		int[] outputPos4 = { 3, 4 };
		conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#BlankSplitter");
		mappings[3] = new AttributeMapping(inputPos4, conv, outputPos4);

		for (int i = 0; i < mappings.length; i++) {
			mappings[i].process(globalAttributes, localAttributes);
			System.out.print("globalAttributes " + globalAttributes);
			System.out.println(" localAttributes " + localAttributes);
		}

	}


}