package com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueradix.android.monstersrecyclerviewwithsqlite.Entities.Monster;
import com.blueradix.android.monstersrecyclerviewwithsqlite.R;

import java.util.List;

public class MonsterRecyclerViewAdapter extends RecyclerView.Adapter<MonsterViewHolder> {

    private List<Monster> monsters;
    private Context context;

    public MonsterRecyclerViewAdapter(List<Monster> monsters, Context context) {
        this.monsters = monsters;
        this.context = context;
    }

    //creates a view holder whenever the RecyclerView needs a new one, it creates a view holder
    // from the recycler_item_view xml
    //This is the moment when the row layout is inflated (grab the xml and turning it into GUI),
    // passed to the ViewHolder object, and
    //each child view can be found and stored.
    @NonNull
    @Override
    public MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);

        View monsterView = inflater.inflate(R.layout.recycler_item_view, parent, false);

        //Return a new holder instance.
        MonsterViewHolder monsterViewHolder = new MonsterViewHolder(monsterView);
        return monsterViewHolder;
    }


    //Takes a ViewHolder object and sets the proper list data (from the list) on the view
    @Override
    public void onBindViewHolder(@NonNull MonsterViewHolder holder, int position) {
        //get the data from the list, based on position
        Monster monster = monsters.get(position);
        // call the method to set the values in the MonsterViewHolder
        holder.updateMonster(monster);
    }

    //returns the total number of the list size. The list values are passed by the constructor
    @Override
    public int getItemCount() {
        return monsters.size();
    }
}
