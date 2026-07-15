package toolbox.Network;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import toolbox.Toolbox;

public interface HTTPClientManager extends ClientManager {
    public static final String ERROR501="HTTP/1.1 501 Not Implemented\r\n" + //
                "Content-Type: text/plain; charset=UTF-8\r\n" + //
                "Content-Length: 39\r\n" + //
                "\r\n" + //
                "This HTTP method is not implemented yet.";

    public static String readHttpRequest(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Read until "\r\n\r\n"
        int state = 0;
        {
            int b = in.read();
            if(b==-1){
                return null;
            }
        while (true) {
            
            if (b == -1)
                throw new EOFException("Connection closed before headers completed.");

            out.write(b);
            
            switch (state) {
                case 0 -> state = (b == '\r') ? 1 : 0;
                case 1 -> state = (b == '\n') ? 2 : 0;
                case 2 -> state = (b == '\r') ? 3 : 0;
                case 3 -> {
                    if (b == '\n')
                        break;
                    state = 0;
                }
            }

            if (state == 3 && b == '\n')
                break;
            b = in.read();
            }
        }

        byte[] headerBytes = out.toByteArray();
        if(Toolbox.DEBUG){
            System.out.println("recieving "+out.toString(StandardCharsets.ISO_8859_1));
        }
        String headers = new String(headerBytes, StandardCharsets.ISO_8859_1);

        // Find Content-Length
        int contentLength = 0;
        for (String line : headers.split("\r\n")) {
            if (line.toLowerCase().startsWith("content-length:")) {
                contentLength = Integer.parseInt(line.substring(15).trim());
                break;
            }
        }

        // Read body
        for (int i = 0; i < contentLength; i++) {
            int b = in.read();
            if (b == -1)
                throw new EOFException("Connection closed during body.");
            out.write(b);
        }
        if(Toolbox.DEBUG){
            System.out.println("recieved "+out.toString(StandardCharsets.ISO_8859_1));
        }
        return out.toString(StandardCharsets.ISO_8859_1);
    }
    
    @Override
    default void TakeClient(TCPClient c) {
            if(Toolbox.DEBUG){
                //System.out.println(c+" recieved");
            }
            Thread localThread=new Thread(){
                @Override
                public void run() {
                    storeClient(c);
                    try{
                        while (c.isOpen()) {
                            
                            try {
                                String request=readHttpRequest(c.receiveStream());
                                if(request==null){
                                    break;
                                }
                                HttpRequest HTTP=HttpRequest.parse(request);
                                proccessHTTPRequest(HTTP, c);

                            } catch (IOException e) {
                                
                                e.printStackTrace();
                            }
                            
                                
                                
                        }
                        //System.out.println(c+" closed connection");
                    }finally{
                        try{
                            if(Toolbox.DEBUG)
                            System.out.println("disconnecting "+c);
                            if(c.isOpen())
                            c.close();
                            removeClient(c);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            localThread.start();
        
    }
    //so that it can store it if it wants, by ovveriding this, rather than the more complex TakeClient
    public default void storeClient(TCPClient c){

    }
    public default void removeClient(TCPClient c){

    }
    public default void proccessHTTPRequest(HttpRequest http){
        proccessHTTPRequest(http, null);
    }
    public default void proccessHTTPRequest(HttpRequest http,TCPClient c){
        switch (http.getMethod()) {
            case "GET":
                GET(http, c);
                break;
            case "HEAD":
                HEAD(http, c);
                break;
            case "POST":
                POST(http, c);
                break;
            case "PUT":
                PUT(http, c);
                break;
            case "DELETE":
                DELETE(http, c);
                break;
            case "CONNECT":
                CONNECT(http, c);
                break;
            case "OPTIONS":
                OPTIONS(http, c);
                break;
            case "TRACE":
                TRACE(http, c);
                break;
            case "PATCH":
                PATCH(http, c);
                break;
            default:
                unknownRequest(http, c);
                break;
        }
    }
    public default void GET(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void HEAD(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void POST(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void PUT(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void DELETE(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void CONNECT(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void OPTIONS(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void TRACE(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void PATCH(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public default void unknownRequest(HttpRequest http, TCPClient client){
        try {
            if(client!=null){
                client.sendPacket(ERROR501);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    
}
