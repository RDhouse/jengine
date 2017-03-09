package com.rdhouse.engine.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> readAllLines(String fileName) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
    }
        return list;
    }

    public static FloatBuffer createFloatBuffer(float[] src) {
        FloatBuffer result = ByteBuffer.allocateDirect(src.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.put(src).flip();
        return result;
    }

    public static IntBuffer createIntBuffer(int[] src) {
        IntBuffer result = ByteBuffer.allocateDirect(src.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(src).flip();
        return result;
    }
}
