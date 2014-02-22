/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.xls.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extentech.ExtenXLS.CellHandle;
import com.extentech.ExtenXLS.WorkBookHandle;
import com.extentech.ExtenXLS.WorkSheetHandle;
import com.extentech.formats.XLS.CellNotFoundException;
import com.extentech.formats.XLS.WorkSheetNotFoundException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolMonitor;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class XLSProtocolHandler<T extends Tuple<?>> extends AbstractProtocolHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(XLSProtocolHandler.class);
    private static final String NAME = "XLS";

    private static final String WORKSHEET = "worksheet";
    private static final String XLS_FLOATING_FORMATTER = "xls.floatingformatter";
    private static final String XLS_NUMBER_FORMATTER = "xls.numberformatter";
    private static final String XLS_WRITE_METADATA = "xls.writemetadata";
    private static final String XLS_TRIM = "xls.trim";
    private static final String DELAY = "delay";
    private static final String NANODELAY = "delay";
    private static final String DELAYEACH = "delayeach";
    private static final String ADDLINENUMBERS = "addlinenumber";
    private static final String READFIRSTLINE = "readfirstline";
    private static final String DUMP_EACH_LINE = "dumpeachline";
    private static final String MEASURE_EACH_LINE = "measureeachline";
    private static final String LAST_LINE = "lastline";
    private static final String MAX_LINES = "maxlines";
    private static final String DEBUG = "debug";
    private static final String DUMPFILE = "dumpfile";

    private InputStream input;
    private OutputStream output;
    private WorkBookHandle workBook;
    private WorkSheetHandle sheet;
    private DecimalFormat floatingFormatter;
    private DecimalFormat numberFormatter;
    private String worksheet;
    private long delay;
    private int nanodelay;
    private int delayeach = 0;
    private long delayCounter = 0L;
    private boolean writeMetadata;
    private boolean trim = false;
    private boolean addLineNumber = false;
    private boolean readFirstLine = true;
    private boolean firstLineSkipped = false;
    private long dumpEachLine = -1;
    private long lastLine = -1;
    private int lineCounter = 0;
    private boolean debug = false;
    private boolean isDone = false;
    private long measureEachLine = -1;
    private long lastDumpTime = 0;
    private long lastMeasureTime = 0;
    private long basetime;
    private String dumpFile = null;
    private PrintWriter dumpOut;

    /**
* 
*/
    public XLSProtocolHandler() {
        super();
    }

    /**
     * Create a new HTML Data Handler
     * 
     * @param transfer
     * @param dataHandler
     * 
     * @param schema
     */
    private XLSProtocolHandler(final ITransportDirection direction, final IAccessPattern access, final IDataHandler<T> dataHandler) {
        super(direction, access, dataHandler);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access, final Map<String, String> options, final IDataHandler<T> dataHandler) {
        final XLSProtocolHandler<T> instance = new XLSProtocolHandler<T>(direction, access, dataHandler);
        instance.setOptionsMap(options);
        instance.init(options);
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return XLSProtocolHandler.NAME;
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        if (this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        }
        else {
            return ITransportExchangePattern.OutOnly;
        }
    }

    @Override
    public void open() throws IOException {
        this.getTransportHandler().open();
        this.lineCounter = 0;
        if (this.getDirection().equals(ITransportDirection.IN)) {
            if ((this.getAccess().equals(IAccessPattern.PULL)) || (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
                this.input = this.getTransportHandler().getInputStream();
                this.workBook = new WorkBookHandle(this.input);
                try {
                    this.sheet = this.workBook.getWorkSheet(this.getWorksheet());
                }
                catch (final WorkSheetNotFoundException e) {
                    XLSProtocolHandler.LOG.error(e.getMessage(), e);
                    throw new IOException(e);
                }
            }
        }
        else {
            this.output = this.getTransportHandler().getOutputStream();
            this.workBook = new WorkBookHandle();
            try {
                this.sheet = this.workBook.getWorkSheet(this.getWorksheet());
            }
            catch (final WorkSheetNotFoundException e) {
                XLSProtocolHandler.LOG.debug(e.getMessage(), e);
                this.sheet = this.workBook.createWorkSheet(this.getWorksheet());
            }
            this.sheet.setFastCellAdds(true);
            final SDFSchema schema = this.getDataHandler().getSchema();
            int i = 0;
            if (this.addLineNumber) {
                i++;
            }
            for (; i < schema.size(); i++) {
                this.sheet.add(schema.get(i).getAttributeName(), this.lineCounter, i);
            }
            this.lineCounter++;
        }
        this.delayCounter = 0;

        this.isDone = false;
        this.firstLineSkipped = false;
        if (this.debug) {
            ProtocolMonitor.getInstance().addToMonitor(this);
            if (this.dumpFile != null) {
                this.dumpOut = new PrintWriter(this.dumpFile);
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (this.getDirection().equals(ITransportDirection.IN)) {
            if (this.input != null) {
                this.input.close();
            }
        }
        else {
            this.workBook.write(this.output);
            this.output.flush();
            if (this.sheet != null) {
                this.sheet.close();
            }
            this.workBook.close();
            if (this.output != null) {
                this.output.close();
            }
        }
        this.getTransportHandler().close();
        if (this.debug) {
            ProtocolMonitor.getInstance().informMonitor(this, this.lineCounter);
            ProtocolMonitor.getInstance().removeFromMonitor(this);
            if (this.dumpOut != null) {
                this.dumpOut.close();
            }
        }
    }

    @Override
    public void onConnect(final ITransportHandler caller) {

    }

    @Override
    public void onDisonnect(final ITransportHandler caller) {

    }

    @Override
    public boolean hasNext() throws IOException {
        return this.input.available() > 0;
    }

    @Override
    public T getNext() throws IOException {
        final T tuple = this.getNextLine();
        return tuple;
    }

    protected T getNextLine() throws IOException {
        if (!this.firstLineSkipped && !this.readFirstLine) {
            this.lineCounter++;
            this.firstLineSkipped = true;
        }
        this.delay();
        final SDFSchema schema = this.getDataHandler().getSchema();
        final String[] tuple = new String[schema.size() + (this.addLineNumber ? 1 : 0)];
        int i = 0;
        if (this.addLineNumber) {
            tuple[i++] = this.lineCounter + "";
        }
        for (; i < schema.size(); i++) {
            try {
                final CellHandle value = this.sheet.getCell(this.lineCounter, i);
                String valueString = value.getStringVal();
                if (this.trim) {
                    valueString = valueString.trim();
                }
                tuple[i] = valueString;
            }
            catch (final CellNotFoundException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        if (this.debug) {
            if (this.dumpEachLine > 0) {
                if ((this.lineCounter % this.dumpEachLine) == 0) {
                    final long time = System.currentTimeMillis();
                    XLSProtocolHandler.LOG.debug(this.lineCounter + " " + time + " " + (time - this.lastDumpTime) + " (" + Integer.toHexString(this.hashCode()) + ") line: " + Arrays.toString(tuple));
                    this.lastDumpTime = time;
                }
            }
            if (this.measureEachLine > 0) {
                if ((this.lineCounter % this.measureEachLine) == 0) {
                    final long time = System.currentTimeMillis();
                    // measurements.append(lineCounter).append(";").append(time
                    // - basetime).append("\n");
                    if (this.dumpOut != null) {
                        this.dumpOut.println(this.lineCounter + ";" + (time - this.basetime) + ";" + (time - this.lastMeasureTime));
                    }
                    this.lastMeasureTime = time;
                }
            }

            if ((this.lastLine == this.lineCounter) || (this.lineCounter == 0)) {
                final long time = System.currentTimeMillis();
                if (this.lineCounter == 0) {
                    this.basetime = time;
                }
                XLSProtocolHandler.LOG.debug(this.lineCounter + " " + time);
                if (this.dumpOut != null) {
                    this.dumpOut.println(this.lineCounter + ";" + (time - this.basetime) + ";" + (time - this.lastMeasureTime));
                }
                // measurements.append(lineCounter).append(";").append(time -
                // basetime).append("\n");
                if (this.lastLine == this.lineCounter) {
                    // System.out.println(measurements);
                    ProtocolMonitor.getInstance().informMonitor(this, this.lineCounter);
                    this.isDone = true;
                }
            }
        }
        this.lineCounter++;

        return this.getDataHandler().readData(tuple);
    }

    @Override
    public void write(final T object) throws IOException {
        final SDFSchema schema = this.getDataHandler().getSchema();
        int i = 0;
        if (this.addLineNumber) {
            this.sheet.add(this.lineCounter + "", this.lineCounter, i);
        }
        for (; i < schema.size(); i++) {
            final Object curAttribute = object.getAttribute(i);

            if (curAttribute instanceof Number) {
                if (((curAttribute instanceof Double) || (curAttribute instanceof Float)) && (this.floatingFormatter != null)) {
                    this.sheet.add(this.floatingFormatter.format(curAttribute), this.lineCounter, i);
                }
                else if (!(((curAttribute instanceof Double) || (curAttribute instanceof Float))) && (this.numberFormatter != null)) {
                    this.sheet.add(this.numberFormatter.format(curAttribute), this.lineCounter, i);
                }
                else {
                    this.sheet.add(curAttribute, this.lineCounter, i);
                }
            }
            else {
                this.sheet.add(curAttribute, this.lineCounter, i);
            }
        }
        if (this.writeMetadata) {
            this.sheet.add(object.getMetadata().toString(), this.lineCounter, i + 1);
        }
        this.lineCounter++;
    }

    /**
     * @return the worksheet
     */
    public String getWorksheet() {
        return this.worksheet;
    }

    /**
     * @param worksheet
     *            the worksheet to set
     */
    public void setWorksheet(final String worksheet) {
        this.worksheet = worksheet;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(final long delay) {
        this.delay = delay;
    }

    public int getNanodelay() {
        return this.nanodelay;
    }

    public void setNanodelay(final int nanodelay) {
        this.nanodelay = nanodelay;
    }

    public int getDelayeach() {
        return this.delayeach;
    }

    public void setDelayeach(final int delayeach) {
        this.delayeach = delayeach;
    }

    @Override
    public boolean isDone() {
        return this.isDone;
    }

    @Override
    public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> o) {
        if (!(o instanceof XLSProtocolHandler)) {
            return false;
        }
        final XLSProtocolHandler<?> other = (XLSProtocolHandler<?>) o;
        if ((this.nanodelay != other.getNanodelay()) || (this.delay != other.getDelay()) || (this.delayeach != other.getDelayeach()) || (this.dumpEachLine != other.getDumpEachLine())
                || (this.measureEachLine != other.getMeasureEachLine()) || (this.lastLine != other.getLastLine()) || (this.debug != other.isDebug()) || (this.readFirstLine != other.isReadFirstLine())) {
            return false;
        }
        return true;
    }

    public boolean isReadFirstLine() {
        return this.readFirstLine;
    }

    public long getDumpEachLine() {
        return this.dumpEachLine;
    }

    public long getLastLine() {
        return this.lastLine;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public long getMeasureEachLine() {
        return this.measureEachLine;
    }

    private void delay() {
        if (this.delayeach > 0) {
            this.delayCounter++;
            if (this.delayCounter < this.delayeach) {
                return;
            }
            this.delayCounter = 0;
        }
        if (this.delay > 0) {
            try {
                Thread.sleep(this.delay);
            }
            catch (final InterruptedException e) {
                // interrupting the delay might be correct
                // e.printStackTrace();
            }
        }
        else {
            if (this.nanodelay > 0) {
                try {
                    Thread.sleep(0L, this.nanodelay);
                }
                catch (final InterruptedException e) {
                    // interrupting the delay might be correct
                    // e.printStackTrace();
                }
            }
        }
    }

    private void init(final Map<String, String> options) {
        if (options.containsKey(XLSProtocolHandler.WORKSHEET)) {
            this.setWorksheet(options.get(XLSProtocolHandler.WORKSHEET));
        }
        else {
            this.setWorksheet("Sheet1");
        }
        if (options.containsKey(XLSProtocolHandler.DELAY)) {
            this.setDelay(Long.parseLong(options.get(XLSProtocolHandler.DELAY)));
        }
        if (options.containsKey(XLSProtocolHandler.NANODELAY)) {
            this.setNanodelay(Integer.parseInt(options.get(XLSProtocolHandler.NANODELAY)));
        }
        if (options.containsKey(XLSProtocolHandler.DELAYEACH)) {
            this.setDelayeach(Integer.parseInt(options.get(XLSProtocolHandler.DELAYEACH)));
        }
        if (options.containsKey(XLSProtocolHandler.XLS_FLOATING_FORMATTER)) {
            this.floatingFormatter = new DecimalFormat(options.get(XLSProtocolHandler.XLS_FLOATING_FORMATTER));
        }
        if (options.containsKey(XLSProtocolHandler.XLS_NUMBER_FORMATTER)) {
            this.numberFormatter = new DecimalFormat(options.get(XLSProtocolHandler.XLS_NUMBER_FORMATTER));
        }

        if (options.containsKey(XLSProtocolHandler.XLS_WRITE_METADATA)) {
            this.writeMetadata = Boolean.parseBoolean(options.get(XLSProtocolHandler.XLS_WRITE_METADATA));
        }
        if (options.containsKey(XLSProtocolHandler.XLS_TRIM)) {
            this.trim = Boolean.parseBoolean(options.get(XLSProtocolHandler.XLS_TRIM));
        }
        if (options.containsKey(XLSProtocolHandler.ADDLINENUMBERS)) {
            this.addLineNumber = Boolean.parseBoolean(options.get(XLSProtocolHandler.ADDLINENUMBERS));
        }
        if (options.containsKey(XLSProtocolHandler.READFIRSTLINE)) {
            this.readFirstLine = Boolean.parseBoolean(options.get(XLSProtocolHandler.READFIRSTLINE));
        }
        else {
            this.readFirstLine = true;
        }
        if (options.containsKey(XLSProtocolHandler.DUMP_EACH_LINE)) {
            this.dumpEachLine = Integer.parseInt(options.get(XLSProtocolHandler.DUMP_EACH_LINE));
        }

        if (options.containsKey(XLSProtocolHandler.MEASURE_EACH_LINE)) {
            this.measureEachLine = Integer.parseInt(options.get(XLSProtocolHandler.MEASURE_EACH_LINE));
        }

        if (options.containsKey(XLSProtocolHandler.LAST_LINE)) {
            this.lastLine = Integer.parseInt(options.get(XLSProtocolHandler.LAST_LINE));
        }
        if (options.containsKey(XLSProtocolHandler.MAX_LINES)) {
            this.lastLine = Integer.parseInt(options.get(XLSProtocolHandler.MAX_LINES));
        }
        if (options.containsKey(XLSProtocolHandler.DEBUG)) {
            this.debug = Boolean.parseBoolean(options.get(XLSProtocolHandler.DEBUG));
        }
        if (options.containsKey(XLSProtocolHandler.DUMPFILE)) {
            this.dumpFile = options.get(XLSProtocolHandler.DUMPFILE);
        }
        this.lastDumpTime = System.currentTimeMillis();

    }

}
