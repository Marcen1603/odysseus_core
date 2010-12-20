package windperformancercp.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ConfigurePerspective implements IPerspectiveFactory {
	public static final String ID = "measure.windPerformanceRCP.confPerspective";
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		
	}

}
