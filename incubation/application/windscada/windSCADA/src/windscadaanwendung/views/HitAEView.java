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
	private static DateTime swtDate;
	private static DateTime swtTime;

	public HitAEView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		HitAEView.parent = parent;
		
		Composite toolsContainer = new Composite(parent, SWT.NONE);
		toolsContainer.setLayout(new GridLayout(3, false));
		
		Label lblDatenSeit = new Label(toolsContainer, SWT.NONE);
		lblDatenSeit.setText("Daten seit:");
		
		Composite dateTimeContainer = new Composite(toolsContainer, SWT.NONE);
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
		//TODO default wert setzen
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
		
		Button btnAktualisieren = new Button(toolsContainer, SWT.NONE);
		btnAktualisieren.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO datum aus GUI holen
//				DBConnectionHD.refreshHitAEData(new Date(1411682370357L));
				handleDateTimeEvent();
			}
		});
		btnAktualisieren.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnAktualisieren.setText("Aktualisieren");
		
		Button btnZeigeGelesene = new Button(toolsContainer, SWT.CHECK);
		btnZeigeGelesene.setText("zeige gelesene");
		new Label(toolsContainer, SWT.NONE);
		
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
		hitAEContainer.setLayout(new GridLayout(1, false));
		hitAEHeader = new Composite(hitAEContainer, SWT.NONE);
		hitAEHeader.setLayout(new GridLayout(3, false));
		(new Label(hitAEHeader, SWT.NONE)).setText("Farm");
		(new Label(hitAEHeader, SWT.NONE)).setText("WKA");
		(new Label(hitAEHeader, SWT.NONE)).setText("Value");
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

	private void handleDateTimeEvent() {
		GregorianCalendar cal = new GregorianCalendar();
		System.out.println(System.currentTimeMillis());
		//FIXME
		cal.set(swtDate.getYear(),swtDate.getMonth(), swtDate.getDay(), swtTime.getHours(), swtTime.getMinutes(), swtTime.getSeconds());
		System.out.println("TIMESTAMP ausgewaehlt: " + cal.getTime().toString());
		DBConnectionHD.refreshHitAEData(cal.getTime());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

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
