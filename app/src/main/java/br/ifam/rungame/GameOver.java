package br.ifam.rungame;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

    MainActivity mainActivity = new MainActivity();
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        gameOverSound();


    }


    public void restart(View view){
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
        stopGameOverSound();
        mainActivity.gameSound();

    }
    public void exit(View view){
        Intent intent = new Intent(GameOver.this, MenuActivity.class);
        startActivity(intent);
        finish();
        stopGameOverSound();

    }
    public void gameOverSound(){
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gameoversound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }
    public void stopGameOverSound(){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        stopGameOverSound();
    }
}



