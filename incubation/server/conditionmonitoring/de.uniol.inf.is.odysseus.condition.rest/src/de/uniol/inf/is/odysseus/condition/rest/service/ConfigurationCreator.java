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
        con1.setIp("127.0.0.1");
        con1.setQueryName("cohort1");
        con1.setUseName(true);

        connectionInformation.add(con1);
        clientConfig.connectionInformation = connectionInformation;

        // Visualizations
        List<VisualizationInformation> visualizations = new ArrayList<>();

        VisualizationInformation gauge = new VisualizationInformation();
        gauge.setConnectionInformation(con1);
        gauge.setAttribute("LOF");
        gauge.setVisualizationType(VisualizationType.GAUGE);
        gauge.setMinValue(0.0);
        gauge.setMaxValue(25.0);
        
        VisualizationInformation areaChart1 = new VisualizationInformation();
        areaChart1.setConnectionInformation(con1);
        areaChart1.setAttribute("LOF");
        areaChart1.setVisualizationType(VisualizationType.AREACHART);

        visualizations.add(gauge);
        visualizations.add(areaChart1);
        clientConfig.visualizationInformation = visualizations;
        
        // Collections
        List<CollectionInformation> collections = new ArrayList<>();

        CollectionInformation collectionInformation = new CollectionInformation();
        collectionInformation.setName("Machine1");
        List<ConnectionInformation> connectionInformationList = new ArrayList<>();
        connectionInformationList.add(con1);
        collectionInformation.setConnectionInformation(connectionInformationList);
        collectionInformation.addVisualizationInformation(gauge);
        // Add a link from this overview to a specific collection
        gauge.setCollectionLink(collectionInformation.getIdentifier());

        CollectionInformation collectionInformation2 = new CollectionInformation();
        collectionInformation2.setName("Machine2");
        List<ConnectionInformation> connectionInformationList2 = new ArrayList<>();
        connectionInformationList2.add(con1);
        collectionInformation2.setConnectionInformation(connectionInformationList2);
        collectionInformation2.addVisualizationInformation(areaChart1);

        collections.add(collectionInformation);
        collections.add(collectionInformation2);
        clientConfig.collections = collections;

        

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
