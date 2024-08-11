package mobile.wsmb2024.C10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    EditText etNameE, etICNumE, etPhoneE, etEmailE, etAddressE, etPasswordE;
    RadioButton rbMale, rbFemale;
    RadioGroup rgGender;
    Button btnSaveChanges;
    private FirebaseAuth auth;
    private DatabaseReference userRef, userRef2;
    String gender2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etNameE = findViewById(R.id.etNameE);
        etICNumE = findViewById(R.id.etICNumE);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rgGender = findViewById(R.id.rgGender);
        etPhoneE = findViewById(R.id.etPhoneE);
        etEmailE = findViewById(R.id.etEmailE);
        etAddressE = findViewById(R.id.etAddressE);
        etPasswordE = findViewById(R.id.etPasswordE);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef2 = database.getReference("users");

        SharedPreferences sp;
        sp = getSharedPreferences("user",MODE_PRIVATE);
        String icnum = sp.getString("icnum","");

        userRef.orderByChild("icnum").equalTo(icnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot datasnapshot : snapshot.getChildren()){
                        UserModel user = datasnapshot.getValue(UserModel.class);

                        if(user != null){
                            String name = user.name;
                            String gender = user.gender;
                            String phonenum = user.phone;
                            String email = user.email;
                            String address = user.address;
                            String password = user.password;

                            etNameE.setText(name);
                            etICNumE.setText(icnum);


                            if(gender.equals("Male")){
                                rbMale.setChecked(true);
                            }else{
                                rbFemale.setChecked(true);
                            }
                            etPhoneE.setText(phonenum);
                            etEmailE.setText(email);
                            etAddressE.setText(address);
                            etPasswordE.setText(password);
                        }
                        else{
                            Toast.makeText(EditProfile.this, "No user Info Found!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),FirstPage.class);
                            startActivity(i);
                        }
                    }
                }
                else{
                    Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Failed");
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name2 = etNameE.getText().toString();
                String icnum2 = etICNumE.getText().toString();
                String phone2 = etPhoneE.getText().toString();
                if(rbMale.isChecked()){
                    gender2 = "Male";
                }else if (rbFemale.isChecked()){
                    gender2 = "Female";
                }else{
                    Toast.makeText(EditProfile.this, "Please Choose Your Gender!", Toast.LENGTH_SHORT).show();
                }
                String email2 = etEmailE.getText().toString();
                String address2 = etAddressE.getText().toString();
                String password2 = etPasswordE.getText().toString();

                updateInfo(name2, icnum2, phone2, gender2, email2, address2, password2);
            }
        });

    }

    public void updateInfo(String name2, String icnum2, String phone2, String gender2, String email2, String address2, String password2){
        UpdateModel update = new UpdateModel(name2,icnum2, phone2, gender2, email2, address2, password2);
        userRef2.child(icnum2).setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfile.this, "User Profile Changes Saved!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), FirstPage.class);
                    startActivity(i);
                }else{
                    Toast.makeText(EditProfile.this, "Failed Changes.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}