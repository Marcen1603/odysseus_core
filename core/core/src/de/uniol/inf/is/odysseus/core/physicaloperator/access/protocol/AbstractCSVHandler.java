package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

abstract public class AbstractCSVHandler<T> extends LineProtocolHandler<T> {
    private static final Charset charset = Charset.forName("UTF-8");
    protected char delimiter;
    protected char textDelimiter;
    protected DecimalFormat floatingFormatter;
    protected DecimalFormat numberFormatter;
    protected boolean writeMetadata;
    protected boolean trim = false;
    protected boolean addLineNumber = false;
    protected String delimiterString;
    
    public static final String DELIMITER = "delimiter";
    public static final String CSV_DELIMITER = "csv.delimiter";
    public static final String TEXT_DELIMITER = "textdelimiter";
    public static final String CSV_TEXT_DELIMITER = "csv.textdelimiter";
    public static final String CSV_FLOATING_FORMATTER = "csv.floatingformatter";
    public static final String CSV_NUMBER_FORMATTER = "csv.numberformatter";
    public static final String CSV_WRITE_METADATA = "csv.writemetadata";
    public static final String CSV_TRIM = "csv.trim";
    public static final String ADDLINENUMBERS = "addlinenumber";

    
    public AbstractCSVHandler(ITransportDirection direction, IAccessPattern access) {
        super(direction, access);
    }

    public AbstractCSVHandler() {
        super();
    }

    @Override
    protected void init(Map<String, String> options) {
        super.init(options);
        if (options.containsKey(DELIMITER)) {
            delimiter = options.get(DELIMITER).toCharArray()[0];
        } else {
            delimiter = options.containsKey(CSV_DELIMITER) ? options.get(CSV_DELIMITER).toCharArray()[0] : ",".toCharArray()[0];
        }
        if (options.containsKey(TEXT_DELIMITER)) {
            textDelimiter = options.get(TEXT_DELIMITER).toCharArray()[0];
        } else {
            textDelimiter = options.containsKey(CSV_TEXT_DELIMITER) ? options.get(CSV_TEXT_DELIMITER).toCharArray()[0] : "'".toCharArray()[0];
        }
        // only calc once
        delimiterString = Character.toString(delimiter);
        if (options.containsKey(CSV_FLOATING_FORMATTER)) {
            floatingFormatter = new DecimalFormat(options.get(CSV_FLOATING_FORMATTER));
        }
        if (options.containsKey(CSV_NUMBER_FORMATTER)) {
            numberFormatter = new DecimalFormat(options.get(CSV_NUMBER_FORMATTER));
        }
        if (options.containsKey(CSV_WRITE_METADATA)) {
            writeMetadata = Boolean.parseBoolean(options.get(CSV_WRITE_METADATA));
        }
        if (options.containsKey(CSV_TRIM)) {
            trim = Boolean.parseBoolean(options.get(CSV_TRIM));
        }
        if (options.get(ADDLINENUMBERS) != null) {
            addLineNumber = Boolean.parseBoolean(options.get(ADDLINENUMBERS));
        }

    }

    @Override
    public void onConnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public T getNext() throws IOException {
        String line = super.getNextLine();
        if (line != null) {
            if (addLineNumber) {
                line = super.lineCounter + delimiterString + line;
            }
            return readLine(line);
        }
        return null;
    }

    @Override
    public void process(ByteBuffer message) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(message.array())));
        if (!firstLineSkipped && !readFirstLine) {
            try {
                reader.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            firstLineSkipped = true;
        }
        String line;
        try {
            line = reader.readLine();
            if (line != null) {
                T retValue = readLine(line);
                // System.out.println(retValue);
                getTransfer().transfer(retValue);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected abstract T readLine(String line);

    @Override
    public void write(T object) throws IOException {
        StringBuilder out = new StringBuilder();
        getDataHandler().writeCSVData(out, object, delimiter, textDelimiter, floatingFormatter, numberFormatter, writeMetadata);
        out.append(System.lineSeparator());
        getTransportHandler().send(charset.encode(CharBuffer.wrap(out)).array());
    }

}
