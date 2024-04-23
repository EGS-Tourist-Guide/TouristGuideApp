package com.example.touristGuide_app.Activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.touristGuide_app.R;

public class FilterPopup extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar o layout do seu popup
        View view = inflater.inflate(R.layout.filter_popup, container, false);

        // Configurar o SeekBar para mostrar o valor em Kms
        SeekBar seekBar = view.findViewById(R.id.seekBarRadius);
        final TextView textViewRadius = view.findViewById(R.id.textViewRadius);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Converter o progresso do SeekBar para Kms
                float distanceInMeters = progress; // Valor máximo em metros
                float distanceInKms = distanceInMeters / 1000; // Converter para Kms

                // Atualizar o texto da descrição com o valor em Kms
                textViewRadius.setText("Define a radius to find one place: \n" + distanceInKms + " Kms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return view;
    }

    public void applyFilter(View view) {
        //Toast.makeText(this, "aplicado filtro", Toast.LENGTH_SHORT).show();
        // Lógica para aplicar o filtro quando o botão "Apply" for clicado
        dismiss(); // Fecha o popup após aplicar o filtro
    }



}

