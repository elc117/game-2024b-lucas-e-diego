package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture kanyeTexture, bgTexture;
    private Music music;

    @Override
    public void create() {
      batch = new SpriteBatch();
      music = Gdx.audio.newMusic(Gdx.files.internal("lucesIntermitentes_music.mp3"));
      kanyeTexture = new Texture(Gdx.files.internal("kanye_sprite.png"));
      bgTexture = new Texture(Gdx.files.internal("bg_temp.png"));

      music.setLooping(true);
      music.play();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Gdx.graphics.setSystemCursor(SystemCursor.None);
        batch.begin();
        batch.draw(bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(kanyeTexture, 130, (Gdx.graphics.getHeight() - Gdx.input.getY())-50);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        kanyeTexture.dispose();
        bgTexture.dispose();
    }
}
