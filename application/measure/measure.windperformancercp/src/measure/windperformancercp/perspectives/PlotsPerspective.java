package measure.windperformancercp.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PlotsPerspective implements IPerspectiveFactory {
	public static final String ID = "measure.windperformancercp.plotsPerspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true); //if true, the views can be closed by the user. we dont want that.

	}

}
