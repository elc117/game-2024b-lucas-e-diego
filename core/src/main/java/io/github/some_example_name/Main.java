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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture kanyeTexture, bgTexture, answerTexture;
    private Music music;

    private float timeSeconds = 11;
    private float period = 10;
    private Array<Rectangle> answersArray;

    @Override
    public void create() {
      batch = new SpriteBatch();
      
      music = Gdx.audio.newMusic(Gdx.files.internal("lucesIntermitentes_music.mp3"));
      kanyeTexture = new Texture(Gdx.files.internal("kanye_sprite.png"));
      
      answerTexture = new Texture(Gdx.files.internal("answer_sprite.png"));
      answersArray = new Array<Rectangle>();

      bgTexture = new Texture(Gdx.files.internal("bg_temp.png"));

      music.setLooping(true);
      music.play();
    }

    @Override
    public void render() {
      
        timeSeconds += Gdx.graphics.getDeltaTime();
        if(timeSeconds > period){
            timeSeconds -= period;
            this.spawnAnswers();
        }
        this.moveAnswers();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Gdx.graphics.setSystemCursor(SystemCursor.None);

        batch.begin();
        batch.draw(bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(kanyeTexture, 130, (Gdx.graphics.getHeight() - Gdx.input.getY())-50);

        for (Rectangle answer : answersArray){
          batch.draw(answerTexture, answer.x, answer.y);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        kanyeTexture.dispose();
        bgTexture.dispose();
    }

    private void spawnAnswers(){
      Rectangle answer = new Rectangle(Gdx.graphics.getWidth(), 50, answerTexture.getWidth(), answerTexture.getHeight());
      answersArray.add(answer);
      answer = new Rectangle(Gdx.graphics.getWidth(), 280, answerTexture.getWidth(), answerTexture.getHeight());
      answersArray.add(answer);
    }

    private void moveAnswers(){ 
      for(Iterator<Rectangle> iter = answersArray.iterator(); iter.hasNext();){
        Rectangle answer = iter.next();
        answer.x -= 0.5;

        if (isColisao(130, (Gdx.graphics.getHeight() - Gdx.input.getY())-50, kanyeTexture.getWidth(), kanyeTexture.getHeight(), answer.x, answer.y, answer.getWidth(), answer.getHeight())){
          iter.remove();      
        }

        if (answer.x + answerTexture.getWidth() < 0){
          iter.remove();
        }
      }
    }

    private boolean isColisao(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
      if ((x1 + w1 > x2) && (x1 < x2 + w2) && (y1 + h1 > y2) && (y1 < y2 + h2)){
        return true;
      } 
      else {
        return false;
      }

    }
}
