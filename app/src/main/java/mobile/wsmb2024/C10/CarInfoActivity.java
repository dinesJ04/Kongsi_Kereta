package mobile.wsmb2024.C10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarInfoActivity extends AppCompatActivity {
    EditText etCarModel, etNumber, etCapacity;
    RadioButton rbYes, rbNo;
    RadioGroup rgAccesible;
    Button btnSubmit;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);

        etCarModel = findViewById(R.id.etCarModel);
        etCapacity = findViewById(R.id.etCapacity);
        etNumber = findViewById(R.id.etOrigin);
        rbYes = findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);
        rgAccesible = findViewById(R.id.rgAccessible);
        btnSubmit = findViewById(R.id.btnSubmit);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("car_info");

        SharedPreferences sp;
        sp = getSharedPreferences("user",MODE_PRIVATE);
        String icnum = sp.getString("icnum","");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String model = etCarModel.getText().toString();
                String capacity = etCapacity.getText().toString();
                String number = etNumber.getText().toString();
                String access ;
                if(rbYes.isChecked()){
                    access = "Yes";
                }else{
                    access = "No";
                }

                carInfo(icnum, model, capacity, number, access);
            }
        });

    }

    public void carInfo(String icnum, String model, String capacity, String number, String access){
        CarModel car = new CarModel(icnum, model, capacity, number, access);
        userRef.child(icnum).setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CarInfoActivity.this, "Car Profile Saved!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), FirstPage.class);
                    startActivity(i);
                }else{
                    Toast.makeText(CarInfoActivity.this, "Failed Saved.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}