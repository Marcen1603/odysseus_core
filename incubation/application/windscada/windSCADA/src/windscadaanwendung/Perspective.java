package windscadaanwendung;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IFolderLayout;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView("windscadaanwendung.views.DetailView", IPageLayout.LEFT, 0.79f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("windacadaanwendung.ListView", IPageLayout.LEFT, 0.51f, "windscadaanwendung.views.DetailView");
		{
			IFolderLayout folderLayout = layout.createFolder("folder_2", IPageLayout.BOTTOM, 0.41f, "windscadaanwendung.views.DetailView");
			folderLayout.addView("windscadaanwendung.views.dashboard.CorrectedScoreWindSpeedPart");
			folderLayout.addView("windscadaanwendung.views.dashboard.SVRPart");
		}
		layout.addView("windscadaanwendung.views.dashboard.GierAnglePart", IPageLayout.BOTTOM, 0.5f, "windscadaanwendung.views.dashboard.CorrectedScoreWindSpeedPart");
		layout.addView("windscadaanwendung.views.dashboard.WindDirectionPart", IPageLayout.LEFT, 0.5f, "windscadaanwendung.views.dashboard.GierAnglePart");
		layout.addView("windscadaanwendung.views.UebersichtView", IPageLayout.LEFT, 0.76f, "windacadaanwendung.ListView");
		{
			IFolderLayout folderLayout = layout.createFolder("folder", IPageLayout.BOTTOM, 0.58f, "windscadaanwendung.views.UebersichtView");
			folderLayout.addView("windscadaanwendung.views.dashboard.ParkColorListPart");
			folderLayout.addView("windscadaanwendung.views.HitAEView");
			folderLayout.addView("windscadaanwendung.views.NebulaMapView");
		}
		layout.addView("windscadaanwendung.views.dashboard.ParkSumCorrectedScorePart", IPageLayout.BOTTOM, 0.5f, "windscadaanwendung.views.UebersichtView");
		{
			IFolderLayout folderLayout = layout.createFolder("folder_1", IPageLayout.BOTTOM, 0.75f, "windacadaanwendung.ListView");
			folderLayout.addView("windscadaanwendung.views.dashboard.RotationalSpeedPart");
		}
		layout.addView("windscadaanwendung.views.dashboard.AEListPart", IPageLayout.BOTTOM, 0.45f, "windacadaanwendung.ListView");
	}
}
