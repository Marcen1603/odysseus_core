package de.uniol.inf.is.odysseus.condition.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.AreaChartVisualizationInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ClientConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.CollectionColoringInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ConnectionInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.GaugeVisualizationInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.OverviewInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ServerConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.CollectionInformation;

public class ConfigurationCreator {

	// Demo configuration
	public static void main(String[] args) {

		Configuration config = new Configuration();
		config.setName("Offshore Windpark");
		config.setDescription(
				"Analysis of the offshore windpark with windturbines and transformer substation with its cooling system.");

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
		conTransformer.setIp("127.0.0.1");
		conTransformer.setQueryName("transformer");
		conTransformer.setUseName(true);
		conTransformer.setOperatorName("transformerOutput");
		conTransformer.setOperatorOutputPort(0);
		conTransformer.setUseOperatorOutputPort(true);

		// The throughput of the big cooling pipe
		ConnectionInformation conBigPipeOut = new ConnectionInformation();
		conBigPipeOut.setIp("127.0.0.1");
		conBigPipeOut.setQueryName("bigPipeOutput");
		conBigPipeOut.setUseName(true);
		conBigPipeOut.setOperatorName("bigPipeOutOperator");
		conBigPipeOut.setOperatorOutputPort(0);
		conBigPipeOut.setUseOperatorOutputPort(true);

		// The windspeed on the northsea
		ConnectionInformation conWindspeed = new ConnectionInformation();
		conWindspeed.setIp("127.0.0.1");
		conWindspeed.setQueryName("windspeedQuery");
		conWindspeed.setUseName(true);
		conWindspeed.setOperatorName("windspeedOutput");
		conWindspeed.setOperatorOutputPort(0);
		conWindspeed.setUseOperatorOutputPort(true);

		// Analysis
		// -------
		// The pattern analysis of the cooling system
		ConnectionInformation conPumpPattern = new ConnectionInformation();
		conPumpPattern.setIp("127.0.0.1");
		conPumpPattern.setQueryName("pumpPatternAnalysis");
		conPumpPattern.setUseName(true);
		conPumpPattern.setOperatorName("pumpRarePattern");
		conPumpPattern.setOperatorOutputPort(0);
		conPumpPattern.setUseOperatorOutputPort(true);

		// The temperature analysis of the transformer (valueArea)
		ConnectionInformation conTempValueAreaAnalysis = new ConnectionInformation();
		conTempValueAreaAnalysis.setIp("127.0.0.1");
		conTempValueAreaAnalysis.setQueryName("transformerTempAnalysis");
		conTempValueAreaAnalysis.setUseName(true);
		conTempValueAreaAnalysis.setOperatorName("valueTemp");
		conTempValueAreaAnalysis.setOperatorOutputPort(0);
		conTempValueAreaAnalysis.setUseOperatorOutputPort(true);

		// The temperature analysis of the transformer (valueArea)
		ConnectionInformation conTempChangeAnalysis = new ConnectionInformation();
		conTempChangeAnalysis.setIp("127.0.0.1");
		conTempChangeAnalysis.setQueryName("transformerTempAnalysis");
		conTempChangeAnalysis.setUseName(true);
		conTempChangeAnalysis.setOperatorName("changeTemp");
		conTempChangeAnalysis.setOperatorOutputPort(0);
		conTempChangeAnalysis.setUseOperatorOutputPort(true);

		// The flow of the cooling pipe
		ConnectionInformation conCoolingPipeValueArea = new ConnectionInformation();
		conCoolingPipeValueArea.setIp("127.0.0.1");
		conCoolingPipeValueArea.setQueryName("coolingPipeThroughput");
		conCoolingPipeValueArea.setUseName(true);
		conCoolingPipeValueArea.setOperatorName("coolingPipeValue");
		conCoolingPipeValueArea.setOperatorOutputPort(0);
		conCoolingPipeValueArea.setUseOperatorOutputPort(true);

		connectionInformation.add(conTransformer);
		connectionInformation.add(conBigPipeOut);
		connectionInformation.add(conWindspeed);
		connectionInformation.add(conPumpPattern);
		connectionInformation.add(conTempValueAreaAnalysis);
		connectionInformation.add(conTempChangeAnalysis);
		connectionInformation.add(conCoolingPipeValueArea);
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

		// Collections
		// -----------
		List<CollectionInformation> collections = new ArrayList<>();

		// Transformer
		CollectionInformation transformerCollection = new CollectionInformation();
		transformerCollection.setName("Transformer");
		transformerCollection.addGaugeVisualizationInformation(transformerTemperatureGauge);
		// Define, how it should be colored
		CollectionColoringInformation transformerColoring = new CollectionColoringInformation();
		transformerColoring.setConnectionInformation(conTransformer);
		transformerColoring.setAttribute("temperature");
		transformerColoring.setMinValue(20);
		transformerColoring.setMaxValue(70);
		transformerCollection.setCollectionColoringInformation(transformerColoring);
		// Add a link from this overview to a specific collection
		transformerTemperatureGauge.setCollectionLink(transformerCollection.getIdentifier());

		// Cooling system
		CollectionInformation coolingSystemCollection = new CollectionInformation();
		coolingSystemCollection.setName("Cooling System");
		coolingSystemCollection.addAreaChartVisualizationInformation(bigCoolingPipeThorughputAreaChart);
		List<ConnectionInformation> coolingSystemConnections = new ArrayList<>();
		coolingSystemConnections.add(conPumpPattern);
		// Link
		bigCoolingPipeThorughputAreaChart.setCollectionLink(coolingSystemCollection.getIdentifier());

		collections.add(transformerCollection);
		collections.add(coolingSystemCollection);
		clientConfig.setCollections(collections);

		// --------------------
		// Server Configuration
		// --------------------
		ServerConfiguration serverConfig = new ServerConfiguration();
		serverConfig.addSourcePath(
				"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\demonstration\\sources.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\demonstration\\visualization\\transformer.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\demonstration\\visualization\\bigPipeThroughput.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\demonstration\\visualization\\windspeed.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\demonstration\\analysis\\pumpPatternAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\demonstration\\analysis\\transformerTemperatureAnalysis.qry",
				"OdysseusScript");
		serverConfig.addQueryPath(
				"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\demonstration\\analysis\\coolingPipeFlowAnalysis.qry",
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
