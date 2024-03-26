package com.example.musicapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterUsername, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterPsw, editTextRegisterConfirmPsw;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Đăng kí");

        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_LONG).show();
        editTextRegisterUsername = findViewById(R.id.editText_Register_username);
        editTextRegisterEmail = findViewById(R.id.editText_Register_email);
        editTextRegisterDoB = findViewById(R.id.editText_Register_dob);
        editTextRegisterPsw = findViewById(R.id.editText_Register_psw);
        editTextRegisterConfirmPsw = findViewById(R.id.editText_Register_confirm_psw);

        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        Button btnRegister = findViewById(R.id.btn_confirm_Register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                String textUsername = editTextRegisterUsername.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textPsw = editTextRegisterPsw.getText().toString();
                String textConfirmPsw = editTextRegisterConfirmPsw.getText().toString();
                String textGender;

                if(TextUtils.isEmpty(textUsername)){
                    Toast.makeText(RegisterActivity.this,"Vui lòng điền tên hoặc biệt danh của bạn", Toast.LENGTH_LONG).show();
                    editTextRegisterUsername.setError("Username is required");
                    editTextRegisterUsername.requestFocus();
                } else if(TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this,"Vui lòng điền Email của bạn", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this,"Vui lòng điền lại Email của bạn", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                } else if(TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền ngày sinh của bạn", Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("DoB is required");
                    editTextRegisterDoB.requestFocus();
                } else if(radioGroupRegisterGender.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng chọn giới tính của bạn", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if(TextUtils.isEmpty(textPsw)) {
                    Toast.makeText(RegisterActivity.this,"Vui lòng điền mật khẩu của bạn", Toast.LENGTH_LONG).show();
                    editTextRegisterPsw.setError("Password is required");
                    editTextRegisterPsw.requestFocus();
                } else if(textPsw.length() < 6) {
                    Toast.makeText(RegisterActivity.this,"Mật khẩu phải có ít nhất 7 kí tự", Toast.LENGTH_LONG).show();
                    editTextRegisterPsw.setError("Password too week");
                    editTextRegisterPsw.requestFocus();
                } else if(TextUtils.isEmpty(textConfirmPsw)) {
                    Toast.makeText(RegisterActivity.this,"Vui lòng điền xác nhận lại mật khẩu của bạn", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPsw.setError("Password Confirm is required");
                    editTextRegisterConfirmPsw.requestFocus();
                } else if(!textPsw.equals(textConfirmPsw)) {
                    Toast.makeText(RegisterActivity.this,"Mật khẩu xác nhận không trùng nhau", Toast.LENGTH_LONG).show();
                    editTextRegisterPsw.setError("Password Confirm is required");
                    editTextRegisterPsw.requestFocus();

                    editTextRegisterPsw.clearComposingText();
                    editTextRegisterConfirmPsw.clearComposingText();
                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    rgisterUser(textUsername,textEmail,textDoB,textGender,textPsw);
                }
            }
        });
    }

    private void rgisterUser(String textUsername, String textEmail, String textDoB, String textGender, String textPsw) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPsw).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User register successfully",Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            firebaseUser.sendEmailVerification();

                            /*Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK) | Intent.FLAG_ACTIVITY_NEW_TASK );
                            startActivity(intent);
                            finish();*/
                        }
                    }
                });
    }
}
