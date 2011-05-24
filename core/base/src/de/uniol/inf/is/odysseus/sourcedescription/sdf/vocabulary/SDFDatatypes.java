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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class SDFDatatypes extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_datatypes.sdf#";

	public static String filename = baseDir + "sdf_datatypes.sdf";

	public static String namespaceName = "sdfDatatypes";

	public static String sDFDatatype = baseURI + "SDFDatatype";

	public static String sDFBaseDatatype = baseURI + "SDFBaseDatatype";

	public static String string = baseURI + "String";

	public static String number = baseURI + "Number";

	public static String intervall = baseURI + "Intervall";

	public static String measurementValue = baseURI + "MeasurementValue";

	public static String list = baseURI + "List";

	public static String hasBaseDatatype = baseURI + "hasBaseDatatype";

	public static String hasIntervallStartOpen = baseURI + "hasIntervallStartOpen";

	public static String hasIntervallStartClose = baseURI + "hasIntervallStartClose";

	public static String hasIntervallEndOpen = baseURI + "hasIntervallEndOpen";

	public static String hasIntervallEndClose = baseURI + "hasIntervallEndClose";

	public static boolean isNumerical(de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype type) {
		return type.getURI(false).equals("Long") || type.getURI(false).equals("Integer") || type.getURI(false).equals("Double") || type.getURI(false).equals("MV");
	}

	public static boolean isInteger(SDFDatatype type) {
		return type.getURI(false).equals("Integer");
	}

	public static boolean isLong(SDFDatatype type) {
		return type.getURI(false).equals("Long");
	}

	public static boolean isDouble(SDFDatatype type) {
		return type.getURI(false).equals("Double");
	}

	public static boolean isMeasurementValue(de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype type) {
		String uri = type.getURI(false);
		if( uri.startsWith("MV")) return true;
		return type.getURI(false).equals("MV");
	}

	public static boolean isString(de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype type) {
		return type.getURI(false).equals("String");
	}

	public static boolean isDate(SDFDatatype type) {
		return type.getURI(false).equals("Date");
	}
	
	public static boolean isSpatial(SDFDatatype type){
		String uri = type.getURI(false);
		return uri.equalsIgnoreCase("Point2D") ||
				uri.equalsIgnoreCase("Point3D") ||
				uri.equalsIgnoreCase("Line") ||
				uri.equalsIgnoreCase("Polygon") ||
				uri.equalsIgnoreCase("Cuboid");
	}

	protected SDFDatatypes() {
		super.baseUri = SDFDatatypes.baseURI;
		super.namespaceName = SDFDatatypes.namespaceName;
	}
}