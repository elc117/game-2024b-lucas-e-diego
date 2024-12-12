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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    //criando as games screens
    private enum Screen { MENU, JOGANDO, ENDING;}
    private Screen telaAtual = Screen.MENU;

    private BitmapFont font;
    private SpriteBatch batch;
    private Texture kanyeTexture, bgTexture, menubgTexture, endingbgTexture, questionTexture, answerTexture;
    private Music musicMain, musicEnding;

    private float timeSeconds = 16;
    private float period = 15;
    private int countAnswers = 0, i = 0, acertos = 0, erros = 0;
    private Array<Answer> answersArray;
    private Array<Texture> answersTextures;
    private Array<Texture> questionsTextures;
    private Answer correctAnswer, incorrectAnswer, answer1, answer2;
    private Rectangle correctAnswerRect, incorrectAnswerRect;

    @Override
    public void create() {
      batch = new SpriteBatch();
      font = new BitmapFont();
      font.setColor(Color.WHITE);
      
      musicMain = Gdx.audio.newMusic(Gdx.files.internal("music_main.mp3"));
      musicEnding = Gdx.audio.newMusic(Gdx.files.internal("music_ending.mp3"));
      kanyeTexture = new Texture(Gdx.files.internal("kanye_sprite.png"));
      menubgTexture = new Texture(Gdx.files.internal("bg_menu.png"));
      bgTexture = new Texture(Gdx.files.internal("bg_main.png"));
      endingbgTexture = new Texture(Gdx.files.internal("bg_ending.png"));
      
      questionsTextures = new Array<Texture>();
      for (i = 0; i < 10; i++) {
        questionTexture = new Texture(Gdx.files.internal("pergunta" + (i+1) + ".png"));
        questionsTextures.add(questionTexture);
      }

      answersTextures = new Array<Texture>();
      answersArray = new Array<Answer>();
      for (i = 0; i < 20; i++) {
        answerTexture = new Texture(Gdx.files.internal("resposta" + (i+1) + ".png"));
        answersTextures.add(answerTexture);
      }
      
      musicEnding.setLooping(true);
    }

    @Override
    public void render() {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Gdx.graphics.setSystemCursor(SystemCursor.None);

        batch.begin();
        
        if(telaAtual == Screen.MENU) {
          exibirMenu();
        }
        else if (telaAtual == Screen.JOGANDO) {
          musicMain.play();
          timeSeconds += Gdx.graphics.getDeltaTime();
          timeSeconds = atualizarJogo(timeSeconds);
        }
        else {
          musicMain.pause();
          musicEnding.play();
          batch.draw(endingbgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

          font.getData().setScale(8);
          font.setColor(Color.WHITE);
          font.draw(batch, "Obrigado por jogar!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 + 100);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        kanyeTexture.dispose();
        bgTexture.dispose();
    }

    private float atualizarJogo(float timeSeconds) {  
      if (timeSeconds > period) {
        timeSeconds -= period;
        if (countAnswers < 19){
          countAnswers = this.spawnAnswers(countAnswers);
        }
        else {
          telaAtual = Screen.ENDING;
        }
        
      }

      batch.draw(bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

      font.getData().setScale(5);
      font.setColor(Color.GREEN);
      font.draw(batch, "Acertos: " + acertos, 20, 120);
      

      font.getData().setScale(5);
      font.setColor(Color.RED);
      font.draw(batch, "Erros: " + erros, 450, 120);
      
      moveKanye();
      timeSeconds = this.moveAnswers(countAnswers, timeSeconds);

      return timeSeconds;
    }

    private void exibirMenu() {
      //fundo menu
      batch.draw(menubgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

      font.getData().setScale(5);
      font.draw(batch, "Clique para inciar!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 + 100);
      font.setColor(Color.WHITE);
      //muda estado do jogo  quando clica no menu
      if(Gdx.input.isTouched()) {
        telaAtual = Screen.JOGANDO;
      }
    }

    private void moveKanye(){
      float mouseLocationY = (Gdx.input.getY()-50);

      if (mouseLocationY < 185){
        batch.draw(kanyeTexture, 130, (Gdx.graphics.getHeight()-304));
      }
      else if (mouseLocationY > 630){
        batch.draw(kanyeTexture, 130, (Gdx.graphics.getHeight()-749));
      }
      else{
        batch.draw(kanyeTexture, 130, (Gdx.graphics.getHeight() - Gdx.input.getY())-50);
      }
    }

    private int spawnAnswers(int countAnswers){
      correctAnswerRect = new Rectangle(Gdx.graphics.getWidth(), 600, answersTextures.get(countAnswers).getWidth(), answersTextures.get(countAnswers).getHeight());
      correctAnswer = new Answer(correctAnswerRect, true, answersTextures.get(countAnswers));
  
      incorrectAnswerRect = new Rectangle(Gdx.graphics.getWidth(), 350, answersTextures.get(countAnswers+1).getWidth(), answersTextures.get(countAnswers+1).getHeight());
      incorrectAnswer = new Answer(incorrectAnswerRect, false, answersTextures.get(countAnswers+1));
  
      answersArray.add(correctAnswer);
      answersArray.add(incorrectAnswer);
  
      countAnswers += 2;
      return countAnswers;
    }

    public float moveAnswers(int countAnswers, float timeSeconds){
      for (i = countAnswers-2; i < answersArray.size; i+=2){
        answer1 = answersArray.get(i);
        answer2 = answersArray.get(i+1);
  
        batch.draw(questionsTextures.get(i/2), 250, Gdx.graphics.getHeight()-100);
        batch.draw(answersTextures.get(i), answersArray.get(i).rectangle.x, answersArray.get(i).rectangle.y);
        batch.draw(answersTextures.get(i+1), answersArray.get(i+1).rectangle.x, answersArray.get(i+1).rectangle.y);
  
        answer1.rectangle.x -= 1;
        answer2.rectangle.x -= 1;
  
        if (isColisao(130, (Gdx.graphics.getHeight() - Gdx.input.getY())-50, kanyeTexture.getWidth(), kanyeTexture.getHeight(), answer1.rectangle.x, answer1.rectangle.y, answer1.rectangle.getWidth(), answer1.rectangle.getHeight())){
          if (answer1.isCorrect){
            acertos++;
          }
          else {
            erros++;
          }
  
          answer1.rectangle.y = Gdx.graphics.getWidth()+50;
          answer2.rectangle.y = Gdx.graphics.getWidth()+50;

          timeSeconds = 16;
        }
  
        if (isColisao(130, (Gdx.graphics.getHeight() - Gdx.input.getY())-50, kanyeTexture.getWidth(), kanyeTexture.getHeight(), answer2.rectangle.x, answer2.rectangle.y, answer2.rectangle.getWidth(), answer2.rectangle.getHeight())){
          if (answer2.isCorrect){
            acertos++;
          }
          else {
            erros++;
          }
  
          answer1.rectangle.y = Gdx.graphics.getWidth()+50;
          answer2.rectangle.y = Gdx.graphics.getWidth()+50;

          timeSeconds = 16;
        }
      }
      return timeSeconds;
    }

    private boolean isColisao(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
      if ((x1 + w1 > x2) && (x1 < x2 + w2) && (y1 + h1 > y2) && (y1 < y2 + h2)){
        return true;
      } 
      else {
        return false;
      }

    }

    class Answer {
      Rectangle rectangle;
      boolean isCorrect;
      Texture texture;
  
      public Answer(Rectangle rectangle, boolean isCorrect, Texture texture) {
        this.rectangle = rectangle;
        this.isCorrect = isCorrect;
        this.texture = texture;
      }
    }
}
