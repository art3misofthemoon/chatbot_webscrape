package com.example.chatbot;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class fragmenta extends Fragment {
    private DatabaseHelper dbHelper;
    public fragmenta() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenta, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            String userEmail = bundle.getString("user_email", "");

            UserProfile userProfile = dbHelper.getUserProfile(userEmail);

            if (userProfile != null) {
                TextView nameTextView = view.findViewById(R.id.textView9);
                nameTextView.setText(userProfile.getName());
            }
        }

        return view;


    }
}
