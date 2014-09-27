package windscadaanwendung.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.ca.FarmList;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;
import windscadaanwendung.views.dashboard.DashboardPartViewObserver;

/**
 * @author MarkMilster
 * 
 */
public class ListView extends ViewPart {

	public static final String ID = "windscadaanwendung.views.ListView";
	static Tree tree;

	public ListView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		tree = new Tree(parent, SWT.BORDER);

		tree.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				// es kann hier immer nur ein Element selektiert werden
				TreeItem selected = tree.getSelection()[0];
				if (selected.getParentItem() == null) {
					// selected ist oberste Ebene, also ein Windpark
					WindFarm selectedFarm = FarmList.getFarm(Integer
							.parseInt(selected.getText()));
					UebersichtView.setSelectedWindpark(selectedFarm);
					DetailView.setSelectedWKA(null);
					// MapView.setSelectedFarm(selectedFarm);
				} else {
					// selected is a WKA
					handleSelectionWKA(selected);
				}
			}

		});

		this.createFarmList(tree);

	}

	/**
	 * Creates a FarmList from the CA Database into the Specified tree
	 * 
	 * @param tree
	 *            The tree which will show the data
	 */
	private void createFarmList(Tree tree) {

		for (WindFarm farm : FarmList.getFarmList()) {
			TreeItem trtmFarm = new TreeItem(tree, SWT.NONE);
			trtmFarm.setText(String.valueOf(farm.getID()));
			for (WKA wka : farm.getWkas()) {
				TreeItem trtmWka = new TreeItem(trtmFarm, SWT.NONE);
				trtmWka.setText(String.valueOf(wka.getID()));
			}
		}
	}

	@Override
	public void setFocus() {
	}

	public static boolean setSelectedWKAById(String id) {
		for (TreeItem farm : tree.getItems()) {
			for (TreeItem wka : farm.getItems()) {
				if (wka.getText().equals(id)) {
					tree.setSelection(wka);
					handleSelectionWKA(wka);
					return true;
				}
			}
		}
		return false;
	}

	private static void handleSelectionWKA(TreeItem wka) {
		WindFarm farm = FarmList.getFarm(Integer.parseInt(wka.getParentItem()
				.getText()));
		UebersichtView.setSelectedWindpark(farm);
		// the TreeItemText has to be eaqual to the ID of the WKA
		WKA selectedWKA = FarmList.getWKA(Integer.parseInt(wka.getText()));
		DetailView.setSelectedWKA(selectedWKA);
		DashboardPartViewObserver.setWKA(selectedWKA);
	}

}
