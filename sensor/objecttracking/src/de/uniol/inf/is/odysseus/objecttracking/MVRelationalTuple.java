/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.objecttracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * A class for multivariate stream processing.
 * 
 * @author Andre Bolles
 * 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class MVRelationalTuple<T extends IProbability> extends RelationalTuple<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921538607877809462L;
	private int[] measurementValuePositions;

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
	public MVRelationalTuple(SDFSchema schema, String line, char delimiter) {
		super();
		// super(schema, line, delimiter);
		// if(this.getAttribute(3).equals(19.3906)){
		// try{
		// throw new Exception("Doppelt.");
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		this.attributes = splittLineToAttributes(line, delimiter, schema);

		this.findMeasurementValuePositions(schema);
	}

	/**
	 * Erzeugt ein neues leeres Object, zur Erzeugung von Zwischenergebnissen
	 * 
	 * @param attributeCount
	 *            enthaelt die Anzahl der Attribute die das Objekt speichern
	 *            koennen soll
	 */
	public MVRelationalTuple(SDFSchema schema) {
		super();
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
		// if(this.getAttribute(3).equals(19.3906)){
		// try{
		// throw new Exception("Doppelt.");
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		if (copy.measurementValuePositions != null) {
			this.measurementValuePositions = new int[copy.measurementValuePositions.length];
			System.arraycopy(copy.measurementValuePositions, 0, this.measurementValuePositions, 0, copy.measurementValuePositions.length);
		}

		// deep copy for objectrelationals
		for (int i = 0; i < size(); i++) {
			Object attribute = getAttribute(i);
			if (attribute instanceof MVRelationalTuple) {
				setAttribute(i, new MVRelationalTuple<T>((MVRelationalTuple<T>) attribute)); // recursive
																								// copy
			} else if (attribute instanceof List) {
				setAttribute(i, copyList((List<Object>) attribute)); // recursive
																		// copy

			}
		}
	}

	private List<Object> copyList(List<Object> attribute) {
		List<Object> oldList = attribute;
		List<Object> newList = new ArrayList<Object>(oldList.size());
		for (Object obj : oldList) {
			if (obj instanceof MVRelationalTuple) {
				newList.add(new MVRelationalTuple((MVRelationalTuple) obj));
			} else if (obj instanceof List) {
				newList.add(copyList((List<Object>) obj));
			}
		}
		return newList;
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
	// @Deprecated
	// public MVRelationalTuple(SDFSchema schema, Object... attributes) {
	// if (schema.size() != attributes.length) {
	// throw new IllegalArgumentException("listsize doesn't match schema");
	// }
	//
	// for (int i = 0; i < schema.size(); ++i) {
	// if (!checkDataType(attributes[i], schema.get(i))) {
	// throw new IllegalArgumentException("attribute " + i
	// + " has an invalid type");
	// }
	// }
	//
	// // this.schema = schema;
	// this.attributes = attributes.clone();
	// // if(this.getAttribute(3).equals(19.3906)){
	// // try{
	// // throw new Exception("Doppelt.");
	// // }catch(Exception e){
	// // e.printStackTrace();
	// // }
	// // }
	// this.findMeasurementValuePositions(schema);
	// }

	/**
	 * Erzeugt ein neues Tuple mit Attributen und ohne Schemainformationen
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public MVRelationalTuple(Object[] attributes) {
		super(attributes);
		// if(this.getAttribute(3).equals(19.3906)){
		// try{
		// throw new Exception("Doppelt.");
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
	}

	/**
	 * Creates a new tuple with the passed attributes. The schema will NOT be
	 * stored in the tuple, but it will be used to find the measurement value
	 * positions.
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public MVRelationalTuple(Object[] attributes, SDFSchema schema) {
		super(attributes);
		// if(this.getAttribute(3).equals(19.3906)){
		// try{
		// throw new Exception("Doppelt.");
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		this.findMeasurementValuePositions(schema);
	}

	// public Object getORAttribute(int[] path ) {
	// Object actualAttribute = null;
	// Object[] actualList = getAttributes();
	// for( int i = 0; i < path.length; i++ ) {
	// actualAttribute = actualList[path[i]];
	// if( actualAttribute instanceof MVRelationalTuple<?>) {
	// actualList = ((MVRelationalTuple<?>)actualAttribute).getAttributes();
	// }
	// }
	//
	// return actualAttribute;
	// }

//	private boolean checkDataType(Object object, SDFAttribute attribute) {
//
//		if (object instanceof SetEntry[]) {
//			return SDFORDatatypes.isSet(attribute.getDatatype());
//		}
//
//		if (object instanceof String) {
//			return SDFDatatypes.isString(attribute.getDatatype());
//		}
//
//		if (object instanceof Integer) {
//			if (attribute.getDatatype().equals("Integer")) {
//				return true;
//			}
//			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints().iterator();
//
//			while (i.hasNext()) {
//				SDFDatatypeConstraint constraint = i.next();
//				if (SDFDatatypeConstraints.isInteger(constraint)) {
//					return true;
//				}
//			}
//			return false;
//		}
//		if (object instanceof Long) {
//			if (attribute.getDatatype().equals("Long") || attribute.getDatatype().equals("Date")) {
//				return true;
//			}
//		}
//		if (object instanceof Double) {
//			if (attribute.getDatatype().equals("Double") || SDFDatatypes.isMeasurementValue(attribute.getDatatype())) {
//				return true;
//			}
//			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints().iterator();
//			while (i.hasNext()) {
//				SDFDatatypeConstraint constraint = i.next();
//				if (SDFDatatypeConstraints.isRational(constraint) || SDFDatatypeConstraints.isMeasurementValue(constraint)) {
//					return true;
//				}
//			}
//			return false;
//		}
//		return false;
//	}

	/**
	 * This method should be called by an operator after creating a new tuple.
	 * 
	 * @param schema
	 *            By this schema it can be found out which attributes are
	 *            measurement values.
	 * @deprecated This can be done before query runtime. Use
	 *             setMeasurementValuePositions instead.
	 */
	@Deprecated
	public void findMeasurementValuePositions(SDFSchema schema) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < schema.size(); i++) {
			SDFAttribute attr = schema.get(i);
			if (attr.getDatatype().isMeasurementValue()) {
				list.add(new Integer(i));
			}
		}

		this.measurementValuePositions = new int[list.size()];
		for (int i = 0; i < this.measurementValuePositions.length; i++) {
			this.measurementValuePositions[i] = list.get(i);
		}
	}

	public int[] getMeasurementValuePositions() {
		return measurementValuePositions;
	}

	public void setMeasurementValuePositions(int[] mvPos) {
		this.measurementValuePositions = mvPos;
	}

	public double[] getMeasurementValues() {
		double[] mvs = new double[this.measurementValuePositions.length];
		for (int i = 0; i < this.measurementValuePositions.length; i++) {
			mvs[i] = ((Double) this.attributes[this.measurementValuePositions[i]]).doubleValue();
		}
		return mvs;
	}

