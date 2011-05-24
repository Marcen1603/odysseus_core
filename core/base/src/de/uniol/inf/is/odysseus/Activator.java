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
package de.uniol.inf.is.odysseus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AccessAOBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.FileAccessAOBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.Distance;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.DolToEur;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.Now;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.Polygon;

public class Activator implements BundleActivator {

	private IFunction[] functions = new IFunction[] { new DolToEur(),
			new Now(), new Distance(), new Polygon() };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		// Add default Functions
//		for (IFunction function : functions) {
//			MEP.registerFunction(function);
//		}
		OperatorBuilderFactory.putOperatorBuilderType("ACCESS",
				new AccessAOBuilder());
		OperatorBuilderFactory.putOperatorBuilderType("FILE",
				new FileAccessAOBuilder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		for (IFunction function : functions) {
			MEP.unregisterFunction(function.getSymbol());
		}
	}

}
