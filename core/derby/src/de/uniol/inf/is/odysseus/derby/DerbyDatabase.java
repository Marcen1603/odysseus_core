package de.uniol.inf.is.odysseus.derby;

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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is only the Derby database loader. 
 * The connection is managed in the odysseus storing bundle.
 */
public class DerbyDatabase implements BundleActivator {

	volatile protected static Logger LOGGER = LoggerFactory.getLogger(DerbyDatabase.class);
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOGGER.info("Start Derby Database.");
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOGGER.info("Stop Derby Database.");
	}

}
