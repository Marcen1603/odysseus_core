/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.service.sensor.data;

public enum DataType {
 
	/**
	 * Ein Long-Value
	 */
	LONG,
	/**
	 * Ein String-Value
	 */
	STRING,
	/**
	 * Ein Double-Value
	 */
	DOUBLE,
	/**
	 * Ein Integer-Value
	 */
	INTEGER,
	/**
	 * Ein Zeitstempel, dessen Datetyp ein Long sein muss
	 */
	TIMESTAMP,
	/**
	 * Ein Start-Zeitstempel, dessen Datetyp ein Long sein muss.
	 * Dieser gibt an, ab wann der Messwert gültig ist. In der Regel ist das der Messzeitpunkt
	 */
	STARTTIMESTAMP,
	/**
	 * Ein End-Zeitstempel, dessen Datentyp ein Long sein muss.
	 * Dieser gibt an, ab wann der Messwert nicht mehr gültig ist. 
	 * Wird eigentlich in der Regel vom System gesetzt.
	 */
	ENDTIMESTAMP
}
