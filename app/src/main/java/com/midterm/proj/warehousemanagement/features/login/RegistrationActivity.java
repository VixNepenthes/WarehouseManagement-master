package com.midterm.proj.warehousemanagement.features.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.model.UserModel;


public class RegistrationActivity extends AppCompatActivity {

    Button signUp;
    EditText name, email, password;
    TextView signIn;

    FirebaseAuth auth;


    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initUi();
        setEvent();

    }

    private void setEvent() {


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                //  progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initUi() {
        signUp = findViewById(R.id.login_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        signIn = findViewById(R.id.sign_in);

    }

    private void createUser() {
        auth = FirebaseAuth.getInstance();


        String userName = name.getText().toString();
        String userEmail =  email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Name không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 6 ){
            Toast.makeText(this, "Mật khẩu phải lớn hơn 6 chữ cái", Toast.LENGTH_SHORT).show();
            return;
        }

        //create user auth
        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    try {
                        String id = task.getResult().getUser().getUid();

//                        UserModel userModel = new UserModel(userName,userEmail,userPassword);
//                        dbRef = database.getReference();
//                        dbRef.child(id).setValue(userModel);

                        writeNewUser(id,userName,userEmail,userPassword);

                    }catch (Exception e){
                        Toast.makeText(RegistrationActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }




                    Toast.makeText(RegistrationActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                }else {

                   // Toast.makeText(RegistrationActivity.this, "Đăng ký thất bại vui lòng kiểm tra lại" + task.getException(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegistrationActivity.this, "Đăng ký thất bại vui lòng kiểm tra lại" , Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    public void writeNewUser(String userId, String name, String email, String password) {
        UserModel user = new UserModel(name, email, password);

        mDatabase.child("Users").child(userId).setValue(user);
    }
}