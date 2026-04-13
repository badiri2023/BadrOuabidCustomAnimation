package com.badrouabid.customanimation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {

    private static final float MIN_VIRTUAL_WIDTH = 800;
    private static final float MIN_VIRTUAL_HEIGHT = 480;

    Animation<TextureRegion> slimeCaminar;
    Texture slimeSheet;
    float stateTime;
    boolean mirandoIzquierda = false;
    boolean moviendose = false;
    Texture textureFondo;
    TextureRegion regionFondo;
    float posicionMapaX = 0;
    float posicionMapaY = 0;
    SpriteBatch spriteBatch;

    OrthographicCamera camera;
    Viewport viewport;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(MIN_VIRTUAL_WIDTH, MIN_VIRTUAL_HEIGHT, camera);

        textureFondo = new Texture(Gdx.files.internal("forest-map.jpg"));
        textureFondo.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        regionFondo = new TextureRegion(textureFondo);

        slimeSheet = new Texture(Gdx.files.internal("Slime.png"));

        int anchoFrame = slimeSheet.getWidth() / 5;
        int altoFrame = slimeSheet.getHeight() / 2;
        TextureRegion[][] tmp = TextureRegion.split(slimeSheet, anchoFrame, altoFrame);

        TextureRegion[] framesCaminar = new TextureRegion[5];
        framesCaminar[0] = tmp[1][0];
        framesCaminar[1] = tmp[1][1];
        framesCaminar[2] = tmp[1][2];
        framesCaminar[3] = tmp[1][3];
        framesCaminar[4] = tmp[1][4];

        slimeCaminar = new Animation<TextureRegion>(0.08f, framesCaminar);
        stateTime = 0f;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();
        moviendose = false;
        float velocidad = 200 * delta;

        // Movimiento
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            posicionMapaX += velocidad;
            mirandoIzquierda = false;
            moviendose = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            posicionMapaX -= velocidad;
            mirandoIzquierda = true;
            moviendose = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            posicionMapaY -= velocidad;
            moviendose = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            posicionMapaY += velocidad;
            moviendose = true;
        }

        float anchoMundo = viewport.getWorldWidth();
        float altoMundo = viewport.getWorldHeight();
        regionFondo.setRegion((int)posicionMapaX, (int)posicionMapaY, (int)anchoMundo, (int)altoMundo);

        if (moviendose) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }

        TextureRegion currentFrame = slimeCaminar.getKeyFrame(stateTime, true);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();

        spriteBatch.draw(regionFondo, 0, 0, anchoMundo, altoMundo);

        float anchoOriginal = currentFrame.getRegionWidth();
        float altoOriginal = currentFrame.getRegionHeight();
        float origenX = anchoOriginal / 2f;
        float origenY = altoOriginal / 2f;

        float escalaX = mirandoIzquierda ? -3.0f : 3.0f;
        float escalaY = 3.0f;

        float posX = (anchoMundo / 2f) - origenX;
        float posY = (altoMundo / 2f) - origenY;

        spriteBatch.draw(currentFrame,
            posX, posY,
            origenX, origenY,
            anchoOriginal, altoOriginal,
            escalaX, escalaY,
            0);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        slimeSheet.dispose();
        textureFondo.dispose();
    }
}
