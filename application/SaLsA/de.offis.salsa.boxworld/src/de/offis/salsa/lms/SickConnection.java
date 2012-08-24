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
package de.offis.salsa.lms;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.offis.salsa.lms.model.Measurement;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface SickConnection {
	void close() throws IOException;

	boolean isConnected();

	void open() throws FileNotFoundException;

	void onMeasurement(Measurement measurement, long timestamp);

}
