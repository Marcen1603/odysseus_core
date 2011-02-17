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
package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import java.io.File;
import java.io.IOException;

public class CacheTool {
	
	public static void checkForExistingConfigurationDeletion(String Name,
			File ConfigurationFile) throws IOException {
		recursiveDelete(ConfigurationFile);
	}

	public static void recursiveDelete(File TheFile) {
		File[] SubFiles = TheFile.listFiles();
		if (SubFiles != null) {
			for (int i = 0; i < SubFiles.length; i++) {
				if (SubFiles[i].isDirectory()) {
					recursiveDelete(SubFiles[i]);
				}
				SubFiles[i].delete();
			}
			TheFile.delete();
			
		}
	}

}
