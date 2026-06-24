package toolbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ByteFile implements Byteable{
    @Override
    public byte[] toBytes() {
        return data;
    }
    
    private byte[] data;
    private String filePath;
    public ByteFile(byte ... data) {
        this.data = data;
        
    }
    public byte[] getData() {
        return data;
    }
    public ByteFile(String filePath) throws IOException{
        this.data = readFile(filePath);
        this.filePath = filePath;
        
    }
    public void setData(byte[] data){
        this.data=data;
    }
    public void saveToFile(String filePath) throws IOException{
        this.filePath = filePath;
        saveBytesToFile(this.data, filePath);
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String path){
        filePath=path;
    }
    public void saveToFile() throws IllegalStateException, IOException {
        if (filePath == null) {
            throw new IllegalStateException("File path is not set.");
        }
        saveBytesToFile(this.data, filePath);
    }
    public ByteFile(byte[] ... data){
        int length = 0;
        for(byte[] arr : data) {
            length += arr.length;
        }
        this.data = new byte[length];
        int pos = 0;
        for(byte[] arr : data) {
            System.arraycopy(arr, 0, this.data, pos, arr.length);
            pos += arr.length;
        }
    }
    
    private ByteFile(){

    }

    public static ByteFile zip(String fileName, ByteFile... files) {
        ByteFile zipped = new ByteFile();
        
        zipped.setFilePath(fileName);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            for (int i = 0; i < files.length; i++) {
                ByteFile file = files[i];

                // Use original file name if available, otherwise fallback
                String entryName = file.getFilePath();
                if (entryName == null) {
                    entryName = "file" + i;
                } else {
                    // strip directories if present
                    entryName = Path.of(entryName).getFileName().toString();
                }

                ZipEntry entry = new ZipEntry(entryName);
                zos.putNextEntry(entry);

                byte[] data = file.getData();
                zos.write(data, 0, data.length);

                zos.closeEntry();
            }

            zos.close();

            zipped.setData(baos.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return zipped;
    }

    public static ByteFile[] unzip(ByteFile archive) {
        
        
        

        ArrayList<ByteFile> files = new ArrayList<>();

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(archive.getData());
            ZipInputStream zis = new ZipInputStream(bais);

            ZipEntry entry;
            byte[] buffer = new byte[1024];

            while ((entry = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }

                ByteFile file = new ByteFile();
                file.setData(baos.toByteArray());
                file.setFilePath(entry.getName()); // preserve name
                

                files.add(file);

                zis.closeEntry();
            }

            zis.close();

        } catch (IOException e) {
        throw new RuntimeException(e);
        }

        return files.toArray(new ByteFile[0]);
    }


    public static byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(Path.of(filePath));
    }
    public static void saveBytesToFile(byte[] data, String filePath) throws IOException {
        Files.write(Path.of(filePath), data);
    }
    public static byte[] compress(byte[] data){
        return compress(data, Deflater.DEFAULT_COMPRESSION);
    }
    public static byte[] compress(byte[] data, int level) {
        Deflater deflater = new Deflater(level);
        deflater.setInput(data);
        deflater.finish();

        byte[] buffer = new byte[1024];
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            output.write(buffer, 0, count);
        }

        return output.toByteArray();
    }
    public static byte[] decompress(byte[] compressed) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressed);

        byte[] buffer = new byte[1024];
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();

        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            output.write(buffer, 0, count);
        }

        return output.toByteArray();
    }
    
    
}
