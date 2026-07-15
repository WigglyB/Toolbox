package toolbox.Network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import toolbox.Toolbox;

public class TCPServer implements Closeable,ClientManager {

    private final int port;
    private ServerSocket server;
    private boolean running;
    private final ClientManager cm;
    public TCPServer(int port) {
        this.port = port;
        cm=this;
    }
    public TCPServer(int port,ClientManager manager) {
        this.port = port;
        cm=manager;
    }

    public void start() {
        running = true;
        long l;
        if(Toolbox.DEBUG){
            l=System.nanoTime();
        }
        new Thread(() -> {
            try {
                server = new ServerSocket(port);
                
                while (running) {
                    if(Toolbox.DEBUG){
                        if(System.nanoTime()-l>60000000000L){
                            running=false;
                        }
                    }
                    Socket clientSock = server.accept();
                    TCPClient client=new TCPClient();
                    client.connect(clientSock);
                    if(Toolbox.DEBUG){
                        System.out.println(client+" connected");
                        
                    }
                    cm.TakeClient(client);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void close() throws IOException {
        running = false;
        server.close();
    }

    @Override
    public void TakeClient(TCPClient c) {
        System.err.println("Server Lacks Client Manager, closing connection to "+c.toString());
        try {
            c.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}