package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data.Entry;
import de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data.Error;
import de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data.Warning;

/**
 * 
 * @author MarkMilster
 * 
 */
public class WarningsErrorsListDashboardPart extends AbstractDashboardPart {

	private Composite container;
	private ScrolledComposite sc;
	private long updateInterval = 100;
	private boolean showWarning = true;
	private boolean showError = true;

	/**
	 * @return the showWarning
	 */
	public boolean isShowWarning() {
		return showWarning;
	}

	/**
	 * @param showWarning
	 *            the showWarning to set
	 */
	public void setShowWarning(boolean showWarning) {
		this.showWarning = showWarning;
	}

	/**
	 * @return the showError
	 */
	public boolean isShowError() {
		return showError;
	}

	/**
	 * @param showError
	 *            the showError to set
	 */
	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	/**
	 * @return the updateInterval
	 */
	public long getUpdateInterval() {
		return updateInterval;
	}

	/**
	 * @param updateInterval
	 *            the updateInterval to set
	 */
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	private int maxElements = 200;

	private List<Tuple<?>> tupleList = new ArrayList<Tuple<?>>();
	private Composite parent;
	private int warningIndex = 4;
	private int errorIndex = 5;
	private boolean showWarnings;

	/**
	 * @return the showWarnings
	 */
	public boolean isShowWarnings() {
		return showWarnings;
	}

	/**
	 * @param showWarnings
	 *            the showWarnings to set
	 */
	public void setShowWarnings(boolean showWarnings) {
		this.showWarnings = showWarnings;
	}

	/**
	 * @return the showErrors
	 */
	public boolean isShowErrors() {
		return showErrors;
	}

	/**
	 * @param showErrors
	 *            the showErrors to set
	 */
	public void setShowErrors(boolean showErrors) {
		this.showErrors = showErrors;
	}

	private boolean showErrors;
	private int timestampIndex = 0;
	private Thread updateThread;
	private int wkaIdIndex = 0;
	private int farmIdIndex = 1;
	private int valueTypeIndex = 2;
	private Composite header;

	/**
	 * @return the timestampIndex
	 */
	public int getTimestampIndex() {
		return timestampIndex;
	}

	/**
	 * @param timestampIndex
	 *            the timestampIndex to set
	 */
	public void setTimestampIndex(int timestampIndex) {
		this.timestampIndex = timestampIndex;
	}

