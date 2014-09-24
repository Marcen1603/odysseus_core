/**
 * 
 */
package windscadaanwendung.views;

import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
// import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.db.DBConnectionHD;
import windscadaanwendung.hd.HitWKAData;
import windscadaanwendung.views.dashboard.PhaseShiftPart;
import windscadaanwendung.views.dashboard.PitchPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author MarkMilster
 * 
 */
public class DetailView extends ViewPart implements Observer{
	public DetailView() {
	}

	public static final String ID = "windscadaanwendung.views.DetailView";
	private static Text hitWindRi;
	private static Text hitLei;
	private static Text hitDreh;
	private static Text hitWindgeschw;
	// private static Text aktPhas;
	// private static Text aktPitch;
	private Label lblWKA;
	private DateTime swtDate;
	private DateTime swtTime;
	private static Text nameWKA;
	// private Canvas canvas;
	private static Composite pitchComp;
	private static PitchPart pitchPart;
	private static Composite phaseComp;
	private static PhaseShiftPart phaseShiftPart;
	private static WKA selectedWKA;
	private Composite form;
	private Composite tableContainer;
	private Table table;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		ObserverHandler.addObserverToWKA(this);
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		form = new Composite(parent, SWT.NONE);
		form.setLayout(new GridLayout(2, false));
		new Label(form, SWT.NONE);
		new Label(form, SWT.NONE);
		Label lblHistorischeDatenSeit = new Label(form, SWT.NONE);
		lblHistorischeDatenSeit.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		lblHistorischeDatenSeit.setText("Historische Daten seit:");
		
				Composite dateTimeContainer = new Composite(form, SWT.NONE);
				dateTimeContainer.setLayout(new GridLayout(2, false));
				
