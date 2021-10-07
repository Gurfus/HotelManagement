package ferranechaves.ioc.HolidayManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Benvinguda extends AppCompatActivity {
    Button login ;
    Button registre;
    Button sortir ;
    FirebaseAuth mAuth;
    FirebaseUser user ;
    @Override
    public void onStart() {
        super.onStart();
         user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Intent intentReg = new Intent(Benvinguda.this,InApp.class);
            startActivity(intentReg);

        } else {
            Toast.makeText(Benvinguda.this, "Benvingut a Holiday Management", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benvinguda);
        login = findViewById(R.id.btnLog);
        registre = findViewById(R.id.btnRegistre);
        sortir = findViewById(R.id.btnSortir);
        mAuth = FirebaseAuth.getInstance();

        //intent activi login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(Benvinguda.this,Login.class);
                startActivity(intentReg);

            }
        });
        // intent activity regitre
        registre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(Benvinguda.this,Registre.class);
                startActivity(intentReg);

            }
        });
        //tanca la sessio i la app
        sortir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });



    }

}