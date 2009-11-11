package de.uniol.inf.is.odysseus.objecttracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeConstraint;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * A class for multivariate stream processing.
 * @author Andre Bolles
 *
 */
@SuppressWarnings("unchecked")
public class MVRelationalTuple<T extends IProbability> extends RelationalTuple<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8921538607877809462L;
	private int[] measurementValuePositions;
	
	public int[] getMeasurementValuePositions() {
		return measurementValuePositions;
	}

	/**
	 * Erzeugt ein neues Object, anhand der Zeile und des Trennzeichens
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 * @param noOfAttribs
	 *            enthaelt die Anzahl der Attribute (Effizienzgr�nde)
	 */
	public MVRelationalTuple(SDFAttributeList schema, String line, char delimiter) {
		super(schema, line, delimiter);
//		if(this.getAttribute(3).equals(19.3906)){
//			try{
//				throw new Exception("Doppelt.");
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		this.findMeasurementValuePositions(schema);
	}

	/**
	 * Erzeugt ein neues leeres Object, zur Erzeugung von Zwischenergebnissen
	 * 
	 * @param attributeCount
	 *            enthaelt die Anzahl der Attribute die das Objekt speichern
	 *            koennen soll
	 */
	public MVRelationalTuple(SDFAttributeList schema) {
		super(schema);
		this.findMeasurementValuePositions(schema);
	}

	/**
	 * Erzeugt ein leeres Tuple ohne Schemainformationen
	 * 
	 * @param attributeCount
	 *            Anzahl der Attribute des Tuples
	 */
	public MVRelationalTuple(int attributeCount) {
		super(attributeCount);
	}

	public MVRelationalTuple(MVRelationalTuple copy) {
		super(copy);
//		if(this.getAttribute(3).equals(19.3906)){
//			try{
//				throw new Exception("Doppelt.");
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		if(copy.measurementValuePositions != null){
			this.measurementValuePositions = new int[copy.measurementValuePositions.length];
			System.arraycopy(copy.measurementValuePositions, 0, 
					this.measurementValuePositions, 0, 
					copy.measurementValuePositions.length);
		}
	}

	/**
	 * Erzeugt ein neues Tuple und ueberprueft die uebergebenen Attribute
	 * mittels des uebergebenen Schemas
	 * 
	 * @deprecated Only use this constructor for debugging.
	 * @param schema
	 *            Schema des neuen Tuples
	 * @param attributes
	 *            Attributbelegungen des neuen Tuples
	 */
	@Deprecated
	public MVRelationalTuple(SDFAttributeList schema, Object... attributes) {
		if (schema.size() != attributes.length) {
			throw new IllegalArgumentException("listsize doesn't match schema");
		}

		for (int i = 0; i < schema.size(); ++i) {
			if (!checkDataType(attributes[i], schema.get(i))) {
				throw new IllegalArgumentException("attribute " + i
						+ " has an invalid type");
			}
		}

		this.schema = schema;
		this.attributes = attributes.clone();
//		if(this.getAttribute(3).equals(19.3906)){
//			try{
//				throw new Exception("Doppelt.");
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		this.findMeasurementValuePositions(schema);
	}

	/**
	 * Erzeugt ein neues Tuple mit Attributen und ohne Schemainformationen
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public MVRelationalTuple(Object[] attributes) {
		super(attributes);
//		if(this.getAttribute(3).equals(19.3906)){
//			try{
//				throw new Exception("Doppelt.");
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
	}

	/**
	 * Creates a new tuple with the passed attributes. The schema
	 * will NOT be stored in the tuple, but it will be used
	 * to find the measurement value positions.
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public MVRelationalTuple(Object[] attributes, SDFAttributeList schema) {
		super(attributes);
//		if(this.getAttribute(3).equals(19.3906)){
//			try{
//				throw new Exception("Doppelt.");
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		this.findMeasurementValuePositions(schema);
	}
	
	private boolean checkDataType(Object object, SDFAttribute attribute) {
		if (object instanceof String) {
			return SDFDatatypes.isString(attribute.getDatatype());
		}
		
		if (object instanceof Integer) {
			if(attribute.getDatatype().equals("Integer")){
				return true;
			}
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isInteger(constraint)) {
					return true;
				}
			}
			return false;
		}
		if(object instanceof Long){
			if(attribute.getDatatype().equals("Long") || attribute.getDatatype().equals("Date")){
				return true;
			}
		}
		if (object instanceof Double) {
			if(attribute.getDatatype().equals("Double") || SDFDatatypes.isMeasurementValue(attribute.getDatatype())){
				return true;
			}
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isRational(constraint) || SDFDatatypeConstraints.isMeasurementValue(constraint)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	/**
	 * This method should be called by an operator after creating a new tuple.
	 * 
	 * @param schema By this schema it can be found out which attributes are measurement values.
	 */
	public void findMeasurementValuePositions(SDFAttributeList schema){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i<schema.size(); i++){
			SDFAttribute attr = schema.get(i);
			if(SDFDatatypes.isMeasurementValue(attr.getDatatype())){
				list.add(new Integer(i));	
			}
		}
		
		this.measurementValuePositions = new int[list.size()];
		for(int i = 0; i<this.measurementValuePositions.length; i++){
			this.measurementValuePositions[i] = list.get(i);
		}
	}
	
	/**
	 * erzeugen eines neuen Objektes, in dem nur die Attribute betrachtet
	 * werden, die in der attrList uebergeben werden, die Reihenfolge des neuen
	 * Objektes wird durch die Reihenfolge der Attribute im Array festgelegt
	 * Beispiel: attrList[1]=14,attrList[2]=12 erzeugt ein neues Objekt, welches
	 * die Attribute 14 und 12 enthaelt.
	 * 
	 * It is important to pass the original schema. Otherwise it is impossible
	 * to find out whether an attribute to put into the output is a measurement
	 * value or not.
	 * 
	 * The matrix matrix and vector b must agree to the number of measurement
	 * values and to the number of measurement attributes in attrList.
	 * If not matrix is passed a normal relational projection will be done. If
	 * no vector b is passed but a matrix the vector b will be handled as zero
	 * vector.
	 * 
	 * @param attrList
	 *            erzeugt ein neues Objekt das die Attribute der Positionen aus
	 *            attrList enth�lt
	 * @param matrix projection matrix for the measurement values
	 * @param b a vector for moving the projected measurement values matrix * mv + b
	 * @param originalSchema will be used to determine wether an attribute is a measurement value or not.
	 */
	public MVRelationalTuple restrict(int[] attrList, RealMatrix matrix, RealMatrix b, SDFAttributeList overwriteSchema, SDFAttributeList originalSchema) {
		
		MVRelationalTuple newAttrList = null;
		
		SDFAttributeList newSchema = overwriteSchema;
		if (overwriteSchema == null){
			// Schema anpassen		
			if (schema != null){
				newSchema = new SDFAttributeList();
				for (int i: attrList) {
					newSchema.add(getSchema().get(i));
				}
			}
		}
		
		// first, project the measurment values
		RealMatrix result = null;
		// only process matrix projection if a matrix is given
		if(matrix != null){
			double[] mv = new double[this.measurementValuePositions.length];
			for(int i = 0; i< this.measurementValuePositions.length; i++){
				mv[i] = (Double)this.getAttribute(this.measurementValuePositions[i]);
			}
			RealMatrix m = new RealMatrixImpl(mv);
			
			result = matrix.multiply(m);
			// if no vector be is given, do not process
			// an addition operation.
			if(b != null){
				result = result.add(b);
			}
		}
		
		if (this.schema == null && overwriteSchema == null) {
			newAttrList = new MVRelationalTuple(attrList.length);
		} else{
			newAttrList = new MVRelationalTuple(newSchema);
		}

		int mvCounter = 0;
		ArrayList<Integer> mvPos = new ArrayList<Integer>();
		for (int i = 0; i < attrList.length; i++) {
			boolean isMV = false;
			SDFAttribute attr = originalSchema.get(i);
			isMV = SDFDatatypes.isMeasurementValue(attr.getDatatype());
			
			Object curAttribute = null;
			// if no matrix is given the original
			// measurement value can be taken from
			// the schema.
			if(!isMV || matrix == null){
				curAttribute = this.attributes[attrList[i]];
			}
			else{
				// select the current measurementValue from
				// the result matrix and increment the counter
				// So the next time the next MV will be selected.
				curAttribute = result.getColumn(0)[mvCounter++];
				mvPos.add(i);
			}
			newAttrList.setAttribute(i, curAttribute);
		}
		
		newAttrList.measurementValuePositions = new int[mvPos.size()];
		for(int i = 0; i<mvPos.size(); i++){
			newAttrList.measurementValuePositions[i] = mvPos.get(i);
		}
		
		return newAttrList;
	}
	
	@Override
	public MVRelationalTuple<T> clone() {
		return new MVRelationalTuple<T>(this);
	}
	
}
