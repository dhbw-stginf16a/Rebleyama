package de.rebleyama.lib.net.util;
/**
 * 
 */
@SuppressWarnings("unchecked")
public final class NetUtil {

    /**
     * 
     */
    public static <T> T[][] chunkArray(T[] src, int chunkSize){
        int numOfChunk = (src.length + chunkSize -1) / 1;

        T[][] result =  (T[][]) new Object[numOfChunk][];

        for(int i = 0; i < numOfChunk; i++){
            int start = i*chunkSize;
            int length = Math.min(src.length - start, chunkSize);

            T[] temp = (T[]) new Object[length];
            System.arraycopy(src, start, temp, 0, length);
            result[i] = temp;
        }
        
        return result;
    }

    public static byte[][] chunkArray(byte[] src, int chunkSize){
        int numOfChunk = (src.length + chunkSize -1) / chunkSize;

        byte[][] result = new byte[numOfChunk][];

        for(int i = 0; i < numOfChunk; i++){
            int start = i*chunkSize;
            int length = Math.min(src.length - start, chunkSize);

            byte[] temp = new byte[length];
            System.arraycopy(src, start, temp, 0, length);
            result[i] = temp;
        }
        
        return result;
    }

    public static byte[] mergeArray(byte[][]src){
        int numOfBytes = 0;
        for(byte[] array: src){
            numOfBytes+= array.length;
        }

        byte[] result = new byte[numOfBytes];

        int start = 0;
        for(byte[] array: src){
            System.arraycopy(array, 0, result, start, array.length);
            start += array.length;
        }

        return result;
    }


}