package ferranechaves.ioc.HolidayManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registre extends AppCompatActivity {

    TextView register,stat;
    EditText pwd,email,nom,telf,cognom;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ProgressBar bar ;
    DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);
        bar = findViewById(R.id.barLoad);
        nom = (EditText) findViewById(R.id.txtnames);
        pwd = (EditText) findViewById(R.id.pwdR);
        email = (EditText) findViewById(R.id.userR);
        telf = findViewById(R.id.userTelef);
        cognom = findViewById(R.id.userCognom);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        register = (TextView) findViewById(R.id.txtRegis);
        stat = findViewById(R.id.statusRegistre);

        if(mAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = email.getText().toString().trim();
                final String nm = nom.getText().toString().trim();
                final String tel = telf.getText().toString().trim();

                final String cog = cognom.getText().toString().trim();
                final String pss = pwd.getText().toString().trim();
                if(TextUtils.isEmpty(mail)){
                    email.setError("Email es necesari");
                    stat.setText("Email es necesari");
                    return;
                }
                if(TextUtils.isEmpty(nm)){
                    nom.setError("El nom es necesari");
                    stat.setText("El nom es necesari");
                    return;
                }
                if(TextUtils.isEmpty(pss)){
                    pwd.setError("La contrasenya es necesaria");
                    stat.setText("La contrasenya es necesaria");
                    return;
                }
                if(pss.length() <6){
                    pwd.setError("Contrasenya masa curta");
                    stat.setText("Contrasenya masa curta");
                    return;
                }
                if(TextUtils.isEmpty(cog)){
                    cognom.setError("El cognom es necesari");
                    stat.setText("El cognom es necesari");
                    return;
                }
                if(TextUtils.isEmpty(telf.getText().toString().trim())){
                    telf.setError("El telèfon es necesari");
                    stat.setText("El telèfon es necesari");
                    return;
                }


                bar.setVisibility(View.VISIBLE);
                //Registre
                mAuth.createUserWithEmailAndPassword(mail,pss).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String client = "client";
                            Map<String, Object> mapUser = new HashMap<>();
                            mapUser.put("nom",nm);
                            mapUser.put("email",mail);
                            mapUser.put("cognom",cog);
                            mapUser.put("telefon",tel);
                            mapUser.put("rol",client);
                            String id = mAuth.getCurrentUser().getUid();
                            mdatabase.child("Usuaris").child(id).setValue(mapUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){
                                        Toast.makeText(Registre.this, "Usuari creat", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),InApp.class));
                                        bar.setVisibility(View.INVISIBLE);

                                    }else{
                                        Toast.makeText(Registre.this, "Error "+ task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        bar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(Registre.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
    }
}