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
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ServerConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.CollectionInformation;

public class ConfigurationCreator {

	// Demo configuration
	public static void main(String[] args) {

		Configuration config = new Configuration();
		config.setName("Fridge");
		config.setDescription("Fridge analysis.");

		// --------------------
		// Client Configuration
		// --------------------
		ClientConfiguration clientConfig = new ClientConfiguration();

		// Connections to the right queries
		// --------------------------------
		List<ConnectionInformation> connectionInformation = new ArrayList<>();

		// The analysis of the durations
		ConnectionInformation con1Fridge1 = new ConnectionInformation();
		con1Fridge1.setIp("127.0.0.1");
		con1Fridge1.setQueryName("fridgeIntervall");
		con1Fridge1.setUseName(true);

		ConnectionInformation con2Fridge1 = new ConnectionInformation();
		con2Fridge1.setIp("127.0.0.1");
		con2Fridge1.setQueryName("fridgeVibration");
		con2Fridge1.setUseName(true);
		
		ConnectionInformation con1Fridge2 = new ConnectionInformation();
		con1Fridge2.setIp("127.0.0.1");
		con1Fridge2.setQueryName("fridgeIntervall2");
		con1Fridge2.setUseName(true);

		ConnectionInformation con2Fridge2 = new ConnectionInformation();
		con2Fridge2.setIp("127.0.0.1");
		con2Fridge2.setQueryName("fridgeVibration2");
		con2Fridge2.setUseName(true);

		connectionInformation.add(con1Fridge1);
		connectionInformation.add(con2Fridge1);
		connectionInformation.add(con1Fridge2);
		connectionInformation.add(con2Fridge2);
		clientConfig.setConnectionInformation(connectionInformation);

		// Visualizations
		// --------------
		List<GaugeVisualizationInformation> gaugeVisualizations = new ArrayList<>();
		List<AreaChartVisualizationInformation> areaChartVisualizationInformations = new ArrayList<>();

		GaugeVisualizationInformation gauge = new GaugeVisualizationInformation();
		gauge.setConnectionInformation(con1Fridge1);
		gauge.setAttribute("anomalyScore");
		gauge.setMinValue(0.0);
		gauge.setMaxValue(1.0);
		gauge.setStretch(true);
		gauge.setTitle("Fridge One");
		
		GaugeVisualizationInformation gaugeFridge2 = new GaugeVisualizationInformation();
		gaugeFridge2.setConnectionInformation(con1Fridge2);
		gaugeFridge2.setAttribute("anomalyScore");
		gaugeFridge2.setMinValue(0.0);
		gaugeFridge2.setMaxValue(1.0);
		gaugeFridge2.setStretch(true);
		gaugeFridge2.setTitle("Fridge Two");

		AreaChartVisualizationInformation areaChartOverview = new AreaChartVisualizationInformation();
		areaChartOverview.setConnectionInformation(con2Fridge1);
		areaChartOverview.setAttribute("vibration");
		areaChartOverview.setMaxElements(1000);
		areaChartOverview.setTitle("Fridge One Vibration (Last 1000)");
		areaChartOverview.setShowSymbols(false);

		AreaChartVisualizationInformation areaChartFullTime = new AreaChartVisualizationInformation();
		areaChartFullTime.setConnectionInformation(con2Fridge1);
		areaChartFullTime.setAttribute("vibration");
		areaChartFullTime.setTitle("Fridge One Vibration (Full)");
		areaChartFullTime.setTimeAttribute("start");
		
		AreaChartVisualizationInformation areaChartFullTimeFridge2 = new AreaChartVisualizationInformation();
		areaChartFullTimeFridge2.setConnectionInformation(con2Fridge2);
		areaChartFullTimeFridge2.setAttribute("vibration");
		areaChartFullTimeFridge2.setTitle("Fridge Two Vibration (Full)");

		// For the overview
		gaugeVisualizations.add(gauge);
		gaugeVisualizations.add(gaugeFridge2);
		areaChartVisualizationInformations.add(areaChartOverview);
		areaChartVisualizationInformations.add(areaChartFullTime);
		clientConfig.setGaugeVisualizationInformation(gaugeVisualizations);
		clientConfig.setAreaChartVisualizationInformation(areaChartVisualizationInformations);

		// Collections
		// -----------
		List<CollectionInformation> collections = new ArrayList<>();

		CollectionInformation collectionInformation = new CollectionInformation();
		collectionInformation.setName("Fridge One");
		List<ConnectionInformation> connectionInformationList = new ArrayList<>();
		connectionInformationList.add(con1Fridge1);
		collectionInformation.setConnectionInformation(connectionInformationList);
		collectionInformation.addGaugeVisualizationInformation(gauge);
		// Define, how it should be colored
		CollectionColoringInformation coloring1 = new CollectionColoringInformation();
		coloring1.setConnectionInformation(con1Fridge1);
		coloring1.setAttribute("anomalyScore");
		coloring1.setMinValue(0);
		coloring1.setMaxValue(1);
		collectionInformation.setCollectionColoringInformation(coloring1);
		collectionInformation.addAreaChartVisualizationInformation(areaChartFullTime);
		// Add a link from this overview to a specific collection
		gauge.setCollectionLink(collectionInformation.getIdentifier());

		CollectionInformation collectionInformation2 = new CollectionInformation();
		collectionInformation2.setName("Fridge Two");
		List<ConnectionInformation> connectionInformationList2 = new ArrayList<>();
		connectionInformationList2.add(con1Fridge2);
		collectionInformation2.setConnectionInformation(connectionInformationList2);
		collectionInformation2.addGaugeVisualizationInformation(gaugeFridge2);
		collectionInformation2.addAreaChartVisualizationInformation(areaChartFullTimeFridge2);
		// Link to collection
		gaugeFridge2.setCollectionLink(collectionInformation2.getIdentifier());

		collections.add(collectionInformation);
		collections.add(collectionInformation2);
		clientConfig.setCollections(collections);

		// --------------------
		// Server Configuration
		// --------------------
		ServerConfiguration serverConfig = new ServerConfiguration();
		serverConfig
				.addSourcePath(
						"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\sources\\vibrationSensorSource.qry",
						"OdysseusScript");
		serverConfig
				.addQueryPath(
						"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\analysis\\fridge\\fridgeVibration.qry",
						"OdysseusScript");
		serverConfig
				.addQueryPath(
						"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\analysis\\fridge\\fridgeVibrationIntervallAnalysis.qry",
						"OdysseusScript");
		serverConfig
				.addQueryPath(
						"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\analysis\\fridge\\fridgeVibration2.qry",
						"OdysseusScript");
		serverConfig
				.addQueryPath(
						"D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\analysis\\fridge\\fridgeVibrationIntervallAnalysis2.qry",
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
