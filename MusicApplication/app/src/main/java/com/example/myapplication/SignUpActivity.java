package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import data_classes.User;

public class SignUpActivity extends AppCompatActivity {

    private Button back_button;
    private Button signup_button;
    private EditText name;
    private EditText surname;
    private EditText birth_date;
    private EditText email;
    private EditText phone;
    private EditText password;
    private EditText re_password;
    private SharedPreferences sharedPreferences;
    public static final String MyPrefs = "MyPrefs";
    public static final String UserKey = "user_list";

    public ArrayList<User> user_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.etName);
        surname = (EditText) findViewById(R.id.etSurname);
        birth_date = (EditText) findViewById(R.id.etBirthDate);
        email = (EditText) findViewById(R.id.etMail);
        phone = (EditText) findViewById(R.id.etPhoneNumber);
        password = (EditText) findViewById(R.id.etPasswordSignUp);
        re_password = (EditText) findViewById(R.id.etPasswordReSignUp);
        signup_button = (Button) findViewById(R.id.btnSignUpSUP);
        back_button = (Button) findViewById(R.id.btnBackLoginScreen);

        // load user list from file
        user_list = load_list();


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_user();

                // clear list
//                save_list(new ArrayList<User>());

                // test email intent
//                User nu = new User();
//                nu.setEmail("ahmetssaglam@gmail.com");
//                nu.setSurname("saglam");
//                nu.setName("ahmet");
//                nu.setPhoneNumber("5445305324");
//                nu.setBirthDate("11/12/1999");
//                nu.setPassword("123456");
//                send_email(nu);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginScreen();
            }
        });
    }


    public void save_list(ArrayList<User> user_list) {

      sharedPreferences = this.getSharedPreferences(MyPrefs, MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      Gson gson = new Gson();

      String json = gson.toJson(user_list);
      editor.putString(UserKey, json);
      editor.apply();

      Toast.makeText(this, "Sign Up Success !", Toast.LENGTH_SHORT).show();

    }

    public ArrayList<User> load_list() {
        sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(UserKey, null);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        ArrayList<User> users = gson.fromJson(json, type);

        return users != null ? users : new ArrayList<User>();
    }


    public String get_email_subject(User user) {
//        return name.getText().toString() + " " + surname.getText().toString() + " Sign Up Confirmation";
        return user.getName() + " " + user.getSurname() + " Sign Up Confirmation";
    }

    /**
     *
     * @param user
     * @return
     */
    public String get_email_body(User user) {
//        return  "Name = " + name.getText().toString() +
//                " Surname = " + surname.getText().toString() + " \n" +
//                " Birth Date = " + birth_date.getText().toString() + " \n" +
//                " Email = " + email.getText().toString() + " \n" +
//                " Phone Number = " + phone.getText().toString();

        return  "Name = " + user.getName() +
                " Surname = " + user.getSurname() + " \n" +
                " Birth Date = " + user.getBirthDate() + " \n" +
                " Email = " + user.getEmail() + " \n" +
                " Phone Number = " + user.getPhoneNumber();
    }


    private void send_email(User user){

        String email_address = user.getEmail();
        String email_subject = get_email_subject(user);
        String email_body = get_email_body(user);

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[] {email_address});
        email.putExtra(Intent.EXTRA_SUBJECT, email_subject);
        email.putExtra(Intent.EXTRA_TEXT, email_body);

        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));

    }

    private boolean add_user() {

        if(!control_password(password.getText().toString(), re_password.getText().toString())) {
            Toast.makeText(this, "Incorrect Password !", Toast.LENGTH_SHORT).show();
            return false;
        }

        User new_user = new User();
        new_user.setName(name.getText().toString());
        new_user.setSurname(surname.getText().toString());
        new_user.setEmail(email.getText().toString());
        new_user.setBirthDate(birth_date.getText().toString());
        new_user.setPhoneNumber(phone.getText().toString());
        new_user.setPassword(password.getText().toString());


        int control = control_user(user_list, new_user);
        if (control == 1) {
            Toast.makeText(this, "Name and Surname Already Taken !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(control == 2) {
            Toast.makeText(this, "Email Already Taken !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (control == 3) {
            Toast.makeText(this, "Phone Number Already Taken !", Toast.LENGTH_SHORT).show();
            return false;
        }


        send_email(new_user);
        System.out.println("MAIL GONDERILDI");
        user_list.add(new_user);
        save_list(user_list);

        for(User user : user_list) {
            System.out.println(user.toString());
        }

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        return true;
    }

    private boolean control_password(String pw1, String pw2) {
        System.out.println("Password 1 = " + pw1);
        System.out.println("Password 2 = " + pw2);
        return pw1.equals(pw2);
    }

    private int control_user(ArrayList<User> users, User new_user) {
        int return_value = 0;
        for(User user : users) {
            String user_name = user.getName() + " " + user.getSurname();
            String input_user_name = new_user.getName() + " " + new_user.getSurname();
            if (user_name.equals(input_user_name)) {
                return_value = 1;
                break;
            }
            if (user.getEmail().equals(new_user.getEmail())) {
                return_value =  2;
                break;
            }
            if (user.getPhoneNumber().equals(new_user.getPhoneNumber())) {
                return_value = 3;
                break;
            }
        }
        return return_value;
    }


    public void openLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}