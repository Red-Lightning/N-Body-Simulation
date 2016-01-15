package nbody.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class Body {
    float rx, ry;
    float m;
    float fx, fy;
    float vx, vy;
    final float G = 6.67e-5f;

    public Body(Vector2 pos, float mass) {
        rx = pos.x;
        ry = pos.y;
        this.m = mass;
        fx = 0; fy = 0;
        vx = 0; vy = 0;
    }

    public Body(Vector2 pos, Vector2 vel, float mass) {
        rx = pos.x; ry = pos.y;
        this.m = mass;
        fx = 0; fy = 0;
        vx = vel.x; vy = vel.y;
    }

    public void updatePos(float dt) {
        vx += dt * fx/m;
        vy += dt * fy/m;

        rx += dt * vx;
        ry += dt * vy;
    }

    public int belongsTo(float x, float y, float length) {
        if( rx < x + length/2.0f && ry >= y + length/2.0f ) { return 0; }
        if( rx >= x + length/2.0f && ry >= y + length/2.0f ) { return 1; }
        if( rx < x + length/2.0f && ry < y + length/2.0f ) { return 2; }
        if( rx >= length/2.0f && ry < y + length/2.0f ) { return 3; }
        return -1;
    }

    public void addForce(Body b) {
        float dist = dist(b);
        fx += G * this.m * b.m * (b.rx - this.rx) / (dist*dist*dist + 1000);
        fy += G * this.m * b.m * (b.ry - this.ry) / (dist*dist*dist + 1000);
    }

    public void resetForce() {
        fx = 0.0f;
        fy = 0.0f;
    }

    public float dist(Body b) {
        if(b == null) { Gdx.app.log("Error in body", "null pointer"); }
        return Vector2.dst(this.rx, this.ry, b.rx, b.ry);
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(new Color(
                /*0.6f/(1e6f - 1e4f)*m + 48.0f/99.0f.*/0.8f/(1e6f - 1e4f)*m + 19.0f/99.0f,
               0.4f,
                0.8f/(1e4f - 1e6f)*m + 99.8f/99.0f,
                1));
        renderer.circle(rx, ry, 2.0f/(1e6f - 1e4f)*m + 98.0f/99.0f);
    }
}
