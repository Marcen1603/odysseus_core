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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.quality;

/**
 * Nur ein enum um von den eigentlichen Werte abstahieren zu koennen (in java 1.5
 * soll es sowas ja dann auch direkt geben. Ist nicht so richtig schoen, die
 * Zahlen sowohl in der SDF-Beschreibung (sdf_quality) als auch hier zu haben
 * ...
 */

public class SDFQualityLevel {

	public static float VERY_GOOD = 1f;

	public static float GOOD = 0.75f;

	public static float MEDIUM = 0.5f;

	public static float BAD = 0.25f;

	public static float VERY_BAD = 0f;
}