package ferranechaves.ioc.HolidayManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText pwd,email,nom;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ProgressBar bar ;
    String error;
    TextView status,login,registre;
    @Override
    //al començar si ja esta iniciat la sessió enviara a l'activity Inapp
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intentReg = new Intent(getApplicationContext(),InApp.class);
            startActivity(intentReg);
        } else {
            Toast.makeText(Login.this, "Pot Iniciar sessio", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
    public String getError(){
     return error;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//barra per tirar enrrera
        registre = findViewById(R.id.tRegistre);
        pwd = (EditText) findViewById(R.id.EdPwd);
        email = (EditText) findViewById(R.id.EdEmail);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.tInici);
        status = findViewById(R.id.tStatus);
        //click en el boto regitre carrga activity
        registre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(getApplicationContext(),Registre.class);
                startActivity(intentReg);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guarda els valors del editText
                String mail = email.getText().toString().trim();
                String pss = pwd.getText().toString().trim();

                //comprova si el mail esta buit
                if(TextUtils.isEmpty(mail)){
                    status.setText("Email es necesari");
                    email.setError("Email es necesari");
                    cerrarTeclado();
                    return;
                }
                //comprova si el password esta buid
                if(TextUtils.isEmpty(pss)){
                    status.setText("La contrasenya es necesaria");
                    pwd.setError("La contrasenya es necesaria");
                    cerrarTeclado();
                    return;
                    //comprova si el password es mes llarg que 6
                } else if (!TextUtils.isEmpty(pss) && !isPasswordValid(pss)) {
                    status.setText("Contrasenya massa curta");
                    pwd.setError("Contrasenya massa curta");
                    cerrarTeclado();
                }

                //metode de la llibreria de firebase que fa el login
                mAuth.signInWithEmailAndPassword(mail,pss).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //conecta amb el firebase
                        if(task.isSuccessful()){
                            status.setText("Login correcte");
                            if(status.getText() == "Login correcte"){
                                Toast.makeText(Login.this, "Benvingut", Toast.LENGTH_LONG).show();
                                Intent intentReg = new Intent(getApplicationContext(),InApp.class);
                                startActivity(intentReg);
                                finish();

                            }
                        //si no connecta
                        }else{
                            Toast.makeText(Login.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            error = "Error "+ task.getException().getMessage();
                            status.setText(error);
                            cerrarTeclado();
                        }
                    }
                });

            }
        });
    }
    //tanca el teclat
    private void cerrarTeclado() {
        View v = this.getCurrentFocus();
        if(v != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);

        }

    }

}