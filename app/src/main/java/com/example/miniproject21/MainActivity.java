package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    TextView textViewToggle;
    Button buttonLogin, buttonSignup;
    EditText editTextEmail, editTextPasswordLogin, editTextPasswordSignup, editTextPasswordSignupAgain;
    boolean isLoginOnScreen;
    final String SIGN_UP_HERE = "New user? Signup here";
    final String LOG_IN_HERE = "Already a user? Login here";

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    final ArrayList<String> listItems=new ArrayList<String>();
    ListView allergenListView;


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
            editTextPasswordSignup.setVisibility(View.VISIBLE);
            editTextPasswordSignupAgain.setVisibility(View.VISIBLE);
            toggleText = LOG_IN_HERE;

        } else {
            isLoginOnScreen = true;
            buttonLogin.setVisibility(View.VISIBLE);
            buttonSignup.setVisibility(View.INVISIBLE);
            editTextPasswordLogin.setVisibility(View.VISIBLE);
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

        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                assert user != null;
                                Log.i("SUCCESS", "Logged in " + user.getDisplayName());
                                launchHomeActivity();

                            } else {

                                Log.i("FAIL", "Log in failed " + task.getException());
                                Toast.makeText(MainActivity.this, "Failed to Log In", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    public void doSignup(View view) {
        Log.i("Action", "Sign up");
        Toast.makeText(this, "Creating account...", Toast.LENGTH_LONG).show();
        final String email = editTextEmail.getText().toString();
        String password = editTextPasswordSignup.getText().toString();
        String passwordAgain = editTextPasswordSignupAgain.getText().toString();

        Log.i("SIGN IN", email + " " + password + " " + passwordAgain );
        if (email.equals("") || password.equals("") || passwordAgain.equals("")) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();

        } else if (!password.equals(passwordAgain)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();

        } else{
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> mMap = new HashMap<>();
                                mMap.put("email", email);
                                mMap.put("history", new ArrayList<String>());

                                assert user != null;
                                db.collection("Users").document(user.getUid()).set(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(MainActivity.this, EditProfile.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                                //firebaseDatabase = FirebaseDatabase.getInstance();
                                //databaseReference = firebaseDatabase.getReference("users");
                                // assert user != null;
                                //databaseReference.child(user.getUid()).child("email").setValue(email);
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");

                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed."Toast.LENGTH_SHORT).show();
                                Log.i("FAIL", "Sign Up failed " + task.getException());
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            launchHomeActivity();
        }

        textViewToggle = findViewById(R.id.textViewToggle);

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
        editTextPasswordSignup.setVisibility(View.INVISIBLE);
        editTextPasswordSignupAgain.setVisibility(View.INVISIBLE);

    }

}
