package ferranechaves.ioc.HolidayManagement.dialeg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ferranechaves.ioc.HolidayManagement.Login;
import ferranechaves.ioc.HolidayManagement.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarDades#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarDades extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    TextView guardar,statusRegis;
    EditText pwd,email,nom,telf,cognom;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mdatabase;
    FirebaseUser user ;
    String id ;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarDades() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialegEdit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarDades.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarDades newInstance(String param1, String param2) {
        EditarDades fragment = new EditarDades();
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



    private AlertDialog crearDialegEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_editar_dades,null);
        builder.setView(v);

        nom = (EditText) v.findViewById(R.id.txtnames);
        pwd = (EditText) v.findViewById(R.id.pwdR);
        email = (EditText) v.findViewById(R.id.userR);
        statusRegis = (TextView) v.findViewById(R.id.statusRegistre);
        telf = v.findViewById(R.id.userTelef);
        cognom = v.findViewById(R.id.userCognom);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mdatabase = database.getReference("Usuaris");
        user = mAuth.getCurrentUser();
        guardar = (TextView) v.findViewById(R.id.txtGuardar);
        id = mAuth.getCurrentUser().getUid();
        eventsGuardar();
        return builder.create();


    }
//funcio per guardar la nova info
    private void eventsGuardar() {
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nom.getText().toString().isEmpty()){
                   mdatabase.child(id).child("nom").setValue(nom.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {//modifica el nom
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                            //falta un toast, nom canviat
                           }
                       }
                   });


                }

                if (!telf.getText().toString().isEmpty()){
                    mdatabase.child(id).child("telefon").setValue(telf.getText().toString());//modifica el telefon

                }
                if (!cognom.getText().toString().isEmpty()){
                    mdatabase.child(id).child("cognom").setValue(cognom.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {//modifica el cognom
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }
                        }
                    });
                    
                }
                if (!pwd.getText().toString().isEmpty()){
                    user.updatePassword(pwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {//amb la llibreria de firebase canvia el pass
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mAuth.signOut();

                                Intent intentReg = new Intent(getActivity(),Login.class);
                                startActivity(intentReg);



                            }
                        }
                    });

                }
                dismiss();//tanca el dialog
            }

        });

    }

}