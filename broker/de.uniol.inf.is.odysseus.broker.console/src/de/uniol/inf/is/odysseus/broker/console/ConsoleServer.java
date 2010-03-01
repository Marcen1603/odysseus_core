package de.uniol.inf.is.odysseus.broker.console;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.eclipse.swt.widgets.Display;

@WebService
@SOAPBinding(style = Style.RPC)
public class ConsoleServer {

	private int port;

	public void givenPortDelegate(int port) {
		this.port = port;
	}

	public int registerView(final String[] attributes) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ViewController.getInstance().getViews().isEmpty()) {
					int givenPort = ViewController.getInstance().createNewView(
							attributes);
					givenPortDelegate(givenPort);
					ViewController.getInstance().refreshAll();
				} else {
					givenPortDelegate(-1);
				}
			}
		});
		return port;
	}

	public void sendTuple(final int port, final String[] values) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (ViewController.getInstance().isQueryViewTabOpen(port)) {
					ViewController.getInstance().getContentProvider(port).addValue(values);
					ViewController.getInstance().refreshAll();
				}
			}
		});

	}

}
