package mining.generator.base.socket;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import mining.generator.base.tuple.DataTuple;

public abstract class StreamClientHandler extends Thread{

	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
    private ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
    private Socket connection;
    
   
    public abstract void init();
    public abstract void close();
    public abstract DataTuple next();
    
    @Override
    public void run() {
    	init();
    	DataTuple next = next();
    	while(next!=null){
    		try {
    			if(this.connection.isClosed()){
    				System.out.println("Connection closed.");
    				break;
    			}
				transferTuple(next);
				next = next();
				
			} catch (IOException e) { 
				System.out.println("Connection closed.");
				break;
			}
    	}
    	close();
    }
    
    
    public void transferTuple(DataTuple tuple) throws IOException {    	
        if (tuple != null) {
            ByteBuffer buffer = getByteBuffer(tuple);
            synchronized (gbuffer) {
                gbuffer.clear();
                gbuffer.putInt(buffer.limit());
                gbuffer.flip();
                SocketChannel ch = connection.getChannel();
                ch.write(gbuffer);
                ch.write(buffer);
            }
        } else {
            connection.getChannel().close();

        }
    }

    private ByteBuffer getByteBuffer(DataTuple tuple) {        
        bytebuffer = ByteBuffer.allocate(tuple.memSize(false));
        bytebuffer.clear();
        for (Object data : tuple.getAttributes()) {
            if (data instanceof Integer) {
                bytebuffer.putInt((Integer) data);
            } else if (data instanceof Double) {
                bytebuffer.putDouble((Double) data);
            } else if (data instanceof Long) {
                bytebuffer.putLong((Long) data);
            } else if (data instanceof String) {
                String s = (String) data;
                bytebuffer.putInt(s.length());
                for (int i = 0; i < s.length(); i++) {
                    bytebuffer.putChar(s.charAt(i));
                }
            } else {
                throw new RuntimeException("illegal datatype " + data);
            }
        }
        bytebuffer.flip();
        return bytebuffer;
    }

	public void setConnection(Socket connection) {
		this.connection = connection;		
	}
	
	public void remove() {				
	}
}
