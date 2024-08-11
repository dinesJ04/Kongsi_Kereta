package mobile.wsmb2024.C10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RideInfo extends AppCompatActivity {
    EditText etDate, etTime, etOrigin, etDestination, etFare;
    Button btnSubmit;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info);

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etOrigin = findViewById(R.id.etOrigin);
        etDestination = findViewById(R.id.etDestination);
        etFare = findViewById(R.id.etFare);
        btnSubmit = findViewById(R.id.btnSubmit);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("ride_info");

        SharedPreferences sp;
        sp = getSharedPreferences("user",MODE_PRIVATE);
        String icnum = sp.getString("icnum","");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = etDate.getText().toString();
                String time = etTime.getText().toString();
                String origin = etOrigin.getText().toString();
                String destination = etDestination.getText().toString();
                String fare = etFare.getText().toString();

                rideInfo(icnum, date, time, origin, destination, fare);
            }
        });
    }
    public void rideInfo(String icnum, String date, String time,String origin, String destination, String fare){
        RideModel ride = new RideModel(icnum, date, time, origin, destination, fare);
        userRef.child(icnum).setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RideInfo.this, "Ride Profile Saved!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), FirstPage.class);
                    startActivity(i);
                }else{
                    Toast.makeText(RideInfo.this, "Failed Saved.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}