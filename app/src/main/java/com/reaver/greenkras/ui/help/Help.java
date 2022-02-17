package com.reaver.greenkras.ui.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.reaver.greenkras.R;

public class Help extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ImageButton btnNameTree = view.findViewById(R.id.btnNameTree);
        ImageButton btnNameScrub = view.findViewById(R.id.btnNameScrub);
        ImageButton btnNameParterres = view.findViewById(R.id.btnNameParterres);
        ImageButton btnHealthTree = view.findViewById(R.id.btnHealthTree);
        ImageButton btnHealthScrub = view.findViewById(R.id.btnHealthScrub);
        ImageButton btnHealthParterres = view.findViewById(R.id.btnHealthParterres);
        btnNameParterres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Help_parterres_name.class);
                requireContext().startActivity(intent);
            }
        });
        btnNameScrub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Help_scrub_name.class);
                requireContext().startActivity(intent);
            }
        });
        btnNameTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Help_tree_name.class);
                requireContext().startActivity(intent);
            }
        });
        btnHealthTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new Help_health_tree();
                fragment.show(requireFragmentManager(), "custom");
            }
        });
        btnHealthScrub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new Help_health_scrub();
                fragment.show(requireFragmentManager(), "custom");
            }
        });
        btnHealthParterres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new Help_health_parterres();
                fragment.show(requireFragmentManager(), "custom");
            }
        });

        return view;
    }
}
