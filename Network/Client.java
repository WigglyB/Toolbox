package toolbox.Network;

import java.net.*;
import java.io.*;


import toolbox.Toolbox;

public class Client{
    private DatagramSocket socket;
    private final PacketHandler handler;
    private boolean running = false;
    
    
    

    public Client(int localPort, PacketHandler handler) throws SocketException {
        this.socket = new DatagramSocket(localPort); // local port or 0 for automatic
        this.handler = handler;
    }

    // Start listening for packets in a separate thread
    public void start() {
        running = true;
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (running) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet); // blocks until a packet arrives

                    // Copy the data to avoid overwriting buffer
                    byte[] data = new byte[packet.getLength()];
                    System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());

                    // Pass to owner for immediate processing
                    handler.processPacket(data, packet.getPort(), packet.getAddress());
                    if(Toolbox.DEBUG){
                        System.out.println("Packet recieved");
                    }
                } catch (IOException e) {
                    if (running) e.printStackTrace();
                }
            }
        }).start();
    }

    

    // Send a packet to a server (or any address/port)
    public void sendPacket(byte[] data, InetAddress serverAddress, int serverPort) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
        socket.send(packet);
    }

    public void stop() {
        running = false;
        socket.close();
    }
}