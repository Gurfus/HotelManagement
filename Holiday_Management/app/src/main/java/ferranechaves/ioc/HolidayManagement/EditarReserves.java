package ferranechaves.ioc.HolidayManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditarReserves extends AppCompatActivity {
// Variables
    private ListView listview;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mdatabase;
    FirebaseUser user ;
    String key,info,idReserva ;
    ArrayList<String> totsHores = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<Reserva> Reserves = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_reserves);
        //Constructor variables
        listview = findViewById(R.id.listRes);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mdatabase = database.getReference("Reserves");//branca reserves
        key = mdatabase.child("Reserves").push().getKey();// genera una clau id per cada reserva
        user = mAuth.getCurrentUser();
        reserves();

        // si es clica en un item del list view es podra borrar
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Alerta amb opcio a cancelar o a estar d'acord amb l'eliminacio
                AlertDialog.Builder ok = new AlertDialog.Builder(EditarReserves.this);
                ok.setTitle("Eliminar Reserva");
                ok.setMessage(totsHores.get(position));
                ok.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                ok.setPositiveButton("D'acord", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Borra el item clitat
                        mdatabase.child(keys.get(position)).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(EditarReserves.this,"Reserva Anulada",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(getIntent());
                            }
                        });


                    }
                });
                ok.create();
                ok.show();

            }
        });



    }
    public void reserves() {
        //envia a la base de dades la info capturada

        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
// Busca totes les reserves de la base de dades
                    int i = 0;
                    String key = dataSnapshot.getKey();
                    String dia = dataSnapshot.child("DiaReserva").getValue().toString();
                    String durada = dataSnapshot.child("Durada").getValue().toString();
                    String hFi = dataSnapshot.child("HoraFi").getValue().toString();
                    String hIni = dataSnapshot.child("HoraInici").getValue().toString();
                    String idPis = dataSnapshot.child("IdPistas").getValue().toString();
                    String idU = dataSnapshot.child("IdUsuaris").getValue().toString();
                    Reserva res = new Reserva();
                    res.setDia(dia);
                    res.setDurada(durada);
                    res.setHoraFi(hFi);
                    res.setHoraIni(hIni);
                    res.setIdPista(idPis);
                    res.setIdUser(idU);
                    res.setId(key);
                    Reserves.add(res);
                }
                reservesUsuari(Reserves);
                //Carrega el adapter amb les hores de aquest usuari
                if(totsHores.size() > 0){
                  adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,totsHores);
                  listview.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Aixo", error.getMessage());
            }
        });
    }
    public ArrayList<String> reservesUsuari(ArrayList<Reserva> reservas){

       for(int i = 0; i < reservas.size();i++){
//Busca en totes les reserves, las que estan al nom del userId
           if (reservas.get(i).getIdUser().equals(user.getUid())){
                // estructura de la info enviada al listView i arrayList per guarda les keys de les reverves
               info = reservas.get(i).getDia() + ", PISTA: "+ reservas.get(i).getIdPista() +", HORA: " + reservas.get(i).getHoraIni() + "- " + reservas.get(i).getHoraFi();
               keys.add(reservas.get(i).getId()) ;
               totsHores.add(info);
           }
       }
        return totsHores;

    }


}