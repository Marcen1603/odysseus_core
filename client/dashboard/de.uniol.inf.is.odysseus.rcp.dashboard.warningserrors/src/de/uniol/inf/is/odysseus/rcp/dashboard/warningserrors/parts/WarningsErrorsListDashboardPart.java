package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data.Entry;
import de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data.Error;
import de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data.Warning;

/**
 * This Dashboard-Part can be used to show a List of current Messages with
 * different prioritys. The expired messages will be deleted automatically. Also
 * if there are messages from the same type, but with a differenr priority, the
 * old prioryty will be replaced, because a message can only exist in one
 * priority each time.
 *
 * @author MarkMilster
 *
 */
public class WarningsErrorsListDashboardPart extends AbstractDashboardPart {

	private Composite container;
	private ScrolledComposite sc;
	private long updateInterval = 100;

	/**
	 * In the updateInterval the List will be redrawn.
	 *
	 * @return the updateInterval
	 */
	public long getUpdateInterval() {
		return updateInterval;
	}

	/**
	 * In the updateInterval the List will be redrawn.
	 *
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
	 * If true, this List will show the Messages, which represent a warning.
	 *
	 * @return the showWarnings
	 */
	public boolean isShowWarnings() {
		return showWarnings;
	}

	/**
	 * If true, this List will show the Messages, which represent a warning.
	 *
	 * @param showWarnings
	 *            the showWarnings to set
	 */
	public void setShowWarnings(boolean showWarnings) {
		this.showWarnings = showWarnings;
	}

	/**
	 * If true, this List will show the Messages, which represent an error.
	 *
	 * @return the showErrors
	 */
	public boolean isShowErrors() {
		return showErrors;
	}

	/**
	 * If true, this List will show the Messages, which represent an error.
	 *
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

	/**
	 * Index of the timestamp in the tuples of the data-stream
	 *
	 * @return the timestampIndex
	 */
	public int getTimestampIndex() {
		return timestampIndex;
	}

	/**
	 * Index of the timestamp in the tuples of the data-stream
	 *
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

	/**
	 * This method let the updateThread sleep
	 *
	 * @param length
	 *            Time to sleep
	 */
	private static void waiting(long length) {
		try {
			Thread.sleep(length);
		} catch (final InterruptedException e) {
		}
	}

	/**
	 * This method refreshs the Container, which draws alle the Entrys. So the
	 * actual given List of Messages will be shown
	 */
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
				// Entry e will be null, of this message wasnt there befor in a
				// other priority, otherwise there where the same message in a
				// other priority befor, so the new message will replace the old
				// one
				Entry e = findEntry(wka_id, value_type);
				if ((boolean) tuple.getAttribute(warningIndex)) {
					// tuple is a warning
					if (e != null) {
						// if this entry was a warning or a error earlyer the
						// warning or error will be r
						e.dispose();
					}
					if ((boolean) tuple.getAttribute(errorIndex)) {
						// error
						e = new Error(container, SWT.NONE);
					} else {
						// warning
						e = new Warning(container, SWT.NONE);
					}
					e.setWka_id(wka_id);
					e.setFarm_id(farm_id);
					e.setValue_type(value_type);
				} else {
					// nothing -> no warning or error here anymore -> dispose
					// entry
					// it isnt allowed that you got error but no warning
					// so this case represents the fact, that the message is
					// expired
					if (e != null) {
						e.dispose();
					} else {
						// ignore
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
			// this will remove the expired messages
			if (tupleOld != null) {
				tupleList.remove(tupleOld);
			}
			tupleList.add(tuple);
		}
	}

	/**
	 * This method searches in the stored tuples for a message by the specified
	 * wka with the specified value_type
	 *
	 * @param wka_id
	 *            The wka_id to search for
	 * @param type
	 *            The value_type to search for
	 * @return The Message, represented by his tuple out of the datastream or
	 *         null, if it does'nt exist
	 */
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

	/**
	 * This method searches in the drawn entrys for a message by the specified
	 * wka with the specified value_type
	 *
	 * @param wka_id
	 *            The wka_id to search for
	 * @param type
	 *            The value_type to search for
	 * @return The Message, represented by his Entry-Composite or null, if it
	 *         does'nt exist
	 */
	private Entry findEntry(int wka_id, String type) {
		for (Control c : container.getChildren()) {
			if (c != null && c instanceof Entry) {
				Entry e = (Entry) c;
				if (e.getWka_id() == wka_id && e.getValue_type().equals(type)) {
					return e;
				}
			}
		}
		return null;
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
	}

	/**
	 * The max number of messages shown by this list
	 *
	 * @param maxElements
	 */
	public void setMaxElements(int maxElements) {
		this.maxElements = maxElements;
	}

	/**
	 * The max number of messages shown by this list
	 *
	 * @return the max number if messages as int
	 */
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
	 * The position on the tuples, which represent the wka-ID
	 *
	 * @return the wkaIdIndex
	 */
	public int getWkaIdIndex() {
		return wkaIdIndex;
	}

	/**
	 * The position on the tuples, which represent the wka-ID
	 *
	 * @param wkaIdIndex
	 *            the wkaIdIndex to set
	 */
	public void setWkaIdIndex(int wkaIdIndex) {
		this.wkaIdIndex = wkaIdIndex;
	}

	/**
	 * The position on the tuples, which represent the farm-ID
	 *
	 * @return the farmIdIndex
	 */
	public int getFarmIdIndex() {
		return farmIdIndex;
	}

	/**
	 * The position on the tuples, which represent the farm-ID
	 *
	 * @param farmIdIndex
	 *            the farmIdIndex to set
	 */
	public void setFarmIdIndex(int farmIdIndex) {
		this.farmIdIndex = farmIdIndex;
	}

	/**
	 * The position on the tuples, which represent the valueType
	 *
	 * @return the valueTypeIndex
	 */
	public int getValueTypeIndex() {
		return valueTypeIndex;
	}

	/**
	 * The position on the tuples, which represent the valueType
	 *
	 * @param valueTypeIndex
	 *            the valueTypeIndex to set
	 */
	public void setValueTypeIndex(int valueTypeIndex) {
		this.valueTypeIndex = valueTypeIndex;
	}

	/**
	 * The position on the tuples, which represent the warningFlag
	 *
	 * @return the warningIndex
	 */
	public int getWarningIndex() {
		return warningIndex;
	}

	/**
	 * The position on the tuples, which represent the warningFlag
	 *
	 * @param warningIndex
	 *            the warningIndex to set
	 */
	public void setWarningIndex(int warningIndex) {
		this.warningIndex = warningIndex;
	}

	/**
	 * The position on the tuples, which represent the errorFlag
	 *
	 * @return the errorIndex
	 */
	public int getErrorIndex() {
		return errorIndex;
	}

	/**
	 * The position on the tuples, which represent the errorFlag
	 *
	 * @param errorIndex
	 *            the errorIndex to set
	 */
	public void setErrorIndex(int errorIndex) {
		this.errorIndex = errorIndex;
	}

}
