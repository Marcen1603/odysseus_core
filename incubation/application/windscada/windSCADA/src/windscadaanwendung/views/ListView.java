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
 * This class shows a lis of all windfarms and WKAs
 * 
 * @author MarkMilster
 * 
 */
public class ListView extends ViewPart {

	public static final String ID = "windscadaanwendung.views.ListView";
	static Tree tree;

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
	 * Fills a swt-tree with the data out of the static FarmList.
	 * It will show all currently loaded windFarms and WKAs
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

	/**
	 * Selects a WKA in the swt-tree in this view. Also this mehtods handles the selectionevent with the same consequences as a user will select the WKA in the swt-tree
	 * @param id
	 * 			The id of the WKA to select
	 * @return true if the WKA exists in this static swt-tree
	 * 			false if it does'nt exist
	 */
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

	/**
	 * This method should be called if you select a WKA in the static swt-tree. It sets the WKA to the selected one in the DashboardPartViewObserver and the DetailView
	 * 
	 * @param wka a swt-TreeItem which is selected
	 */
	private static void handleSelectionWKA(TreeItem wka) {
		// get the windFarm of the wka and set this as selected windFarm
		WindFarm farm = FarmList.getFarm(Integer.parseInt(wka.getParentItem()
				.getText()));
		UebersichtView.setSelectedWindpark(farm);
		// the TreeItemText has to be eaqual to the ID of the WKA
		WKA selectedWKA = FarmList.getWKA(Integer.parseInt(wka.getText()));
		DetailView.setSelectedWKA(selectedWKA);
		DashboardPartViewObserver.setWKA(selectedWKA);
	}

}
