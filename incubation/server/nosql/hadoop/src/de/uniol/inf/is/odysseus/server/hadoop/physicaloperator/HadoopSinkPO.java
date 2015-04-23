package de.uniol.inf.is.odysseus.server.hadoop.physicaloperator;

import de.uniol.inf.is.odysseus.server.hadoop.connectionwrapper.HadoopConnectionWrapper;
import de.uniol.inf.is.odysseus.server.hadoop.logicaloperator.HadoopSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivilegedAction;
import java.util.List;

public class HadoopSinkPO extends AbstractNoSQLJsonSinkPO {

    private Path path;
    private FileSystem fileSystem;
    private UserGroupInformation userGroupInformation;
    private PrivilegedActionImpl privilegedAction = new PrivilegedActionImpl();

    public HadoopSinkPO(HadoopSinkAO hadoopSinkAO) {
        super(hadoopSinkAO);

        String pathAsString = hadoopSinkAO.getPath();
        this.path = new Path(pathAsString);

        String user = hadoopSinkAO.getUser();

        if(user == null){
            throw new IllegalStateException("User must not be null");
        }
        
        userGroupInformation = UserGroupInformation.createRemoteUser(user);
    }

    @Override
    public void setupConnection(Object connection) {
        fileSystem = (FileSystem) connection;
    }

    @Override
    protected void process_next_json_to_write(List<String> jsonToWrite) {

        StringBuilder builder = new StringBuilder();

        for (String json : jsonToWrite) {
            builder.append(json).append("\r\n");
        }

        appendToFile(builder.toString());
    }

    private void appendToFile(String append) {

    	privilegedAction = new PrivilegedActionImpl();
        privilegedAction.setAppend(append);
        userGroupInformation.doAs(privilegedAction);
    }

    @Override
    public Class<? extends NoSQLConnectionWrapper<?>> getNoSQLConnectionWrapperClass() {
        return HadoopConnectionWrapper.class;
    }

    private class PrivilegedActionImpl implements PrivilegedAction<Void> {

        private String append;

        @Override
        public Void run() {

            try {

                OutputStream outputStream = fileSystem.append(path).getWrappedStream();

                outputStream.write(append.getBytes());
                outputStream.flush();
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setAppend(String append) {
            this.append = append;
        }
    }
}

