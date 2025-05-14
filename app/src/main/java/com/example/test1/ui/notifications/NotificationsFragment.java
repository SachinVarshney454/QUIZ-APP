package com.example.test1.ui.notifications;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.test1.R;
import com.example.test1.databinding.FragmentNotificationsBinding;
import com.example.test1.databinder.holderemail;
import com.example.test1.databinder.holdername;
import com.example.test1.authentication.logicscreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NotificationsFragment extends Fragment {

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



            }
        });

        String username= holdername.getInstance().getData().toString();
        String useremail =user.getEmail().toString();
        name.setText(username);
        email.setText(useremail);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}