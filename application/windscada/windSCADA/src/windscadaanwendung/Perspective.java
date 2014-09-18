package windscadaanwendung;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView("windscadaanwendung.views.DetailView", IPageLayout.LEFT, 0.79f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("windacadaanwendung.ListView", IPageLayout.LEFT, 0.51f, "windscadaanwendung.views.DetailView");
		layout.addView("windscadaanwendung.views.dashboard.CorrectedScorePart", IPageLayout.BOTTOM, 0.5f, "windscadaanwendung.views.DetailView");
		layout.addView("windscadaanwendung.views.dashboard.WindSpeedPart", IPageLayout.BOTTOM, 0.5f, "windscadaanwendung.views.dashboard.CorrectedScorePart");
		layout.addView("windscadaanwendung.views.dashboard.RotationalSpeedPart", IPageLayout.RIGHT, 0.5f, "windscadaanwendung.views.DetailView");
		layout.addView("windscadaanwendung.views.UebersichtView", IPageLayout.LEFT, 0.76f, "windacadaanwendung.ListView");
		
	}
}
