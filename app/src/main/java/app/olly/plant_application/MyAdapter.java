package app.olly.plant_application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.olly.plant_application.sdata.Plant;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {
    private List<Plant> mData;
    ItemClickListener itemClickListener;
    private LayoutInflater mInflater;

    public MyAdapter(Context context, List<Plant> mData) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item, viewGroup,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Plant plant = getItem(i);
        viewHolder.setPlant(plant);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setOnClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    Plant getItem(int id) {
        return mData.get(id);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Plant plant;
        TextView TextViewName;
        ImageView imageView;
        TextView TextViewPlantType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TextViewName =  itemView.findViewById(R.id.plant_name);
            imageView = itemView.findViewById(R.id.plant_image);
            TextViewPlantType = itemView.findViewById(R.id.plant_type);

            itemView.setOnClickListener(this);
        }

        public void onClick(View view){
            if(itemClickListener != null) {
                itemClickListener.onItemCLick(view, getAdapterPosition());
            }
        }

        /**
         * for setting plant to viewHolder
         * @param plant
         */
        public void setPlant(Plant plant) {
            this.plant = plant;
            if (TextViewName == null || imageView == null || TextViewPlantType == null) return;
            updatePlant();
        }

        /**
         * for updating view regarding to plants attributes
         */
        public void updatePlant() {
            TextViewName.setText(plant.getName());
            TextViewPlantType.setText(plant.getPlantType().getName());
            imageView.setImageResource(plant.getImage());
        }

    }

    interface ItemClickListener {
        void onItemCLick(View view, int pos);
    }
}
