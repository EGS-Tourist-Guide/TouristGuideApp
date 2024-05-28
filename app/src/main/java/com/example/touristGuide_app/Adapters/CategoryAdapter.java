package com.example.touristGuide_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.touristGuide_app.R;
import com.example.touristGuide_app.Domains.CategoryDomain;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>  {
    private OnCategorySelectedListener listener; // Listener para comunicar a seleção
    ArrayList<CategoryDomain> items;
    private HashMap<String, Integer> categoryImageMap; // Mapeamento de nomes de categoria para IDs de recursos de imagem

    public CategoryAdapter(ArrayList<CategoryDomain> items, OnCategorySelectedListener listener) {
        this.items = items;
        this.listener = listener; // Inicializa o listener
        this.categoryImageMap = initializeCategoryImageMap(); // Inicialize o mapa de imagens
    }
    // Método para inicializar o mapeamento de imagens
    private HashMap<String, Integer> initializeCategoryImageMap() {
        HashMap<String, Integer> map = new HashMap<>();
        // Adicione entradas para cada categoria e seu ID de recurso de imagem correspondente
        map.put("nature", R.drawable.cat1);
        map.put("food", R.drawable.cat2);
        map.put("culture", R.drawable.cat3);
        map.put("shopping", R.drawable.cat4);
        map.put("landmarks", R.drawable.cat5);

        // Adicione mais entradas conforme necessário para outras categorias
        return map;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryDomain category = items.get(position);
        holder.titleTxt.setText(items.get(position).getTitle());
        // Obtenha o ID do recurso de imagem da categoria atual do mapa
        Integer drawableResourceId = categoryImageMap.get(category.getTitle());
        // Carregue a imagem usando Glide
        if (drawableResourceId != null) {
            Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.picImg);
        }
        // Atualiza a aparência com base no estado de seleção
        if (holder.isSelected) {
            // Altere a aparência do item selecionado aqui
            holder.itemView.setBackgroundResource(R.drawable.category_bg_selected);
        } else {
            // Volte à aparência padrão se não estiver selecionado
            holder.itemView.setBackgroundResource(R.drawable.category_bg);
        }
        // Configura o clique no item para alternar o estado de seleção
        holder.itemView.setOnClickListener(v -> {
            holder.isSelected = !holder.isSelected; // Alterna o estado de seleção
            notifyDataSetChanged(); // Notifica o adaptador de que os dados foram alterados para atualizar a aparência do item
            // Notifica o listener da categoria selecionada
            if (holder.isSelected) {
                listener.onCategorySelected(category.getTitle());
            } else {
                listener.onCategoryDeselected(category.getTitle());
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt;
        ImageView picImg;
        boolean isSelected; // Variável para rastrear o estado de seleção do item

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt=itemView.findViewById(R.id.titleTxt);
            picImg=itemView.findViewById(R.id.catImg);
            isSelected = false; // Inicializa como não selecionado
        }
    }
    // Interface para comunicar a seleção de categoria
    public interface OnCategorySelectedListener {
        void onCategorySelected(String category);
        void onCategoryDeselected(String category);
    }
}


