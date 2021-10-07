package ferranechaves.ioc.HolidayManagement.dialeg;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ferranechaves.ioc.HolidayManagement.R;
import ferranechaves.ioc.HolidayManagement.Reserva;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservaTenis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservaTenis extends DialogFragment implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    //declaracio variables
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Reserva reserva[] ;
    TextView reservar;
    ImageView img;
    int durada;
    EditText dia,horaI,horaF;
    Spinner idPista;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mdatabase;
    FirebaseUser user ;
    String id , pista;
    String key;
    ArrayList<String> totsHores = new ArrayList<>();
    ArrayList<Reserva> Reserves = new ArrayList<>();
    Calendar calendari = Calendar.getInstance();
    int diaC = calendari.get(Calendar.DAY_OF_MONTH);
    int mesC = calendari.get(Calendar.MONTH);
    int anyC = calendari.get(Calendar.YEAR);
    int horaCalcI;
    int minutsCalcI, minutsCalcF;
    int horaCalcF;
    boolean ocupatB, reservaFeta = false;
    Map <String, Object> mapReserva = new HashMap<>();

    Spinner spinner;
    String data;
    Boolean clickedI,clickedF  = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReservaTenis() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReservaTenis newInstance(String param1, String param2) {
        ReservaTenis fragment = new ReservaTenis();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialegEdit();
    } //criada per fer la estructura del dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserva_tenis, container, false);
    }

    private AlertDialog crearDialegEdit() {
        //inicialitzem el constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_reserva_tenis,null);
        builder.setView(v);
        spinner = (Spinner) v.findViewById(R.id.idPista);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pistes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        img = v.findViewById(R.id.imgPista);
        img.setImageResource(R.drawable.tennis);
        dia = (EditText) v.findViewById(R.id.diaRes);
        dia.setShowSoftInputOnFocus(false);
        horaI = (EditText) v.findViewById(R.id.horaIni);
        horaI.setShowSoftInputOnFocus(false);
        horaF = (EditText) v.findViewById(R.id.horaFin);
        horaF.setShowSoftInputOnFocus(false);
        idPista = v.findViewById(R.id.idPista);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mdatabase = database.getReference("Reserves");//branca reserves
        key = mdatabase.child("Reserves").push().getKey();
        user = mAuth.getCurrentUser();
        reservar = (TextView) v.findViewById(R.id.txtreservar);
        id = mAuth.getCurrentUser().getUid();
        Reserves = reserves();
        eventsReservar();
        return builder.create();


    }





    private void eventsReservar() {
        dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calendari per triar data
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
                    data = month + "/" + dayOfMonth +"/" + year;
                    dia.setText(data);
                }, anyC,mesC,diaC);
                dpd.show();
            }
        });



        //triar hora final amb un reloj
        horaF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!horaI.getText().toString().isEmpty()){
                        com.wdullaer.materialdatetimepicker.time.TimePickerDialog t = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(ReservaTenis.this::onTimeSet,Calendar.HOUR_OF_DAY,Calendar.MINUTE,Calendar.SECOND,true);
                        t.enableMinutes(false);
                        t.enableSeconds(false);
                        String [] horesLessMin = horaI.getText().toString().split(":");
                        int horap = Integer.parseInt(horesLessMin[0]);
                        t.setMinTime(horap +1 ,0,0);
                        t.setMaxTime(20,0,0);
                        t.show(getParentFragmentManager(),"TimePicker");
                        clickedF = true;

                    }else{
                        horaI.setError("Has de entrar primer la hora Inicial");


                    }

                }
            });




        horaI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                com.wdullaer.materialdatetimepicker.time.TimePickerDialog t = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(ReservaTenis.this::onTimeSet,Calendar.HOUR_OF_DAY,Calendar.MINUTE,Calendar.SECOND,true);
                t.enableMinutes(false);
                t.enableSeconds(false);
                t.setMaxTime(20,0,0);//rang de hores a escollir, per el rejotge
                t.setMinTime(8,0,0);
                t.show(getParentFragmentManager(),"TimePicker");
                clickedI = true;

            }

        });
        durada = horaCalcF - horaCalcI;

        reservar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //objecte amb les dades de la reserva introduida
                pista = spinner.getSelectedItem().toString();

                mapReserva.put("DiaReserva",data);
                mapReserva.put("Durada",horaCalcF - horaCalcI);
                mapReserva.put("HoraFi", horaCalcF+":"+"00");
                mapReserva.put("HoraInici",horaCalcI+":"+ "00");
                mapReserva.put("IdPistas",pista);
                mapReserva.put("IdUsuaris",user.getUid());
                String id = mAuth.getCurrentUser().getUid();
                if(TextUtils.isEmpty(horaF.getText().toString())){
                    horaF.setError("Hora final necesaria");
                    return;
                }
                if(TextUtils.isEmpty(horaI.getText().toString())){
                    horaI.setError("Hora inicial necesaria");
                    return;
                }
                if(TextUtils.isEmpty(dia.getText().toString())){
                    dia.setError("La data es necesaria");
                    return;
                }
                //perque no entri buit
                if(Reserves.size() > 0){
                    reservaCompletada(reserves());
                }





            }

        });
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
//gestio del rellotge
        if(clickedI == true){
            horaI.setText(hourOfDay + ":"+ "00");
            horaCalcI = hourOfDay;
            minutsCalcI = Integer.parseInt("00");

        }else if(clickedF == true){
            horaF.setText(hourOfDay + ":"+ "00");
            horaCalcF = hourOfDay;
            minutsCalcF = Integer.parseInt("00");
        }
        clickedF = false;
        clickedI = false;
    }
     public ArrayList reserves(){
         //guardem totes les reserves del firebase

         mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {

             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for( DataSnapshot dataSnapshot : snapshot.getChildren()){

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

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Log.d("Aixo",error.getMessage());
             }
         });
        return Reserves;
     }
     public  void reservaCompletada(ArrayList<Reserva> totesReverves){
//es comprova si la hora escollida no esta ocupada
         if(totesReverves !=null){
             for (int i = 0; i < totesReverves.size();i++){
                 if(totesReverves.get(i).getIdPista().equals(pista)){
                     if( totesReverves.get(i).getDia().equals(dia.getText().toString()) ){
                         int duraraRes =  Integer.parseInt(totesReverves.get(i).getDurada());
                         String [] horesLessMin = totesReverves.get(i).getHoraIni().split(":");
                         int horap = Integer.parseInt(horesLessMin[0]);
                         for(int k = 0; k <=duraraRes;k++){
                             String ocupa = String.valueOf(horap+k);
                             totsHores.add(ocupa + ":"+"00");
                         }

                     }

                 }
             }
             //comprova totes les hores trovades aquell dia i pista
             if(totsHores.size() > 0){
                 for(int i = 0; i <= totsHores.size();i++){
                     if(horaI.getText().toString().equals(totsHores.get(i))){
                         Toast.makeText(getContext(),"Reserva Ocupada",Toast.LENGTH_LONG).show();
                         AlertDialog.Builder ocupat = new AlertDialog.Builder(getActivity());
                         ocupat.setTitle("Hora Ocupada");
                         ocupat.setMessage(horaI.getText()+"- "+ horaF.getText());
                         ocupat.setPositiveButton("Tornar", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 ocupatB = true;
                                 reservaFeta = false;
                                dialog.dismiss();
                                dismiss();

                             }
                         });
                         ocupat.create();
                         ocupat.show();
                         return;

                     }else{
                         //si tot va be la reserva esfaraa
                         if (ocupatB == false){
                             mdatabase.child(key).setValue(mapReserva).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                         reservaFeta = true;
                                         if(reservaFeta == true){
                                             AlertDialog.Builder ok = new AlertDialog.Builder(getActivity());
                                             ok.setTitle("Hora Reservada");
                                             ok.setMessage(horaI.getText()+"- "+ horaF.getText());
                                             ok.setPositiveButton("D'acord", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     ocupatB = true;
                                                     reservaFeta = false;
                                                     dialog.dismiss();
                                                     dismiss();
                                                 }
                                             });
                                             ok.create();
                                             ok.show();
                                         }

                                         Toast.makeText(getContext(),"Reserva feta",Toast.LENGTH_LONG).show();
                                         ocupatB = false;

                                     }
                                 }
                             });


                         }
                     }
                 }
             }else{
                 //es fara la reserva
                 if (ocupatB == false){
                     mdatabase.child(key).setValue(mapReserva).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 reservaFeta = true;
                                 if(reservaFeta == true){
                                     AlertDialog.Builder ok = new AlertDialog.Builder(getActivity());
                                     ok.setTitle("Hora Reservada");
                                     ok.setMessage(horaI.getText()+"- "+ horaF.getText());
                                     ok.setPositiveButton("D'acord", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             ocupatB = true;
                                             reservaFeta = false;
                                             dialog.dismiss();
                                             dismiss();
                                         }
                                     });
                                     ok.create();
                                     ok.show();
                                 }

                                 Toast.makeText(getContext(),"Reserva feta",Toast.LENGTH_LONG).show();
                                 ocupatB = false;
                             }
                         }
                     });


                 }

             }


         }


     }


}