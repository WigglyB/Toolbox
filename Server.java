package toolbox;

import java.net.*;
import java.io.*;

public class Server{
    private final int port;
    private final PacketHandler handler;
    private boolean running = false;
    private DatagramSocket socket;
    private InetAddress lastSenderAddress;
    private int lastSenderPort;

    public Server(int port, PacketHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void start() {
        running = true;
        new Thread(() -> {
            try {
                socket = new DatagramSocket(port);
                byte[] buffer = new byte[1024];

                while (running) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet); // wait for incoming packet

                    // store sender info for reply
                    lastSenderAddress = packet.getAddress();
                    lastSenderPort = packet.getPort();

                    // copy data to avoid buffer overwrite
                    byte[] data = new byte[packet.getLength()];
                    System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());

                    handler.processPacket(data,lastSenderPort,lastSenderAddress); // give data to owner
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendPacket(byte[] data,InetAddress address,int Port) throws IOException {
        
            DatagramPacket reply = new DatagramPacket(data, data.length, address, Port);
            socket.send(reply);
        
    }

    public void stop() {
        running = false;
        if (socket != null) socket.close();
    }
}
