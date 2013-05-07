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
package de.uniol.inf.is.odysseus.mining;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

public class Activator implements BundleActivator {
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public void bindDataDictionary(IDataDictionary dd){				
		dd.addDatatype(MiningDatatypes.FREQUENT_ITEM_SET.getURI(), MiningDatatypes.FREQUENT_ITEM_SET);
		dd.addDatatype(MiningDatatypes.ASSOCIATION_RULE.getURI(), MiningDatatypes.ASSOCIATION_RULE);
		dd.addDatatype(MiningDatatypes.CLASSIFICATION_TREE.getURI(), MiningDatatypes.CLASSIFICATION_TREE);	
	}
	
	public void unbindDataDictionary(IDataDictionary dd){
		dd.removeDatatype(MiningDatatypes.FREQUENT_ITEM_SET.getURI());
		dd.removeDatatype(MiningDatatypes.ASSOCIATION_RULE.getURI());
		dd.removeDatatype(MiningDatatypes.CLASSIFICATION_TREE.getURI());
	}

}
