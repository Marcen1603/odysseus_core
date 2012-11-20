/**
 * 
 */
package de.offis.gui.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiLinkModel;

/**
 * Class to translate from the GWT-Apps plan structure to PQL
 * 
 * @author Merlin Wasmann
 * 
 */
public class PQLTranslator {

	private static Set<String> sources = new HashSet<String>();

	private static Map<String, String> dataElementToDatatype = new HashMap<String, String>();

	private static Map<String, String> idToName = new HashMap<String, String>();

	@SuppressWarnings("unused")
	public static String translateToPQL(GWTPlan plan) {
		String operatorGroup = plan.getOperatorGroup();
		List<InputModuleModel> sensors = plan.getSensors();
		List<OutputModuleModel> outputs = plan.getOutputs();
		List<OperatorModuleModel> operators = plan.getOperators();
		List<ScaiLinkModel> links = plan.getLinks();

		// setup
		setDataElementToDatatypeMapping();
		setNameToId(sensors, operators, outputs);

		// StringBuilder containing the PQL-Query
		StringBuilder sb = new StringBuilder();
		sb.append("#PARSER PQL\n");
		sb.append(translateSourcesToPQL(sensors));
		sb.append(translateOperatorsToPQL(operators, links));
		
		// outputs sind nicht nötig
		
		return sb.toString();
	}

	/**
	 * translate sensors into PQL <domain>_<sensor> := ACCESS({
	 * source='<domain>:<sensor>', wrapper='GenericPush',
	 * transport='NonBlockingTcp', protocol='SizeByteBuffer',
	 * dataHandler='Tuple', options=[ ['host',<host>], ['port',<port>],
	 * ['ByteOrder','LittleEndian']], schema=[ ['timestamp','StartTimestamp'],
	 * ['<dataElement>','<datatype>']]
	 * 
	 * @param sensors
	 * @param sb
	 * @return
	 */
	private static String translateSourcesToPQL(List<InputModuleModel> sensors) {
		StringBuilder sb = new StringBuilder();
		for (InputModuleModel sensor : sensors) {
			if (sources.contains(sensor.getDomain() + "_" + sensor.getName())) {
				continue;
			}
			String wrapper = "GenericPush";
			String transport = "NonBlockingTcp";
			String protocol = "SizeByteBuffer";
			String dataHandler = "Tuple";
			// TODO: das wird von Scampi übernommen. Wie soll es hier geregelt
			// werden?
			String host = "localhost";
			String port = "1234";

			String byteOrder = "LittleEndian";

			sb.append("#QUERY\n");
			sb.append(sensor.getDomain() + "_" + sensor.getName() + " := ");
			sb.append("ACCESS({");
			sb.append("source='" + sensor.getDomain() + ":" + sensor.getName()
					+ "',");
			sb.append("wrapper='" + wrapper + "',");
			sb.append("transport='" + transport + "',");
			sb.append("protocol='" + protocol + "',");
			sb.append("dataHandler='" + dataHandler + "',");
			sb.append("options=[");
			sb.append("['host','" + host + "'],");
			sb.append("['port','" + port + "'],");
			sb.append("['ByteOrder','" + byteOrder + "']");
			sb.append("],");
			sb.append(translateSchemaToPQL(sensor.getDataElements()));
			sb.append("})\n");
		}
		return sb.toString();
	}

	/**
	 * translate the schema to pql
	 * 
	 * @param dataElements
	 * @return
	 */
	private static String translateSchemaToPQL(String[] dataElements) {
		StringBuilder sb = new StringBuilder();
		sb.append("schema=[");
		sb.append("['timestamp','StartTimestamp'],");
		for (String element : dataElements) {
			sb.append("['" + element + "','"
					+ dataElementToDatatype.get(element) + "'],");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * translate operators to pql <operatorname> = <operatortype>({<param1> =
	 * ['<value1>'], <param2> = [['<value2.1>','<value2.2>']]},
	 * <previous_operator>)
	 * 
	 * @param operators
	 * @return
	 */
	private static String translateOperatorsToPQL(
			List<OperatorModuleModel> operators, List<ScaiLinkModel> links) {
		StringBuilder sb = new StringBuilder();
		sb.append("#QUERY\n");
		for (OperatorModuleModel operator : operators) {
			sb.append(operator.getName() + " = ");
			sb.append(operator.getOperatorType());
			sb.append("({");
			HashMap<String, String> parameters = operator.getProperties();
			for(String key : parameters.keySet()) {
				sb.append(key + " = ['" + parameters.get(key) + "'],");
			}
			if(!parameters.isEmpty()) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("},");
			List<String> sources = getSourceNamesToPQL(operator.getId(), links);
			for(String source : sources) {
				sb.append(source + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")\n");
		}
		return sb.toString();
	}
	
	private static List<String> getSourceNamesToPQL(String id, List<ScaiLinkModel> links) {
		List<String> sources = new ArrayList<String>();
		for(ScaiLinkModel link : links) {
			if(link.getDestination().equals(id)) {
				sources.add(idToName.get(id));
			}
		}
		return sources;
	}
	
	private static void setDataElementToDatatypeMapping() {
		// TODO: Hello Magic! How are you?
		// TODO: leider besteht ein dataElement nur aus einem Namen. Ohne
		// Datentyp.
	}

	private static void setNameToId(List<InputModuleModel> sources,
			List<OperatorModuleModel> operators, List<OutputModuleModel> sinks) {
		for (InputModuleModel source : sources) {
			idToName.put(source.getId(),
					source.getDomain() + "_" + source.getName());
		}
		for (OperatorModuleModel operator : operators) {
			idToName.put(operator.getId(), operator.getName());
		}
		for (OutputModuleModel sink : sinks) {
			idToName.put(sink.getId(), sink.getName());
		}
	}

	// TODO: implement me!
	public static GWTPlan translateFromPQL(String pqlPlan) {
		String operatorGroup = getQueryId(pqlPlan);
		List<InputModuleModel> sensors = getSensors(pqlPlan);
		List<OutputModuleModel> outputs = getOutputs(pqlPlan);
		List<OperatorModuleModel> operators = getOperators(pqlPlan);
		List<ScaiLinkModel> links = getLinks(pqlPlan);
		return new GWTPlan(operatorGroup, sensors, outputs, operators, links);
	}

	private static List<ScaiLinkModel> getLinks(String pqlPlan) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<OperatorModuleModel> getOperators(String pqlPlan) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<OutputModuleModel> getOutputs(String pqlPlan) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<InputModuleModel> getSensors(String pqlPlan) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getQueryId(String pqlPlan) {
		// TODO Auto-generated method stub
		return null;
	}

}
