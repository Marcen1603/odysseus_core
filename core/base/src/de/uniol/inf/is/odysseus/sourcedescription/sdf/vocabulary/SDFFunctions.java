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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDF;

public class SDFFunctions extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#";

	public static String filename = baseDir + "sdf_functions.sdf";

	public static String namespaceName = "sdfFunctions";

	public static String SDFFunction = baseURI + "SDFFunction";

	public static String OneToOne = baseURI + "OneToOne";

	public static String OneToMany = baseURI + "OneToMany";

	public static String ManyToOne = baseURI + "ManyToOne";

	public static String Identity = baseURI + "Identity";

	public static String MathCalc = baseURI + "MathCalc";

	public static String Translate = baseURI + "Translate";

	public static String ToString = baseURI + "ToString";

	public static String ToNumber = baseURI + "ToNumber";

	public static String TSplittReg = baseURI + "TSplittReg";

	public static String TSplitt = baseURI + "TSplitt";

	public static String MSplitt = baseURI + "MSplitt";

	public static String Merge = baseURI + "Merge";

	public static String hasReverse = baseURI + "hasReverse";

	protected SDFFunctions() {
		super.baseUri = SDFFunctions.baseURI;
		super.namespaceName = SDFFunctions.namespaceName;
	}
}