package cm.configuration;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class ConfigCreator {

    public static void main(String args[]) {
        // Demo configuration
        // ------------------
        Configuration config = new Configuration();

        // Connections to the right queries
        List<ConnectionInformation> connectionInformation = new ArrayList<>();

        ConnectionInformation con1 = new ConnectionInformation();
        con1.setIp("127.0.0.1");
        con1.setQueryName("cohort1");
        con1.setUseName(true);

        connectionInformation.add(con1);
        config.connectionInformation = connectionInformation;

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
        config.collections = collections;

        // Visualizations
        List<VisualizationInformation> visualizations = new ArrayList<>();

        VisualizationInformation visualizationInformation = new VisualizationInformation();
        visualizationInformation.setConnectionInformation(con1);
        visualizationInformation.setAttribute("LOF");
        visualizationInformation.setVisualizationType(VisualizationType.GAUGE);
        visualizationInformation.setMinValue(0.0);
        visualizationInformation.setMaxValue(1.0);

        visualizations.add(visualizationInformation);
        config.visualizationInformation = visualizations;

        Gson gson = new Gson();
        String json = gson.toJson(config);
        System.out.println(json);
    }
}
