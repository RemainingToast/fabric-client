package me.remainingtoast.faxhax.api.util;

import me.remainingtoast.faxhax.FaxHax;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtil {

    private static final int shaderProgram = glCreateProgram();

    private static final int vertexShader = glCreateShader(GL_VERTEX_SHADER);

    private static final int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

    private static boolean compiled = false;

    public static void compileShader(String shader){
        if(!compiled) {
            StringBuilder fragmentShaderSource = new StringBuilder();
            StringBuilder vertexShaderSource = new StringBuilder();
            try {
                InputStream input = ShaderUtil.class.getResourceAsStream("/assets/faxhax/shaders/" + shader + ".vert");
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    vertexShaderSource.append(line).append("\n");
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            try {
                InputStream input = ShaderUtil.class.getResourceAsStream("/assets/faxhax/shaders/" + shader + ".frag");
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    fragmentShaderSource.append(line).append("\n");
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            glShaderSource(vertexShader, vertexShaderSource);
            glCompileShader(vertexShader);
            if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
                FaxHax.LOGGER.fatal("Vertex shader failed to compile!");
            }
            glShaderSource(fragmentShader, fragmentShaderSource);
            glCompileShader(fragmentShader);
            if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
                FaxHax.LOGGER.fatal("Fragment shader failed to compile!");
            }
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glValidateProgram(shaderProgram);
            glUseProgram(shaderProgram);
            compiled = true;
        }
    }

    public static void render(){
        compileShader("mandelbrot");
        glUseProgram(shaderProgram);
    }
}
