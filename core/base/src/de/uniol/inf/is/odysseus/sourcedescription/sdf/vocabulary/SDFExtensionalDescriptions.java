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

public class SDFExtensionalDescriptions extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_extensional_descriptions.sdf#";

	public static String filename = baseDir + "sdf_extensional_descriptions.sdf";

	public static String namespaceName = "sdfExtensionalDescriptions";

	public static String hasDescriptionPredicate = baseURI
			+ "hasDescriptionPredicate";

	public static String hasSimpleDescriptionPredicate = baseURI
			+ "hasSimpleDescriptionPredicate";

	public static String hasComplexDescriptionPredicate = baseURI
			+ "hasComplexDescriptionPredicate";

	protected SDFExtensionalDescriptions() {
		super.baseUri = SDFExtensionalDescriptions.baseURI;
		super.namespaceName = SDFExtensionalDescriptions.namespaceName;
	}
}