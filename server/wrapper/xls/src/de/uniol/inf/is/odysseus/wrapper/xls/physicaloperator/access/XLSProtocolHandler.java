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
    private XLSProtocolHandler(final ITransportDirection direction, final IAccessPattern access, IDataHandler<T> dataHandler) {
        super(direction, access, dataHandler);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options, IDataHandler<T> dataHandler) {
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
        return NAME;
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
        lineCounter = 0;
        if (this.getDirection().equals(ITransportDirection.IN)) {
            if ((this.getAccess().equals(IAccessPattern.PULL)) || (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
                this.input = this.getTransportHandler().getInputStream();
            }
        }
        else {
            this.output = this.getTransportHandler().getOutputStream();
            this.workBook = new WorkBookHandle();
            try {
                sheet = workBook.getWorkSheet(getWorksheet());
            }
            catch (WorkSheetNotFoundException e) {
                LOG.debug(e.getMessage(), e);
                sheet = workBook.createWorkSheet(getWorksheet());

            }
            sheet.setFastCellAdds(true);
            final SDFSchema schema = this.getDataHandler().getSchema();
            int i = 0;
            if (addLineNumber) {
                i++;
            }
            for (; i < schema.size(); i++) {
                this.sheet.add(schema.get(i).getAttributeName(), lineCounter, i);
            }
            lineCounter++;
        }
        delayCounter = 0;

        isDone = false;
        firstLineSkipped = false;
        if (debug) {
            ProtocolMonitor.getInstance().addToMonitor(this);
            if (dumpFile != null) {
                dumpOut = new PrintWriter(dumpFile);
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
            workBook.write(this.output);
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
        if (debug) {
            ProtocolMonitor.getInstance().informMonitor(this, lineCounter);
            ProtocolMonitor.getInstance().removeFromMonitor(this);
            if (dumpOut != null) {
                dumpOut.close();
            }
        }
    }

    @Override
    public void onConnect(ITransportHandler caller) {

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {

    }

    @Override
    public boolean hasNext() throws IOException {
        return this.input.available() > 0;
    }

    @Override
    public T getNext() throws IOException {
        T tuple = getNextLine();
        return tuple;
    }

    protected T getNextLine() throws IOException {
        if (!firstLineSkipped && !readFirstLine) {
            lineCounter++;
            firstLineSkipped = true;
        }
        delay();
        final SDFSchema schema = this.getDataHandler().getSchema();
        final String[] tuple = new String[schema.size() + (addLineNumber ? 1 : 0)];
        int i = 0;
        if (addLineNumber) {
            tuple[i++] = lineCounter + "";
        }
        for (; i < schema.size(); i++) {
            try {
                CellHandle value = this.sheet.getCell(lineCounter, i);
                String valueString = value.getStringVal();
                if (trim) {
                    valueString = valueString.trim();
                }
                tuple[i] = valueString;
            }
            catch (CellNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (debug) {
            if (dumpEachLine > 0) {
                if (lineCounter % dumpEachLine == 0) {
                    long time = System.currentTimeMillis();
                    LOG.debug(lineCounter + " " + time + " " + (time - lastDumpTime) + " (" + Integer.toHexString(hashCode()) + ") line: " + Arrays.toString(tuple));
                    lastDumpTime = time;
                }
            }
            if (measureEachLine > 0) {
                if (lineCounter % measureEachLine == 0) {
                    long time = System.currentTimeMillis();
                    // measurements.append(lineCounter).append(";").append(time
                    // - basetime).append("\n");
                    if (dumpOut != null) {
                        dumpOut.println(lineCounter + ";" + (time - basetime) + ";" + (time - lastMeasureTime));
                    }
                    lastMeasureTime = time;
                }
            }

            if (lastLine == lineCounter || lineCounter == 0) {
                long time = System.currentTimeMillis();
                if (lineCounter == 0) {
                    basetime = time;
                }
                LOG.debug(lineCounter + " " + time);
                if (dumpOut != null) {
                    dumpOut.println(lineCounter + ";" + (time - basetime) + ";" + (time - lastMeasureTime));
                }
                // measurements.append(lineCounter).append(";").append(time -
                // basetime).append("\n");
                if (lastLine == lineCounter) {
                    // System.out.println(measurements);
                    ProtocolMonitor.getInstance().informMonitor(this, lineCounter);
                    isDone = true;
                }
            }
        }
        lineCounter++;

        return this.getDataHandler().readData(tuple);
    }

    @Override
    public void write(final T object) throws IOException {
        final SDFSchema schema = this.getDataHandler().getSchema();
        int i = 0;
        if (addLineNumber) {
            this.sheet.add(lineCounter + "", lineCounter, i);
        }
        for (; i < schema.size(); i++) {
            Object curAttribute = object.getAttribute(i);

            if (curAttribute instanceof Number) {
                if ((curAttribute instanceof Double || curAttribute instanceof Float) && floatingFormatter != null) {
                    this.sheet.add(floatingFormatter.format(curAttribute), lineCounter, i);
                }
                else if (!((curAttribute instanceof Double || curAttribute instanceof Float)) && numberFormatter != null) {
                    this.sheet.add(numberFormatter.format(curAttribute), lineCounter, i);
                }
                else {
                    this.sheet.add(curAttribute, lineCounter, i);
                }
            }
            else {
                this.sheet.add(curAttribute, lineCounter, i);
            }
        }
        if (writeMetadata) {
            this.sheet.add(object.getMetadata().toString(), lineCounter, i + 1);
        }
        lineCounter++;
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
    public void setWorksheet(String worksheet) {
        this.worksheet = worksheet;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getNanodelay() {
        return nanodelay;
    }

    public void setNanodelay(int nanodelay) {
        this.nanodelay = nanodelay;
    }

    public int getDelayeach() {
        return delayeach;
    }

    public void setDelayeach(int delayeach) {
        this.delayeach = delayeach;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
        if (!(o instanceof XLSProtocolHandler)) {
            return false;
        }
        XLSProtocolHandler<?> other = (XLSProtocolHandler<?>) o;
        if (this.nanodelay != other.getNanodelay() || this.delay != other.getDelay() || this.delayeach != other.getDelayeach() || this.dumpEachLine != other.getDumpEachLine()
                || this.measureEachLine != other.getMeasureEachLine() || this.lastLine != other.getLastLine() || this.debug != other.isDebug() || this.readFirstLine != other.isReadFirstLine()) {
            return false;
        }
        return true;
    }

    public boolean isReadFirstLine() {
        return readFirstLine;
    }

    public long getDumpEachLine() {
        return dumpEachLine;
    }

    public long getLastLine() {
        return lastLine;
    }

    public boolean isDebug() {
        return debug;
    }

    public long getMeasureEachLine() {
        return measureEachLine;
    }

    private void delay() {
        if (delayeach > 0) {
            delayCounter++;
            if (delayCounter < delayeach) {
                return;
            }
            delayCounter = 0;
        }
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            }
            catch (InterruptedException e) {
                // interrupting the delay might be correct
                // e.printStackTrace();
            }
        }
        else {
            if (nanodelay > 0) {
                try {
                    Thread.sleep(0L, nanodelay);
                }
                catch (InterruptedException e) {
                    // interrupting the delay might be correct
                    // e.printStackTrace();
                }
            }
        }
    }

    private void init(Map<String, String> options) {
        if (options.containsKey(WORKSHEET)) {
            setWorksheet(options.get(WORKSHEET));
        }
        else {
            setWorksheet("Sheet1");
        }
        if (options.containsKey(DELAY)) {
            setDelay(Long.parseLong(options.get(DELAY)));
        }
        if (options.containsKey(NANODELAY)) {
            setNanodelay(Integer.parseInt(options.get(NANODELAY)));
        }
        if (options.containsKey(DELAYEACH)) {
            setDelayeach(Integer.parseInt(options.get(DELAYEACH)));
        }
        if (options.containsKey(XLS_FLOATING_FORMATTER)) {
            floatingFormatter = new DecimalFormat(options.get(XLS_FLOATING_FORMATTER));
        }
        if (options.containsKey(XLS_NUMBER_FORMATTER)) {
            numberFormatter = new DecimalFormat(options.get(XLS_NUMBER_FORMATTER));
        }

        if (options.containsKey(XLS_WRITE_METADATA)) {
            writeMetadata = Boolean.parseBoolean(options.get(XLS_WRITE_METADATA));
        }
        if (options.containsKey(XLS_TRIM)) {
            trim = Boolean.parseBoolean(options.get(XLS_TRIM));
        }
        if (options.containsKey(ADDLINENUMBERS)) {
            addLineNumber = Boolean.parseBoolean(options.get(ADDLINENUMBERS));
        }
        if (options.containsKey(READFIRSTLINE)) {
            readFirstLine = Boolean.parseBoolean(options.get(READFIRSTLINE));
        }
        else {
            readFirstLine = true;
        }
        if (options.containsKey(DUMP_EACH_LINE)) {
            dumpEachLine = Integer.parseInt(options.get(DUMP_EACH_LINE));
        }

        if (options.containsKey(MEASURE_EACH_LINE)) {
            measureEachLine = Integer.parseInt(options.get(MEASURE_EACH_LINE));
        }

        if (options.containsKey(LAST_LINE)) {
            lastLine = Integer.parseInt(options.get(LAST_LINE));
        }
        if (options.containsKey(MAX_LINES)) {
            lastLine = Integer.parseInt(options.get(MAX_LINES));
        }
        if (options.containsKey(DEBUG)) {
            debug = Boolean.parseBoolean(options.get(DEBUG));
        }
        if (options.containsKey(DUMPFILE)) {
            dumpFile = options.get(DUMPFILE);
        }
        lastDumpTime = System.currentTimeMillis();

    }

}
