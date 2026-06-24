package toolbox;

import java.net.InetAddress;

public interface PacketHandler {
    public void processPacket(byte[] data,int port,InetAddress address);
    
}
