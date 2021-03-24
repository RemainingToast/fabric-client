package me.remainingtoast.faxhax.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtil {

    private static final String VERTEX_SHADER = "#version 130\n" +
            "\n" +
            "void main() {\n" +
            "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
            "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
            "}";
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final int program;
    private final long startTime;

    /**
     * @param fragment The fragment shader code (not the resource location)
     */
    public ShaderUtil(@NotNull String fragment) {
        program = glCreateProgram();
        startTime = System.currentTimeMillis();
        initShader(fragment);
    }

    private void initShader(@NotNull String frag) {
        int vertex = glCreateShader(GL_VERTEX_SHADER), fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertex, VERTEX_SHADER);
        glShaderSource(fragment, frag);
        glValidateProgram(program);
        glCompileShader(vertex);
        glCompileShader(fragment);
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);
    }

    public void renderFirst() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(program);
    }

    public void renderSecond() {
        Window win = mc.getWindow();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBegin(GL_QUADS);
        glTexCoord2d(0, 1);
        glVertex2d(0, 0);
        glTexCoord2d(0, 0);
        glVertex2d(0, win.getScaledHeight());
        glTexCoord2d(1, 0);
        glVertex2d(win.getScaledWidth(), win.getScaledHeight());
        glTexCoord2d(1, 1);
        glVertex2d(win.getScaledWidth(), 0);
        glEnd();
        glUseProgram(0);
    }

    public void bind() {
        glUseProgram(program);
    }

    public int getProgram() {
        return program;
    }

    @NotNull
    public ShaderUtil uniform1i(@NotNull String loc, int i) {
        glUniform1i(glGetUniformLocation(program, loc), i);
        return this;
    }

    @NotNull
    public ShaderUtil uniform2i(@NotNull String loc, int i, int i1) {
        glUniform2i(glGetUniformLocation(program, loc), i, i1);
        return this;
    }

    @NotNull
    public ShaderUtil uniform3i(@NotNull String loc, int i, int i1, int i2) {
        glUniform3i(glGetUniformLocation(program, loc), i, i1, i2);
        return this;
    }

    @NotNull
    public ShaderUtil uniform4i(@NotNull String loc, int i, int i1, int i2, int i3) {
        glUniform4i(glGetUniformLocation(program, loc), i, i1, i2, i3);
        return this;
    }

    @NotNull
    public ShaderUtil uniform1f(@NotNull String loc, float f) {
        glUniform1f(glGetUniformLocation(program, loc), f);
        return this;
    }

    @NotNull
    public ShaderUtil uniform2f(@NotNull String loc, float f, float f1) {
        glUniform2f(glGetUniformLocation(program, loc), f, f1);
        return this;
    }

    @NotNull
    public ShaderUtil uniform3f(@NotNull String loc, float f, float f1, float f2) {
        glUniform3f(glGetUniformLocation(program, loc), f, f1, f2);
        return this;
    }

    @NotNull
    public ShaderUtil uniform4f(@NotNull String loc, float f, float f1, float f2, float f3) {
        glUniform4f(glGetUniformLocation(program, loc), f, f1, f2, f3);
        return this;
    }

    @NotNull
    public ShaderUtil uniform1b(@NotNull String loc, boolean b) {
        glUniform1i(glGetUniformLocation(program, loc), b ? 1 : 0);
        return this;
    }

    public void addDefaultUniforms() {
        glUniform2f(glGetUniformLocation(program, "resolution"), mc.getWindow().getWidth(), mc.getWindow().getHeight());
        float time = (System.currentTimeMillis() - this.startTime) / 1000f;
        glUniform1f(glGetUniformLocation(program, "time"), time);
    }

    public static String getFragmentShaderString(String shaderName){
        StringBuilder output = new StringBuilder();
        try {
            InputStream input = ShaderUtil.class.getResourceAsStream("/assets/faxhax/shaders/" + shaderName + ".frag");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) { e.printStackTrace(); }
        return output.toString();
    }
}
