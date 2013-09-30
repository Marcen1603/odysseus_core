/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.images;

import org.eclipse.jface.resource.ImageDescriptor;

import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;

/**
 * @author DGeesen
 * 
 */
public class ImageFactory {

	public static ImageDescriptor createImageForOperator(String operatorName) {
		operatorName = operatorName.toUpperCase();
		String file = "";
		switch (operatorName) {
		case "JOIN":
			file = "join.png";
			break;
		case "ACCESS":
		case "STREAM":
		case "SINK":
		case "SENDER":
		case "SOCKETSINK":
			file = "right_grey.png";
			break;
		case "DATABASESOURCE":
		case "DATABASESINK":
		case "DBENRICH":
		case "BUFFER":
			file = "database.png";
			break;
		case "CSVFILESOURCE":
		case "CSVFILESINK":
			file = "csv_text.png";
			break;
		case "STORE":
			file = "system_file_manager.png";
			break;
		case "BUFFEREDFILTER":
		case "FILTER":
		case "SELECT":
			file = "filter.png";
			break;
		case "UDO":
			file = "machine.png";
			break;
	
		default:
			file = "default.png";
			break;
		}

		return Activator.getImageDescriptor("operator-images/" + file);

	}
}
