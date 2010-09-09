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
				int givenPort = ViewController.getInstance().createNewView(
						attributes);
				givenPortDelegate(givenPort);
			}
		});
		return port;
	}

	public void sendTuple(final int port, final String[] values) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				ViewController.getInstance().sendTuple(port, values);
			}
		});

	}

}
