package com.midterm.proj.warehousemanagement.features.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.midterm.proj.warehousemanagement.R;


public class SendEmailActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnSendEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        initUi();
        initListener();
    }

    private void initUi() {
        progressDialog = new ProgressDialog(this);
        edtEmail = findViewById(R.id.edt_send_password_reset_email);
        btnSendEmail = findViewById(R.id.btn_send_email);
    }
    private void initListener() {
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendEmail();
            }
        });
    }
    private void onClickSendEmail(){
        progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = edtEmail.getText().toString().trim();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(SendEmailActivity.this, "Vui lòng kiểm tra hộp thư Email", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SendEmailActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                        else {
                            Toast.makeText(SendEmailActivity.this, "Gửi mail không thành công", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}