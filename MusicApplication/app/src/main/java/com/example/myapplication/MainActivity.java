package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import data_classes.User;

public class MainActivity extends AppCompatActivity {

    private Button login_button;
    private Button signUp_button;
    private EditText user_name;
    private EditText password;
    private SharedPreferences sharedPreferences;
    private int log_in_attempt_count = 0;
    private int button_color;

    public ArrayList<User> user_list = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button) findViewById(R.id.btnLogin);
        signUp_button = (Button) findViewById(R.id.btnSignUp);
        user_name = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);

        user_list = load_list();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(User user : user_list) {
                    System.out.println(user.toString());
                }
                System.out.println("ATTEMPTS : " + log_in_attempt_count);


                if (validate(user_name.getText().toString(), password.getText().toString(), user_list)){
                    openHomePage();
                }
                else {
                    log_in_attempt_count++;
                }

                if (log_in_attempt_count == 3) {
//                    button_color = getResources().getColor(R.color.dark_gray);
//                    login_button.setBackgroundColor(button_color);
//                    login_button.setEnabled(false);
                    Toast.makeText(getBaseContext(), "Too many failed attempts. Please wait ! ", Toast.LENGTH_SHORT).show();
                    openSignUpPage();
//                    (new Handler()).postDelayed(this::reset_attempts, 5000);
                }
            }

            private void reset_attempts() {
                log_in_attempt_count = 0;
                button_color = getResources().getColor(R.color.white);
                login_button.setBackgroundColor(button_color);
                login_button.setEnabled(true);
            }

        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpPage();
            }
        });
    }


    public void openHomePage() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    public void openMusicPlayerPage() {
        Intent intent = new Intent(this, MusicActivity.class);
        startActivity(intent);
    }

    public void openSignUpPage() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private boolean validate(String user_name, String user_password, ArrayList<User> user_list) {
        boolean control_login = false;

        System.out.println("INPUTS :" + user_name + "  pass:" + user_password);

        for (User user : user_list) {
            String phone = user.getPhoneNumber();
            String password = user.getPassword();
            System.out.print(phone);
            System.out.println(" " + password);
            if ((phone.equals(user_name)) && (password.equals(user_password))) {
                control_login = true;
                break;
            }
        }

        if(control_login){
            System.out.println("Login Successful ! ");
        }
        else {
            System.out.println("Incorrect username or password !");
            Toast.makeText(this, "Incorrect username or password !", Toast.LENGTH_SHORT).show();
        }
        return control_login;
    }

    private ArrayList<User> load_list() {
        sharedPreferences = getSharedPreferences(SignUpActivity.MyPrefs, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SignUpActivity.UserKey, null);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        ArrayList<User> users = gson.fromJson(json, type);

        return users != null ? users : new ArrayList<User>();
    }





}