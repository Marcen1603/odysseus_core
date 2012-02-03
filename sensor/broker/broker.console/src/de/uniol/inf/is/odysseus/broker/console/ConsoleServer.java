/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
			@Override
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
			@Override
			public void run() {
				ViewController.getInstance().sendTuple(port, values);
			}
		});

	}

}
