package toolbox;


import toolbox.Expression.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class Toolbox{
    public static final String VERSION = "1.0.2";
    @Override
    public String toString() {
        return VERSION;
    }
    private Toolbox() {
        System.out.println("??? how did you even get here ??? easter egg found!");
    }
    public static final boolean DEBUG=false;
    //public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception{
        System.out.println("running main interface of toolbox");
        System.out.println("this is not intended to be ran directly, but it can (and is) used for testing purposes");
        System.out.println("Toolbox version: " + VERSION);
        String thisFilesDirectory=System.getProperty("user.dir") + "/toolbox/";
        //System.out.println("current directory? (true/false): " + thisFilesDirectory);
        
        ByteFile LogFile = new ByteFile(thisFilesDirectory + "/changeLog.txt");
        System.out.println("reading change log...");
        System.out.println(bytesToString(LogFile.toBytes()));


        System.out.println("running tests...");
        

    }
    
    public static boolean[] byteToBooleans(byte b) {
        boolean[] bools = new boolean[8];
        for (int i = 0; i < 8; i++) {
            bools[7 - i] = (b & (1 << i)) != 0;
        }
        return bools;
        
    }
    public static byte booleansToByte(boolean ... bools) throws IllegalArgumentException {
        if (bools.length != 8) {
            throw new IllegalArgumentException("Boolean array must have length 8.");
        }
        byte b = 0;
        for (int i = 0; i < 8; i++) {
            if (bools[7 - i]) {
                b |= (1 << i);
            }
        }
        return b;
    }

    public static short bytesToShort(byte high, byte low) {
        return (short) ((high << 8) | (low & 0xFF));
    }
    public static int bytesToInt(byte b1, byte b2, byte b3, byte b4) {
        return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }
    public static long bytesToLong(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return ((long)(b1 & 0xFF) << 56) | ((long)(b2 & 0xFF) << 48) | ((long)(b3 & 0xFF) << 40) | ((long)(b4 & 0xFF) << 32) |
               ((long)(b5 & 0xFF) << 24) | ((long)(b6 & 0xFF) << 16) | ((long)(b7 & 0xFF) << 8) | (b8 & 0xFF);
    }
    public static byte[] shortToBytes(short value) {
        return new byte[] {
            (byte) ((value >> 8) & 0xFF),
            (byte) (value & 0xFF)
        };
    }
    public static byte[] intToBytes(int value) {
        return new byte[] {
            (byte) ((value >> 24) & 0xFF),
            (byte) ((value >> 16) & 0xFF),
            (byte) ((value >> 8) & 0xFF),
            (byte) (value & 0xFF)
        };
    }
        public static byte[] longToBytes(long value) {
            return new byte[] {
                (byte) ((value >> 56) & 0xFF),
                (byte) ((value >> 48) & 0xFF),
                (byte) ((value >> 40) & 0xFF),
                (byte) ((value >> 32) & 0xFF),
                (byte) ((value >> 24) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF)
            };
        }
        public static byte[] stringToBytes(String str) {
            return str.getBytes();
        }
        public static String bytesToString(byte[] bytes) {
            return new String(bytes);
        }
        public static byte[] booleanArrayToBytes(boolean... bools) throws IllegalArgumentException {
            if (bools.length % 8 != 0) {
                throw new IllegalArgumentException("Boolean array length must be a multiple of 8.");
            }
            byte[] bytes = new byte[bools.length / 8];
            for (int i = 0; i < bools.length; i++) {
                if (bools[i]) {
                    bytes[i / 8] |= (1 << (7 - (i % 8)));
                }
            }
            return bytes;
        }



    public static byte[] concatenateByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] arr : arrays) {
            totalLength += arr.length;
        }
        byte[] result = new byte[totalLength];
        int pos = 0;
        for (byte[] arr : arrays) {
            System.arraycopy(arr, 0, result, pos, arr.length);
            pos += arr.length;
        }
        return result;
    }


    public static Process executeJava(
            String className,
            String[] args,
            Pointer<InputStream> javaOutputs,
            Pointer<InputStream> errors,
            Pointer<OutputStream> javaInputs,
            String... otherParams
    ) throws IOException {

        List<String> command = new ArrayList<>();

        // Current JVM executable
        String javaExe =
                System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        command.add(javaExe);
        
        // JVM arguments
        Collections.addAll(command, otherParams);
        boolean b=false;
        {
            for(String s:otherParams){
                if(s.startsWith("-cp")){
                    b=true;
                    break;
                }
            }
        }
        
        if(!b){
        // Current classpath
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        if(DEBUG){
            System.out.println("DEBUG:cp:"+System.getProperty("java.class.path"));
        }
        }
        // Main class
        command.add(className);

        // Program arguments
        Collections.addAll(command, args);
        if(DEBUG){
            System.out.println("DEBUG:");
            for(String s:command){
                System.out.println(s);
            }
        }

        Process process = new ProcessBuilder(command).start();

        if (javaOutputs != null) {
            javaOutputs.value = process.getInputStream();
        }

        if (errors != null) {
            errors.value = process.getErrorStream();
        }

        if (javaInputs != null) {
            javaInputs.value = process.getOutputStream();
        }

        return process;
    }
}
