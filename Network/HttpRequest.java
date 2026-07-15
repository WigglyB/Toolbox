package toolbox.Network;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HttpRequest {
    private String method;
    private String path;
    private String version;
    

    private static Map<String,String> headers=new HashMap<>();

    private byte[] body;

    public static HttpRequest parse(String request) {
        HttpRequest http = new HttpRequest();

        // Find where the headers end
        int split = request.indexOf("\r\n\r\n");
        if (split == -1)
            throw new IllegalArgumentException("Missing header terminator.");

        String headerPart = request.substring(0, split);
        String bodyPart = request.substring(split + 4);

        // Break headers into lines
        String[] lines = headerPart.split("\r\n");

        if (lines.length == 0)
            throw new IllegalArgumentException("Missing request line.");

        // Parse request line
            String[] first = lines[0].split(" ", 3);

        if (first.length != 3)
            throw new IllegalArgumentException("Malformed request line.");

        http.method = first[0];
        http.path = first[1];
        http.version = first[2];

        // Parse headers
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];

            int colon = line.indexOf(':');
            if (colon == -1)
                continue; // or throw an exception

            String key = line.substring(0, colon).trim();
            String value = line.substring(colon + 1).trim();

            http.headers.put(key, value);
        }

        http.body = bodyPart.getBytes(StandardCharsets.ISO_8859_1);

        return http;
    }

    public String getMethod(){
        return method;
    }
    public String getPath(){
        return path;
    }
    public String getVersion(){
        return version;
    }
    public String getHeaderValue(String property){
        return headers.get(property);
    }
    public byte[] getBody(){
        return body.clone();
    }
    public byte[] getBodyDirect(){
        return body;
    }
}
