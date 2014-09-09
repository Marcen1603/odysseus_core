/**
 * 
 */
package windscadaanwendung.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
// import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.db.DBConnectionHD;


/**
 * @author MarkMilster
 *
 */
public class DetailView extends ViewPart {
	
	public static final String ID = "windscadaanwendung.views.DetailView";
	//TODO: so umbauen, dass für jede WKA eine einzelne View erzugt wird und mit klick auf die entsprechende im tree nur die entsprechende view geöffnet wird ? -> Alle Daten alle WKA immer aufzeichnen und analysieren ?
	private static Text hitWindRi;
	private static Text hitLei;
	private static Text hitDreh;
	private static Text hitWindgeschw;
	// private static Text aktPhas;
	// private static Text aktPitch;
	private Label lblWKA;
	private static Text nameWKA;
	// private Canvas canvas;

	/**
	 * 
	 */
	public DetailView() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		lblWKA = new Label(parent, SWT.NONE);
		lblWKA.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWKA.setText("Windkraftanlage:");
		
		nameWKA = new Text(parent, SWT.BORDER);
		
		Label lblHistorischerMittelwertDrehzahl = new Label(parent, SWT.NONE);
		lblHistorischerMittelwertDrehzahl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHistorischerMittelwertDrehzahl.setText("Historischer Mittelwert Drehzahl:");
		
		hitDreh = new Text(parent, SWT.BORDER);
		
		Label lblHistorischerMittelwertWindrichtung = new Label(parent, SWT.NONE);
		lblHistorischerMittelwertWindrichtung.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHistorischerMittelwertWindrichtung.setText("Historischer Mittelwert Windrichtung:");
		
		hitWindRi = new Text(parent, SWT.BORDER);
		
		Label lblHistorischerMittelwertLeistung = new Label(parent, SWT.NONE);
		lblHistorischerMittelwertLeistung.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHistorischerMittelwertLeistung.setText("Historischer Mittelwert Leistung:");
		
		hitLei = new Text(parent, SWT.BORDER);
		
		Label lblHistorischerMittelwertWindgeschwindigkeit = new Label(parent, SWT.NONE);
		lblHistorischerMittelwertWindgeschwindigkeit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHistorischerMittelwertWindgeschwindigkeit.setText("Historischer Mittelwert Windgeschwindigkeit:");
		
		hitWindgeschw = new Text(parent, SWT.BORDER);
		
		Label lblPhasenverschiebung = new Label(parent, SWT.NONE);
		lblPhasenverschiebung.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPhasenverschiebung.setText("Phasenverschiebung:");
		
		// aktPhas = new Text(parent, SWT.BORDER);
		
		Label lblPitchwinkel = new Label(parent, SWT.NONE);
		lblPitchwinkel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPitchwinkel.setText("Pitchwinkel:");
		
		// aktPitch = new Text(parent, SWT.BORDER);

		//TODO: an sinvollere Stelle packen
		DBConnectionHD.setNewConnection();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}
	
	/**
	 * Diese Methode lädt die Werte der entsprechenden WKA in diese View.
	 * 
	 * @param selected
	 * 				In der ListView selektierte WKA
	 */
	public static void setSelectedWKA(WKA wka) {
		
		if (wka == null) {
			nameWKA.setText("");
		} else {
			nameWKA.setText(String.valueOf(wka.getID()));
			
				DBConnectionHD.setHitWKAData(wka);
				if (wka.getHitWKAData() != null) {
					hitDreh.setText(String.valueOf(wka.getHitWKAData().getAvgRotationalSpeed()));
					hitWindRi.setText(String.valueOf(wka.getHitWKAData().getAvgWindDirection()));
					hitLei.setText(String.valueOf(wka.getHitWKAData().getAvgPerformance()));
					hitWindgeschw.setText(String.valueOf(wka.getHitWKAData().getAvgWindSpeed()));
				} else {
					// no historical Data Found
				}
				//TODO: implementieren, dass nach x sekunden aktualisiert wird
		
			
		}
		
		
		//TODO: Daten aus HD einlesen! -> WKA objekt an DB-Klasse geben -> in dieses entspr. Daten eintragen
	}
}
