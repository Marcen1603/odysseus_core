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
package de.uniol.inf.is.odysseus.generator.txt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.StreamServer;

public class TxtDataProvider extends StreamClientHandler {

	public static void main(String[] args) throws Exception {
		StreamServer server = new StreamServer(54321, new TxtDataProvider());
		server.start();
	}

	private String charset = "UTF-8";
	private Scanner scanner;
	private String delimiter = ";";
	private boolean keepDelimiter = true;

	@Override
	public void init() {
		System.out.println("startng stream...");
		initFileStream();
	}

	@Override
	public void close() {
		scanner.close();
	}

	private void initFileStream() {
		URL fileURL = Activator.getContext().getBundle()
				.getEntry("/data/data.txt");
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
			this.scanner = new Scanner(inputStream, charset);
			scanner.useDelimiter(delimiter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		String elem;
		if (scanner.hasNext()) {
			elem = scanner.next();
		} else {
			System.out.println("restarting stream...");
			// restart data
			scanner.close();
			initFileStream();
			elem = scanner.next();
		}
		if (keepDelimiter ){
			elem = elem + scanner.delimiter();
		}
		tuple.addAttribute(elem);
		System.out.println(elem);

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}

	@Override
	public StreamClientHandler clone() {
		return new TxtDataProvider();
	}

}
