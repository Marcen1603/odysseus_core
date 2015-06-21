package de.uniol.inf.is.odysseus.condition.rest.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.AreaChartVisualizationInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ClientConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ConnectionInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.GaugeVisualizationInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ServerConfiguration;
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
        clientConfig.setConnectionInformation(connectionInformation);

        // Visualizations
        List<GaugeVisualizationInformation> gaugeVisualizations = new ArrayList<>();
        List<AreaChartVisualizationInformation> areaChartVisualizationInformations = new ArrayList<>();

        GaugeVisualizationInformation gauge = new GaugeVisualizationInformation();
        gauge.setConnectionInformation(con1);
        gauge.setAttribute("LOF");
        gauge.setVisualizationType(VisualizationType.GAUGE);
        gauge.setMinValue(0.0);
        gauge.setMaxValue(25.0);
        gauge.setTitle("LOF Value");
        
        AreaChartVisualizationInformation areaChartOverview = new AreaChartVisualizationInformation();
        areaChartOverview.setConnectionInformation(con1);
        areaChartOverview.setAttribute("LOF");
        areaChartOverview.setVisualizationType(VisualizationType.AREACHART);
        areaChartOverview.setMaxElements(50);
        areaChartOverview.setTitle("LOF Chart");
        
        AreaChartVisualizationInformation areaChartCollection = new AreaChartVisualizationInformation();
        areaChartCollection.setConnectionInformation(con1);
        areaChartCollection.setAttribute("LOF");
        areaChartCollection.setVisualizationType(VisualizationType.AREACHART);
        areaChartCollection.setTitle("LOF Chart");
        
        // For the overview
        gaugeVisualizations.add(gauge);
        areaChartVisualizationInformations.add(areaChartOverview);
        clientConfig.setGaugeVisualizationInformation(gaugeVisualizations);
        clientConfig.setAreaChartVisualizationInformation(areaChartVisualizationInformations);
        
        // Collections
        List<CollectionInformation> collections = new ArrayList<>();

        CollectionInformation collectionInformation = new CollectionInformation();
        collectionInformation.setName("Machine1");
        List<ConnectionInformation> connectionInformationList = new ArrayList<>();
        connectionInformationList.add(con1);
        collectionInformation.setConnectionInformation(connectionInformationList);
        collectionInformation.addGaugeVisualizationInformation(gauge);
        // Add a link from this overview to a specific collection
        gauge.setCollectionLink(collectionInformation.getIdentifier());

        CollectionInformation collectionInformation2 = new CollectionInformation();
        collectionInformation2.setName("Machine2");
        List<ConnectionInformation> connectionInformationList2 = new ArrayList<>();
        connectionInformationList2.add(con1);
        collectionInformation2.setConnectionInformation(connectionInformationList2);
        collectionInformation2.addAreaChartVisualizationInformation(areaChartCollection);
        // Link to collection
        areaChartCollection.setCollectionLink(collectionInformation2.getIdentifier());

        collections.add(collectionInformation);
        collections.add(collectionInformation2);
        clientConfig.setCollections(collections);

        

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
