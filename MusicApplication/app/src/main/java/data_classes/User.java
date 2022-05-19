package data_classes;

import androidx.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String Name;
    public String Surname;
    public String BirthDate;
    public String Email;
    public String PhoneNumber;
    public String Password;

    public User(){

    }

    public User(String name, String surname, String birth_date, String email, String phoneNumber, String password) {
        Name = name;
        Surname = surname;
        BirthDate = birth_date;
        Email = email;
        PhoneNumber = phoneNumber;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "Name='" + Name + '\'' +
                ", Surname='" + Surname + '\'' +
                ", BirthDate='" + BirthDate + '\'' +
                ", Email='" + Email + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }



//    public void write_user_list_to_file(ArrayList<User> user_list, String file_path) {
////        "/home/ahmetsaidsaglam/YTU/Mobil/Projects/deneme/app/data/users.ser"
//        FileOutputStream fout = null;
//        ObjectOutputStream oos = null;
//        try {
//            fout = new FileOutputStream(file_path);
//            oos = new ObjectOutputStream(fout);
//            oos.writeObject(user_list);
//
//            oos.close();
//            fout.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static ArrayList<User> read_user_list_from_file(String file_path) {
//        ArrayList<User> user_list = new ArrayList<User>();
//        FileInputStream fin = null;
//        ObjectInputStream ois = null;
//        try {
//            fin = new FileInputStream(file_path);
//            ois = new ObjectInputStream(fin);
//
//            user_list = (ArrayList<User>) ois.readObject();
//            ois.close();
//            fin.close();
//        }
//        catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return user_list;
//    }



}
