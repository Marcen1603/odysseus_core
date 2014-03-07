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
package de.uniol.inf.is.odysseus.rcp.startup;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.login.Login;

public class SplashHandler extends AbstractSplashHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(SplashHandler.class);
	
	@Override
	public void init(Shell splash) {	
		super.init(splash);
		LOG.debug("Starting login screen and waiting for executor...");
		waitForExecutor();	
		LOG.debug("There is one executor, so show up the login window.");
		Login.login(splash, false, false);
	}

	private static void waitForExecutor() {
		try {
			OdysseusRCPPlugIn.waitForExecutor();
		} catch (InterruptedException e) {			
			LOG.error("Could not wait for executor", e);
		}
	}
}
