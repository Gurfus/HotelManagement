package ferranechaves.ioc.HolidayManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ferranechaves.ioc.HolidayManagement.dialeg.EditarDades;
import ferranechaves.ioc.HolidayManagement.dialeg.ReservaTenis;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class InApp extends AppCompatActivity {

    Button logOut ;
    FirebaseAuth mAuth;
    TextView mail,nom,cognom,telefon, edit, editarReserves,factura, borrarUser;
    FirebaseUser user;
    DatabaseReference mdata,mdataFactura;
    String cognomUser,nomUser,telefonUser;
    CardView cardPista,cardUbi ;
    ImageView img;
    Factura facturaObj;
    // Get a non-default Storage bucket
    FirebaseStorage storage ;
    // Create a storage reference from our app
    StorageReference storageRef ;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app);
        //Relacio de amb el disseny
        img = findViewById(R.id.imgUser);
        img.setImageResource(R.drawable.avatar);
        mail = findViewById(R.id.txtMail);
        nom = findViewById(R.id.txtNom);
        editarReserves = findViewById(R.id.textEditarReserves);
        cognom = findViewById(R.id.txtCognom);
        telefon = findViewById(R.id.txtTlf);
        logOut = findViewById(R.id.btnlogout);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mdata = database.getReference("Usuaris");
        mdataFactura = database.getReference("Factura");
        user = mAuth.getCurrentUser();
        id = mAuth.getCurrentUser().getUid();
        edit = findViewById(R.id.txtEditar);
        cardPista = findViewById(R.id.cardReservesTennis);
        cardUbi = findViewById(R.id.cardUbi);
        factura = findViewById(R.id.txtFactura);
        borrarUser = findViewById(R.id.txtborrar);
        storage = FirebaseStorage.getInstance("gs://proyecte-aff9d.appspot.com");
        storageRef = storage.getReference();

       mdata.child(id).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   cognomUser = snapshot.child("cognom").getValue().toString();
                   nomUser = snapshot.child("nom").getValue().toString();
                   telefonUser = snapshot.child("telefon").getValue().toString();
                   mail.setText("Benvingut: "+ user.getEmail());
                   nom.setText("Nom: "+ nomUser);
                   cognom.setText("Cognom: "+ cognomUser);
                   telefon.setText("Telèfon: "+ telefonUser);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.i("Error", error.getMessage());
           }
       });




        //tanca la sessió i fa intent a la login
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intentReg = new Intent(getApplicationContext(),Login.class);
                startActivity(intentReg);
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarDades dades = new EditarDades();
                dades.show(getSupportFragmentManager(),"Modificació dades");

            }
        });
        cardPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservaTenis reserva = new ReservaTenis();
                reserva.show(getSupportFragmentManager(),"Reserva pista tennis");
            }
        });
        cardUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Ubicacio.class);
                startActivity(intent);
            }
        });
//descarrega la factura
        factura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdataFactura.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            //es crea l'objecte factura
                            facturaObj = new Factura(snapshot.child("url").getValue().toString(),snapshot.child("userId").getValue().toString());
                          //si el userId es igual que l'usuari actual, la descarregara
                            if(facturaObj.getUserId().equals(user.getUid())){
                                storageRef.child(facturaObj.getUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //es crea la url del arxiu
                                        String url = uri.toString();
                                        download(InApp.this,facturaObj.url,".pdf",DIRECTORY_DOWNLOADS,url);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                });
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });
        borrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getUid();
                AlertDialog.Builder ok = new AlertDialog.Builder(InApp.this);
                ok.setTitle("Eliminar Usuari");
                ok.setMessage("Estas segur d'eliminar el teu usuari?");
                ok.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mdata.child(userId);
                                    Toast.makeText(InApp.this,"Usuari Eliminat",Toast.LENGTH_LONG).show();
                                    Intent intentReg = new Intent(InApp.this,Benvinguda.class);
                                    startActivity(intentReg);

                                }
                            }
                        });
                    }
                });
                ok.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ok.create();
                ok.show();
            }
        });



    }
    public void setEditarReserves(View v){
        Intent intent = new Intent(getApplicationContext(),EditarReserves.class);
        startActivity(intent);
    }
    //manager per fer el download natiu android
    public  void download(Context context, String fileName, String extencio, String destinacioDirectori,String Url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(Url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinacioDirectori,fileName+extencio);

        downloadManager.enqueue(request);

    }




}