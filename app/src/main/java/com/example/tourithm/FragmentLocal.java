package com.example.tourithm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentLocal extends Fragment implements View.OnClickListener {
    MainScreen mainScreen;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainScreen = (MainScreen) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainScreen = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.localchoice, container, false);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
    }
}
