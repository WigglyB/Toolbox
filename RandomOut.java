package toolbox;


import java.io.File;

import java.io.IOException;
import java.util.Random;
import java.util.random.RandomGenerator;


public class RandomOut implements RandomGenerator{
    static CompactBitArray defaultSource=new CompactBitArray(6400);
    static{
        Random rnd=new Random();
        for(int i=0;i<6400;i++){
            defaultSource.set(i, rnd.nextBoolean());
        }
    }
    public static void setDefaultSource(CompactBitArray cba){
        defaultSource=cba.clone();
        
    }
    CompactBitArray source;
    long address=0;
    long value=0;
    long offset=1;
    public RandomOut(File randFile) throws IOException{
        this(new CompactBitArray(randFile));
    }
    public RandomOut(File randFile,long seed) throws IOException{
        this(new CompactBitArray(randFile),seed);
    }
    public RandomOut(){
        source=defaultSource.clone();
        if(Toolbox.DEBUG){
            //System.out.println("default:\n"+defaultSource);
            //System.out.println("primary:\n"+source);
        }
        address=(System.nanoTime()^(System.nanoTime()>>>32))&Long.MAX_VALUE;
        
    }

    public RandomOut(CompactBitArray cba,long seed){
        source=cba.clone();
        address=seed;
        nextLong();
        
    }
    public RandomOut(CompactBitArray compactBitArray) {
        this(compactBitArray,(System.nanoTime()^(System.nanoTime()>>>32))&Long.MAX_VALUE);
    }
    @Override
    public long nextLong() {
        if(Toolbox.DEBUG){
            //System.out.println("randomizing...");  
        }
        if(source.length<address+64){
            for(long i=0;i<source.length;i++){
                source.set(i, source.get(Math.floorMod((i+offset),source.length))!=source.get(i));
            }
            address=(address+64)%source.length;
            offset=nextLong();
            if(offset==0){
                offset=1;
            }
        }
        //*
        else
        if(Toolbox.DEBUG){
            //System.out.println("retrieving...");
            //System.out.print(address+":");
        }
        //*/
        value=0;
        for(int i=0;i<64;i++){
            /*
            if(Toolbox.DEBUG){
                System.out.print(source.get(address)?1:0);
            }
            //*/
            value=((value<<1)|(source.get(address++)?1:0));

        }
        /*
        if(Toolbox.DEBUG){
            System.out.print(":"+value+"\n");
        }
        //*/
        
        
        return value;
    }
    
}
