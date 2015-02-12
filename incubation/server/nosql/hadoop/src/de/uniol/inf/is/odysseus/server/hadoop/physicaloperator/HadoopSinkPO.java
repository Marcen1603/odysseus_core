package de.uniol.inf.is.odysseus.server.hadoop.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.hadoop.logicaloperator.HadoopSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSinkPO;

import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * Erstellt von RoBeaT
 * Date: 14.01.2015
 */
public class HadoopSinkPO extends AbstractNoSQLJsonSinkPO {

    private Path path;
    private FileSystem fileSystem;
    private UserGroupInformation userGroupInformation;

    public HadoopSinkPO(HadoopSinkAO hadoopSinkAO) {
        super(hadoopSinkAO);
        String pathAsString = hadoopSinkAO.getPath();
        this.path = new Path(pathAsString);
    }

    @Override
    protected void process_open_connection() throws OpenFailedException {

        userGroupInformation = UserGroupInformation.createRemoteUser("hadoop");

        try {

            userGroupInformation.doAs(new PrivilegedExceptionAction<Void>() {

                @Override
                public Void run() throws Exception {

                    Configuration configuration = new Configuration();
                    configuration.set("fs.defaultFS", "hdfs://134.106.56.17:9000");
                    configuration.set("dfs.replication", "1");

                    fileSystem = FileSystem.get(configuration);

//                    FSDataOutputStream fsDataOutputStream;
                    if(!fileSystem.exists(path)){
                        fileSystem.create(path).close();
                    }
//                    else {
//                        fsDataOutputStream = fileSystem.append(path);
//                    }


                    return null;
                }

            });

        } catch (IOException | InterruptedException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    protected void process_next_tuple_to_write(List<Tuple<ITimeInterval>> tupleToWrite) {

        StringBuilder builder = new StringBuilder();

        for (Tuple<ITimeInterval> tuple : tupleToWrite) {

            String json = toJsonString(tuple);
            builder.append(json).append("\r\n");
        }

        appendToFile(builder.toString());
    }

    private void appendToFile(String append) {

        // ToDo: find a better way
        userGroupInformation.doAs(new PrivilegedActionImpl(append));
    }

    private class PrivilegedActionImpl implements PrivilegedAction<Void> {

        private String append;

        public PrivilegedActionImpl(String append) {
            this.append = append;
        }

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
    }
}

