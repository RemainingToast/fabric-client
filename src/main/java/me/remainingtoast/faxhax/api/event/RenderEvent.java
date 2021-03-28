package me.remainingtoast.faxhax.api.event;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent extends Event {

    private final Stage stage;

    public enum Stage {
        WORLD,
        SCREEN
    }

    public RenderEvent(Stage stage){
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public static class World extends RenderEvent {

        MatrixStack matrices;
        float delta;
        Camera camera;

        public World(MatrixStack matrices, float delta, Camera camera) {
            super(Stage.WORLD);
            this.matrices = matrices;
            this.delta = delta;
            this.camera = camera;
        }

        public MatrixStack getMatrices() {
            return matrices;
        }

        public float getDelta() {
            return delta;
        }

        public Camera getCamera() {
            return camera;
        }
    }

    public static class Screen extends RenderEvent {
        public Screen() {
            super(Stage.SCREEN);
        }
    }

}
