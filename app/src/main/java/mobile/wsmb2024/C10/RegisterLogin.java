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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterLogin extends AppCompatActivity {
    EditText etNameE, etICNumE, etPhoneE, etEmailE, etAddressE, etPasswordR;
    Button btnRegister;
    RadioButton rbMale, rbFemale;
    RadioGroup rgGender;
    TextView tvSignIn;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        etNameE = findViewById(R.id.etNameE);
        etICNumE = findViewById(R.id.etICNumE);
        etPhoneE = findViewById(R.id.etPhoneE);
        etEmailE = findViewById(R.id.etEmailE);
        etAddressE = findViewById(R.id.etAddressE);
        etPasswordR = findViewById(R.id.etPasswordR);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnRegister = findViewById(R.id.btnRegister);
        tvSignIn = findViewById(R.id.tvSignIn);

        String selectedGender;
        if(rbMale.isChecked()){
            selectedGender = "Male";
        }
        else{
            selectedGender = "Female";
        }

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etNameE.getText().toString();
                String icnum = etICNumE.getText().toString();
                String gender = selectedGender.toString();
                String phone = etPhoneE.getText().toString();
                String email = etEmailE.getText().toString();
                String address = etAddressE.getText().toString();
                String password = etPasswordR.getText().toString();

                registerUser(name, icnum, gender, phone, email, address, password);
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    public void registerUser(String name, String icnum, String gender, String phone, String email, String address, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebase = auth.getCurrentUser();
                    if(firebase != null){
                        String userId = firebase.getUid();
                        UserModel user = new UserModel(name, icnum, gender, phone, email, address, password);

                        userRef.child(icnum).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(RegisterLogin.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                   Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                   startActivity(i);
                               }
                               else{
                                   Toast.makeText(RegisterLogin.this, "Failed to Register. Try Again.", Toast.LENGTH_SHORT).show();
                                   etNameE.setText("");
                                   etICNumE.setText("");
                                   etPhoneE.setText("");
                                   rbMale.setSelected(false);
                                   rbFemale.setSelected(false);
                                   etEmailE.setText("");
                                   etAddressE.setText("");
                                   etPasswordR.setText("");
                               }
                            }
                        });
                    }
                }
                else{
                    Log.d("Error","");
                    Toast.makeText(RegisterLogin.this, "Failed " + email, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}