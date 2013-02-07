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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Geesen
 *
 */
public class OdysseusScriptPartitionRegsitry {
	
	private static List<IOdysseusScriptPartition> partitions = new ArrayList<>();
	
	static{
		// order is important!		
		addParition(new OdysseusScriptSimpleCommentPartition());
		addParition(new OdysseusScriptQueryKeywordPartition());
		addParition(new OdysseusScriptKeywordPartition());		
	}
	
	

	public static List<IOdysseusScriptPartition> getParitions() {
		return partitions;
	}

	public static void addParition(IOdysseusScriptPartition parition) {
		partitions.add(parition);
	}
	
	public static String[] getAllPartionNames(){		
		return getAllPartionNamesAsList().toArray(new String[0]);
	}

	/**
	 * @return
	 */
	public static List<String> getAllPartionNamesAsList() {
		List<String> names = new ArrayList<>();
		for(IOdysseusScriptPartition partition : partitions){
			names.add(partition.getPartitionTokenName());
		}
		return names;
	}
	
}
