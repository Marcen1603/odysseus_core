package windscadaanwendung;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IFolderLayout;

/**
 * This class represents the windSCADA-Perspective with all views, without the
 * ones which use machine learning-tools
 * 
 * @author MarkMilster
 * 
 */
public class PerspectiveWithoutMachineLearning implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView("windscadaanwendung.views.DetailView", IPageLayout.LEFT,
				0.79f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("windacadaanwendung.ListView", IPageLayout.LEFT, 0.51f,
				"windscadaanwendung.views.DetailView");
		layout.addView("windscadaanwendung.views.dashboard.WindDirectionPart",
				IPageLayout.BOTTOM, 0.72f,
				"windscadaanwendung.views.DetailView");
		layout.addView(
				"windscadaanwendung.views.dashboard.CorrectedScoreWindSpeedPart",
				IPageLayout.BOTTOM, 0.5f, "windscadaanwendung.views.DetailView");
		layout.addView(
				"windscadaanwendung.views.dashboard.CorrectedScoreWindSpeedPart",
				IPageLayout.TOP, 0.5f,
				"windscadaanwendung.views.dashboard.SVRPart");
		layout.addView("windscadaanwendung.views.dashboard.GierAnglePart",
				IPageLayout.RIGHT, 0.5f,
				"windscadaanwendung.views.dashboard.WindDirectionPart");
		layout.addView("windscadaanwendung.views.UebersichtView",
				IPageLayout.LEFT, 0.76f, "windacadaanwendung.ListView");
		{
			IFolderLayout folderLayout = layout.createFolder("folder",
					IPageLayout.BOTTOM, 0.58f,
					"windscadaanwendung.views.UebersichtView");
			folderLayout.addView("windscadaanwendung.views.HitAEView");
			folderLayout.addView("windscadaanwendung.views.NebulaMapView");
		}
		layout.addView(
				"windscadaanwendung.views.dashboard.ParkSumCorrectedScorePart",
				IPageLayout.BOTTOM, 0.5f,
				"windscadaanwendung.views.UebersichtView");
		layout.addView("windscadaanwendung.views.dashboard.AEListPart",
				IPageLayout.BOTTOM, 0.45f, "windacadaanwendung.ListView");
		layout.addView(
				"windscadaanwendung.views.dashboard.RotationalSpeedPart",
				IPageLayout.BOTTOM, 0.5f,
				"windscadaanwendung.views.dashboard.AEListPart");
	}
}
