package toolbox;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CompactBitArray implements Iterable<Boolean>{
    long[] bits;
    public final long length;
    static final long[] bitkeys=new long[]{1,2,4,8,16,32,64,128,0x100,0x200,0x400,0x800,0x1000,0x2000,0x4000,0x8000,0x10000,0x20000,0x40000,0x80000,0x100000,0x200000,0x400000,0x800000,0x1000000,0x2000000,0x4000000,0x8000000,0x10000000,0x20000000,0x40000000,0x80000000L,0x100000000L,0x200000000L,0x400000000L,0x800000000L,0x1000000000L,0x2000000000L,0x4000000000L,0x8000000000L,0x10000000000L,0x20000000000L,0x40000000000L,0x80000000000L,0x100000000000L,0x200000000000L,0x400000000000L,0x800000000000L,0x1000000000000L,0x2000000000000L,0x4000000000000L,0x8000000000000L,0x10000000000000L,0x20000000000000L,0x40000000000000L,0x80000000000000L,0x100000000000000L,0x200000000000000L,0x400000000000000L,0x800000000000000L,0x1000000000000000L,0x2000000000000000L,0x4000000000000000L,0x8000000000000000L};
    //every individual bit
    
    public CompactBitArray(long length){
        this.length=length;
        if((length/64)>Integer.MAX_VALUE){
            throw new ArrayIndexOutOfBoundsException("tried to create CompactBitArray with length greater than possible");
        }
        bits=new long[(int)(length/64)];
    }
    public CompactBitArray(boolean[] values){
        length=values.length;
        bits=new long[(values.length/64)+1];
        for(int i=0;i<values.length;i++){
            set(i,values[i]);
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
    
    

}