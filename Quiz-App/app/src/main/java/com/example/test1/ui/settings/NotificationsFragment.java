package com.example.test1.ui.settings;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.test1.R;
import com.example.test1.databinding.FragmentNotificationsBinding;
import com.example.test1.databinder.holdername;
import com.example.test1.authentication.logicscreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationsFragment extends Fragment {
    FirebaseFirestore db;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button signout=binding.signout;

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), logicscreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        db= FirebaseFirestore.getInstance();
        Context context =getContext();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        Dialog dialogchange= new Dialog(context);
        dialogchange.setContentView(R.layout.activity_changepass);


        TextView name = binding.name;
        TextView email = binding.email;
        Button delete =binding.delete;
        Button change = binding.change;
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Window window = dialogchange.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // or specific size in pixels, e.g., 500
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // or specific size in pixels, e.g., 600
                    window.setAttributes(layoutParams);
                }
                dialogchange.show();
                Button change =dialogchange.findViewById(R.id.change);
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText old = dialogchange.findViewById(R.id.old);
                        EditText newp = dialogchange.findViewById(R.id.newp);
                        EditText renew =dialogchange.findViewById(R.id.renew);
                        if(newp.getText().toString().isEmpty()||newp.getText().toString().length()<4){
                            Toast.makeText(getActivity(), "Enter Better Password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newpass = newp.getText().toString();
                        String renewpass = renew.getText().toString();
                        if(newpass.equals(renewpass)){
                            user.updatePassword(newpass);
                        }
                    }
                });

            }
        });

        Dialog dialog= new Dialog(context);
        dialog.setContentView(R.layout.deleteaccount);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // or specific size in pixels, e.g., 500
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // or specific size in pixels, e.g., 600
                    window.setAttributes(layoutParams);
                }
                dialog.show();
                Button delete= dialog.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email=user.getEmail();
                        delete(email);
                        user.delete();
                        auth.signOut();
                        Intent intent = new Intent(getActivity(), logicscreen.class);
                        startActivity(intent);
                    }
                });





            }
        });

        String username= holdername.getInstance().getData().toString();
        String useremail =user.getEmail().toString();
        name.setText(username);
        email.setText(useremail);
        return root;
    }
    public void delete(String email){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(email.equals(document.getString("email"))){
                                    String documentId = document.getId();
                                    db.collection("users").document(documentId)
                                            .delete()
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("Firestore", "Document with email " + email + " deleted successfully.");
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("Firestore", "Error deleting document: ", e);
                                            });
                                    break;

                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}