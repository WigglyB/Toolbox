package toolbox.Network;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.Socket;

public class TCPClient implements Closeable {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private boolean isOpen;
    //private InputStream pubIn;
    
    public void connect(InetAddress address, int port) throws IOException {
        socket = new Socket(address, port);
        connect(socket);
    }
    void connect(Socket s) throws IOException{
        isOpen=true;
        socket=s;
        in=socket.getInputStream();
        
        out=socket.getOutputStream();
        
    }

    
    public OutputStream receiveOutputStream(){
        return out;
    }
    

    public InputStream receiveStream(){
        return in;
    }

    

    // Send a packet to a server (or any address/port)
    public void sendPacket(byte[] data) throws IOException {
        out.write(data);
        out.flush();
    }
    public void sendPacket(String dataString) throws IOException{
        out.write(dataString.getBytes());
    }

    public void close() throws IOException{
        socket.close();
        isOpen=false;
    }
    public boolean isOpen(){
        return isOpen;
    }

    
    @Override
    public String toString() {
        return "client:"+socket.getPort()+"@"+socket.getInetAddress();
    }
}