						swtDate = new DateTime(dateTimeContainer, SWT.BORDER | SWT.LONG);
						swtDate.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								handleDateTimeEvent();
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								handleDateTimeEvent();
							}

						});
						
								swtTime = new DateTime(dateTimeContainer, SWT.BORDER | SWT.TIME);
								swtTime.addSelectionListener(new SelectionListener() {

									@Override
									public void widgetSelected(SelectionEvent e) {
										handleDateTimeEvent();
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent e) {
										handleDateTimeEvent();
									}

								});
		lblWKA = new Label(form, SWT.NONE);
		lblWKA.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblWKA.setText("Windkraftanlage:");
		
				nameWKA = new Text(form, SWT.BORDER);
		Label lblHistorischerMittelwertDrehzahl = new Label(form, SWT.NONE);
		lblHistorischerMittelwertDrehzahl.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		lblHistorischerMittelwertDrehzahl
				.setText("Mittelwert Drehzahl:");
		
				hitDreh = new Text(form, SWT.BORDER);
		new Label(form, SWT.NONE);
		new Label(form, SWT.NONE);

		Label lblHistorischerMittelwertWindrichtung = new Label(form,
				SWT.NONE);
		lblHistorischerMittelwertWindrichtung.setLayoutData(new GridData(
				SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHistorischerMittelwertWindrichtung
				.setText("Mittelwert Windrichtung:");

		hitWindRi = new Text(form, SWT.BORDER);

		Label lblHistorischerMittelwertLeistung = new Label(form, SWT.NONE);
		lblHistorischerMittelwertLeistung.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		lblHistorischerMittelwertLeistung
				.setText("Mittelwert Leistung:");

		hitLei = new Text(form, SWT.BORDER);

		Label lblHistorischerMittelwertWindgeschwindigkeit = new Label(form,
				SWT.NONE);
		lblHistorischerMittelwertWindgeschwindigkeit
				.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
						false, 1, 1));
		lblHistorischerMittelwertWindgeschwindigkeit
				.setText("Historischer Mittelwert Windgeschwindigkeit:");

		hitWindgeschw = new Text(form, SWT.BORDER);

		Label lblPitchwinkel = new Label(form, SWT.NONE);
		lblPitchwinkel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblPitchwinkel.setText("Aktueller Pitchwinkel:");

		pitchComp = new Composite(form, SWT.NONE);

		Label lblPhasenverschiebung = new Label(form, SWT.NONE);
		lblPhasenverschiebung.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblPhasenverschiebung.setText("Aktuelle Phasenverschiebung:");

		phaseComp = new Composite(form, SWT.NONE);
		
		tableContainer = new Composite(parent, SWT.NONE);
		tableContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		table = new Table(tableContainer, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnMesswert = new TableColumn(table, SWT.NONE);
		tblclmnMesswert.setWidth(100);
		
		TableColumn tblclmnMittelwert = new TableColumn(table, SWT.NONE);
		tblclmnMittelwert.setWidth(100);
		tblclmnMittelwert.setText("Mittelwert");
		
		TableColumn tblclmnMinimum = new TableColumn(table, SWT.NONE);
		tblclmnMinimum.setWidth(100);
		tblclmnMinimum.setText("Minimum");
		
		TableColumn tblclmnMaximum = new TableColumn(table, SWT.NONE);
		tblclmnMaximum.setWidth(100);
		tblclmnMaximum.setText("Maximum");
		
		TableItem hitWindDirection = new TableItem(table, SWT.NONE);
		hitWindDirection.setText(new String[] {"Windrichtung"});
		
		TableItem hitWindSpeed = new TableItem(table, SWT.NONE);
		hitWindSpeed.setText(new String[] {"Windgeschwindigkeit"});
		
		TableItem hitRotationalSpeed = new TableItem(table, SWT.NONE);
		hitRotationalSpeed.setText(new String[] {"Drehzahl"});
		
		TableItem hitPerformance = new TableItem(table, SWT.NONE);
		hitPerformance.setText(new String[] {"Leistung"});
		pitchPart = new PitchPart();
		phaseShiftPart = new PhaseShiftPart();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	/**
	 * Diese Methode l������dt die Werte der entsprechenden WKA in diese View.
	 * 
	 * @param selected
	 *            In der ListView selektierte WKA
	 */
	public static void setSelectedWKA(WKA wka) {
		
		selectedWKA = wka;

		if (selectedWKA == null) {
			nameWKA.setText("");
			// TODO: alle anderen Felder leeren
		} else {
			nameWKA.setText(String.valueOf(selectedWKA.getID()));
			printHitWKAData();

			for (Control c : pitchComp.getChildren()) {
				c.dispose();
			}
			pitchComp.layout();
			pitchPart = new PitchPart();
			pitchPart.createPartControl(pitchComp, selectedWKA);
			pitchComp.layout();

			for (Control c : phaseComp.getChildren()) {
				c.dispose();
			}
			phaseComp.layout();
			phaseShiftPart = new PhaseShiftPart();
			phaseShiftPart.createPartControl(phaseComp, selectedWKA);
			phaseComp.layout();
		}

	}

	private void handleDateTimeEvent() {
		System.out.println("Test:");
		System.out.println(swtDate.toString() + swtTime.toString());
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(swtDate.getYear(),swtDate.getMonth(), swtDate.getDay(), swtTime.getHours(), swtTime.getMinutes(), swtTime.getSeconds());
		System.out.println(cal.getTime().toString());
		DBConnectionHD.refreshHitWKAData(cal.getTime());
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof WKA && arg instanceof HitWKAData) {
			// there are new HitWKAData in a WKA
			WKA wka = (WKA) o;
			if (wka.equals(selectedWKA)) {
				printHitWKAData();
			}
		}
	}

	private static void printHitWKAData() {
		if (selectedWKA.getHitWKAData() != null) {
			hitDreh.setText(String.valueOf(selectedWKA.getHitWKAData()
					.getAvgRotationalSpeed()));
			hitWindRi.setText(String.valueOf(selectedWKA.getHitWKAData()
					.getAvgWindDirection()));
			hitLei.setText(String.valueOf(selectedWKA.getHitWKAData()
					.getAvgPerformance()));
			hitWindgeschw.setText(String.valueOf(selectedWKA.getHitWKAData()
					.getAvgWindSpeed()));
		} else {
			// no historical Data Found
		}
	}
}
