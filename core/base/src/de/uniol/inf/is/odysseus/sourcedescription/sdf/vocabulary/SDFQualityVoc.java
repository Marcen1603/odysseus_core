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

public class SDFQualityVoc extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_quality.sdf#";
	public static String filename = baseDir + "sdf_quality.sdf";
	public static String namespaceName = "sdfQuality";
	public static String QualityLevel = baseURI + "QualityLevel";
	public static String hasQualityLevel = baseURI + "hasQualityLevel";
	public static String correspondingValue = baseURI + "correspondingValue";
	public static String lowerBound = baseURI + "lowerBound";
	public static String upperBound = baseURI + "upperBound";
	public static String VeryGood = baseURI + "VeryGood";
	public static String Good = baseURI + "Good";
	public static String Medium = baseURI + "Medium";
	public static String Bad = baseURI + "Bad";
	public static String VeryBad = baseURI + "VeryBad";
	public static String SDFQuality = baseURI + "SDFQuality";
	public static String Correctness = baseURI + "Correctness";
	public static String Coverage = baseURI + "Coverage";
	public static String Security = baseURI + "Security";
	public static String Costs = baseURI + "Costs";
	public static String LatencyTime = baseURI + "LatencyTime";
	public static String ResponseTime = baseURI + "ResponseTime";
	public static String Timelineness = baseURI + "Timelineness";
	public static String Credibility = baseURI + "Credibility";
	public static String Objectivity = baseURI + "Objectivity";
	public static String Reputation = baseURI + "Reputation";

	protected SDFQualityVoc() {
		super.baseUri = SDFQualityVoc.baseURI;
		super.namespaceName = SDFQualityVoc.namespaceName;
	}
}