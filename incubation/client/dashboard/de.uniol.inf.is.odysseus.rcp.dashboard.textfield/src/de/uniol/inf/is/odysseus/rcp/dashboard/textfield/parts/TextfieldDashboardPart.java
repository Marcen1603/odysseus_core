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
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class TextfieldDashboardPart extends AbstractDashboardPart {
	
	private String pendingElement = "";

	private Text text;
	private Thread updateThread;

	private boolean showHeartbeats = false;
	private long updateInterval = 1000;

	private int atributeToShow = 0;

	private int warningIndex = 1;

	/**
	 * @return the warningIndex
	 */
	public int getWarningIndex() {
		return warningIndex;
	}

	/**
	 * @param warningIndex the warningIndex to set
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
	 * @param errorIndex the errorIndex to set
	 */
	public void setErrorIndex(int errorIndex) {
		this.errorIndex = errorIndex;
	}

	private int errorIndex = 2;

	private boolean warning;
	private boolean error;

	private boolean showWarning = true;;
	/**
	 * @return the showWarning
	 */
	public boolean isShowWarning() {
		return showWarning;
	}

	/**
	 * @param showWarning the showWarning to set
	 */
	public void setShowWarning(boolean showWarning) {
		this.showWarning = showWarning;
	}

	private boolean showError = true;

	/**
	 * @return the atributeToShow
	 */
	public int getAtributeToShow() {
		return atributeToShow;
	}

	/**
	 * @param atributeToShow the atributeToShow to set
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
			int[] atr = {atributeToShow };
			pendingElement = (elementTuple != null ? elementTuple.toString(atr) : "null");
			warning = elementTuple.getAttribute(warningIndex);
			error =  elementTuple.getAttribute(errorIndex);
		}
	}

	public boolean isShowHeartbeats() {
		return showHeartbeats;
	}

	public void setShowHeartbeats(boolean showHeartbeats) {
		this.showHeartbeats = showHeartbeats;
	}


	public long getUpdateInterval() {
		return updateInterval;
	}

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

	private static void waiting(long length) {
		try {
			Thread.sleep(length);
		} catch (final InterruptedException e) {
		}
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void securityPunctuationElementRecieved(
			IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the showError
	 */
	public boolean isShowError() {
		return showError;
	}

	/**
	 * @param showError the showError to set
	 */
	public void setShowError(boolean showError) {
		this.showError = showError;
	}
}
