package de.uniol.inf.is.odysseus.anomalydetection.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.AreaChartVisualizationInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.ClientConfiguration;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.CollectionColoringInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.ConnectionInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.GaugeVisualizationInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.OverviewInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.ServerConfiguration;
import de.uniol.inf.is.odysseus.anomalydetection.rest.dto.response.CollectionInformation;

/**
 * This class gives an example of how to create a configuration for the
 * condition monitoring application
 * 
 * @author Tobias Brandt
 *
 */
public class ConfigurationCreator {

	// Demo configuration
	public static void main(String[] args) {

		Configuration config = new Configuration();
		config.setName("Offshore Windpark");
		config.setDescription(
				"Analysis of the offshore windpark with windturbines and transformer substation with its cooling system.");
		config.addUser("System");

		// --------------------
		// Client Configuration
		// --------------------
		ClientConfiguration clientConfig = new ClientConfiguration();

		// Connections to the right queries
		// --------------------------------
		List<ConnectionInformation> connectionInformation = new ArrayList<>();

		// Simple values
		// -------------
		// The temperature of the transformer
		ConnectionInformation conTransformer = new ConnectionInformation();
		conTransformer.setName("Transformer temperature");
		conTransformer.setIp("127.0.0.1");
		conTransformer.setQueryName("transformer");
		conTransformer.setUseName(true);
		conTransformer.setOperatorName("transformerOutput");
		conTransformer.setOperatorOutputPort(0);
		conTransformer.setUseOperatorOutputPort(true);

		// The throughput of the big cooling pipe
		ConnectionInformation conBigPipeOut = new ConnectionInformation();
		conBigPipeOut.setName("Big cooling pipe");
		conBigPipeOut.setIp("127.0.0.1");
		conBigPipeOut.setQueryName("bigPipeOutput");
		conBigPipeOut.setUseName(true);
		conBigPipeOut.setOperatorName("bigPipeOutOperator");
		conBigPipeOut.setOperatorOutputPort(0);
		conBigPipeOut.setUseOperatorOutputPort(true);

		// The windspeed on the northsea
		ConnectionInformation conWindspeed = new ConnectionInformation();
		conWindspeed.setName("Windspeed");
		conWindspeed.setIp("127.0.0.1");
		conWindspeed.setQueryName("windspeedQuery");
		conWindspeed.setUseName(true);
		conWindspeed.setOperatorName("windspeedOutput");
		conWindspeed.setOperatorOutputPort(0);
		conWindspeed.setUseOperatorOutputPort(true);

		// Pump 1
		ConnectionInformation conPump1 = new ConnectionInformation();
		conPump1.setName("Pump 1");
		conPump1.setIp("127.0.0.1");
		conPump1.setQueryName("pump1");
		conPump1.setUseName(true);
		conPump1.setOperatorName("pump1Output");
		conPump1.setOperatorOutputPort(0);
		conPump1.setUseOperatorOutputPort(true);

		// Pump 2
		ConnectionInformation conPump2 = new ConnectionInformation();
		conPump2.setName("Pump 2");
		conPump2.setIp("127.0.0.1");
		conPump2.setQueryName("pump2");
		conPump2.setUseName(true);
		conPump2.setOperatorName("pump2Output");
		conPump2.setOperatorOutputPort(0);
		conPump2.setUseOperatorOutputPort(true);

		// Pump 3
		ConnectionInformation conPump3 = new ConnectionInformation();
		conPump3.setName("Pump 3");
		conPump3.setIp("127.0.0.1");
		conPump3.setQueryName("pump3");
		conPump3.setUseName(true);
		conPump3.setOperatorName("pump3Output");
		conPump3.setOperatorOutputPort(0);
		conPump3.setUseOperatorOutputPort(true);

		// Analysis
		// -------
		// The pattern analysis of the cooling system
		ConnectionInformation conPumpPattern = new ConnectionInformation();
		conPumpPattern.setName("Pump switch pattern");
		conPumpPattern.setDescription("The pattern of closing / opening valves and starting / stopping is anomal.");
		conPumpPattern.setIp("127.0.0.1");
		conPumpPattern.setQueryName("pumpPatternAnalysis");
		conPumpPattern.setUseName(true);
		conPumpPattern.setOperatorName("pumpRarePattern");
		conPumpPattern.setOperatorOutputPort(0);
		conPumpPattern.setUseOperatorOutputPort(true);

		// The temperature analysis of the transformer (valueArea)
		ConnectionInformation conTempValueAreaAnalysis = new ConnectionInformation();
		conTempValueAreaAnalysis.setName("Transformer temperature");
		conTempValueAreaAnalysis.setDescription("The temperature of the transformer is too cold or too hot.");
		conTempValueAreaAnalysis.setIp("127.0.0.1");
		conTempValueAreaAnalysis.setQueryName("transformerTempAnalysis");
		conTempValueAreaAnalysis.setUseName(true);
		conTempValueAreaAnalysis.setOperatorName("valueTemp");
		conTempValueAreaAnalysis.setOperatorOutputPort(0);
		conTempValueAreaAnalysis.setUseOperatorOutputPort(true);

		// The temperature analysis of the transformer (valueArea)
		ConnectionInformation conTempChangeAnalysis = new ConnectionInformation();
		conTempChangeAnalysis.setName("Transformer temperature");
		conTempChangeAnalysis.setDescription("The temperature of the transformer changed too quickly.");
		conTempChangeAnalysis.setIp("127.0.0.1");
		conTempChangeAnalysis.setQueryName("transformerTempAnalysis");
		conTempChangeAnalysis.setUseName(true);
		conTempChangeAnalysis.setOperatorName("changeTemp");
		conTempChangeAnalysis.setOperatorOutputPort(0);
		conTempChangeAnalysis.setUseOperatorOutputPort(true);

		// The flow of the cooling pipe
		ConnectionInformation conCoolingPipeValueArea = new ConnectionInformation();
		conCoolingPipeValueArea.setName("Cooling pipe");
		conCoolingPipeValueArea.setDescription("The amount of water in the cooling pipe is not ok.");
		conCoolingPipeValueArea.setIp("127.0.0.1");
		conCoolingPipeValueArea.setQueryName("coolingPipeThroughput");
		conCoolingPipeValueArea.setUseName(true);
		conCoolingPipeValueArea.setOperatorName("coolingPipeValue");
		conCoolingPipeValueArea.setOperatorOutputPort(0);
		conCoolingPipeValueArea.setUseOperatorOutputPort(true);

		// Cohort analysis of the windturbines (Deviation)
		ConnectionInformation conCohortDeviation = new ConnectionInformation();
		conCohortDeviation.setName("Windturbines");
		conCohortDeviation.setDescription(
				"The amount of produced energy of one windtubine is different from the other turbines.");
		conCohortDeviation.setIp("127.0.0.1");
		conCohortDeviation.setQueryName("windturbineCohort");
		conCohortDeviation.setUseName(true);
		conCohortDeviation.setOperatorName("cohortDeviationOutput");
		conCohortDeviation.setOperatorOutputPort(0);
		conCohortDeviation.setUseOperatorOutputPort(true);

		// Interval analysis for pump switches
		ConnectionInformation conIntervalPumpSwitch = new ConnectionInformation();
		conIntervalPumpSwitch.setName("Pump switches");
		conIntervalPumpSwitch.setDescription("The time between a pump switch is anomal.");
		conIntervalPumpSwitch.setIp("127.0.0.1");
		conIntervalPumpSwitch.setQueryName("pumpSwitchInterval");
		conIntervalPumpSwitch.setUseName(true);
		conIntervalPumpSwitch.setOperatorName("deviationIntervalOut");
		conIntervalPumpSwitch.setOperatorOutputPort(0);
		conIntervalPumpSwitch.setUseOperatorOutputPort(true);

		// Pump 1 vibration analysis
		ConnectionInformation conPump1Vibration = new ConnectionInformation();
		conPump1Vibration.setName("Pump 1 vibration");
		conPump1Vibration
				.setDescription("The vibration of pump 1 is too high or too low compared to the current flow.");
		conPump1Vibration.setIp("127.0.0.1");
		conPump1Vibration.setQueryName("pump1VibrationAnalysis");
		conPump1Vibration.setUseName(true);
		conPump1Vibration.setOperatorName("pump1VibrationDetection");
		conPump1Vibration.setOperatorOutputPort(0);
		conPump1Vibration.setUseOperatorOutputPort(true);

		// Pump 2 vibration analysis
		ConnectionInformation conPump2Vibration = new ConnectionInformation();
		conPump2Vibration.setName("Pump 2 vibration");
		conPump2Vibration
				.setDescription("The vibration of pump 2 is too high or too low compared to the current flow.");
		conPump2Vibration.setIp("127.0.0.1");
		conPump2Vibration.setQueryName("pump2VibrationAnalysis");
		conPump2Vibration.setUseName(true);
		conPump2Vibration.setOperatorName("pump2VibrationDetection");
		conPump2Vibration.setOperatorOutputPort(0);
		conPump2Vibration.setUseOperatorOutputPort(true);

		// Pump 3 vibration analysis
		ConnectionInformation conPump3Vibration = new ConnectionInformation();
		conPump3Vibration.setName("Pump 3 vibration");
		conPump3Vibration
				.setDescription("The vibration of pump 3 is too high or too low compared to the current flow.");
		conPump3Vibration.setIp("127.0.0.1");
		conPump3Vibration.setQueryName("pump3VibrationAnalysis");
		conPump3Vibration.setUseName(true);
		conPump3Vibration.setOperatorName("pump3VibrationDetection");
		conPump3Vibration.setOperatorOutputPort(0);
		conPump3Vibration.setUseOperatorOutputPort(true);

		connectionInformation.add(conTransformer);
		connectionInformation.add(conBigPipeOut);
		connectionInformation.add(conWindspeed);
		connectionInformation.add(conPump1);
		connectionInformation.add(conPump2);
		connectionInformation.add(conPump3);
		connectionInformation.add(conPumpPattern);
		connectionInformation.add(conTempValueAreaAnalysis);
		connectionInformation.add(conTempChangeAnalysis);
		connectionInformation.add(conCoolingPipeValueArea);
		connectionInformation.add(conCohortDeviation);
		connectionInformation.add(conIntervalPumpSwitch);
		connectionInformation.add(conPump1Vibration);
		connectionInformation.add(conPump2Vibration);
		connectionInformation.add(conPump3Vibration);
		clientConfig.setConnectionInformation(connectionInformation);

		// Overview
		// --------
		OverviewInformation overviewInformation = new OverviewInformation();
		clientConfig.setOverviewInformation(overviewInformation);

		List<ConnectionInformation> overviewConnections = new ArrayList<>();
		overviewConnections.add(conPumpPattern);
		overviewConnections.add(conTempValueAreaAnalysis);
		overviewConnections.add(conTempChangeAnalysis);
		overviewConnections.add(conCoolingPipeValueArea);
		overviewConnections.add(conCohortDeviation);
		overviewConnections.add(conIntervalPumpSwitch);
		overviewConnections.add(conPump1Vibration);
		overviewConnections.add(conPump2Vibration);
		overviewConnections.add(conPump3Vibration);
		overviewInformation.setOverviewConnections(overviewConnections);

		// Visualizations
		// --------------
		List<GaugeVisualizationInformation> gaugeVisualizations = new ArrayList<>();
		List<AreaChartVisualizationInformation> areaChartVisualizationInformation = new ArrayList<>();

		// The temperature of the transformer
		GaugeVisualizationInformation transformerTemperatureGauge = new GaugeVisualizationInformation();
		transformerTemperatureGauge.setConnectionInformation(conTransformer);
		transformerTemperatureGauge.setAttribute("temperature");
		transformerTemperatureGauge.setMinValue(20.0);
		transformerTemperatureGauge.setMaxValue(70.0);
		transformerTemperatureGauge.setTitle("Transformer Temperature");

		// The cooling pipe of the transformer
		AreaChartVisualizationInformation bigCoolingPipeThorughputAreaChart = new AreaChartVisualizationInformation();
		bigCoolingPipeThorughputAreaChart.setConnectionInformation(conBigPipeOut);
		bigCoolingPipeThorughputAreaChart.setAttribute("lastThroughput");
		bigCoolingPipeThorughputAreaChart.setMaxElements(600);
		bigCoolingPipeThorughputAreaChart.setTimeAttribute("start");
		bigCoolingPipeThorughputAreaChart.setTitle("Cooling-pipe throughput in l/m");

		// The windspeed on the northsea
		AreaChartVisualizationInformation windspeedAreaChart = new AreaChartVisualizationInformation();
		windspeedAreaChart.setConnectionInformation(conWindspeed);
		windspeedAreaChart.setAttribute("windSpeed");
		windspeedAreaChart.setMaxElements(600);
		windspeedAreaChart.setTimeAttribute("start");
		windspeedAreaChart.setTitle("Windspeed in m/s");

		// Produced energy by the windpark
		AreaChartVisualizationInformation energyOutputAreaChart = new AreaChartVisualizationInformation();
		energyOutputAreaChart.setConnectionInformation(conTransformer);
		energyOutputAreaChart.setAttribute("lastTransformedEnergy");
		energyOutputAreaChart.setMaxElements(600);
		energyOutputAreaChart.setTimeAttribute("start");
		energyOutputAreaChart.setTitle("Produced energy in kW");

		// For the overview
		gaugeVisualizations.add(transformerTemperatureGauge);
		areaChartVisualizationInformation.add(bigCoolingPipeThorughputAreaChart);
		areaChartVisualizationInformation.add(windspeedAreaChart);
		areaChartVisualizationInformation.add(energyOutputAreaChart);
		overviewInformation.setGaugeVisualizationInformation(gaugeVisualizations);
		overviewInformation.setAreaChartVisualizationInformation(areaChartVisualizationInformation);

		// -----------
		// Collections
		// -----------
		List<CollectionInformation> collections = new ArrayList<>();

		// Transformer
		// -----------
		CollectionInformation transformerCollection = new CollectionInformation();
		transformerCollection.setName("Transformer");
		// Visualization

		// Temperature of the transformer
		AreaChartVisualizationInformation transformerTempertureAreaChart = new AreaChartVisualizationInformation();
		transformerTempertureAreaChart.setConnectionInformation(conTransformer);
		transformerTempertureAreaChart.setAttribute("temperature");
		transformerTempertureAreaChart.setMaxElements(600);
		transformerTempertureAreaChart.setTimeAttribute("start");
		transformerTempertureAreaChart.setTitle("Temperature of transformer in Celsius");

		transformerCollection.addGaugeVisualizationInformation(transformerTemperatureGauge);
		transformerCollection.addAreaChartVisualizationInformation(transformerTempertureAreaChart);
		transformerCollection.addAreaChartVisualizationInformation(energyOutputAreaChart);
		// Define, how it should be colored
		CollectionColoringInformation transformerColoring = new CollectionColoringInformation();
		transformerColoring.setConnectionInformation(conTransformer);
		transformerColoring.setAttribute("temperature");
		transformerColoring.setMinValue(20);
		transformerColoring.setMaxValue(80);
		transformerCollection.setCollectionColoringInformation(transformerColoring);
		// Connections for the collection
		List<ConnectionInformation> transformerConnections = new ArrayList<>();
		transformerConnections.add(conTempChangeAnalysis);
		transformerConnections.add(conTempValueAreaAnalysis);
		transformerCollection.setConnectionInformation(transformerConnections);
		// Add a link from this overview to a specific collection
		transformerTemperatureGauge.setCollectionLink(transformerCollection.getIdentifier());

		// Cooling system
		// --------------
		CollectionInformation coolingSystemCollection = new CollectionInformation();
		coolingSystemCollection.setName("Cooling System");
		// Visualization
		AreaChartVisualizationInformation pump1AreaChart = new AreaChartVisualizationInformation();
		pump1AreaChart.setConnectionInformation(conPump1);
		pump1AreaChart.setAttribute("lastOutFlowRate");
		pump1AreaChart.setTimeAttribute("start");
		pump1AreaChart.setMaxElements(1000);
		pump1AreaChart.setTitle("Pump 1");

		AreaChartVisualizationInformation pump2AreaChart = new AreaChartVisualizationInformation();
		pump2AreaChart.setConnectionInformation(conPump2);
		pump2AreaChart.setAttribute("lastOutFlowRate");
		pump2AreaChart.setTimeAttribute("start");
		pump2AreaChart.setMaxElements(1000);
		pump2AreaChart.setTitle("Pump 2");

		AreaChartVisualizationInformation pump3AreaChart = new AreaChartVisualizationInformation();
		pump3AreaChart.setConnectionInformation(conPump3);
		pump3AreaChart.setAttribute("lastOutFlowRate");
		pump3AreaChart.setTimeAttribute("start");
		pump3AreaChart.setMaxElements(1000);
		pump3AreaChart.setTitle("Pump 3");

		coolingSystemCollection.addAreaChartVisualizationInformation(bigCoolingPipeThorughputAreaChart);
		coolingSystemCollection.addAreaChartVisualizationInformation(pump1AreaChart);
		coolingSystemCollection.addAreaChartVisualizationInformation(pump2AreaChart);
		coolingSystemCollection.addAreaChartVisualizationInformation(pump3AreaChart);

		// Coloring
		CollectionColoringInformation coolingColoring = new CollectionColoringInformation();
		coolingColoring.setConnectionInformation(conBigPipeOut);
		coolingColoring.setAttribute("lastThroughput");
		coolingColoring.setMinValue(1800);
		coolingColoring.setMaxValue(5000);
		coolingSystemCollection.setCollectionColoringInformation(coolingColoring);
		// Connections for the collection
		List<ConnectionInformation> coolingSystemConnections = new ArrayList<>();
		coolingSystemConnections.add(conPumpPattern);
		coolingSystemConnections.add(conIntervalPumpSwitch);
		coolingSystemConnections.add(conCoolingPipeValueArea);
		coolingSystemConnections.add(conPump1Vibration);
		coolingSystemConnections.add(conPump2Vibration);
		coolingSystemConnections.add(conPump3Vibration);
		coolingSystemCollection.setConnectionInformation(coolingSystemConnections);
		// Link
		bigCoolingPipeThorughputAreaChart.setCollectionLink(coolingSystemCollection.getIdentifier());

		// Windturbines
		// ------------
		CollectionInformation windturbineCollection = new CollectionInformation();
		windturbineCollection.setName("Windturbines");
		// Visualizations
		windturbineCollection.addAreaChartVisualizationInformation(windspeedAreaChart);
		// Connections
		List<ConnectionInformation> windturbineConnections = new ArrayList<>();
		windturbineConnections.add(conCohortDeviation);
		windturbineCollection.setConnectionInformation(windturbineConnections);
		// Link
		windspeedAreaChart.setCollectionLink(windturbineCollection.getIdentifier());
		energyOutputAreaChart.setCollectionLink(windturbineCollection.getIdentifier());

		collections.add(transformerCollection);
		collections.add(coolingSystemCollection);
		collections.add(windturbineCollection);
		clientConfig.setCollections(collections);

		// --------------------
		// Server Configuration
		// --------------------
		ServerConfiguration serverConfig = new ServerConfiguration();
		// Visualization
		serverConfig.addSourcePath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\sources.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\visualization\\transformer.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\visualization\\bigPipeThroughput.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\visualization\\windspeed.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\visualization\\pump1.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\visualization\\pump2.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\visualization\\pump3.qry",
				"OdysseusScript");
		// Analysis
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\pumpPatternAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\transformerTemperatureAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\coolingPipeFlowAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\windturbineCohortAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\pumpSwitchIntervalAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\pump1VibrationAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\pump2VibrationAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"C:\\Users\\Tobias\\development\\odysseusWS\\ConditionMonitoring_Master\\demonstration\\analysis\\pump3VibrationAnalysis.qry",
				"OdysseusScript");

		// Put these to the big config
		config.setClientConfiguration(clientConfig);
		config.setServerConfiguration(serverConfig);

		Gson gson = new Gson();
		String json = gson.toJson(config);
		System.out.println(json);

		try {
			MongoClient mongoClient = new MongoClient("127.0.0.1:27017");
			DBCollection mongoDBCollection = mongoClient.getDB("odysseus").getCollection("conditionConfigurations");

			// Delete the old version of this configuration
			String deleteString = "{\"" + "name" + "\":\"" + config.getName() + "\"}";
			DBObject remove = (DBObject) JSON.parse(deleteString);
			mongoDBCollection.remove(remove);

			// Add the new version to the database
			List<DBObject> dbObjects = new ArrayList<>();

			DBObject dbObject = (DBObject) JSON.parse(json);
			dbObjects.add(dbObject);
			mongoDBCollection.insert(dbObjects);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
