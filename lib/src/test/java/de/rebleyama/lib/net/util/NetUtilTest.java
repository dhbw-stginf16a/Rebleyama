package de.rebleyama.lib.net.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NetUtilTest{

    @Test
    public void TestByteArrayChunk(){
        byte[] data = new byte[]{1,2,3,4,5,6,7,8,9,10};

        byte[][] result = NetUtil.chunkArray(data, 3);
        
        assertArrayEquals(new byte[]{1,2,3}, result[0]);
        assertArrayEquals(new byte[]{4,5,6}, result[1]);
        assertArrayEquals(new byte[]{7,8,9}, result[2]);
        assertArrayEquals(new byte[]{10}, result[3]);
    }

    @Test
    public void TestByteArrayMerge(){
        byte[] expected = new byte[]{1,2,3,4,5,6,7,8,9,10};
        byte[][] data = new byte[][]{{1,2,3},{4,5,6},{7,8,9},{10}};

        byte[]result = NetUtil.mergeArray(data);
        
        assertArrayEquals(expected, result);
    }
}