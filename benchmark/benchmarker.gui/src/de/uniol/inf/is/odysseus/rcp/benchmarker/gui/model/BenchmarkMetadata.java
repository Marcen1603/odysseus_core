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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Diese Klasse enth�lt die Daten f�r BenchmarkMetadata
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkMetadata implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, String> metadata;

	public BenchmarkMetadata() {
		metadata = new HashMap<String, String>();
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}
}
