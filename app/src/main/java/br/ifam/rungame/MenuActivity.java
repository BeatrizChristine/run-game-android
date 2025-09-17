package br.ifam.rungame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        Button bJogar = findViewById(R.id.jogar);
        Button bSair = findViewById(R.id.sair);
        Button bSobre = findViewById(R.id.sobre);

        bJogar.setOnClickListener(this);
        bSair.setOnClickListener(this);
        bSobre.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

        switch (v.getId()) {
            case R.id.sobre:
                builder.setTitle("Sobre:")
                        .setIcon(android.R.drawable.star_big_on)
                        .setMessage("Run Game!\nJogo feito para obtenção de nota da matéria de Jogos Mobile.")
                        .setPositiveButton("VOLTAR", (dialog, which) -> {
                        })
                        .show();
                break;

            case R.id.jogar:
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.sair:
                finishAffinity(); // fecha o app inteiro
                break;
        }
    }
}
