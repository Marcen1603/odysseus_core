package de.uniol.inf.is.odysseus.rcp.p2p_new.opdetail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.rcp.server.AbstractOperatorDetailProvider;

@SuppressWarnings("rawtypes")
public class JxtaReceiverOperatorDetailProvider extends AbstractOperatorDetailProvider<JxtaReceiverPO> {

	private static final String JXTA_TITLE = "JXTA";
	
	private Text peerText;
	private Text downloadedText;
	private Text downloadRateText;
	private Text transferTypeText;
	
	private Thread updateThread;
	private boolean updateRunning = true;

	@Override
	public void createPartControl(Composite parent, final JxtaReceiverPO operator) {
		Composite comp = new Composite( parent, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		comp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		addKeyValuePair( comp, "PipeID", operator.getPipeAdvertisement().getPipeID().toString());
		peerText = addKeyValuePair( comp, "Peer", operator.getConnectedPeerName());
		downloadedText = addKeyValuePair( comp, "Downloaded", toByteFormat(operator.getReceivedByteCount()));
		downloadRateText = addKeyValuePair( comp, "Download/s", toByteFormat(operator.getReceivedByteDataRate()));
		transferTypeText = addKeyValuePair( comp, "Transfertype", operator.getConnectionType().getName());
		
		updateThread = new Thread( new Runnable() {
			@Override
			public void run() {
				while( updateRunning ) {
					Display disp = PlatformUI.getWorkbench().getDisplay();
					if( !disp.isDisposed() ) {
						disp.asyncExec(new Runnable() {
							@Override
							public void run() {
								if( !peerText.isDisposed() ) {
									peerText.setText(operator.getConnectedPeerName());
								}
								if( !downloadedText.isDisposed() ) {
									downloadedText.setText(toByteFormat(operator.getReceivedByteCount()));
								}
								if( !downloadRateText.isDisposed() ) {
									downloadRateText.setText(toByteFormat(operator.getReceivedByteDataRate()));
								}
								if( !transferTypeText.isDisposed() ) {
									transferTypeText.setText(operator.getConnectionType().getName());
								}
							}
						});
					}
					trySleep();		
				}
			}
		});
		updateThread.setDaemon(true);
		updateThread.setName("JxtaReceiver detail info updater");
		updateThread.start();
	}

	@Override
	public void dispose() {
		updateRunning = false;
	}

	@Override
	public String getTitle() {
		return JXTA_TITLE;
	}

	@Override
	protected Class<? extends JxtaReceiverPO> getOperatorType() {
		return JxtaReceiverPO.class;
	}
	
	private static Text addKeyValuePair( Composite comp, String key, String value) {
		Label keyLabel = new Label(comp, SWT.NONE);
		keyLabel.setText(Strings.isNullOrEmpty(key) ? "" : key);
		keyLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		Text valueText = new Text(comp, SWT.NONE);
		valueText.setEditable(false);
		valueText.setText(Strings.isNullOrEmpty(value) ? "" : value);
		return valueText;
	}

	private static String toByteFormat(double receivedByteDataRate) {
		double rawDataRate = receivedByteDataRate;
		final String[] units = new String[] { "Bytes", "KB", "MB", "GB" };
		
		int unitIndex = -1;
		for( int i = 0; i < units.length ; i++) {
			if( rawDataRate < 1024.0 ) {
				unitIndex = i;
				break;
			} 
			rawDataRate /= 1024.0;
		}
		if( unitIndex == -1 ) {
			unitIndex = units.length - 1;
		}
		
		return String.format("%-10.1f", rawDataRate) + " " + units[unitIndex];
	}
	
	private static void trySleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

}
