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

package de.uniol.inf.is.odysseus.mining.clustering.feature;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.memory.ISnapshotMergeFunction;
import de.uniol.inf.is.odysseus.mining.memory.tiltedtimeframe.TiltedTimeWindow;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 06.09.2011
 */
public class ExampleRun {

	public static void main(String[] args) {
		
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		SDFAttribute attributeID = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
		attrs.add(attributeID);
		SDFAttribute attributeName = new SDFAttribute(null,"name", SDFDatatype.STRING);
		attrs.add(attributeName);

		SDFSchema schema = new SDFSchema("dummy", attrs);

		ISnapshotMergeFunction<ClusteringFeature<Tuple<ITimeInterval>>> datamergeFunction = new RelationalTIClusteringFeatureMergeFunction<ITimeInterval>();

		TiltedTimeWindow<ClusteringFeature<Tuple<ITimeInterval>>> window = new TiltedTimeWindow<ClusteringFeature<Tuple<ITimeInterval>>>(datamergeFunction);

		for (int i = 1; i <= 10; i++) {
			Tuple<ITimeInterval> example = new Tuple<ITimeInterval>(schema.size(), false);
			example.setAttribute(0, i);
			example.setAttribute(1, "Item " + i);
			RelationalTIClusteringFeature<ITimeInterval> cf = new RelationalTIClusteringFeature<ITimeInterval>(example); 			
			System.out.println("Inserting: " + cf);
			window.store(cf);
			System.out.println("*************************************");
			System.out.println(window);
			System.out.println("*************************************");
		}
		System.out.println("************************************* FINAL **************************************");
		System.out.println("**********************************************************************************");
		System.out.println(window.toString());

	}
}
