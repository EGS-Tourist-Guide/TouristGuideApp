package com.example.touristGuide_app.Activities;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FilterPopup extends DialogFragment {
    private OnLocationSelectedListener mListener;
    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.mListener = listener;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLocationSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnLocationSelectedListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar o layout do seu popup
        View view = inflater.inflate(R.layout.filter_popup, container, false);
        // Configurar o SeekBar para mostrar o valor em Kms
        SeekBar seekBar = view.findViewById(R.id.seekBarRadius);
        final TextView textViewRadius = view.findViewById(R.id.textViewRadius);
        // Configurar o botão PIN para procurar cidade
        FloatingActionButton btnPin = view.findViewById(R.id.btnPin);
        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a atividade PickMap
                Intent intent = new Intent(getActivity(), PickMap.class);
                startActivityForResult(intent, 1);
            }
        });
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
        // QUANDO O USER FIZER AS SUAS OPÇÕES DE FILTROS GUARDAMOS A INFO E CLICAMOS EM APPLY E CHAMAMOS O ENDPOINT
        // COM OS INPUT SELECIONADOS NO FILTRO E MANDAMOS PARA RECEBER UMA LISTA DE IDS DE EVENTOS
        dismiss(); // Fecha o popup após aplicar o filtro
    }
    // Método para atualizar as coordenadas exibidas no popup
    public void updateLocation(double latitude, double longitude) {
        // Atualize a exibição das coordenadas aqui
        TextView textViewLocationName = getView().findViewById(R.id.textViewLocationName);
        System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
        textViewLocationName.setText("Latitude: " + latitude + ", Longitude: " + longitude);
    }
}

