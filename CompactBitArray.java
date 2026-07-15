package toolbox;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;

import java.util.NoSuchElementException;

public class CompactBitArray implements Iterable<Boolean>, Byteable, Cloneable{
    long[] bits;
    public final long length;
    static final long[] bitkeys=new long[]
    {0x8000000000000000L,
0x4000000000000000L,
0x2000000000000000L,
0x1000000000000000L,
0x800000000000000L,
0x400000000000000L,
0x200000000000000L,
0x100000000000000L,
0x80000000000000L,
0x40000000000000L,
0x20000000000000L,
0x10000000000000L,
0x8000000000000L,
0x4000000000000L,
0x2000000000000L,
0x1000000000000L,
0x800000000000L,
0x400000000000L,
0x200000000000L,
0x100000000000L,
0x80000000000L,
0x40000000000L,
0x20000000000L,
0x10000000000L,
0x8000000000L,
0x4000000000L,
0x2000000000L,
0x1000000000L,
0x800000000L,
0x400000000L,
0x200000000L,
0x100000000L,
0x80000000L,
0x40000000,
0x20000000,
0x10000000,
0x8000000,
0x4000000,
0x2000000,
0x1000000,
0x800000,
0x400000,
0x200000,
0x100000,
0x80000,
0x40000,
0x20000,
0x10000,
0x8000,
0x4000,
0x2000,
0x1000,
0x800,
0x400,
0x200,
0x100,
128,
64,
32,
16,
8,
4,
2,
1};
    
    //every individual bit
    public CompactBitArray(File source) throws IOException{
        this(ByteFile.readFile(source.getAbsolutePath()));
    }
    public CompactBitArray(long length){
        this.length=length;
        if((length/64)>Integer.MAX_VALUE){
            throw new ArrayIndexOutOfBoundsException("tried to create CompactBitArray with length greater than possible");
        }
        bits=new long[(int)(length/64)+1];
    }
    public CompactBitArray(boolean[] values){
        length=values.length;
        bits=new long[(values.length/64)+1];
        for(int i=0;i<values.length;i++){
            set(i,values[i]);
        }
    }
    public CompactBitArray(byte[] values){
        length=values.length*8;
        bits=new long[(values.length/8)+1];
        for(int i=0;i<values.length;i++){
            for(int j=0;j<8;j++){
                set((i*8)+j, ((values[i]>>j)&1)!=0);
            }
        }
    }
    public void set(long address,boolean value){
        if(address>=length||address<0){
            throw new ArrayIndexOutOfBoundsException("location "+address+" is out of bounds for length "+length);
        }
        int chunkAddress=(int)(address/64);
        int localAddress=(int)(address%64);
        if(get(address)!=value){
            bits[chunkAddress]^=bitkeys[localAddress];
        }
        
    }
    public boolean get(long address){
        int chunkAddress=(int)(address/64);
        int localAddress=(int)(address%64);
        if(Toolbox.DEBUG){
            
            //System.out.println("getting at "+chunkAddress+":"+localAddress);
        }
        return ((bits[chunkAddress]&bitkeys[localAddress])!=0);
    }
    
    @Override
    public Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
            long targ=length;
            long current=0;

            @Override
            public boolean hasNext() {
                return current<targ;
            }

            @Override
            public Boolean next() {
                if(current<targ){
                return get(current++);
                }else{
                    throw new NoSuchElementException("there are no bits left to obtain from this bit array");
                }
            }
            
        };
    }
    @Override
    public byte[] toBytes() {
        if((length/8)>Integer.MAX_VALUE){
            throw new ArrayIndexOutOfBoundsException("tried to convert CompactBitArray of length "+length+" into a byte array of length "+(length/8));
        }
        byte[] B=new byte[(int)(length/8)];
        if(Toolbox.DEBUG){
            for(int i=0;i<B.length;i++){
                B[i]=(byte)((bits[i/8]>>(8*(7-(i%8))))&0xFF);
                for(int j=0;j<8;j++){
                    System.out.print((get((i*8)+j)?1:0));
                }
                System.out.print(":"+B[i]);
                System.out.println();
            }
        }else{
            for(int i=0;i<B.length;i++){
                B[i]=(byte)((bits[i/8]>>(8*(i%8)))&0xFF);
            }
        }
        return B;
    }
    
    public CompactBitArray clone(){
        try {
            CompactBitArray cba=(CompactBitArray)super.clone();
            cba.bits=bits.clone();
            if(Toolbox.DEBUG){
                if(cba==this||cba.bits==this.bits||cba.length!=this.length){
                    System.out.println("Error cloning CBA");
                }
                for(int i=0;i<bits.length;i++){
                    if(cba.bits[i]!=this.bits[i]){
                        System.out.println("Error cloning longs in CBA");
                    }
                }
            }
            return cba;
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            return this;
        }
        
    }
    @Override
    public String toString() {
        StringBuilder b=new StringBuilder();
        for(long l=0;l<length;l++){
            if(l%64==0){
                b.append("\n");
            }else
            if(l%8==0){
                b.append(" ");
            }
            b.append((get(l)?1:0));
            
        }
        return b.toString();
    }

}