package br.ifam.rungame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    // Sprites normais e de dano
    Bitmap background, ground;
    Bitmap catNormal, catDamage, cat;
    boolean isTakingDamage = false;
    long damageTime = 0;

    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPain = new Paint();
    Paint health = new Paint();
    float TEXT_SIZE = 120;
    int pontos = 0;
    int vida = 3;
    static int dWidth, dHeight;
    Random random;
    float catX, catY;
    float oldX;
    float oldCatX;
    ArrayList<Espinho> espinhos;
    ArrayList<Explosion> explosions;

    public GameView(Context context) {
        super(context);
        this.context = context;

        // Bitmaps
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);

        // Carregar sprites do gato
        catNormal = BitmapFactory.decodeResource(getResources(), R.drawable.catgame);
        catDamage = BitmapFactory.decodeResource(getResources(), R.drawable.catgame1);
        cat = catNormal; // começa com normal

        // Medidas da tela
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;

        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        textPain.setColor(Color.rgb(255, 165, 0));
        textPain.setTextSize(TEXT_SIZE);
        textPain.setTextAlign(Paint.Align.LEFT);
        textPain.setTypeface(ResourcesCompat.getFont(context, R.font.fonte));

        health.setColor(Color.GREEN);
        random = new Random();

        // Posição inicial do gato
        catX = dWidth / 2 - cat.getWidth() / 2;
        catY = dHeight - ground.getHeight() - cat.getHeight();

        espinhos = new ArrayList<>();
        explosions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Espinho espinho = new Espinho(context);
            espinhos.add(espinho);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);

        // Verificar se passou o tempo do dano
        if (isTakingDamage && System.currentTimeMillis() - damageTime > 200) {
            cat = catNormal;
            isTakingDamage = false;
        }

        // Desenhar gato
        canvas.drawBitmap(cat, catX, catY, null);

        // Desenhar espinhos
        for (int i = 0; i < espinhos.size(); i++) {
            canvas.drawBitmap(espinhos.get(i).getEspinho(espinhos.get(i).espinhoFrame),
                    espinhos.get(i).espinhoX, espinhos.get(i).espinhoY, null);
            espinhos.get(i).espinhoFrame++;
            if (espinhos.get(i).espinhoFrame > 2) {
                espinhos.get(i).espinhoFrame = 0;
            }
            espinhos.get(i).espinhoY += espinhos.get(i).espinhoVelocity;
            if (espinhos.get(i).espinhoY + espinhos.get(i).getEspinhoHeight() >= dHeight - ground.getHeight()) {
                pontos += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = espinhos.get(i).espinhoX;
                explosion.explosionY = espinhos.get(i).espinhoY;
                explosions.add(explosion);
                espinhos.get(i).resetPosition();
            }
        }

        // Colisão com o gato
        for (int i = 0; i < espinhos.size(); i++) {
            if (espinhos.get(i).espinhoX + espinhos.get(i).getEspinhoWidth() >= catX
                    && espinhos.get(i).espinhoX <= catX + cat.getWidth()
                    && espinhos.get(i).espinhoY + espinhos.get(i).getEspinhoWidth() >= catY
                    && espinhos.get(i).espinhoY + espinhos.get(i).getEspinhoWidth() <= catY + cat.getHeight()) {

                vida--;
                espinhos.get(i).resetPosition();

                // Trocar sprite para dano
                cat = catDamage;
                isTakingDamage = true;
                damageTime = System.currentTimeMillis();

                if (vida == 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("pontos: ", pontos);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        // Desenhar explosões
        for (int i = 0; i < explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame),
                    explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 3) {
                explosions.remove(i);
            }
        }

        // Barra de vida
        if (vida == 2) {
            health.setColor(Color.YELLOW);
        } else if (vida == 1) {
            health.setColor(Color.RED);
        }
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * vida, 80, health);

        // Pontos
        canvas.drawText("" + pontos, 20, TEXT_SIZE, textPain);

        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= catY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldCatX = catX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX;
                float newCatX = oldCatX - shift;

                if (newCatX <= 0)
                    catX = 0;
                else if (newCatX >= dWidth - cat.getWidth())
                    catX = dWidth - cat.getWidth();
                else
                    catX = newCatX;
            }
        }
        return true;
    }
}
