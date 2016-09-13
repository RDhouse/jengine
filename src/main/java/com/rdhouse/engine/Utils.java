package com.rdhouse.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by rutgerd on 13-9-2016.
 */
public class Utils {

    private Utils() {

    }

    public static String loadResource(String resource) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(resource))) {
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line + '\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static FloatBuffer createFloatBuffer(float[] src) {
        FloatBuffer result = ByteBuffer.allocateDirect(src.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.put(src).flip();
        return result;
    }
}
