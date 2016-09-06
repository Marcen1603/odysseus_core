package cm.view;

import cm.configuration.ConfigurationService;
import cm.configuration.GaugeVisualizationInformation;
import cm.controller.MainController;
import cm.data.DataHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.labs.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.labs.scene.control.gauge.linear.elements.Segment;

import java.io.IOException;

/**
 * @author Tobias
 * @since 23.06.2015.
 */
public class GaugeElement extends VBox {

    private final static double strechFactor = 100;
    private Parent root;
    private static final int NUMBER_OF_SEGMENTS = 10;

    @FXML
    Label gaugeTitle;
    @FXML
    SimpleMetroArcGauge gauge;

    private GaugeVisualizationInformation visualizationInformation;

    public GaugeElement(GaugeVisualizationInformation visualizationInformation) {
        super();
        this.visualizationInformation = visualizationInformation;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gaugeElement.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        gaugeTitle.setText(visualizationInformation.getTitle());

        if (visualizationInformation.isStretch()) {
            gauge.setMinValue(visualizationInformation.getMinValue() * strechFactor);
            gauge.setMaxValue(visualizationInformation.getMaxValue() * strechFactor);
        } else {
            gauge.setMinValue(visualizationInformation.getMinValue());
            gauge.setMaxValue(visualizationInformation.getMaxValue());
        }
        gauge.setValue(0);

        // Style
        gauge.segments().clear();
        for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) {
            Segment segment = new PercentSegment(gauge, i * 100 / NUMBER_OF_SEGMENTS, (i + 1) * (100 / NUMBER_OF_SEGMENTS));
            gauge.segments().add(segment);
        }

//        String colorSchemeClass = "colorscheme-green-to-red-10";
//        gauge.getStyleClass().add(colorSchemeClass);

        // As the colorSchema does not work, just set the colors manually
        String animatedStyle = "-fxx-animated: YES;";
        String segments = "-fxx-segment0-color: #97b329;\n" +
                "    -fxx-segment1-color: #aacc2a;\n" +
                "    -fxx-segment2-color: #d4ea35;\n" +
                "    -fxx-segment3-color: #f2de31;\n" +
                "    -fxx-segment4-color: #fccb2e;\n" +
                "    -fxx-segment5-color: #f3a429;\n" +
                "    -fxx-segment6-color: #f18c23;\n" +
                "    -fxx-segment7-color: #f65821;\n" +
                "    -fxx-segment8-color: #f3351f;\n" +
                "    -fxx-segment9-color: #f61319;";
        gauge.setStyle(animatedStyle + "\n" + segments);

        // Click listener
        addClickListener();
    }

    public void setValue(double value) {
        if (visualizationInformation.isStretch()) {
            double stretchedValue = value * strechFactor;
            gauge.setValue(stretchedValue);
        } else {
            gauge.setValue(value);
        }
    }

    public void addClickListener() {
        // Click listener to get to the linked collection (if there is one)
        if (visualizationInformation.getCollectionLink() != null) {
            MainController mainController = ConfigurationService.getInstance().getMainController();
            this.setOnMouseClicked(event -> {
                cm.model.Collection collection = DataHandler.getInstance().getCollection(visualizationInformation.getCollectionLink());
                mainController.switchToCollection(collection);
            });
            gauge.setOnMouseClicked(event -> {
                cm.model.Collection collection = DataHandler.getInstance().getCollection(visualizationInformation.getCollectionLink());
                mainController.switchToCollection(collection);
            });
        }
    }
}
