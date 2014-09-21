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
	}
	
	@Override
	public Map<String, String> onSave() {
		Map<String, String> saveMap = Maps.newHashMap();
		saveMap.put("ShowHeartbeats", String.valueOf(showHeartbeats));
		saveMap.put("UpdateInterval", String.valueOf(updateInterval));
		return saveMap;
	}

	private void refreshText() {
		synchronized (pendingElement) {
			if (pendingElement != null) {
				text.setText(pendingElement);
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
}
