package windscadaanwendung.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;
import windscadaanwendung.db.DBConnectionHD;

/**
 * 
 * @author MarkMilster
 *
 */
public class UebersichtView extends ViewPart {
	
	public static final String ID = "windscadaanwendung.views.DetailView";
	private static Text hitLeistung;
	private static Table table;
	private static Text nameWindpark;

	public UebersichtView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblwindpark = new Label(composite, SWT.NONE);
		lblwindpark.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblwindpark.setText("Windpark:");
		new Label(composite, SWT.NONE);
		
		nameWindpark = new Text(composite, SWT.BORDER);
		nameWindpark.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblHistorischeGesamtleistungIm = new Label(composite, SWT.NONE);
		lblHistorischeGesamtleistungIm.setText("Historische Gesamtleistung im Durchschnitt:");
		
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setSize(198, 22);
		
		hitLeistung = new Text(composite, SWT.BORDER);
		hitLeistung.setSize(297, 469);
		
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnWindkraftanlage = new TableColumn(table, SWT.NONE);
		tblclmnWindkraftanlage.setWidth(100);
		tblclmnWindkraftanlage.setText("Windkraftanlage");
		
		TableColumn tblclmnAktuelleLeistung = new TableColumn(table, SWT.NONE);
		tblclmnAktuelleLeistung.setWidth(100);
		tblclmnAktuelleLeistung.setText("Aktuelle Leistung");
		
		TableColumn tblclmnDurchschnittlicheLeistung = new TableColumn(table, SWT.NONE);
		tblclmnDurchschnittlicheLeistung.setWidth(100);
		tblclmnDurchschnittlicheLeistung.setText("durchschnittliche Leistung");
	}

	@Override
	public void setFocus() {
	}
	
	public static void setSelectedWindpark(WindFarm windFarm) {
		nameWindpark.setText(String.valueOf(windFarm.getID()));
		DBConnectionHD.setHitFarmData(windFarm);
		if (windFarm.getHitWindFarmData() != null) {
			hitLeistung.setText(String.valueOf(windFarm.getHitWindFarmData().getAvgPervormance()));
		}
		
		table.removeAll();
		TableItem item;
		for (WKA wka: windFarm.getWkas()) {
			DBConnectionHD.setHitWKAData(wka);
			item = new TableItem(table, SWT.NONE);
			item.setText(0, String.valueOf(wka.getID()));
			item.setText(1, "akt");
			if (wka.getHitWKAData() != null) {
				item.setText(2, String.valueOf(wka.getHitWKAData().getAvgPerformance()));
			} else {
				item.setText(2, "No Data");
			}
			//TODO: implementieren, dass nach x sekunden aktualisiert wird
			
		}
		
	}

}
