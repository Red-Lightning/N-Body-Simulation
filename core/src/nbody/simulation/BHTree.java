package nbody.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class BHTree {

    private Node root;
    private float LENGTH;
    private final float THETA = 0.5f;

    /* the node inner class */
    class Node {

        Node NW, NE, SW, SE;
        float x, y;
        float length;
        Body body;

        Node(float length, float x, float y) {
            NW = null; NE = null; SW = null; SE = null;
            body = null;
            this.x = x; this.y = y; this.length = length;
        }

        // if node doesn't have a body, simply insert b
        // if node is a leaf node, create a new node and place it in the appropriate quadtrant
        // update the current node bodies position and mass
        // if node is internal, recursively insert body into it and update current nodes body
        private void insert(Body b) {
            if( body == null ) {
                body = b;
            }
            else if( isExternal() ) {
                NW = new Node(length/2.0f, x, y + length/2.0f);
                NE = new Node(length/2.0f, x + length/2.0f, y + length/2.0f);
                SW = new Node(length/2.0f, x, y);
                SE = new Node(length/2.0f, x + length/2.0f, y);
                insertNew( body );
                insertNew( b );
                body = new Body(newPos(b), body.m + b.m);

            }
            else {
                insertNew(b);
                body = new Body(newPos(b), body.m + b.m);
            }
        }

        private Vector2 newPos(Body b) {
            return new Vector2((body.m*body.rx + b.m*b.rx)/(body.m + b.m),
                    (body.m*body.ry + b.m*b.ry)/(body.m + b.m));
        }

        private void insertNew(Body b) {
            switch(b.belongsTo(x, y, length)) {
                case 0:
                    NW.insert(b);
                    break;
                case 1:
                    NE.insert(b);
                    break;
                case 2:
                    SW.insert(b);
                    break;
                case 3:
                    SE.insert(b);
                    break;
                default:
                    Gdx.app.log("ERROR", "insertNew not working!");
            }
        }

        private void updateForce(Body b) {

            if(body == null) { Gdx.app.log("Error", "null pointer"); }

            if(isExternal() && !b.equals(body)) {
                if(body == null) { Gdx.app.log("Error 1", "null pointer"); return; }
                b.addForce(this.body);
            }
            else if( length/body.dist(b) < THETA ) {
                if(body == null) { Gdx.app.log("Error 2", "null pointer"); }
                b.addForce(this.body);
            }
            else {
                if(NW != null) {if(NW.body != null) NW.updateForce(b);}
                if(NE != null) {if(NE.body != null) NE.updateForce(b);}
                if(SW != null) {if(SW.body != null) SW.updateForce(b);}
                if(SE != null) {if(SE.body != null) SE.updateForce(b);}
            }
        }

        private boolean isExternal() {
            if(NW == null && NE == null && SE == null && SW == null) {
                //Gdx.app.log("isExternal", "True");
                return true;
            }
            return false;
        }

        /* render quadtree bounding boxes */
        private void render(ShapeRenderer renderer) {
            renderer.rect(x, y, length, length);
            if(NW != null) { NW.render(renderer); }
            if(NE != null) { NE.render(renderer); }
            if(SW != null) { SW.render(renderer); }
            if(SE != null) { SE.render(renderer); }
        }
    }

    public BHTree(float LENGTH) {
        this.LENGTH = LENGTH;
        root = new Node(LENGTH, 0, 0);
    }

    public void clearTree() {
        root = new Node(LENGTH, 0, 0);
    }

    public void update(Body b) { root.updateForce(b); }

    public void insert(Body b) {
        root.insert(b);
    }

    public void render(ShapeRenderer renderer) {
        root.render(renderer);
    }
}
