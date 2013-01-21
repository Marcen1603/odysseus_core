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
package de.uniol.inf.is.odysseus.nexmark;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.uniol.inf.is.odysseus.nexmark.simulation.NexmarkServer;

public class NexmarkApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();
		System.out.println("starting....");		
//		String[] args = new String[6];
//		args[0] = "-gcf";
//		args[1] = "/config/NEXMarkGeneratorConfiguration_SLOW.properties";
//		args[2] = "-useNIO";
//		args[3] = "true";
//		args[4] = "-pr";
//		args[5] = "65440";
		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);		
//		for (String s: args2){
//			System.err.println(s);
//		}
		NexmarkServer.main(args);
		return IApplicationContext.EXIT_ASYNC_RESULT;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
