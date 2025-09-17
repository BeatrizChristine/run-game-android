package br.ifam.rungame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Espinho {
    Bitmap espinho[] = new Bitmap[3];
    int espinhoFrame = 0;
    int espinhoX, espinhoY, espinhoVelocity;
    Random random;

    public Espinho(Context context){
        espinho[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike0);
        espinho[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike1);
        espinho[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike2);
        random = new Random();
        resetPosition();
    }
    public Bitmap getEspinho(int espinhoFrame){
        return espinho[espinhoFrame];

    }
    public int getEspinhoWidth(){
        return espinho[0].getWidth();
    }
    public int getEspinhoHeight(){
        return espinho[0].getHeight();
    }


    public void resetPosition() {
        espinhoX =  random.nextInt(GameView.dWidth - getEspinhoWidth());
        espinhoY = -150 + random.nextInt(600) * -1;
        espinhoVelocity = 35 + random.nextInt(16);

    }
}
