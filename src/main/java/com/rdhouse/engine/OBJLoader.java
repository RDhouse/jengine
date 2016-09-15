package com.rdhouse.engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rutgerd on 15-9-2016.
 */
public class OBJLoader {

    private OBJLoader() {

    }

    public static Mesh loadMesh(String fileName) throws Exception {
        List<String> lines = Utils.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s");
            switch (tokens[0]) {
                case "v":
                    // Geometric vertex
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                    break;
                case "vt":
                    // Texture Coord
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(vec2f);
                    break;
                case "vn":
                    // Normals
                    Vector3f vec3fn = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(vec3fn);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;
                default:
                    // ignore the line
                    break;
            }
        }
        return reorderLists(vertices, textures, normals, faces);
    }

    private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> textCoordList, List<Vector3f> normalList, List<Face> facesList) {
        List<Integer> indices = new ArrayList<>();

        // Create position array in the order is had been declared
        float[] posArr = new float[posList.size() * 3];
        int i = 0;
        for (Vector3f pos : posList) {
            posArr[i * 3] = pos.x;
            posArr[i * 3 + 1] = pos.y;
            posArr[i * 3 + 2] = pos.z;
            i++;
        }
        float[] textCoordArr = new float[posList.size() * 2];
        float[] normArr = new float[posList.size() * 3];

        for (Face face : facesList) {
            IndexGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for (IndexGroup indValue : faceVertexIndices) {
                processFaceVertex(indValue, textCoordList, normalList, indices, textCoordArr, normArr);
            }
        }
        int[] indicesArr = new int[indices.size()];
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        Mesh mesh = new Mesh(posArr, textCoordArr, normArr, indicesArr);
        return mesh;
    }

    private static void processFaceVertex(IndexGroup indices, List<Vector2f> textCoordList, List<Vector3f> normList, List<Integer> indicesList, float[] textCoordArr, float[] normArr) {
        // Set index for vertex coords
        int posIndex = indices.indexPos;
        indicesList.add(posIndex);

        // Reorder texture coords
        if (indices.indexTextCoord >= 0) {
            Vector2f textCoord = textCoordList.get(indices.indexTextCoord);
            textCoordArr[posIndex * 2] = textCoord.x;
            textCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
        }
        if (indices.indexVecNormal >= 0) {
            // Reorder vectornormals
            Vector3f vecNorm = normList.get(indices.indexVecNormal);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

    protected static class IndexGroup {

        public static final int NO_VALUE = 0;

        public int indexPos;
        public int indexTextCoord;
        public int indexVecNormal;

        public IndexGroup() {
            indexPos = NO_VALUE;
            indexTextCoord = NO_VALUE;
            indexVecNormal = NO_VALUE;
        }

    }

    protected static class Face {

        private IndexGroup[] indexGroups = new IndexGroup[3];

        public Face(String v1, String v2, String v3) {
            indexGroups = new IndexGroup[3];
            // Parse
            indexGroups[0] = parseLine(v1);
            indexGroups[1] = parseLine(v2);
            indexGroups[2] = parseLine(v3);
        }

        private IndexGroup parseLine(String line) {
            IndexGroup indexGroup = new IndexGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            indexGroup.indexPos = Integer.parseInt(lineTokens[0]) - 1;
            if (length > 1) {
                // It can be empty if the obj does not define text coords
                String textCoord = lineTokens[1];
                indexGroup.indexTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IndexGroup.NO_VALUE;
                if (length > 2) {
                    indexGroup.indexVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }
            return indexGroup;
        }

        public IndexGroup[] getFaceVertexIndices() {
            return indexGroups;
        }

    }


}
