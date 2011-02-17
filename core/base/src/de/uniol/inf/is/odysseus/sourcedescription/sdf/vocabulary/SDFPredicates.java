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

public class SDFPredicates extends SDF {
	

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_predicates.sdf#";

	public static String filename = baseDir + "sdf_predicates.sdf";

	public static String namespaceName = "sdfPredicates";

	public static String CompareOperator = baseURI + "CompareOperator";

	public static String StringCompareOperator = baseURI
			+ "StringCompareOperator";

	public static String NumberCompareOperator = baseURI
			+ "NumberCompareOperator";

	public static String Equal = baseURI + "Equal";

	public static String Unequal = baseURI + "Unequal";

	public static String LowerThan = baseURI + "LowerThan";

	public static String LowerOrEqualThan = baseURI + "LowerOrEqualThan";

	public static String GreaterOrEqualThan = baseURI + "GreaterOrEqualThan";

	public static String GreaterThan = baseURI + "GreaterThan";
	
	public static String Intervall = baseURI + "Intervall";

	public static String LogicalOperator = baseURI + "LogicalOperator";

	public static String And = baseURI + "And";

	public static String Or = baseURI + "Or";

	public static String Predicate = baseURI + "Predicate";

	public static String SimplePredicate = baseURI + "SimplePredicate";

	public static String InPredicate = baseURI + "InPredicate";

	public static String StringPredicate = baseURI + "StringPredicate";

	public static String StringInPredicate = baseURI + "StringInPredicate";

	public static String NumberPredicate = baseURI + "NumberPredicate";

	public static String NumberInPredicate = baseURI + "NumberInPredicate";
	
	public static String IntervallPredicate = baseURI + "IntervallPredicate";

	public static String ComplexPredicate = baseURI + "ComplexPredicate";

	public static String AndPredicate = baseURI + "AndPredicate";

	public static String OrPredicate = baseURI + "OrPredicate";

	public static String hasSimpleLeftPart = baseURI + "hasSimpleLeftPart";

	public static String hasSimpleRightPart = baseURI + "hasSimpleRightPart";

	public static String hasComplexLeftPart = baseURI + "hasComplexLeftPart";

	public static String hasComplexRightPart = baseURI + "hasComplexRightPart";

	public static String predAttribute = baseURI + "predAttribute";

	public static String predConstant = baseURI + "predConstant";

	public static String predValue = baseURI + "predValue";

	public static String predConstantSet = baseURI + "predConstantSet";

	public static String predStringOperator = baseURI + "predStringOperator";

	public static String predNumberOperator = baseURI + "predNumberOperator";

	protected SDFPredicates() {
		super.baseUri = SDFPredicates.baseURI;
		super.namespaceName = SDFPredicates.namespaceName;
	}
}