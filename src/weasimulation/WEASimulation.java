package weasimulation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class WEASimulation {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		if (args.length != 1 || args[0].equals("--help")) {
			System.out.println("Usage: WEASimulation <configfile>");
			return;
		}

		Document config = null;
		try {
			config = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		List<SimulationServer> simulations = new ArrayList<SimulationServer>();
		NodeList daNodes = config.getElementsByTagName("simulation");
		for (int i = 0; i < daNodes.getLength(); ++i) {
			Element curNode = (Element) daNodes.item(i);
//			String name = curNode.getAttribute("name");
			String inputFile = curNode.getAttribute("input");
			String portStr = curNode.getAttribute("port");
			NodeList timePeriodList = curNode
					.getElementsByTagName("time_period");
			if (inputFile == null || portStr == null
					|| timePeriodList.getLength() != 1) {
				continue;
			}

			int port = 0;
			try {
				port = Integer.parseInt(portStr);

				Element timePeriodElement = (Element) timePeriodList.item(0);
				TimePeriod timePeriod = TimePeriodFactory.getInstance()
						.createTimePeriod(timePeriodElement);

				Simulation simulation = new Simulation(timePeriod,
						new FileInputStream(inputFile).getChannel());
				SimulationServer simServer = new SimulationServer(port,
						simulation);
				simulations.add(simServer);
				System.out.println("Starting simulation on port " + port);
				simServer.start();
			} catch (NumberFormatException e) {
				System.err.println("Illegal port in configuration: " + portStr);
				continue;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		
		System.out.println("System up and running");
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait();	
		}
		return;
	}

}
