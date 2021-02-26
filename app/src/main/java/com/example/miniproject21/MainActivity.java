package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    TextView textViewToggle;
    Button buttonLogin, buttonSignup;
    EditText editTextAllergen, editTextEmail, editTextPasswordLogin, editTextPasswordSignup, editTextPasswordSignupAgain;
    boolean isLoginOnScreen;
    final String SIGN_UP_HERE = "New user? Signup here";
    final String LOG_IN_HERE = "Already a user? Login here";

    private FirebaseAuth firebaseAuth;

    public void launchHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomePage.class);

        startActivity(intent);
        finish();
    }

    public void toggleOption(View view) {
        String toggleText;

        if (isLoginOnScreen) {
            isLoginOnScreen = false;
            buttonLogin.setVisibility(View.INVISIBLE);
            buttonSignup.setVisibility(View.VISIBLE);
            editTextPasswordLogin.setVisibility(View.INVISIBLE);
            editTextAllergen.setVisibility(View.VISIBLE);
            editTextPasswordSignup.setVisibility(View.VISIBLE);
            editTextPasswordSignupAgain.setVisibility(View.VISIBLE);
            toggleText = LOG_IN_HERE;

        } else {
            isLoginOnScreen = true;
            buttonLogin.setVisibility(View.VISIBLE);
            buttonSignup.setVisibility(View.INVISIBLE);
            editTextPasswordLogin.setVisibility(View.VISIBLE);
            editTextAllergen.setVisibility(View.INVISIBLE);
            editTextPasswordSignup.setVisibility(View.INVISIBLE);
            editTextPasswordSignupAgain.setVisibility(View.INVISIBLE);
            toggleText = SIGN_UP_HERE;
        }

        textViewToggle.setText(toggleText);
    }

    public void doLogin(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPasswordLogin.getText().toString();

        Log.i("LOGIN", " " + email + " " + password);

        // I'll add firebase auth later
    }

    public void doSignup(View view) {
        Log.i("Action", "Sign up");
        Toast.makeText(this, "Creating account...", Toast.LENGTH_LONG).show();

        String allergen = editTextAllergen.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPasswordSignup.getText().toString();
        String passwordAgain = editTextPasswordSignupAgain.getText().toString();
        ListView allergenListView = findViewById(R.id.listViewAllergens);

        Log.i("SIGN IN", email + " " + password + " " + passwordAgain + " " + allergen);

        // allergen is the auto complete text view
        // allergen list view is the list view to display the allergens
        // Just copy paste your logic of adding items in list view

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        textViewToggle = findViewById(R.id.textViewToggle);

        editTextAllergen = findViewById(R.id.editTextAllergens);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        editTextPasswordSignup = findViewById(R.id.editTextPasswordSignup);
        editTextPasswordSignupAgain = findViewById(R.id.editTextPasswordSignupAgain);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        isLoginOnScreen = true;
        buttonLogin.setVisibility(View.VISIBLE);
        buttonSignup.setVisibility(View.INVISIBLE);
        editTextPasswordLogin.setVisibility(View.VISIBLE);
        editTextAllergen.setVisibility(View.INVISIBLE);
        editTextPasswordSignup.setVisibility(View.INVISIBLE);
        editTextPasswordSignupAgain.setVisibility(View.INVISIBLE);

    }


//    public void logIn(View view){
//          EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
//          EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
//        String email = editTextEmail.getText().toString();
//        String password = editTextPassword.getText().toString();
//        if (email.equals("") || password.equals("")) {
//            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            firebaseAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                FirebaseUser user = firebaseAuth.getCurrentUser();
//                                assert user != null;
//                                Log.i("SUCCESS", "Logged in " + user.getDisplayName());
//                                Intent intent = new Intent(MainActivity.this, HomePage.class);
//                                startActivity(intent);
//                                finish();
//
//                            } else {
//
//                                Log.i("FAIL", "Log in failed " + task.getException());
//                                Toast.makeText(MainActivity.this, "Failed to Log In", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
//    }
//    public void signUp(View view){
//        Intent intent = new Intent(MainActivity.this, SignUp.class);
//        startActivity(intent);
//        finish();
//    }
}
