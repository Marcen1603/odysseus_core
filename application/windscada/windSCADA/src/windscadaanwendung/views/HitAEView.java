package windscadaanwendung.views;

import java.util.GregorianCalendar;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.db.DBConnectionHD;
import windscadaanwendung.hd.ae.AEEntry;
import windscadaanwendung.hd.ae.AEEntryComp;
import windscadaanwendung.hd.ae.AEErrorComp;
import windscadaanwendung.hd.ae.AEObserver;
import windscadaanwendung.hd.ae.AEWarningComp;
import windscadaanwendung.hd.ae.HitAEData;

/**
 * This class shows the data of the historical Alarms and Events / Messages and
 * builds the GUI to store additional information about them into the database.
 * 
 * @author MarkMilster
 * 
 */
public class HitAEView extends ViewPart implements AEObserver {
	public HitAEView() {
	}

	private static Composite hitAEContainer;
	private static Composite parent;
	private static DateTime swtDateSince;
	private static DateTime swtTimeSince;
	private static DateTime swtDateUntil;
	private static DateTime swtTimeUntil;
	private static Button btnZeigeGelesene;
	private static ScrolledComposite sc;

	@Override
	public void createPartControl(final Composite parent) {
		this.setParent(parent);
		parent.setLayout(GridLayoutFactory.fillDefaults().create());
		parent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1,
				1));
		Composite toolsContainer = new Composite(parent, SWT.NONE);
		toolsContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolsContainer.setLayout(new GridLayout(3, false));

		Label lblDatenSeit = new Label(toolsContainer, SWT.NONE);
		lblDatenSeit.setText("Daten seit:");

		swtDateSince = new DateTime(toolsContainer, SWT.BORDER);
		swtDateSince.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		swtDateSince.setDate(2005, 0, 1);

		swtTimeSince = new DateTime(toolsContainer, SWT.BORDER | SWT.TIME);
		swtTimeSince.setTime(0, 0, 0);

		Label lblDatenBis = new Label(toolsContainer, SWT.NONE);
		lblDatenBis.setText("Daten bis:");

		swtDateUntil = new DateTime(toolsContainer, SWT.BORDER);
		swtDateUntil.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		swtDateUntil.setDate(2005, 0, 1);

		swtTimeUntil = new DateTime(toolsContainer, SWT.BORDER | SWT.TIME);
		swtTimeUntil.setDate(0, 45, 0);

		btnZeigeGelesene = new Button(toolsContainer, SWT.CHECK);
		btnZeigeGelesene.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}
		});
		btnZeigeGelesene.setText("zeige gelesene");

		Button btnAktualisieren = new Button(toolsContainer, SWT.NONE);
		btnAktualisieren.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}
		});
		btnAktualisieren.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		btnAktualisieren.setText("Aktualisieren");

		Button btnSpeichern = new Button(toolsContainer, SWT.NONE);
		btnSpeichern.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		btnSpeichern.setText("Speichern");
		btnSpeichern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveChangedAEEntrys();
			}
		});
		sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		hitAEContainer = new Composite(sc, SWT.NONE);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		hitAEContainer.setLayout(new GridLayout(1, false));

		// added to Observable to be informed, if there are new historical data
		// to show
		HitAEData.addAEObserver(this);

		sc.setContent(hitAEContainer);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(hitAEContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		hitAEContainer.layout();
	}

	@Override
	public void dispose() {
		super.dispose();
		HitAEData.removeAEListener(this);
	}

	/**
	 * This method stores the historical data, which has be modified by the user
	 * into the database. To check if they are modified there is a flag changed
	 * in the entry
	 */
	protected void saveChangedAEEntrys() {
		for (Control c : hitAEContainer.getChildren()) {
			if (c instanceof AEEntryComp) {
				AEEntryComp aeC = (AEEntryComp) c;
				if (aeC.isChanged()) {
					DBConnectionHD.updateAEEntry(aeC.getAeEntry());
					aeC.setChanged(false);
				}
			}
		}
	}

	/**
	 * This method will call the Database to refresh the historical data to the
	 * historical data since the selected DateTime and until the selected
	 * datetime. Also if the data, which is already checked, will be shown,
	 * depends on the selection of the checkbox in this gui
	 */
	private void prepareLoadNewHitData() {
		GregorianCalendar calSince = new GregorianCalendar();
		calSince.set(swtDateSince.getYear(), swtDateSince.getMonth(),
				swtDateSince.getDay(), swtTimeSince.getHours(),
				swtTimeSince.getMinutes(), swtTimeSince.getSeconds());
		GregorianCalendar calUntil = new GregorianCalendar();
		calUntil.set(swtDateUntil.getYear(), swtDateUntil.getMonth(),
				swtDateUntil.getDay(), swtTimeUntil.getHours(),
				swtTimeUntil.getMinutes(), swtTimeUntil.getSeconds());
		DBConnectionHD.refreshHitAEData(calSince.getTime(), calUntil.getTime(),
				btnZeigeGelesene.getSelection());
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void onChangedData(AEEntry aeEntry) {
		Point scrollPosition = sc.getOrigin();
		if (aeEntry == null) {
			// data is cleared
			for (Control c : hitAEContainer.getChildren()) {
				if (c instanceof AEEntryComp) {
					c.dispose();
				}
			}
		} else {
			if (aeEntry.isWarning()) {
				if (aeEntry.isError()) {
					// this is an Error
					new AEErrorComp(hitAEContainer, SWT.NONE, aeEntry);
				} else {
					// this is a Warning
					new AEWarningComp(hitAEContainer, SWT.NONE, aeEntry);
				}
			} else {
				// ignore this case because its only a flag that a AE is done or
				// there is error but no warning which is forbidden
			}
		}
		sc.setMinSize(hitAEContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		hitAEContainer.layout();
		getParent().layout();
		sc.setOrigin(scrollPosition);
	}

	/**
	 * @return the parent Composite
	 */
	public static Composite getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent Composite to set
	 */
	public void setParent(Composite parent) {
		HitAEView.parent = parent;
	}

}
