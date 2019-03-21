package app.olly.plant_application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import app.olly.plant_application.sdata.Plant;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {
    private List<Plant> mData;
    private ItemClickListener itemClickListener;
    private ItemLongClickListener longClickListener;

    private ItemLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(ItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }


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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        Plant plant;
        TextView TextViewName;
        ImageView imageView;
        TextView TextViewPlantType;
        TextView lastWaterDay;
        TextView nextWaterDay;
        View item;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            TextViewName =  itemView.findViewById(R.id.plant_name);
            imageView = itemView.findViewById(R.id.plant_image);
            TextViewPlantType = itemView.findViewById(R.id.plant_type);
            lastWaterDay = itemView.findViewById(R.id.last_date);
            nextWaterDay = itemView.findViewById(R.id.date_for_water);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
            itemView.setClickable(true);
            item = itemView;
        }


        /**
         * for setting plant to viewHolder
         * @param plant setted plant
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
            TextViewPlantType.setText(plant.getPlantTypeName());
            imageView.setImageResource(plant.getImage());
            lastWaterDay.setText("last water: " + Plant.DATE_FORMAT.format(plant.getLast_time()));
            Calendar c = new GregorianCalendar();
            c.setTime(plant.getLast_time());
            c.add(Calendar.DATE, plant.getPeriod());
            nextWaterDay.setText("next water: " + Plant.DATE_FORMAT.format(c.getTime()));
            if (plant.checkNeedWater()) {
                item.setBackgroundResource(R.color.ActiveListItem);
            } else {
                item.setBackgroundResource(R.color.White);
            }
        }


        @Override
        public boolean onLongClick(View view) {
            if(longClickListener != null) {
               return longClickListener.onLongItemCLick(view, getAdapterPosition(), plant.getId());
            }
            return true;
        }
        @Override
        public void onClick(View view){
            if(itemClickListener != null) {
                itemClickListener.onItemCLick(view, plant.getName(), plant.getId());
            }
        }
    }

    interface ItemClickListener {
        void onItemCLick(View view, String name, int id);
    }
    interface ItemLongClickListener{
        boolean onLongItemCLick(View view, int pos, int id);
    }

}
