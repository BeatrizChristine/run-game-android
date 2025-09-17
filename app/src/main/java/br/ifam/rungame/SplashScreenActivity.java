package br.ifam.rungame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable splashRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.splashsound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> mp.release());

        handler = new Handler();

        // Verifica usuário logado imediatamente
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final Intent intent;
        if (currentUser != null) {
            intent = new Intent(SplashScreenActivity.this, MenuActivity.class);
        } else {
            intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        }
        // Flags que garantem que apenas uma Activity será aberta
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Runnable da Splash
        splashRunnable = () -> {
            startActivity(intent);
            finish();
        };

        handler.postDelayed(splashRunnable, 6000); // 6 segundos
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove qualquer Runnable pendente para evitar duplicação
        if (handler != null && splashRunnable != null) {
            handler.removeCallbacks(splashRunnable);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
