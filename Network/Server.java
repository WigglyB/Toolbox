package toolbox.Network;

import java.net.*;

import toolbox.Toolbox;


import java.io.*;

public class Server implements Closeable{
    private final int port;
    private final PacketHandler handler;
    private boolean running = false;
    private DatagramSocket socket;
    private InetAddress lastSenderAddress;
    private int lastSenderPort;
    private String encpasscode;
    private boolean killable=false;

    public void setKillable(String passcode){
        killable=true;
        encpasscode=encrypt(passcode);
    }
    public void setKillable(String passcode,boolean isKillable){
        killable=isKillable;
        encpasscode=encrypt(passcode);
    }
    public boolean getKillable(String passcode){
        if(encpasscode.equals(encrypt(passcode))){
            return killable;
        }
        throw new SecurityException("tried to access server passcode without proper password");
    }

    public Server(int port, PacketHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void start() {
        running = true;
        new Thread(() -> {
            String passcode;
            try {
                socket = new DatagramSocket(port);
                
                socket.setSoTimeout(Integer.MAX_VALUE);
                if(Toolbox.DEBUG){
                    socket.setSoTimeout(3600000);//1 hour, good for debugging
                }
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
                    if(killable){
                        passcode=new String(data);
                        if(encrypt(passcode).equals(encpasscode)){
                            stop();
                            break;
                        }
                    }
                    handler.processPacket(data,lastSenderPort,lastSenderAddress); // give data to owner
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private String encrypt(String og){//as far as I know, doesn't really need to be all that secure, as the password is unlikly to leak, as it is primarily for debug purposes.
        return og;
    }
    public void sendPacket(byte[] data,InetAddress address,int Port) throws IOException {
        
            DatagramPacket reply = new DatagramPacket(data, data.length, address, Port);
            socket.send(reply);
        
    }

    public void stop() {
        running = false;
        if (socket != null) socket.close();
    }
    @Override
    public void close(){
        stop();
    }
}
