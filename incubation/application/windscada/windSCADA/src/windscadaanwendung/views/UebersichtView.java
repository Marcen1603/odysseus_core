package windscadaanwendung.views;

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
import windscadaanwendung.views.dashboard.CorrectedScoreTfPart;

/**
 * 
 * @author MarkMilster
 *
 */
public class UebersichtView extends ViewPart {
	
	public static final String ID = "windscadaanwendung.views.DetailView";
	private static Text hitLeistung;
	private static Text nameWindpark;
	private static Composite wkaContainer;

	public UebersichtView() {
	}

	@Override
	public void createPartControl(Composite parent) {
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
		nameWindpark.setText(String.valueOf(windFarm.getID()));
		DBConnectionHD.setHitFarmData(windFarm);
		if (windFarm.getHitWindFarmData() != null) {
			hitLeistung.setText(String.valueOf(windFarm.getHitWindFarmData().getAvgPervormance()));
		}
		
		// clear container
		
		for (Control c : wkaContainer.getChildren()) {
	        c.dispose(); 
	    }
	    wkaContainer.layout();
	    setDefaultWKAContainerLabels();

//		Label lbl;
		Button btn;
		Text txtHit;
		Composite comp;
		CorrectedScoreTfPart tfP;
		for (WKA wka: windFarm.getWkas()) {
			DBConnectionHD.setHitWKAData(wka);
			btn = new Button(wkaContainer, SWT.NONE);
			btn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			btn.setText(String.valueOf(wka.getID()));
			btn.addMouseListener(new WKABTNListener());
//			lbl = new Label(wkaContainer, SWT.NONE);
//			lbl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
//			lbl.setText(String.valueOf(wka.getID()));
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
