package club.faxhax.client.api.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.*;

public class RenderUtils {

    public static void drawFilledBox(BlockPos blockPos, float r, float g, float b, float a) {
        drawFilledBox(new Box(blockPos), r, g, b, a);
    }

    public static void drawFilledBox(Box box, float r, float g, float b, float a) {
        drawFilledBox(box, r, g, b, a, 2.5f);
    }

    public static void drawFilledBox(Box box, float r, float g, float b, float a, float width) {
        setup();

        MatrixStack matrix = matrixFromOrigin();

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();

        // Fill
        buffer.begin(7, VertexFormats.POSITION_COLOR);
        VertexManager.vertexBoxQuads(matrix, buffer, box, r, g, b, a);
        tessellator.draw();

        // Outline
        RenderSystem.lineWidth(width);

        buffer.begin(3, VertexFormats.POSITION_COLOR);
        VertexManager.vertexBoxLines(matrix, buffer, box, r, g, b, a);
        tessellator.draw();

        cleanup();
    }

    public static void drawFill(BlockPos blockPos, float r, float g, float b, float a) {
        drawFill(new Box(blockPos), r, g, b, a);
    }

    public static void drawFill(Box box, float r, float g, float b, float a) {
        setup();

        MatrixStack matrix = matrixFromOrigin();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Fill
        buffer.begin(7, VertexFormats.POSITION_COLOR);
        VertexManager.vertexBoxQuads(matrix, buffer, box, r, g, b, a);
        tessellator.draw();

        cleanup();
    }

    public static void drawOutline(BlockPos blockPos, FaxColor color, float width) {
        drawOutline(new Box(blockPos), color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), width);
    }

    public static void drawOutline(Box box, float r, float g, float b, float a, float width) {
        setup();

        MatrixStack matrix = matrixFromOrigin();

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();

        // Outline
        RenderSystem.lineWidth(width);

        buffer.begin(3, VertexFormats.POSITION_COLOR);
        VertexManager.vertexBoxLines(matrix, buffer, box, r, g, b, a);
        tessellator.draw();

        cleanup();
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float width) {
        setup();

        MatrixStack matrix = matrixFromOrigin();

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();

        // Line
        RenderSystem.lineWidth(width);

        buffer.begin(3, VertexFormats.POSITION_COLOR);
        VertexManager.vertexLine(
                matrix,
                buffer,
                (float) x1,
                (float) y1,
                (float) z1,
                (float) x2,
                (float) y2,
                (float) z2,
                r,
                g,
                b,
                1f
        );
        tessellator.draw();

        cleanup();
    }

    public static MatrixStack matrixFromOrigin() {
        MatrixStack matrix = new MatrixStack();

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        matrix.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));

        matrix.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        return matrix;
    }

    public static void setup() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
    }

    public static void cleanup() {
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static class VertexManager {

        public static void vertexBoxQuads(MatrixStack matrix, VertexConsumer vertexConsumer, Box box, float r, float g, float b, float a) {
            float x1 = (float) box.minX;
            float y1 = (float) box.minY;
            float z1 = (float) box.minZ;
            float x2 = (float) box.maxX;
            float y2 = (float) box.maxY;
            float z2 = (float) box.maxZ;

            // Bottom
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y1, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y1, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y1, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y1, z2).color(r, g, b, a).next();

            // X-
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y1, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y2, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y2, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y1, z1).color(r, g, b, a).next();

            // X+
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y1, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y2, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y2, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y1, z2).color(r, g, b, a).next();

            // Z-
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y1, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y2, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y2, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y1, z1).color(r, g, b, a).next();

            // Z+
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y1, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y2, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y2, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y1, z2).color(r, g, b, a).next();

            // Top
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y2, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y2, z2).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x2, y2, z1).color(r, g, b, a).next();
            vertexConsumer.vertex(matrix.peek().getModel(), x1, y2, z1).color(r, g, b, a).next();
        }

        public static void vertexBoxLines(MatrixStack matrix, VertexConsumer vertexConsumer, Box box, float r, float g, float b, float a) {
            float x1 = (float) box.minX;
            float y1 = (float) box.minY;
            float z1 = (float) box.minZ;
            float x2 = (float) box.maxX;
            float y2 = (float) box.maxY;
            float z2 = (float) box.maxZ;

            // Bottom
            vertexLine(matrix, vertexConsumer, x1, y1, z1, x2, y1, z1, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x2, y1, z1, x2, y1, z2, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x2, y1, z2, x1, y1, z2, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x1, y1, z2, x1, y1, z1, r, g, b, a);

            // Pillars
            vertexLine(matrix, vertexConsumer, x1, y1, z1, x1, y2, z1, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x2, y1, z1, x2, y2, z1, r, g, b, 0f);
            vertexLine(matrix, vertexConsumer, x2, y1, z1, x2, y2, z1, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x2, y1, z2, x2, y2, z2, r, g, b, 0f);
            vertexLine(matrix, vertexConsumer, x2, y1, z2, x2, y2, z2, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x1, y1, z2, x1, y2, z2, r, g, b, 0f);
            vertexLine(matrix, vertexConsumer, x1, y1, z2, x1, y2, z2, r, g, b, a);

            // Top
            vertexLine(matrix, vertexConsumer, x1, y2, z1, x2, y2, z1, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x2, y2, z1, x2, y2, z2, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x2, y2, z2, x1, y2, z2, r, g, b, a);
            vertexLine(matrix, vertexConsumer, x1, y2, z2, x1, y2, z1, r, g, b, a);
        }

        public static void vertexLine(MatrixStack matrix, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
            Matrix4f model = matrix.peek().getModel();
            Matrix3f normal = matrix.peek().getNormal();

            Vector3f normalVec = getNormal(normal, x1, y1, z1, x2, y2, z2);

            vertexConsumer.vertex(model, x1, y1, z1).color(r, g, b, a).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
            vertexConsumer.vertex(model, x2, y2, z2).color(r, g, b, a).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
        }

        public static Vector3f getNormal(Matrix3f normal, float x1, float y1, float z1, float x2, float y2, float z2) {
            float xNormal = x2 - x1;
            float yNormal = y2 - y1;
            float zNormal = z2 - z1;
            float normalSqrt = MathHelper.sqrt(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);

            return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
        }
    }
}
