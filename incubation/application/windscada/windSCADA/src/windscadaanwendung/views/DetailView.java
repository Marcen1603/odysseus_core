package windscadaanwendung.views;

import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.ca.FarmList;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.db.DBConnectionHD;
import windscadaanwendung.hd.HitWKAData;
import windscadaanwendung.views.dashboard.PhaseShiftPart;
import windscadaanwendung.views.dashboard.PitchPart;

/**
 * @author MarkMilster
 * 
 */
public class DetailView extends ViewPart implements Observer {

	public DetailView() {
	}

	public static final String ID = "windscadaanwendung.views.DetailView";
	private Label lblWKA;
	private DateTime swtDate;
	private DateTime swtTime;
	private static Text nameWKA;
	private static Composite pitchComp;
	private static PitchPart pitchPart;
	private static Composite phaseComp;
	private static PhaseShiftPart phaseShiftPart;
	private static WKA selectedWKA;
	private Composite form;
	private Composite tableContainer;
	private Table table;
	private static TableItem hitPerformance;
	private static TableItem hitWindDirection;
	private static TableItem hitWindSpeed;
	private static TableItem hitRotationalSpeed;

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
		FarmList.timer.addObserver(this);
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		form = new Composite(parent, SWT.NONE);
		form.setLayout(new GridLayout(4, false));
		Label lblHistorischeDatenSeit = new Label(form, SWT.NONE);
		lblHistorischeDatenSeit.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		lblHistorischeDatenSeit.setText("Historische Daten seit:");

		Composite dateTimeContainer = new Composite(form, SWT.NONE);
		dateTimeContainer.setLayout(new GridLayout(2, false));

		swtDate = new DateTime(dateTimeContainer, SWT.BORDER);
		swtDate.setDate(1970, 0, 1);
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
		swtTime.setTime(0, 0, 0);
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

		hitWindDirection = new TableItem(table, SWT.NONE);
		hitWindDirection.setText(new String[] { "Windrichtung" });

		hitWindSpeed = new TableItem(table, SWT.NONE);
		hitWindSpeed.setText(new String[] { "Windgeschwindigkeit" });

		hitRotationalSpeed = new TableItem(table, SWT.NONE);
		hitRotationalSpeed.setText(new String[] { "Drehzahl" });

		hitPerformance = new TableItem(table, SWT.NONE);
		hitPerformance.setText(new String[] { "Leistung" });
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
	 * Diese Methode l������dt die Werte der entsprechenden WKA in
	 * diese View.
	 * 
	 * @param selected
	 *            In der ListView selektierte WKA
	 */
	public static void setSelectedWKA(WKA wka) {
		selectedWKA = wka;
		if (selectedWKA == null) {
			nameWKA.setText("");
		} else {
			nameWKA.setText(String.valueOf(selectedWKA.getID()));
			setNewDPVs();
		}
		printHitWKAData();
	}

	private static void setNewDPVs() {
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

	private void handleDateTimeEvent() {
		// System.out.println("Test:");
		// System.out.println(swtDate.toString() + swtTime.toString());
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(swtDate.getYear(), swtDate.getMonth(), swtDate.getDay(),
				swtTime.getHours(), swtTime.getMinutes(), swtTime.getSeconds());
		// System.out.println(cal.getTime().toString());
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
		} else if (o instanceof Timer) {
			this.handleDateTimeEvent();
		}
	}

	private static void printHitWKAData() {
		if (selectedWKA != null && selectedWKA.getHitWKAData() != null) {
			HitWKAData hitData = selectedWKA.getHitWKAData();
			hitPerformance.setText(1,
					String.valueOf(hitData.getAvgPerformance()));
			hitPerformance.setText(2,
					String.valueOf(hitData.getMinPerformance()));
			hitPerformance.setText(3,
					String.valueOf(hitData.getMaxPerformance()));
			hitWindDirection.setText(1,
					String.valueOf(hitData.getAvgWindDirection()));
			hitWindSpeed.setText(1, String.valueOf(hitData.getAvgWindSpeed()));
			hitWindSpeed.setText(2, String.valueOf(hitData.getMinWindSpeed()));
			hitWindSpeed.setText(3, String.valueOf(hitData.getMaxWindSpeed()));
			hitRotationalSpeed.setText(1,
					String.valueOf(hitData.getAvgRotationalSpeed()));
			hitRotationalSpeed.setText(2,
					String.valueOf(hitData.getMinRotationalSpeed()));
			hitRotationalSpeed.setText(3,
					String.valueOf(hitData.getMaxRotationalSpeed()));
		} else {
			hitPerformance.setText(1, "");
			hitPerformance.setText(2, "");
			hitPerformance.setText(3, "");
			hitWindDirection.setText(1, "");
			hitWindSpeed.setText(1, "");
			hitWindSpeed.setText(2, "");
			hitWindSpeed.setText(3, "");
			hitRotationalSpeed.setText(1, "");
			hitRotationalSpeed.setText(2, "");
			hitRotationalSpeed.setText(3, "");
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		ObserverHandler.removeObserverFromWKA(this);
		FarmList.timer.deleteObserver(this);
	}
}
