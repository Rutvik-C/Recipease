package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity implements View.OnKeyListener{
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    final ArrayList<String> listItems=new ArrayList<String>();
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==keyEvent.KEYCODE_ENTER && keyEvent.getAction()==keyEvent.ACTION_DOWN){

        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        RelativeLayout background = findViewById(R.id.layoutbg);
        background.setOnKeyListener(this);
        String[] allergens={"egg","peanuts","soy","wheat","nuts","shellfish","sesame seeds","garlic","maze","poultry meat","none"};
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_list_item_1, allergens);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
      final ListView listView = findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("selected allergen: ", arg0.getItemAtPosition(arg2).toString());
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                listItems.add(arg0.getItemAtPosition(arg2).toString());
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void signUp(View view){
        final EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail1);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPasswordl1);
        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextRePassword = (EditText) findViewById(R.id.editTextReenterpassword1);
        String name = editTextName.getText().toString();
        final String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextRePassword.getText().toString();
        if (name.equals("") || email.equals("") || password.equals("") || passwordAgain.equals("")) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            editTextName.setError("Required");
            editTextName.requestFocus();

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
                                databaseReference.child(user.getUid()).child("email").setValue(email);
                                for(int i=1;i<=listItems.size();i++){
                                    databaseReference.child(user.getUid()).child("allergens").child(String.valueOf(i)).setValue(listItems.get(i-1));
                                }
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                //FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("allergens").setValue(listItems);
                                Intent intent = new Intent(SignUp.this, HomePage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed."Toast.LENGTH_SHORT).show();
                                Log.i("FAIL", "Sign Up failed " + task.getException());
                                Toast.makeText(SignUp.this, "Failed to Sign Up", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }
    }
}