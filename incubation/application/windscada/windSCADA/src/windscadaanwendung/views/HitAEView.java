package windscadaanwendung.views;

import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class HitAEView extends ViewPart implements AEObserver {

	private static Composite hitAEContainer;
	private static Composite hitAEHeader;
	private static Composite parent;
//	private static ScrolledComposite hitAEScroller;
	private static DateTime swtDateSince;
	private static DateTime swtTimeSince;
	private static DateTime swtDateUntil;
	private static DateTime swtTimeUntil;
	private static Button btnZeigeGelesene;

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		HitAEView.parent = parent;
		
		Composite toolsContainer = new Composite(parent, SWT.NONE);
		toolsContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolsContainer.setLayout(new GridLayout(3, false));
		
		Label lblDatenSeit = new Label(toolsContainer, SWT.NONE);
		lblDatenSeit.setText("Daten seit:");
		
		swtDateSince = new DateTime(toolsContainer, SWT.BORDER | SWT.LONG);
		swtDateSince.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		swtDateSince.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}

		});
		
		swtTimeSince = new DateTime(toolsContainer, SWT.BORDER | SWT.TIME);
		swtTimeSince.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}
		});
		
		Label lblDatenBis = new Label(toolsContainer, SWT.NONE);
		lblDatenBis.setText("Daten bis:");
		
		swtDateUntil = new DateTime(toolsContainer, SWT.BORDER);
		swtDateUntil.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}

		});
		
		swtTimeUntil = new DateTime(toolsContainer, SWT.BORDER | SWT.TIME);
		swtTimeUntil.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				prepareLoadNewHitData();
			}

		});
		
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
		btnAktualisieren.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnAktualisieren.setText("Aktualisieren");
		
		Button btnSpeichern = new Button(toolsContainer, SWT.NONE);
		btnSpeichern.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnSpeichern.setText("Speichern");
		btnSpeichern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveChangedAEEntrys();
			}
		});
		
		//TODO hier und wo sonst noetig scrollable machen
//		hitAEScroller = new ScrolledComposite(parent, SWT.V_SCROLL);
		hitAEContainer = new Composite(parent, SWT.NONE);
		hitAEContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		hitAEContainer.setLayout(new GridLayout(1, false));
		hitAEHeader = new Composite(hitAEContainer, SWT.NONE);
		hitAEHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		hitAEHeader.setLayout(new GridLayout(5, false));
		Label label = new Label(hitAEHeader, SWT.NONE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		label.setText("Farm");
		Label label_1 = new Label(hitAEHeader, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		label_1.setText("WKA");
		Label label_2 = new Label(hitAEHeader, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		label_2.setText("Value");
		
		Label lblCheck = new Label(hitAEHeader, SWT.NONE);
		lblCheck.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblCheck.setText("Check");
		
		Label lblComment = new Label(hitAEHeader, SWT.NONE);
		lblComment.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblComment.setText("Comment");
//		hitAEScroller.setContent(hitAEContainer);
//		hitAEScroller.setMinSize(hitAEContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		hitAEContainer.layout();
//		hitAEScroller.layout();
		HitAEView.parent.layout();
		HitAEData.addAEObserver(this);
		

	}
	
	@Override
	public void dispose() {
		super.dispose();
		HitAEData.removeAEListener(this);
	}

	protected void handleParentDispose() {
		HitAEData.removeAEListener(this);
	}

	protected void saveChangedAEEntrys() {
		for (Control c: hitAEContainer.getChildren()) {
			if (c instanceof AEEntryComp) {
				AEEntryComp aeC = (AEEntryComp) c;
				if (aeC.isChanged()) {
					DBConnectionHD.updateAEEntry(aeC.getAeEntry());
					aeC.setChanged(false);
				}
			}
		}
	}

	private void prepareLoadNewHitData() {
		GregorianCalendar calSince = new GregorianCalendar();
		calSince.set(swtDateSince.getYear(),swtDateSince.getMonth(), swtDateSince.getDay(), swtTimeSince.getHours(), swtTimeSince.getMinutes(), swtTimeSince.getSeconds());
		GregorianCalendar calUntil = new GregorianCalendar();
		calUntil.set(swtDateUntil.getYear(),swtDateUntil.getMonth(), swtDateUntil.getDay(), swtTimeUntil.getHours(), swtTimeUntil.getMinutes(), swtTimeUntil.getSeconds());
		DBConnectionHD.refreshHitAEData(calSince.getTime(), calUntil.getTime(), btnZeigeGelesene.getSelection());
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void onChangedData(AEEntry aeEntry) {
		if (aeEntry == null) {
			// data is cleared
			for (Control c: hitAEContainer.getChildren()) {
				if (c instanceof AEEntryComp) {
					c.dispose();
				}
			}
			hitAEContainer.layout();
//			hitAEScroller.layout();
			HitAEView.parent.layout();
		} else {
			System.out.println("draw AEE: " + aeEntry.toString());
			if (aeEntry.isWarning()) {
				if (aeEntry.isError()) {
					new AEErrorComp(hitAEContainer, SWT.NONE, aeEntry);
				} else {
					new AEWarningComp(hitAEContainer, SWT.NONE, aeEntry);
				}
			} else {
				// ignore this case because its only a flag that a AE is done or there is error but no warning which is forbidden
			}
			hitAEContainer.layout();
//			hitAEScroller.layout();
			HitAEView.parent.layout();
		}
		
	}
	
}
