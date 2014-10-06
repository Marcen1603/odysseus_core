package windscadaanwendung.views;

import org.eclipse.nebula.widgets.geomap.GeoMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class NebulaMapView extends ViewPart {

	@Override
	public void createPartControl(Composite parent) {
		new GeoMap(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
	}

}
