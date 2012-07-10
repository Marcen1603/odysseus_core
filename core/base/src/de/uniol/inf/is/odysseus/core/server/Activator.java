/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AccessAOBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileAccessAOBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.CSVTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.TransformerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.ObjectInputStream2ObjectInputStreamTransformer;

public class Activator implements BundleActivator {
	

	private static BundleContext bundleContext;
	
	
	public static BundleContext getBundleContext(){
		return bundleContext;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {		
		OperatorBuilderFactory.addOperatorBuilder(new AccessAOBuilder());
		OperatorBuilderFactory.addOperatorBuilder(new FileAccessAOBuilder());
		TransformerRegistry.register(new CSVTransformer());
		TransformerRegistry.register(new ObjectInputStream2ObjectInputStreamTransformer());
		
		bundleContext = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		bundleContext = null;
	}

}
