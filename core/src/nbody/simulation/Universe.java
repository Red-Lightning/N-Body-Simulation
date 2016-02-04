package nbody.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Universe {
    Array<Body> universe;
    BHTree bh;
    float length;

    public Universe(float length) {
        universe = new Array<Body>();
        bh = new BHTree(length);
        this.length = length;
    }

    public void add(Body b) {
        universe.add(b);
    }

    /* initialize a configuration of bodies */
    public void addUniverse(int NUM) {
        float x0 = 3*length/5.0f;
        float y0 = 3*length/5.0f;
        float R = 420;
        int numR = 20;
        float dr = R/numR;
        int NUM1 = (int) (3.0f * NUM / 4.0f);
        float dTheta = (int) (360.0f * numR / NUM1);

        for(int r = 0; r < R; r += dr) {
            for(int theta = 0; theta < 360; theta += dTheta) {
                float x = x0 + r * MathUtils.cosDeg(theta) + MathUtils.random(-length/250.0f, length/250.0f);
                float y = y0 + r * MathUtils.sinDeg(theta) + MathUtils.random(-length / 250.0f, length / 250.0f);
                add(new Body(new Vector2(x, y), MathUtils.random(1e4f, 1e6f)));
            }
        }

        x0 = length/4.0f;
        y0 = length/4.0f;
        R = 180;
        numR = 10;
        dr = R/numR;
        int NUM2 = (int) (NUM / 4.0f);
        dTheta = (int) (360.0f * numR / NUM2);

        for(int r = 0; r < R; r += dr) {
            for(int theta = 0; theta < 360; theta += dTheta) {
                float x = x0 + r * MathUtils.cosDeg(theta) + MathUtils.random(-length/250.0f, length/250.0f);
                float y = y0 + r * MathUtils.sinDeg(theta) + MathUtils.random(-length / 250.0f, length / 250.0f);
                add(new Body(new Vector2(x, y), MathUtils.random(1e4f, 1e6f)));
            }
        }

    }

    /* a different config of bodies */
    public void addUniverse2(int NUM) {
        float R = 1000;
        float x0 = 3*length/5.0f;
        float y0 = 3*length/5.0f;
        float angle = 6.0f*MathUtils.PI2;
        for(float t = 0; t < angle; t += angle/NUM) {
            float x = x0 + 2*R/angle * (float)Math.sqrt(t)/1.25f * MathUtils.cos(3*t) + MathUtils.random(-length/100.0f, length/100.0f);
            float y = y0 + 2*R/angle * (float)Math.sqrt(t) * MathUtils.sin(3*t) + MathUtils.random(-length/100.0f, length/100.0f);

            float vx = -MathUtils.sin(t) * MathUtils.random(3, 4);
            float vy = MathUtils.cos(t) * MathUtils.random(3, 4);

            add(new Body(new Vector2(x, y), new Vector2(vx, vy), MathUtils.random(1e4f, 1e6f)));
        }

        R = 500;
        x0 = length/3.0f;
        y0 = length/3.0f;
        angle = 6.0f*MathUtils.PI2;
        for(float t = 0; t < angle; t += angle/NUM) {
            float x = x0 + 2*R/angle * (float)Math.sqrt(t)/1.0f * MathUtils.cos(3*t) + MathUtils.random(-length/100.0f, length/100.0f);
            float y = y0 + 2*R/angle * (float)Math.sqrt(t) * MathUtils.sin(3*t) + MathUtils.random(-length/100.0f, length/100.0f);

            float vx = -MathUtils.sin(t) * MathUtils.random(3, 4);
            float vy = MathUtils.cos(t) * MathUtils.random(3, 4);

            add(new Body(new Vector2(x, y), new Vector2(vx, vy), MathUtils.random(1e4f, 1e6f)));
        }
    }

    /* add body to the BH tree */
    private void addToTree() {
        for(Body b : universe) {
            if(b.rx > 0 && b.rx < length && b.ry > 0 && b.ry < length) {
                b.resetForce();
                bh.insert(b);
            }
        }
    }

    private void updateForces() {
        for(Body b : universe) {
            bh.update(b);
        }
    }

    private void updatePositions() {
        float dt = Gdx.graphics.getDeltaTime();
        for(Body b : universe) {
            b.updatePos(dt);
        }
    }

    public void update() {
        addToTree();
        updateForces();
        updatePositions();
        bh.clearTree();
    }

    public void render(ShapeRenderer renderer) {
        for(Body body : universe) {
            body.render(renderer);
        }
    }

    public void renderBoundingBoxes(ShapeRenderer renderer) {
        bh.render(renderer);
        //bh.clearTree();
    }

}
