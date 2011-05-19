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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeConstraint;

public class SDFDatatypeConstraints extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_datatype_constraints.sdf#";

	

	public static String filename = baseDir + "sdf_datatype_constraints.sdf";

	public static String namespaceName = "sdfDatatypeConstraints";

	public static String hasMaxLength = baseURI + "hasMaxLength";

	public static String hasRange = baseURI + "hasRange";

	public static String granularity = baseURI + "Granularity";
	
	public static String hasCovarianceMatrix = baseURI + "hasCovarianceMatrix";
	
	public static String dateFormat = baseURI + "dateFormat";
	
	public static String integerNumbers = baseURI + "IntegerNumbers";

	public static String rationalNumbers = baseURI + "RationalNumbers";
	
	public static String measurementValues = baseURI + "MeasurementValues";

	public static String hasGranularity = baseURI + "hasGranularity";
	
	public static boolean isInteger(SDFDatatypeConstraint constraint) {
		return constraint.getURI(false).equals(integerNumbers);
	}
	
	public static boolean isRational(SDFDatatypeConstraint type) {
		return type.getURI(false).equals(rationalNumbers);
	}
	
	public static boolean isMeasurementValue(SDFDatatypeConstraint type){
		return type.getURI(false).equals(measurementValues);
	}
	
	public static boolean isDateFormat(SDFDatatypeConstraint type){
		return type.getURI(false).equals(dateFormat);
	}

	protected SDFDatatypeConstraints() {
		super.baseUri = SDFDatatypes.baseURI;
		super.namespaceName = SDFDatatypes.namespaceName;
	}
}
