/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.recsys.streamsimulator.run;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import de.uniol.inf.is.recsys.streamsimulator.data.ConfigData;
import de.uniol.inf.is.recsys.streamsimulator.data.FeatureData;
import de.uniol.inf.is.recsys.streamsimulator.data.RandomHelper;

/**
 * @author Cornelius Ludmann
 *
 */
public class RatingsStreamer implements Streamer {

	private volatile boolean stop = false;

	private Socket socket = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("Start ...");

		while (!stop) {

			RandomHelper rh = RandomHelper.getInstance();
			int user = rh.nextInt(0, ConfigData.getInstance().getNoOfUsers());
			int item = rh.nextInt(0, ConfigData.getInstance().getNoOfItems());

			int rating = calcRating(user, item);

			try {
				sendRating(user, item, rating);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(ConfigData.getInstance().getRatingsStreamDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
				stop = false;
			}

		}
		try {
			if (socket != null)
				socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop = false;
	}

	private int calcRating(int user, int item) {
		double rating = 0;
		int noOfFeatures = ConfigData.getInstance().getNoOfFeatures();
		for (int i = 0; i < noOfFeatures; ++i) {
			int userFeature = FeatureData.getInstance().getUserFeature(user, i);
			int itemFeature = FeatureData.getInstance().getItemFeature(item, i);
			rating += ((double) userFeature / 100)
					* ((double) itemFeature / 100);
		}

		double noise = RandomHelper.getInstance().nextGaussian(0, 0.7);

		int intRating = (int) Math.round(rating + noise);

		if (intRating > 5) {
			System.out.println("+++ INFO +++  rating > 5: " + intRating);
			intRating = 5;

		} else if (intRating < 1) {
			System.out.println("+++ INFO +++  rating < 1: " + intRating);
			intRating = 1;
		}

		System.out.println("rating of user " + user + " for item " + item
				+ ": " + intRating + " (rating=" + rating + ", noise=" + noise
				+ ")");
		return intRating;
	}

	private void sendRating(int user, int item, int rating)
			throws UnknownHostException, IOException {
		if (socket == null) {
			String ip = ConfigData.getInstance().getRatingsStreamHost();
			int port = ConfigData.getInstance().getRatingsStreamPort();
			System.out.println("Connect to " + ip + " on port " + port + ".");
			socket = new Socket(ip, port);
		}
		String msg;
		if (ConfigData.getInstance().getAddTimestamp()) {
			msg = user + "," + item + "," + rating + ","
					+ System.currentTimeMillis() + System.lineSeparator();
		} else {
			msg = user + "," + item + "," + rating
					+ System.lineSeparator();
		}
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		printWriter.print(msg);
		printWriter.flush();
		// socket.close();
	}

	@Override
	public void stop() {
		stop = true;
	}

}
