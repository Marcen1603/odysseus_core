package windscadaanwendung.views;

import java.sql.SQLException;

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
import windscadaanwendung.db.DBConnection;


/**
 * @author MarkMilster
 *
 */
public class ListView extends ViewPart {
	
	//TODO: alle Konstanten in separate Datei auslagern
	public static final String ID = "windscadaanwendung.views.ListView";

	public ListView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		final Tree tree = new Tree(parent, SWT.BORDER);
		
		tree.addListener(SWT.Selection, new Listener() {
		      @Override
			public void handleEvent(Event e) {
		    	  // es kann hier immer nur ein Element selektiert werden
		    	  TreeItem selected = tree.getSelection()[0];
		          if (selected.getParentItem() == null) {
		        	  // selected ist oberste Ebene, also ein Windpark
		        	  WindFarm selectedFarm = FarmList.getFarm(Integer.parseInt(selected.getText()));
		        	  UebersichtView.setSelectedWindpark(selectedFarm);
		        	  DetailView.setSelectedWKA(null);
		        	  MapView.setSelectedFarm(selectedFarm);
		          } else {
		        	  // selected is a WKA
		        	  WindFarm farm = FarmList.getFarm(Integer.parseInt(selected.getParentItem().getText()));
		        	  UebersichtView.setSelectedWindpark(farm);
		        	  MapView.setSelectedFarm(farm);
		        	  // the TreeItemText has to be eaqual to the ID of the WKA
		        	  WKA selectedWKA = FarmList.getWKA(Integer.parseInt(selected.getText()));
		        	  DetailView.setSelectedWKA(selectedWKA);
		          }
		        }

			
		      });
		
		//TODO: an sinvollere Stelle packen
		DBConnection.setNewConnection();
		FarmList.setFarmList(DBConnection.getFarmList());
		
		for (WindFarm farm: FarmList.getFarmList()) {
			TreeItem trtmFarm = new TreeItem(tree, SWT.NONE);
			trtmFarm.setText(String.valueOf(farm.getID()));
			for (WKA wka: farm.getWkas()) {
				TreeItem trtmWka = new TreeItem(trtmFarm, SWT.NONE);
				trtmWka.setText(String.valueOf(wka.getID()));
			}
		}
		try {
			DBConnection.conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
	}

	@Override
	public void setFocus() {
	}

}
