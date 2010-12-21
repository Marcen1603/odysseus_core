package windperformancercp.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ConfigurePerspective implements IPerspectiveFactory {
	public static final String ID = "measure.windPerformanceRCP.confPerspective";
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true); //if true, the views can be closed by the user. we dont want that.
		
	}

}
