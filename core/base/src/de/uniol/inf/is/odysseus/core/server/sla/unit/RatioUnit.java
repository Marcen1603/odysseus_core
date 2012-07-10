/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.sla.unit;

/**
 * Possible units for ratio values.
 * 
 * percent: the value is given as 1/100, so the range should normally be 0 to
 * 100
 * 
 * absolute: the value is an absolute ratio, so the range should normally be 0
 * to 1
 * 
 * @author Thomas Vogelgesang
 * 
 */
public enum RatioUnit {

	percent, absolute;
}
