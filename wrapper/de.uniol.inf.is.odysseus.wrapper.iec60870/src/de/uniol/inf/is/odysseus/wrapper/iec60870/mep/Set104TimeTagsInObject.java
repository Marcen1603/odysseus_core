package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.ei.oj104.model.SequenceInformationObject;
import de.uniol.inf.ei.oj104.model.SingleInformationObject;
import de.uniol.inf.ei.oj104.model.timetag.SevenOctetBinaryTime;
import de.uniol.inf.ei.oj104.model.timetag.ThreeOctetBinaryTime;
import de.uniol.inf.ei.oj104.model.timetag.TwoOctetBinaryTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Set104TimeTagsInObject extends AbstractFunction<IInformationObject> {

	private static final long serialVersionUID = 6783232220530492336L;

	private static final Logger logger = LoggerFactory.getLogger(Set104TimeTagsInObject.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Set104TimeTagsInObject.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.LONG } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public Set104TimeTagsInObject() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public IInformationObject getValue() {
		if (!(getInputValue(0) instanceof IInformationObject)) {
			logger.error("'{}' is not an information object!", (Object) getInputValue(0));
			return null;
		}
		IInformationObject io = (IInformationObject) getInputValue(0);
		long timestamp = getInputValue(1);

		if (io instanceof SingleInformationObject) {
			return setTimeTags((SingleInformationObject) io, timestamp);
		} else if (io instanceof SequenceInformationObject) {
			return setTimeTags((SequenceInformationObject) io, timestamp);
		} else {
			logger.error("'{}' is not an information object class!", io.getClass());
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

	private static TwoOctetBinaryTime createTwoOctetBinaryTime(long timestamp) {
		return new TwoOctetBinaryTime((int) (timestamp % 1000), (int) (timestamp / 1000));
	}

	private static ThreeOctetBinaryTime createThreeOctetBinaryTime(long timestamp, boolean substituted,
			boolean invalid) {
		return new ThreeOctetBinaryTime((int) (timestamp % 1000), (int) ((timestamp / 1000) % 60),
				(int) (timestamp / 60000), substituted, invalid);
	}

	private static SevenOctetBinaryTime createSevenOctetBinaryTime(long timestamp, boolean substituted,
			boolean invalid) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		// Month starts with 1 in IEC 60870-5-104
		// Year has only two digits in IEC 60870-5-104
		return new SevenOctetBinaryTime(calendar.get(Calendar.MILLISECOND), calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MINUTE), substituted, invalid, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.YEAR) % 100);
	}

	private static InformationElementSequence setTimeTags(InformationElementSequence seq, long timestamp) {
		if (seq.getTimeTag().isPresent()) {
			ITimeTag oldTimeTag = seq.getTimeTag().get();
			ITimeTag newTimeTag = null;
			if (oldTimeTag instanceof ThreeOctetBinaryTime) {
				boolean substituted = ((ThreeOctetBinaryTime) oldTimeTag).isSubstituted();
				boolean invalid = ((ThreeOctetBinaryTime) oldTimeTag).isInvalid();

				if (oldTimeTag instanceof SevenOctetBinaryTime) {
					newTimeTag = createSevenOctetBinaryTime(timestamp, substituted, invalid);
				} else {
					newTimeTag = createThreeOctetBinaryTime(timestamp, substituted, invalid);
				}
			} else if (oldTimeTag instanceof TwoOctetBinaryTime) {
				newTimeTag = createTwoOctetBinaryTime(timestamp);
			}
			seq.setTimeTag(Optional.of(newTimeTag));
		}
		return seq;
	}

	private static SingleInformationObject setTimeTags(SingleInformationObject io, long timestamp) {
		return (SingleInformationObject) setTimeTags((InformationElementSequence) io, timestamp);
	}

	private static SequenceInformationObject setTimeTags(SequenceInformationObject io, long timestamp) {
		List<InformationElementSequence> seqs = io.getInformationElementSequences().stream()
				.map(seq -> setTimeTags(seq, timestamp)).collect(Collectors.toList());
		io.setInformationElementSequences(seqs);
		return io;
	}

}