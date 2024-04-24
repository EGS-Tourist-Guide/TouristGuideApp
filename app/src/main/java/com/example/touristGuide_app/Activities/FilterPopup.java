package com.example.touristGuide_app.Activities;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristGuide_app.Adapters.CategoryAdapter;
import com.example.touristGuide_app.Domains.CategoryDomain;
import com.example.touristGuide_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterPopup extends DialogFragment {
    private RecyclerView.Adapter adapterCategory;
    private RecyclerView recyclerViewCategory;
    private int selectedCategoryIndex = -1;


    private OnLocationSelectedListener mListener;
    EditText editTextPostcode;
    private double latitude = 0; // Variável para armazenar a latitude recebida
    private double longitude = 0; // Variável para armazenar a longitude recebida

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // Inflar o layout do seu popup
        View view = inflater.inflate(R.layout.filter_popup, container, false);
        // Inicialize o RecyclerView
        initRecyclerView(view);
        // Acessar a EditText e armazená-la como uma variável de instância
        editTextPostcode = view.findViewById(R.id.editTextPostcode);

        // Configurar o SeekBar para mostrar o valor em Kms
        SeekBar seekBar = view.findViewById(R.id.seekBarRadius);
        final TextView textViewRadius = view.findViewById(R.id.textViewRadius);
        // Configurar o botão PIN do mapa para procurar cidade
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
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Retornar a view inflada
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            updateLocation(latitude, longitude); // Atualize as coordenadas no popup
        }
    }
    private void initRecyclerView(View view){
        // Initialize RecyclerView and adapter for categories
        ArrayList<CategoryDomain> catsList = new ArrayList<>();
        catsList.add(new CategoryDomain("Beaches", "cat1"));
        catsList.add(new CategoryDomain("Museums", "cat2"));
        catsList.add(new CategoryDomain("Forest", "cat3"));
        catsList.add(new CategoryDomain("Festivals", "cat4"));
        catsList.add(new CategoryDomain("Camps", "cat5"));

        recyclerViewCategory = view.findViewById(R.id.viewCat);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterCategory = new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCategory);

    }


    public void applyFilter(View view) {
        // Toast.makeText(this, "aplicado filtro", Toast.LENGTH_SHORT).show();
        // QUANDO O USER FIZER AS SUAS OPÇÕES DE FILTROS GUARDAMOS A INFO E CLICAMOS EM
        // APPLY E CHAMAMOS O ENDPOINT
        // COM OS INPUT SELECIONADOS NO FILTRO E MANDAMOS PARA RECEBER UMA LISTA DE IDS
        // DE EVENTOS
        dismiss(); // Fecha o popup após aplicar o filtro
    }

    // Método para atualizar as coordenadas exibidas no popup
    public void updateLocation(double latitude, double longitude) {
        // Obter o nome da localização usando geocodificação reversa
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Extrair a cidade ou outra parte do endereço que deseja
                String cityName = address.getLocality(); // Obtém o nome da cidade
                if (cityName != null && !cityName.isEmpty()) {
                    // Atualize a exibição das coordenadas e da localização
                    TextView textViewLocationName = getView().findViewById(R.id.textViewLocationName);
                    textViewLocationName.setText(String.format("Pick a location to visit:\n%s\nLatitude: %.6f\nLongitude: %.6f", cityName, latitude, longitude));

                    // Verifica se o código postal está disponível e preenche automaticamente o EditText
                    String postalCode = address.getPostalCode(); // Obtém o código postal
                    if (postalCode != null && !postalCode.isEmpty()) {
                        editTextPostcode.setText(postalCode);
                    } else {
                        // Se o código postal não estiver disponível, limpa o EditText
                        editTextPostcode.setText("YYYY-YYY");
                    }
                } else {
                    // Se a cidade não estiver disponível, exiba apenas as coordenadas
                    TextView textViewLocationName = getView().findViewById(R.id.textViewLocationName);
                    textViewLocationName.setText(String.format("Pick a location to visit:\nLatitude: %.6f\nLongitude: %.6f", latitude, longitude));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
}