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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>  {
    ArrayList<CategoryDomain> items;

    public CategoryAdapter(ArrayList<CategoryDomain> items) {
        this.items = items;
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

        int drawableResoureId=holder.itemView.getResources().getIdentifier(items.get(position).getPicPath(),
                "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResoureId).into(holder.picImg);


        // Atualiza a aparência com base no estado de seleção
        if (holder.isSelected) {
            // Altere a aparência do item selecionado aqui
            holder.itemView.setBackgroundResource(R.drawable.category_bg_selected);
        } else {
            // Volte à aparência padrão se não estiver selecionado
            holder.itemView.setBackgroundResource(R.drawable.category_bg);
        }

        // Configura o clique no item para alternar o estado de seleção
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Atualiza o estado de seleção
                holder.isSelected = !holder.isSelected; // FLAG para alternar o estado de seleção
                // Notifica o adaptador de que os dados foram alterados para atualizar a aparência do item
                notifyDataSetChanged();
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
}
