package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MAIN ACTIVITY", "Hello World!");

    }
    public void logIn(View view){
          EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
          EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                assert user != null;
                                Log.i("SUCCESS", "Logged in " + user.getDisplayName());
                                Intent intent = new Intent(MainActivity.this, HomePage.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Log.i("FAIL", "Log in failed " + task.getException());
                                Toast.makeText(MainActivity.this, "Failed to Log In", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    public void signUp(View view){
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
        finish();
    }
}
