package com.example.musicapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference databaseRef;
    private TextInputEditText inputEmail;
    private TextInputEditText uName;
    private CircleImageView profileAvt;
    private Uri avatarUri; // Sử dụng kiểu Uri cho avatarUri
    private Button editAvatarButton;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        inputEmail = findViewById(R.id.inputEmail);
        uName = findViewById(R.id.inputUser);
        profileAvt = findViewById(R.id.profile_avatar);
        editAvatarButton = findViewById(R.id.avatarEditButton);
        saveBtn = findViewById(R.id.save);

        databaseRef = FirebaseDatabase.getInstance().getReference("User");
        // load data from database to edit form
        databaseRef.child("DEKIYJ6dSDYbN9Qv6Kqz8xUcyug1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userModel = snapshot.getValue(User.class);
                inputEmail.setText(userModel.getEmail());
                uName.setText(userModel.getUsername());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        saveBtn.setOnClickListener(this);
        editAvatarButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // Save event button
        if (id == R.id.save){
            String emailText = inputEmail.getText().toString();
            String userName = uName.getText().toString();

            if (emailText.trim().isEmpty() || userName.trim().isEmpty()){
                if (emailText.trim().isEmpty()) {
                    inputEmail.setError("Email is required");
                }
                if (userName.trim().isEmpty()) {
                    uName.setError("Username is required");
                }
            } else {
                if (avatarUri != null) {
                    User userModel = new User(emailText, userName);

                    databaseRef.child("DEKIYJ6dSDYbN9Qv6Kqz8xUcyug1").setValue(userModel)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(EditProfileActivity.this,"Your data is successfully added", Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(EditProfileActivity.this,"Failed to add data", Toast.LENGTH_LONG).show();
                            });
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please select an avatar", Toast.LENGTH_SHORT).show();
                }
            }
        } // change image event button
        else if(id == R.id.avatarEditButton){
            choosePicture();
        }
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() !=null){
            avatarUri = data.getData();
            profileAvt.setImageURI(data.getData());
        }
    }
}
