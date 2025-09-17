package br.ifam.rungame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SingUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText singupEmail, singupPassword;
    private Button singupButton;
    private TextView loginReadingText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        auth = FirebaseAuth.getInstance();
        singupEmail = findViewById(R.id.singup_email);
        singupPassword = findViewById(R.id.singup_password);
        singupButton = findViewById(R.id.singup_button);
        loginReadingText = findViewById(R.id.loginReadingText);

            singupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = singupEmail.getText().toString().trim();
                    String pass = singupPassword.getText().toString().trim();

                    if(user.isEmpty()){
                        singupEmail.setError("O espaço do email está vazio!");
                    }
                    if(pass.isEmpty()){
                        singupPassword.setError("O espaço da senha está vazio!");
                    }else{
                        auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SingUpActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Toast.makeText(SingUpActivity.this,"Cadastro não realizado" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

            loginReadingText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SingUpActivity.this,LoginActivity.class));
                }
            });
    }
}