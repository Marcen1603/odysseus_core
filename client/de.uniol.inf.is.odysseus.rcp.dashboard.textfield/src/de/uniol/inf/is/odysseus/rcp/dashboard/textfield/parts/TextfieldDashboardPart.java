package de.uniol.inf.is.odysseus.rcp.dashboard.textfield.parts;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

/**
 * This is a DashboardPart which shows a specific value from the latest Tuple in
 * a Datastream it can be used in any purpose
 *
 * @author MarkMilster
 *
 */
public class TextfieldDashboardPart extends AbstractDashboardPart {

	private String pendingElement = "";

	private Text text;
	private Thread updateThread;

	private boolean showHeartbeats = false;
	private long updateInterval = 1000;

	private int atributeToShow = 0;

	private int warningIndex = 1;

	/**
	 * @return the warningIndex: The warningIndex is the index of the Attribute
	 *         which represents the boolean flag warning
	 */
	public int getWarningIndex() {
		return warningIndex;
	}

	/**
	 * The warningIndex is the index of the Attribute which represents the
	 * boolean flag warning
	 *
	 * @param warningIndex
	 *            the warningIndex to set
	 */
	public void setWarningIndex(int warningIndex) {
		this.warningIndex = warningIndex;
	}

	/**
	 * The errorIndex is the index of the Attribute which represents the boolean
	 * flag error
	 *
	 * @return the errorIndex
	 */
	public int getErrorIndex() {
		return errorIndex;
	}

	/**
	 * The errorIndex is the index of the Attribute which represents the boolean
	 * flag error
	 *
	 * @param errorIndex
	 *            the errorIndex to set
	 */
	public void setErrorIndex(int errorIndex) {
		this.errorIndex = errorIndex;
	}

	private int errorIndex = 2;

	private boolean warning;
	private boolean error;

	private boolean showWarning = true;;

	/**
	 * If true and if there is the warningFlag set in the current tuple, the
	 * warning will be shown
	 *
	 * @return the showWarning
	 */
	public boolean isShowWarning() {
		return showWarning;
	}

	/**
	 * If true and if there is the warningFlag set in the current tuple, the
	 * warning will be shown
	 *
	 * @param showWarning
	 *            the showWarning to set
	 */
	public void setShowWarning(boolean showWarning) {
		this.showWarning = showWarning;
	}

	private boolean showError = true;

	/**
	 * The index of the attribute which represents the value to show
	 *
	 * @return the atributeToShow
	 */
	public int getAtributeToShow() {
		return atributeToShow;
	}

	/**
	 * The index of the attribute which represents the value to show
	 *
	 * @param atributeToShow
	 *            the atributeToShow to set
	 */
	public void setAtributeToShow(int atributeToShow) {
		this.atributeToShow = atributeToShow;
	}

	@Override
	public void createPartControl(final Composite parent, ToolBar toolbar) {
		text = new Text(parent, SWT.BORDER);
		text.setEditable(false);

		updateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!parent.isDisposed()) {
					final Display disp = PlatformUI.getWorkbench().getDisplay();
					if (!disp.isDisposed()) {
						disp.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (!text.isDisposed()) {
									refreshText();
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

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		synchronized (pendingElement) {
			Tuple<?> elementTuple = (Tuple<?>) element;
			int[] atr = { atributeToShow };
			pendingElement = (elementTuple != null ? elementTuple.toString(atr) : "null");
			if( elementTuple != null ) {
				warning = elementTuple.getAttribute(warningIndex);
				error = elementTuple.getAttribute(errorIndex);
			}
		}
	}

	/**
	 *
	 * @return true if this Dashboard-Part is showing Heartbeats
	 */
	public boolean isShowHeartbeats() {
		return showHeartbeats;
	}

	/**
	 *
	 * @param showHeartbeats
	 */
	public void setShowHeartbeats(boolean showHeartbeats) {
		this.showHeartbeats = showHeartbeats;
	}

	/**
	 * The updateInterval in ms represents the value how often the textfield is
	 * repaintet
	 *
	 * @return the updateInterval
	 */
	public long getUpdateInterval() {
		return updateInterval;
	}

	/**
	 * The updateInterval in ms represents the value how often the textfield is
	 * repaintet
	 *
	 * @param updateInterval
	 */
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		showHeartbeats = Boolean.valueOf(saved.get("ShowHeartbeats"));
		updateInterval = Long.valueOf(saved.get("UpdateInterval"));
		showWarning = Boolean.valueOf(saved.get("ShowWarning"));
		showError = Boolean.valueOf(saved.get("ShowError"));
		atributeToShow = Integer.valueOf(saved.get("AtributeToShow"));
		errorIndex = Integer.valueOf(saved.get("ErrorIndex"));
		warningIndex = Integer.valueOf(saved.get("WarningIndex"));
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> saveMap = Maps.newHashMap();
		saveMap.put("ShowHeartbeats", String.valueOf(showHeartbeats));
		saveMap.put("UpdateInterval", String.valueOf(updateInterval));
		saveMap.put("ShowWarning", String.valueOf(showWarning));
		saveMap.put("ShowError", String.valueOf(showError));
		saveMap.put("AtributeToShow", String.valueOf(atributeToShow));
		saveMap.put("ErrorIndex", String.valueOf(errorIndex));
		saveMap.put("WarningIndex", String.valueOf(warningIndex));
		return saveMap;
	}

	/**
	 * This Method is called in the inteval of the updateInterval variable and
	 * repaints the textfield
	 */
	private void refreshText() {
		synchronized (pendingElement) {
			text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			if (pendingElement != null) {
				text.setText(pendingElement);
			} else {
				text.setText("");
			}
			if (showWarning && warning) {
				text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
			}
			if (showError && error) {
				text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
			}
		}
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

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port) {
	}

	/**
	 * If true and if there is the errorFlag set in the current tuple, the error
	 * will be shown
	 *
	 * @return the showError
	 */
	public boolean isShowError() {
		return showError;
	}

	/**
	 * If true and if there is the errorFlag set to in the current tuple, the
	 * error will be shown
	 *
	 * @param showError
	 *            the showError to set
	 */
	public void setShowError(boolean showError) {
		this.showError = showError;
	}
}
