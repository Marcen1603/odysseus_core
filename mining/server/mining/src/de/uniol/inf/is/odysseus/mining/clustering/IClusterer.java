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
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.clustering;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen
 * Created at: 15.05.2012
 */
public interface IClusterer<M extends ITimeInterval> {

	
	/**
	 * Creates and initializes a new instance of the clusterer 
	 * @param schema The schema of the incoming tuples
	 */
	public IClusterer<M> createInstance();
	
	/**
	 * Initializes the clusterer 
	 * @param schema The schema of the incoming tuples
	 */
	public void init(String algorithm, SDFSchema schema);
	/**
	 * clusters the given set of tuples
	 * @param the tuples that should be clustered
	 * @return tuples a map of clusters (id's) and the assigned tuples
	 */
	public Map<Integer, List<Tuple<M>>> processClustering(List<Tuple<M>> tuples);
	
	/** 
	 * This allows to set some options that can be used by the implementing class
	 * @param options
	 */
	public void setOptions(Map<String, String> options);
	
	/**
	 * A system wide unique name of the classifier
	 * @return the unique name
	 */
	public String getName();
	
	/**
	 * A list of possible algorithms used by this learner
	 * @return a list of names
	 */
	public List<String> getAlgorithmNames();
}
