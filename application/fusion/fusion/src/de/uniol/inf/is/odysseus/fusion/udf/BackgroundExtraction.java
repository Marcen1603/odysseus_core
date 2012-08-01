package de.uniol.inf.is.odysseus.fusion.udf;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;

@UserDefinedFunction(name = "ExtractBackground")
public class BackgroundExtraction
		implements
		IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	private static final Logger LOG = LoggerFactory
			.getLogger(BackgroundExtraction.class);

	private long recordEnd;
	private Background background = null;

	@Override
	public void init(String initString) {
		recordEnd = System.currentTimeMillis() + Integer.parseInt(initString);
		LOG.info("Background Record Time: " + initString + "ms");
	}

	@Override
	public Tuple<? extends IMetaAttribute> process(
			Tuple<? extends IMetaAttribute> in, int port) {
		PolarCoordinate[] polarCoordinates = in.getAttribute(1);

		if (System.currentTimeMillis() > recordEnd) {
			if (this.background == null) {
				this.background = new Background(polarCoordinates);
			}

			this.background = Background.merge(this.background,
					polarCoordinates);
		}

		PolarCoordinate[] extracted = new PolarCoordinate[polarCoordinates.length];
		if (background != null) {
			int counter = 0;
			for (int i = 0; i < polarCoordinates.length; i++) {
				if (polarCoordinates[i].r < background.getDistance(i)) {
					extracted[counter] = polarCoordinates[i];
					counter++;
				}
			}
			polarCoordinates = Arrays.copyOf(extracted, counter);
			in.setAttribute(1, polarCoordinates);
		}
		return in;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
