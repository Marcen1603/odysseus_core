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
package de.uniol.inf.is.odysseus.generator.frequentitem;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

/**
 * @author Dennis Geesen
 *
 */
public class ConsoleCommands implements CommandProvider {


	 public void _stopClients(CommandInterpreter ci) {
		 Activator.stop();
	 }
	 
	 public void _pauseClients(CommandInterpreter ci) {
		 Activator.pause();
	 }
	 
	 public void _proceedClients(CommandInterpreter ci) {
		 Activator.proceed();
	 }

	 @Override
	 public String getHelp() {
	  String s = "\tstopClients - stops all clients of for all servers\n";
	  s = s+"\tpauseClients - pauses all clients of for all servers\n";
	  s = s+"\tproceedClients - proceeds all clients of for all servers\n";
	  return s;
	 }

}
