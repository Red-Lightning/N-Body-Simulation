package nbody.simulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class NBodySimulation extends ApplicationAdapter {

	private Universe universe;
	private final float LENGTH = 1250f;
	FitViewport viewport;
	ShapeRenderer renderer;
	private final boolean TEST_STATE = false;
	final int NUM = 2500;

	@Override
	public void create () {
		universe = new Universe(LENGTH);
		viewport = new FitViewport(LENGTH, LENGTH);
		renderer = new ShapeRenderer();

		/* add test bodies */
		if(TEST_STATE) {
			/*universe.add(new Body(new Vector2(78, 78), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(57, 31), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(50, 50), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(45, 45), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(55, 55), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(55, 45), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(45, 55), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(50, 55), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(15, 85), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(5, 85), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(20, 25), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(21, 26), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(38, 67), MathUtils.random(2, 15)));
			universe.add(new Body(new Vector2(3, 20), MathUtils.random(2, 15)));*/
			universe.add(new Body(new Vector2(LENGTH / 2.0f, LENGTH / 2.0f), new Vector2(0, 0), 1e6f));
			universe.add(new Body(new Vector2(10 + LENGTH / 2.0f, LENGTH / 2.0f), new Vector2(0, 1.0f), 1e4f));
			universe.add(new Body(new Vector2(20 + LENGTH / 2.0f, LENGTH / 2.0f), new Vector2(0, 1.5f), 4e4f));
			universe.add(new Body(new Vector2(5.0f + LENGTH / 2.0f, LENGTH / 2.0f), new Vector2(0, 1.2f), 3e4f));
			universe.add(new Body(new Vector2(30 + LENGTH / 2.0f, LENGTH / 2.0f), new Vector2(0, 1.3f), 15e4f));
			universe.add(new Body(new Vector2(40 + LENGTH / 2.0f, LENGTH / 2.0f), new Vector2(0, 0.9f), 10e4f));
			//universe.add(new Body(new Vector2(5 + 3.0f*LENGTH/4.0f, 5 + LENGTH/2.0f), new Vector2(0, 0), 0.01f));
		}

		if(!TEST_STATE) {
			//universe.addUniverse(NUM);
			universe.addUniverse2(NUM);
		}

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		viewport.apply();
		renderer.setProjectionMatrix(viewport.getCamera().combined);

		/* Update all bodies in universe */
		universe.update();

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		universe.render(renderer);
		renderer.end();


		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(new Color(0.25f, 0.25f, 0.25f, 0.5f));
		universe.renderBoundingBoxes(renderer);
		renderer.end();

	}

	@Override
	public void resize (int width, int height) {
		viewport.update(width, height, true);
	}
}
