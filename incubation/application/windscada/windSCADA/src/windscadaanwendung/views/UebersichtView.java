package windscadaanwendung.views;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;
import windscadaanwendung.db.DBConnectionHD;
import windscadaanwendung.hd.HitWKAData;
import windscadaanwendung.views.dashboard.CorrectedScoreTfPart;

/**
 * 
 * @author MarkMilster
 *
 */
public class UebersichtView extends ViewPart implements Observer {
	
	public static final String ID = "windscadaanwendung.views.DetailView";
	private static Text hitLeistung;
	private static Text nameWindpark;
	private static Composite wkaContainer;
	private static WindFarm selectedWindpark;

	public UebersichtView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		ObserverHandler.addObserverToWKA(this);
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblwindpark = new Label(composite, SWT.NONE);
		lblwindpark.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblwindpark.setText("Windpark:");
		
		nameWindpark = new Text(composite, SWT.BORDER);
		nameWindpark.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblHistorischeGesamtleistungIm = new Label(composite, SWT.NONE);
		lblHistorischeGesamtleistungIm.setText("Historische Gesamtleistung im Durchschnitt:");
		
		hitLeistung = new Text(composite, SWT.BORDER);
		hitLeistung.setSize(297, 469);
		
		wkaContainer = new Composite(parent, SWT.NONE);
		wkaContainer.setLayout(new GridLayout(3, false));
		setDefaultWKAContainerLabels();
	}

	@Override
	public void setFocus() {
	}
	
	public static void setSelectedWindpark(WindFarm windFarm) {
		selectedWindpark = windFarm;
		
		nameWindpark.setText(String.valueOf(selectedWindpark.getID()));
		//TODO Hit Farm Data on FarmList mit auslagern und wie bei WKA mit Observern behandeln und in mysql prozeduren schreiben
		DBConnectionHD.setHitFarmData(selectedWindpark);
		if (selectedWindpark.getHitWindFarmData() != null) {
			hitLeistung.setText(String.valueOf(selectedWindpark.getHitWindFarmData().getAvgPervormance()));
		}
		
		redrawWKAContainer();
		
	}

	private static void redrawWKAContainer() {
		// clear container
				for (Control c : wkaContainer.getChildren()) {
			        c.dispose(); 
			    }
			    wkaContainer.layout();
			    setDefaultWKAContainerLabels();
				Button btn;
				Text txtHit;
				Composite comp;
				CorrectedScoreTfPart tfP;
				for (WKA wka: selectedWindpark.getWkas()) {
					btn = new Button(wkaContainer, SWT.NONE);
					btn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
					btn.setText(String.valueOf(wka.getID()));
					btn.addMouseListener(new WKABTNListener());
					comp = new Composite(wkaContainer, SWT.NONE);
					comp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
					tfP = new CorrectedScoreTfPart();
					tfP.createPartControl(comp, wka.getID());
					
					txtHit = new Text(wkaContainer, SWT.NONE);
					txtHit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
					if (wka.getHitWKAData() != null) {
						txtHit.setText(String.valueOf(wka.getHitWKAData().getAvgPerformance()));
					} else {
						txtHit.setText("No Data");
					}
					//TODO: implementieren, dass nach x sekunden die historischen Daten aktualisiert wird
					
				}
				wkaContainer.layout();
	}

	private static void setDefaultWKAContainerLabels() {
		Label lbl = new Label(wkaContainer, SWT.NONE);
		lbl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lbl.setText("WKA");
		Label lbl2 = new Label(wkaContainer, SWT.NONE);
		lbl2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lbl2.setText("Aktuelle Leistung");
		Label lbl3 = new Label(wkaContainer, SWT.NONE);
		lbl3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lbl3.setText("Historischer Mittelwert Leistung");
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof WKA && arg instanceof HitWKAData) {
			// there are new HitWKAData in a WKA
			WKA wka = (WKA) o;
			if (selectedWindpark.getWkas().contains(wka)) {
				// the changed wka is in the selected windfarm
				redrawWKAContainer();
			}
		}
	}

}

class WKABTNListener implements MouseListener {

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Button btn = (Button) e.getSource();
		ListView.setSelectedWKAById(btn.getText());
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}
	
}