//	/**
//	 * erzeugen eines neuen Objektes, in dem nur die Attribute betrachtet
//	 * werden, die in der attrList uebergeben werden, die Reihenfolge des neuen
//	 * Objektes wird durch die Reihenfolge der Attribute im Array festgelegt
//	 * Beispiel: attrList[1]=14,attrList[2]=12 erzeugt ein neues Objekt, welches
//	 * die Attribute 14 und 12 enthaelt.
//	 * 
//	 * It is important to pass the original schema. Otherwise it is impossible
//	 * to find out whether an attribute to put into the output is a measurement
//	 * value or not.
//	 * 
//	 * The matrix matrix and vector b must agree to the number of measurement
//	 * values and to the number of measurement attributes in attrList. If not
//	 * matrix is passed a normal relational projection will be done. If no
//	 * vector b is passed but a matrix the vector b will be handled as zero
//	 * vector.
//	 * 
//	 * @param attrList
//	 *            erzeugt ein neues Objekt das die Attribute der Positionen aus
//	 *            attrList enth�lt
//	 * @param matrix
//	 *            projection matrix for the measurement values
//	 * @param b
//	 *            a vector for moving the projected measurement values matrix *
//	 *            mv + b
//	 * @param originalSchema
//	 *            will be used to determine wether an attribute is a measurement
//	 *            value or not.
//	 * @deprecated I don't a real application for this method. I think, simply
//	 *             reducing to the values in the restrict list should be
//	 *             adequate.
//	 */
//	@Deprecated
//	public MVRelationalTuple restrict(int[] attrList, RealMatrix matrix, RealMatrix b, SDFSchema overwriteSchema, SDFSchema originalSchema) {
//
//		MVRelationalTuple newAttrList = null;
//
//		// SDFSchema newSchema = overwriteSchema;
//		// if (overwriteSchema == null){
//		// // Schema anpassen
//		// if (schema != null){
//		// newSchema = new SDFSchema();
//		// for (int i: attrList) {
//		// newSchema.add(getSchema().get(i));
//		// }
//		// }
//		// }
//
//		// first, project the measurment values
//		RealMatrix result = null;
//		// only process matrix projection if a matrix is given
//		if (matrix != null) {
//			double[] mv = new double[this.measurementValuePositions.length];
//			for (int i = 0; i < this.measurementValuePositions.length; i++) {
//				mv[i] = (Double) this.getAttribute(this.measurementValuePositions[i]);
//			}
//			RealMatrix m = new RealMatrixImpl(mv);
//
//			result = matrix.multiply(m);
//			// if no vector be is given, do not process
//			// an addition operation.
//			if (b != null) {
//				result = result.add(b);
//			}
//		}
//
//		newAttrList = new MVRelationalTuple(attrList.length);
//
//		int mvCounter = 0;
//		ArrayList<Integer> mvPos = new ArrayList<Integer>();
//		for (int i = 0; i < attrList.length; i++) {
//			boolean isMV = false;
//			SDFAttribute attr = originalSchema.get(i);
//			isMV = SDFDatatypes.isMeasurementValue(attr.getDatatype());
//
//			Object curAttribute = null;
//			// if no matrix is given the original
//			// measurement value can be taken from
//			// the schema.
//			if (!isMV || matrix == null) {
//				curAttribute = this.attributes[attrList[i]];
//			} else {
//				// select the current measurementValue from
//				// the result matrix and increment the counter
//				// So the next time the next MV will be selected.
//				curAttribute = result.getColumn(0)[mvCounter++];
//				mvPos.add(i);
//			}
//			newAttrList.setAttribute(i, curAttribute);
//		}
//
//		newAttrList.measurementValuePositions = new int[mvPos.size()];
//		for (int i = 0; i < mvPos.size(); i++) {
//			newAttrList.measurementValuePositions[i] = mvPos.get(i);
//		}
//
//		return newAttrList;
//	}

	/**
	 * 
	 * @param restrictList
	 *            This list is used to identify the original attribute positions
	 * @param restrictMatrix
	 *            This matrix has been calculated out of the restrict list and
	 *            the measurement value positions. It is used to transform the
	 *            covariance matrix of this tuple.
	 * @param createNew
	 *            If true, a new tuple will be created, else this one will be
	 *            modified.
	 * @return
	 */
	public MVRelationalTuple<T> restrict(int[] restrictList, RealMatrix restrictMatrix, boolean createNew) {
		if (!createNew) {
			MVRelationalTuple<T> modifiedAttrs = (MVRelationalTuple<T>) super.restrict(restrictList, createNew);
			/**
			 * If no measurement attributes are in the output schema, the
			 * restrictMatrix will be null. In this case nothing has to be done.
			 */
			if (restrictMatrix != null) {
				double[][] modifiedCovariance = restrictMatrix.multiply(new RealMatrixImpl(modifiedAttrs.getMetadata().getCovariance())).multiply(restrictMatrix.transpose()).getData();
				modifiedAttrs.getMetadata().setCovariance(modifiedCovariance);
			}
			return modifiedAttrs;
		} else {
			MVRelationalTuple<T> newTuple = new MVRelationalTuple<T>(restrictList.length);
			Object[] newAttrs = new Object[restrictList.length];

			for (int i = 0; i < restrictList.length; i++) {
				newAttrs[i] = this.attributes[restrictList[i]];
			}
			newTuple.setAttributes(newAttrs);
			newTuple.setMetadata((T) this.getMetadata().clone());

			/**
			 * If no measurement attributes are in the output schema, the
			 * restrictMatrix will be null. In this case nothing has to be done.
			 */
			if (restrictMatrix != null) {
				double[][] modifiedCovariance = restrictMatrix.multiply(new RealMatrixImpl(this.getMetadata().getCovariance())).multiply(restrictMatrix.transpose()).getData();

				newTuple.getMetadata().setCovariance(modifiedCovariance);
			}

			return newTuple;

		}
	}

	@Override
	public MVRelationalTuple<T> clone() {
		return new MVRelationalTuple<T>(this);
	}

	public final boolean equals(MVRelationalTuple<T> o) {
		return this.compareTo((MVRelationalTuple<T>) o) == 0;
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	public int compareTo(MVRelationalTuple<T> tuple) {
		int min = tuple.size();
		if (min > this.attributes.length) {
			min = this.attributes.length;
		}
		int compare = 0;
		int i = 0;
		for (i = 0; i < min && compare == 0; i++) {
			try {
				Object objectB = tuple.getAttribute(i);				
				if (objectB instanceof Collection) {
					int containedInA = 0;

					Collection setA = (Collection) this.attributes[i];
					Collection setB = (Collection) objectB;

					for (Object b : setB) {
						if (setA.contains(b)) {
							containedInA++;
						}
					}
					if (containedInA == setB.size()) {
						if (containedInA == setA.size()) {
							return 0;
						} else {
							return 1;
						}
					} else {
						return -1;
					}
				} else {
					compare = ((Comparable) this.attributes[i]).compareTo(tuple.getAttribute(i));
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		if (compare < 0) {
			compare = (-1) * i;
		}
		if (compare > 0) {
			compare = i;
		}
		return compare;
	}

	/**
	 * Splittet die Zeile anhand des Trennzeichens in ein Array von Strings mit
	 * den jeweiligen Attributen auf
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 * @param noOfAttribs
	 *            enthaelt die Anzahl der Attribute
	 * @returns Array mit den Attributen
	 */
	protected final static Object[] splittLineToAttributes(final String line, final char delimiter, final SDFSchema schema) {
		String[] attributes = line.split(Pattern.quote(new String(new char[] { delimiter })));
		// Pattern p = Pattern.compile("(.*)[(" + delimiter + ".*)*");
		// Matcher m = p.matcher(line);
		int count = attributes.length;
		if (count != schema.size()) {
			throw new IllegalArgumentException("invalid number of attributes: got " + count + " expected " + schema.size());
		}
		//
		Object[] tokens = new Object[attributes.length];
		for (int i = 0; i < attributes.length; ++i) {
			tokens[i] = convertAttribute(attributes[i], schema.get(i));
		}
		return tokens;
	}

	private final static Object convertAttribute(String stringValue, SDFAttribute attribute) {
		if (attribute.getDatatype().isString()) {
			return stringValue;
		}

		if (attribute.getDatatype().getURI(false) == "Integer") {
			return Integer.parseInt(stringValue);
		}
		if (attribute.getDatatype().getURI(false) == "Double") {
			return Double.parseDouble(stringValue);
		}
		// TODO richtig machen mit den datentypen
		if (attribute.getDatatype().isInteger()) {
			return Integer.parseInt(stringValue);
		}
		else if(attribute.getDatatype().isDouble() || attribute.getDatatype().isFloat()){
			return Double.parseDouble(stringValue);
		}

		throw new IllegalArgumentException("attributes of type " + attribute.getDatatype() + " can't be used with " + RelationalTuple.class);
	}
}
