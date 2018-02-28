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
package de.uniol.inf.is.odysseus.rcp.image.operator;

import org.eclipse.jface.resource.ImageDescriptor;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

/**
 * @author DGeesen
 * 
 */
public class OperatorImageFactory {

	public static ImageDescriptor createImageForOperator(String operatorName) {
		return createImageForOperator(operatorName, "black");
	}
	
	// TODO: Make config file
	@Deprecated
	public static ImageDescriptor createImageForOperator(String operatorName, String styleName) {
		operatorName = operatorName.toUpperCase();
		String file = "";
		switch (operatorName) {
//		case "JOIN":
//		case "LEFTJOIN":
//			file = "join2.png";
//			break;
//		case "RENAME":
//			file = "pencil.png";
//			break;
//		case "AGGREGATE":
//		case "FASTMEDIAN":
//			file = "text_sum.png";
//			break;
//		case "PROJECT":
//			file = "project.png";
//			break;
//		case "MAP":
//		case "STATEMAP":
//			file = "calculator.png";
//			file = "iconmonstr-calculator-1-64.png";
//			break;
//		case "ACCESS":
//		case "STREAM":
//		case "SUBSCRIBE":
//			file = "arrow_right_blue.png";
//			break;
//		case "SINK":
//		case "SENDER":
//		case "SOCKETSINK":
//			file = "arrow_up_right_blue.png";
//			break;
//		case "DATABASESOURCE":
//		case "DATABASESINK":
//		case "DBENRICH":
//		case "BUFFER":
//		case "CACHE":
//		case "ASSOCIATIVESTORAGE":
//			//file = "database.png";
//			file = "iconmonstr-database-2-64.png";
//			break;
//		case "CSVFILESOURCE":
//		case "CSVFILESINK":
//			file = "csv_text.png";
//			break;
//		case "STORE":
//		case "CONTEXTENRICH":
//			file = "cabinet_open.png";
//			break;
//		case "ENRICH":
//			file = "enrich.png";
//			break;
//		case "BUFFEREDFILTER":
//		case "FILTER":
//		case "SELECT":
//			file = "filter.png";
//			break;
//		case "UDO":
//			file = "machine.png";
//			break;
//		case "WSENRICH":
//			file = "web.png";
//			break;
//		case "PUBLISH":
//			file = "newspaper.png";
//			break;
//		case "PATTERN":
//		case "SASE":
//			file = "search.png";
//			break;
//		case "CONVERTER":
//		case "KEYVALUETOTUPLE":
//		case "TIMESTAMPTOPAYLOAD":
//		case "TUPLETOKEYVALUE":
//			file = "convert.png";
//			break;
//		case "ASSUREHEARTBEAT":
//			file = "heart.png";
//			break;
//		case "WINDOW":
//			file = "window.png";
//			break;
//		case "ELEMENTWINDOW":
//			file = "element_window.png";
//			break;
//		case "TIMEWINDOW":
//			file = "time_window.png";
//			file = "iconmonstr-window-22-64.png";
//			break;
//		case "PREDICATEWINDOW":
//			file = "predicate_window.png";
//			break;
//		case "SYNCWITHSYSTEMTIME":
//		case "TIMESHIFT":
//		case "TIMESTAMP":
//			file = "clock.png";
//			break;
//		case "TIMESTAMPORDERVALIDATE":
//			file = "timecheck.png";
//			break;
//		case "ASSUREORDER":
//			file = "sort_ascending.png";
//			break;
//		case "ROUTE":
//			file = "signpost.png";
//			break;
//		case "UNION":
//			file = "logic_or.png";
//			break;
//		case "DIFFERENCE":
//			file = "logic_not.png";
//			break;
//		case "MERGE":
//			file = "union.png";
//			break;
//		case "CHANGEDETECT":
//		case "SAMPLE":
//		case "COALESCE":
//			file = "c_clamp.png";
//			break;
//		case "REGRESSION":
//			file = "graph.png";
//			break;
//		case "CLASSIFICATION_LEARN":
//		case "CLASSIFY":
//		case "CLUSTERING":
//		case "FEATUREEXTRACTION":
//		case "FREQUENTITEMSET":
//		case "GENERATERULES":
//		case "HMM":
//		case "VECTORQUANTIZATION":
//			file = "brain.png";
//			break;
//		case "GENERATOR":
//			file = "gear_add.png";
//			break;
//		case "DEFAULT":
//		default:
////			file = "gear.png";
//			file = "iconmonstr-gear-1-64.png";
//			break;
		}

		String path = styleName != null ? "operator-images/" + styleName + "/" + file: "operator-images/" + file;
		// TODO check if image path exists! if not return default image
		return OdysseusRCPPlugIn.getImageDescriptor(path);

	}
}
