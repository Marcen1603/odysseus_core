package de.offis.salsa.simulator.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.simulator.car.ui.GridScreen;

public class CarModelServerImpl extends Thread {
	private int port;
	private ServerSocketChannel serverSocketChannel = null;

	public CarModelServerImpl(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			this.serverSocketChannel = ServerSocketChannel.open();
			final InetSocketAddress address = new InetSocketAddress(this.port);
			this.serverSocketChannel.socket().bind(address);

			final List<Thread> processingThreads = new ArrayList<Thread>();
			while (!Thread.currentThread().isInterrupted()) {
				SocketChannel socket = null;
				try {
					socket = this.serverSocketChannel.accept();
					final CarModelProcessor processor = new CarModelProcessor(
							socket);
					final Thread processingThread = new Thread(processor);
					processingThreads.add(processingThread);
					processingThread.start();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			for (final Thread processingThread : processingThreads) {
				processingThread.interrupt();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class CarModelProcessor implements Runnable {
		private SocketChannel channel = null;
		private GridScreen screen;

		public CarModelProcessor(SocketChannel socket) {
			this.channel = socket;
			this.screen = new GridScreen();
		}

		@Override
		public void run() {
			try {
				final ByteBuffer buffer = ByteBuffer.allocateDirect(64 * 1024);
				int nbytes = 0;

				while (!Thread.currentThread().isInterrupted()) {
					if (channel.isOpen()) {
						while ((nbytes = channel.read(buffer)) > 0) {
							int pos = buffer.position();
							buffer.flip();
							try {
								long timestamp = buffer.getLong();
								short id = buffer.getShort();
								short x = buffer.getShort();
								short y = buffer.getShort();
								short length = buffer.getShort();
								short width = buffer.getShort();
								short height = buffer.getShort();
								int cell = buffer.getInt();
								Float[][][] grid = new Float[length][width][height];
								for (int l = 0; l < length; l++) {
									for (int w = 0; w < width; w++) {
										for (int h = 0; h < height; h++) {
											grid[l][w][h] = buffer.getFloat();
										}
									}
								}
								this.screen.onGridData(x, y, length, width,
										height, cell, grid);
							} catch (Exception e) {
								e.printStackTrace();
								buffer.position(pos);
							}
							if (buffer.hasRemaining()) {
								buffer.compact();
							} else {
								buffer.clear();
							}
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
