package com.rdhouse.engine;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Created by rutgerd on 14-9-2016.
 */
public class Texture {

    private final int id;

    public Texture(int id) {
        this.id = id;
    }

    public Texture(String fileName) throws Exception{
        this(loadTexture(fileName));
    }

    public int getId() {
        return id;
    }

    public static int loadTexture(String fileName) throws Exception {
        // Load texture
        PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(fileName));

        // Load texture contents into byte buffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buffer.flip();

        // Create a new OpenGL Texture
        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size.
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        // Generate Mid Map
        glGenerateMipmap(GL_TEXTURE_2D);

        return textureId;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void cleanUp() {
        glDeleteTextures(id);
    }
}
