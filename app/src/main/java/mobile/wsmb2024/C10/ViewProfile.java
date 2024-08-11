package mobile.wsmb2024.C10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfile extends AppCompatActivity {
    TextView tvName, tvICNum, tvGender, tvPhoneNum, tvEmail, tvAddress;

    Button btnBack;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        tvName = findViewById(R.id.tvName);
        tvICNum = findViewById(R.id.tvICNum);
        tvGender = findViewById(R.id.tvGender);
        tvPhoneNum = findViewById(R.id.tvPhoneNum);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        btnBack = findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

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

                            tvName.setText(name);
                            tvICNum.setText(icnum);
                            tvGender.setText(gender);
                            tvPhoneNum.setText(phonenum);
                            tvEmail.setText(email);
                            tvAddress.setText(address);
                        }
                        else{
                            tvName.setText("Unknown Field");
                            tvICNum.setText("Unknown Field");
                            tvGender.setText("Unknown Field");
                            tvPhoneNum.setText("Unknown Field");
                            tvEmail.setText("Unknown Field");
                            tvAddress.setText("Unknown Field");
                        }
                    }
                }
                else{
                    Toast.makeText(ViewProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Failed");
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FirstPage.class);
                startActivity(i);
            }
        });

    }
}