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
package de.uniol.inf.is.odysseus.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rules.RuleProvider;

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
		
		// init rule flow (order is important)
		for (TransformRuleFlowGroup e: TransformRuleFlowGroup.values()){
			TransformationInventory.getInstance().addRuleFlowGroup(e);	
		}
		
		//loading my own rules because self-binding-services don't work
		TransformationInventory.getInstance().bindRuleProvider(new RuleProvider());
		
		// TAccessAOGenericRule handles GenericPush and GenericPull-Wrapper
		WrapperRegistry.registerWrapper(Constants.GENERIC_PUSH);
		WrapperRegistry.registerWrapper(Constants.GENERIC_PULL);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
