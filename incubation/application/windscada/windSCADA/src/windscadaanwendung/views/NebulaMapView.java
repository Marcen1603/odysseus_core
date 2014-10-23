package windscadaanwendung.views;

import org.eclipse.nebula.widgets.geomap.GeoMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This Class is a prototype to show a map in windSCADA. In future work it can
 * be used to show the position of wind turbines, if the feature to draw
 * something onto the map in the Nebula-GeoMap Widget will be added
 * 
 * @author MarkMilster
 * 
 */
public class NebulaMapView extends ViewPart {

	@Override
	public void createPartControl(Composite parent) {
		new GeoMap(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
	}

}
