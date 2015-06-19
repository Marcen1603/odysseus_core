package de.uniol.inf.is.odysseus.condition.rest.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.ClientConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ConnectionInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ServerConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.VisualizationInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.VisualizationType;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.CollectionInformation;

public class ConfigurationCreator {

	// Demo configuration
	public static void main(String[] args) {
		
		Configuration config = new Configuration();
        
		// Client Configuration
		// --------------------		
		ClientConfiguration clientConfig = new ClientConfiguration();

        // Connections to the right queries
        List<ConnectionInformation> connectionInformation = new ArrayList<>();

        ConnectionInformation con1 = new ConnectionInformation();
        con1.ip = "127.0.0.1";
        con1.queryName = "cohort1";
        con1.useName = true;

        connectionInformation.add(con1);
        clientConfig.connectionInformation = connectionInformation;

        // Collections
        List<CollectionInformation> collections = new ArrayList<>();

        CollectionInformation collectionInformation = new CollectionInformation();
        collectionInformation.name = "Machine1";
        List<ConnectionInformation> connectionInformationList = new ArrayList<>();
        connectionInformationList.add(con1);
        collectionInformation.connectionInformation = connectionInformationList;

        CollectionInformation collectionInformation2 = new CollectionInformation();
        collectionInformation2.name = "Machine2";
        List<ConnectionInformation> connectionInformationList2 = new ArrayList<>();
        connectionInformationList2.add(con1);
        collectionInformation2.connectionInformation = connectionInformationList2;

        collections.add(collectionInformation);
        collections.add(collectionInformation2);
        clientConfig.collections = collections;

        // Visualizations
        List<VisualizationInformation> visualizations = new ArrayList<>();

        VisualizationInformation visualizationInformation = new VisualizationInformation();
        visualizationInformation.connectionInformation = con1;
        visualizationInformation.attribute = "LOF";
        visualizationInformation.visualizationType = VisualizationType.GAUGE;
        visualizationInformation.minValue = 0.0;
        visualizationInformation.maxValue = 25.0;
        
        VisualizationInformation areaChart1 = new VisualizationInformation();
        areaChart1.connectionInformation = con1;
        areaChart1.attribute = "LOF";
        areaChart1.visualizationType = VisualizationType.AREACHART;

        visualizations.add(visualizationInformation);
        visualizations.add(areaChart1);
        clientConfig.visualizationInformation = visualizations;

        // Server Configuration
        // --------------------
        ServerConfiguration serverConfig = new ServerConfiguration();
        serverConfig.addSourcePath("D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\sources\\windParkSource.qry", "OdysseusScript");
        serverConfig.addQueryPath("D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\OdysseusWS\\ConditionMonitoring\\analysis\\cohortAnalysis.qry", "OdysseusScript");
        
        // Put these to the big config
        config.setClientConfiguration(clientConfig);
        config.setServerConfiguration(serverConfig);
        
        Gson gson = new Gson();
        String json = gson.toJson(config);
        System.out.println(json);
	}
	
}
