package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap.Heatmap;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap.TraceLayer;

public class TreeListener implements ISelectionChangedListener {

	Composite container;
	MapPropertiesDialog parentDialog;

	public TreeListener(Composite container, MapPropertiesDialog parentDialog) {
		this.container = container;
		this.parentDialog = parentDialog;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection().isEmpty()) {
			return;
		}
		if (event.getSelection() instanceof TreeSelection) {
			IStructuredSelection selection = (IStructuredSelection) event
					.getSelection();

			if (selection.getFirstElement() instanceof Heatmap) {
				// Show the settings for the heatmap
				Heatmap heatmap = (Heatmap) selection.getFirstElement();
				parentDialog.createHeatmapMenu(heatmap, container);

			} else if (selection.getFirstElement() instanceof TraceLayer) {
				// Show the settings for the tracemap
				TraceLayer tracemap = (TraceLayer) selection.getFirstElement();
				parentDialog.createTraceLayerMenu(tracemap, container);

			} else {
				// Show normal content - not for thematic maps

				// Remove everything except the Tree on the left
				parentDialog.removeContent(container);

				// Fill with standard-content
				parentDialog.createStandardContent(container);
			}
		}
	}
}
