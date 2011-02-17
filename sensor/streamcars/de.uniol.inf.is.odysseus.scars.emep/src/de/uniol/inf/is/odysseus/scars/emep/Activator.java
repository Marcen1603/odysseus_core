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
package de.uniol.inf.is.odysseus.scars.emep;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.scars.emep.functions.GetAbsoluteValue;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixAdd;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixGetEntry;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixInvert;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixMult;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixSub;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixTranspose;
import de.uniol.inf.is.odysseus.scars.emep.functions.SquareValue;
import de.uniol.inf.is.odysseus.scars.emep.functions.SqrtValue;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.scars.EMEP"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		MEP.registerFunction(new MatrixInvert());
		MEP.registerFunction(new MatrixAdd());
		MEP.registerFunction(new MatrixSub());
		MEP.registerFunction(new MatrixMult());
		MEP.registerFunction(new MatrixTranspose());
		MEP.registerFunction(new MatrixGetEntry());
		MEP.registerFunction(new GetAbsoluteValue());
		MEP.registerFunction(new SquareValue());
		MEP.registerFunction(new SqrtValue());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		MEP.unregisterFunction("MatrixInv");
		MEP.unregisterFunction("MatrixAdd");
		MEP.unregisterFunction("MatrixSub");
		MEP.unregisterFunction("MatrixMult");
		MEP.unregisterFunction("MatrixTrans");
		MEP.unregisterFunction("MatrixEntry");
		MEP.unregisterFunction("AbsValue");
		MEP.unregisterFunction("SquareValue");
		MEP.unregisterFunction("SqrtValue");
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