	@Override
	public void createPartControl(final Composite parent, ToolBar toolbar) {
		this.parent = parent;
		this.parent.setLayout(GridLayoutFactory.fillDefaults().create());
		sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc.setLayoutData(GridDataFactory.fillDefaults().grab(true, true)
				.hint(SWT.DEFAULT, 200).create());
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		this.container = new Composite(sc, SWT.NONE);
		this.container.setLayout(GridLayoutFactory.swtDefaults().numColumns(1)
				.create());
		this.header = new Composite(container, SWT.NONE);
		this.header.setLayout(new GridLayout(3, false));
		(new Label(header, SWT.NONE)).setText("Farm");
		(new Label(header, SWT.NONE)).setText("WKA");
		(new Label(header, SWT.NONE)).setText("Value");
		sc.setContent(container);
		sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		updateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!parent.isDisposed()) {
					final Display disp = PlatformUI.getWorkbench().getDisplay();
					if (!disp.isDisposed()) {
						disp.syncExec(new Runnable() {
							@Override
							public void run() {
								if (!container.isDisposed()) {
									refreshContainer();
								}
							}

						});
					}

					waiting(updateInterval);
				}
			}

		});

		updateThread.setName("StreamList Updater");
		updateThread.start();

	}

	private static void waiting(long length) {
		try {
			Thread.sleep(length);
		} catch (final InterruptedException e) {
		}
	}

	private void refreshContainer() {
		synchronized (tupleList) {
			// Saving position of the scrollbar
			Point scrollPosition = sc.getOrigin();
			for (int i = 0; i < tupleList.size(); i++) {
				Tuple<?> tuple = tupleList.get(i);
				int wka_id = ((Long) tuple.getAttribute(wkaIdIndex)).intValue();
				int farm_id = ((Long) tuple.getAttribute(farmIdIndex))
						.intValue();
				String value_type = tuple.getAttribute(valueTypeIndex);
				// System.out.println("tuple ->  WKA: " + wka_id +
				// "  Value_type: " + value_type);
				Entry e = findEntry(wka_id, value_type);
				// if e == null -> new entry -> else a old one changes
				if ((boolean) tuple.getAttribute(warningIndex)) {
					// System.out.println("Warning flag ist TRUE");
					if (e != null) {
						// if this entry was a warning or a error earlyer the
						// warning or error will be overwritten
						// TODO: evtl. so aendern dass nicht das alte weg geht
						// und das neue kommt sondern dass das alte sich aendert
						e.dispose();
						// System.out.println("altes entfernt");
					}
					if ((boolean) tuple.getAttribute(errorIndex)) {
						// error
						e = new Error(container, SWT.NONE);
						// System.out.println("New Error!");
					} else {
						// warning
						e = new Warning(container, SWT.NONE);
						// System.out.println("New warning!");
					}
					e.setWka_id(wka_id);
					e.setFarm_id(farm_id);
					e.setValue_type(value_type);
				} else {
					// nothing -> no warning or error here anymore -> dispose
					// entry
					// it isnt allowed that you got error but no warning
					// System.out.println("Warning flag ist FALSE");
					if (e != null) {
						e.dispose();
						// System.out.println("altes entfernt");
					} else {
						// System.out.println("Kein altes vorhanden obwohl 0,0 gesendet -> FEHLER");
					}
				}
			}
			sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			container.layout();
			sc.setOrigin(scrollPosition);
		}
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
		Tuple<?> tuple = (Tuple<?>) element;
		int t_wka_id = ((Long) tuple.getAttribute(wkaIdIndex)).intValue();
		String t_value_type = tuple.getAttribute(valueTypeIndex);
		synchronized (tupleList) {
			Tuple<?> tupleOld = findTuple(t_wka_id, t_value_type);
			if (tupleOld == null) {
				tupleList.remove(tupleOld);
			}
			tupleList.add(tuple);
		}
	}

	private Tuple<?> findTuple(int wka_id, String type) {
		for (Tuple<?> t : tupleList) {
			int t_wka_id = ((Long) t.getAttribute(wkaIdIndex)).intValue();
			String t_value_type = t.getAttribute(valueTypeIndex);
			if (t_wka_id == wka_id && t_value_type.equals(type)) {
				return t;
			}
		}
		return null;

	}

	private Entry findEntry(int wka_id, String type) {
		// System.out.println("Beginne suche...");
		for (Control c : container.getChildren()) {
			// System.out.println(c.toString());
			if (c != null && c instanceof Entry) {
				Entry e = (Entry) c;
				if (e.getWka_id() == wka_id && e.getValue_type().equals(type)) {
					return e;
				}
			}
		}
		// System.out.println(wka_id + " " + type + " nicht gefunden!");
		return null;
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
	}

	@Override
	public void securityPunctuationElementRecieved(
			IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
		punctuationElementRecieved(senderOperator, sp, port);
	}

	public void setMaxElements(int maxElements) {
		this.maxElements = maxElements;
	}

	public int getMaxElements() {
		return this.maxElements;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		updateInterval = Long.valueOf(saved.get("UpdateInterval"));
		// this.maxElements = Integer.valueOf(saved.get("MaxElements"));
		this.showWarnings = Boolean.valueOf(saved.get("ShowWarnings"));
		this.showErrors = Boolean.valueOf(saved.get("ShowErrors"));
		this.warningIndex = Integer.valueOf(saved.get("WarningIndex"));
		this.errorIndex = Integer.valueOf(saved.get("ErrorIndex"));
		this.timestampIndex = Integer.valueOf(saved.get("TimestampIndex"));
		this.farmIdIndex = Integer.valueOf(saved.get("FarmIdIndex"));
		this.valueTypeIndex = Integer.valueOf(saved.get("ValueTypeIndex"));
		this.wkaIdIndex = Integer.valueOf(saved.get("WkaIdIndex"));
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> saveMap = Maps.newHashMap();
		saveMap.put("UpdateInterval", String.valueOf(updateInterval));
		saveMap.put("ShowWarnings", String.valueOf(showWarnings));
		saveMap.put("ShowErrors", String.valueOf(showErrors));
		saveMap.put("WarningIndex", String.valueOf(warningIndex));
		saveMap.put("ErrorIndex", String.valueOf(errorIndex));
		saveMap.put("TimestampIndex", String.valueOf(timestampIndex));
		saveMap.put("FarmIdIndex", String.valueOf(farmIdIndex));
		saveMap.put("ValueTypeIndex", String.valueOf(valueTypeIndex));
		saveMap.put("WkaIdIndex", String.valueOf(wkaIdIndex));
		return saveMap;
	}

	/**
	 * @return the wkaIdIndex
	 */
	public int getWkaIdIndex() {
		return wkaIdIndex;
	}

	/**
	 * @param wkaIdIndex
	 *            the wkaIdIndex to set
	 */
	public void setWkaIdIndex(int wkaIdIndex) {
		this.wkaIdIndex = wkaIdIndex;
	}

	/**
	 * @return the farmIdIndex
	 */
	public int getFarmIdIndex() {
		return farmIdIndex;
	}

	/**
	 * @param farmIdIndex
	 *            the farmIdIndex to set
	 */
	public void setFarmIdIndex(int farmIdIndex) {
		this.farmIdIndex = farmIdIndex;
	}

	/**
	 * @return the valueTypeIndex
	 */
	public int getValueTypeIndex() {
		return valueTypeIndex;
	}

	/**
	 * @param valueTypeIndex
	 *            the valueTypeIndex to set
	 */
	public void setValueTypeIndex(int valueTypeIndex) {
		this.valueTypeIndex = valueTypeIndex;
	}

	/**
	 * @return the warningIndex
	 */
	public int getWarningIndex() {
		return warningIndex;
	}

	/**
	 * @param warningIndex
	 *            the warningIndex to set
	 */
	public void setWarningIndex(int warningIndex) {
		this.warningIndex = warningIndex;
	}

	/**
	 * @return the errorIndex
	 */
	public int getErrorIndex() {
		return errorIndex;
	}

	/**
	 * @param errorIndex
	 *            the errorIndex to set
	 */
	public void setErrorIndex(int errorIndex) {
		this.errorIndex = errorIndex;
	}

}
