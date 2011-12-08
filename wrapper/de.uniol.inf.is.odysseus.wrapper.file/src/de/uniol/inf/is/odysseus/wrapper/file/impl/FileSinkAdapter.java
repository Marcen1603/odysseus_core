package de.uniol.inf.is.odysseus.wrapper.file.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractSinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class FileSinkAdapter extends AbstractSinkAdapter implements SinkAdapter {
	private static final Logger LOG = LoggerFactory
			.getLogger(FileSinkAdapter.class);
	private final Charset charset = Charset.defaultCharset();
	private final Map<SinkSpec, FileChannel> channelMap = new ConcurrentHashMap<SinkSpec, FileChannel>();

	@Override
	public String getName() {
		return "FILE";
	}

	@Override
	public void transfer(SinkSpec sink, long timestamp, Object[] data) {
		FileChannel channel = this.channelMap.get(sink);
		if ((channel != null) && (channel.isOpen())) {
			final StringBuffer sb = new StringBuffer(Long.toString(timestamp));
			for (Object value : data) {
				sb.append(",").append(value.toString());
			}
			final ByteBuffer buffer = this.charset.encode(sb.toString());
			try {
				channel.write(buffer);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}

	}

	@Override
	protected void destroy(SinkSpec sink) {
		FileChannel channel = this.channelMap.get(sink);
		try {
			channel.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	protected void init(SinkSpec sink) {
		String path = sink.getConfiguration().get("path").toString();
		try {
			final File file = new File(path);
			final FileOutputStream out = new FileOutputStream(file);
			FileChannel channel = out.getChannel();
			this.channelMap.put(sink, channel);
		} catch (final FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}

	}

}
