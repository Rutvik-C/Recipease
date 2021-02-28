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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    TextView textViewToggle;
    Button buttonLogin, buttonSignup;
    EditText editTextEmail, editTextPasswordLogin, editTextPasswordSignup, editTextPasswordSignupAgain;
    AutoCompleteTextView autoCompleteTextView;
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
            autoCompleteTextView.setVisibility(View.VISIBLE);
            editTextPasswordSignup.setVisibility(View.VISIBLE);
            editTextPasswordSignupAgain.setVisibility(View.VISIBLE);
            allergenListView.setVisibility(View.VISIBLE);
            toggleText = LOG_IN_HERE;

        } else {
            isLoginOnScreen = true;
            buttonLogin.setVisibility(View.VISIBLE);
            buttonSignup.setVisibility(View.INVISIBLE);
            editTextPasswordLogin.setVisibility(View.VISIBLE);
            autoCompleteTextView.setVisibility(View.INVISIBLE);
            editTextPasswordSignup.setVisibility(View.INVISIBLE);
            editTextPasswordSignupAgain.setVisibility(View.INVISIBLE);
            allergenListView.setVisibility(View.INVISIBLE);
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

        Log.i("SIGN IN", email + " " + password + " " + passwordAgain + " " + listItems.toString());
        // allergen is the auto complete text view
        // allergen list view is the list view to display the allergens
        // Just copy paste your logic of adding items in list view
        if (email.equals("") || password.equals("") || passwordAgain.equals("")) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();

        } else if (!password.equals(passwordAgain)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (task.isSuccessful()) {
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("users");
                                assert user != null;
                                databaseReference.child(user.getUid()).child("email").setValue(email);
                                    for(int i=1;i<=listItems.size();i++){
                                        databaseReference.child(user.getUid()).child("allergens").child(String.valueOf(i)).setValue(listItems.get(i-1));
                                    }
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                launchHomeActivity();

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

        autoCompleteTextView = findViewById(R.id.autoCompleteEditTextAllergens);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        isLoginOnScreen = true;
        buttonLogin.setVisibility(View.VISIBLE);
        buttonSignup.setVisibility(View.INVISIBLE);
        editTextPasswordLogin.setVisibility(View.VISIBLE);
        autoCompleteTextView.setVisibility(View.INVISIBLE);
        editTextPasswordSignup.setVisibility(View.INVISIBLE);
        editTextPasswordSignupAgain.setVisibility(View.INVISIBLE);

        RelativeLayout background = findViewById(R.id.relativeLayout);
        background.setOnKeyListener(this);

        String[] allergens={"egg","peanuts","soy","wheat","nuts","shellfish","sesame seeds","garlic","maze","poultry meat"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, allergens);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listItems);
        allergenListView = findViewById(R.id.listViewAllergens);
        allergenListView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { //to add allergen to listview
                if(listItems.contains( arg0.getItemAtPosition(arg2).toString())){
                    Toast.makeText(MainActivity.this,"Allergen already present in your list!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.i("selected allergen: ", arg0.getItemAtPosition(arg2).toString());
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    listItems.add(arg0.getItemAtPosition(arg2).toString());
                    autoCompleteTextView.setText("");
                    adapter.notifyDataSetChanged();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });
        allergenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //to delete allergen on clicking
                listItems.remove(i);
                adapter.notifyDataSetChanged();
            }
        });
        allergenListView.setVisibility(View.INVISIBLE);

    }

}
