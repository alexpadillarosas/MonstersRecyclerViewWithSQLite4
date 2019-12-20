package com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.blueradix.android.monstersrecyclerviewwithsqlite.R;

import java.util.List;

public class MonsterRecyclerViewAdapter extends RecyclerView.Adapter<MonsterViewHolder> {

    private List<Monster> monsters;
    private Context context;
    private OnMonsterListener onMonsterListener;


    /**
     * Constructor receiving the datasource of the recyclerView monsters list, and the context of the caller.
     * @param monsters      List of monsters to display in the recyclerView
     * @param context
     */
    public MonsterRecyclerViewAdapter(List<Monster> monsters, Context context, OnMonsterListener onMonsterListener) {
        this.monsters = monsters;
        this.context = context;
        this.onMonsterListener = onMonsterListener;
    }

    /**
     * Creates a view holder whenever the RecyclerView needs a new one, it creates a view holder(data in one element of the recyclerView).
     * This is the moment when the row layout is inflated (grab the recycler_item_view.xml and turning it into GUI component).
     * Creates a new view Holder(MonsterViewHolder in this case) by passing the recently inflated view (recycler_item_view.xml in this case)
     *
     * @param parent        The ViewGroup into which the new View will be added after it is bound to
     *                      an adapter position.
     * @param viewType
     * @return      The MonsterViewHolder ready to hold data of one Monster
     */
    @NonNull
    @Override
    public MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);

        View monsterView = inflater.inflate(R.layout.recycler_item_view, parent, false);

        //Return a new holder instance.
        MonsterViewHolder monsterViewHolder = new MonsterViewHolder(monsterView, onMonsterListener);
        return monsterViewHolder;
    }

    /**
     * Takes a ViewHolder object and sets the proper list data (from the list) on the view
     * @param holder    an object of MonsterViewHolder class, representing each item (CardView content)
     *                  in the recyclerView
     * @param position  the position of the monster in the monsters list
     */
    @Override
    public void onBindViewHolder(@NonNull MonsterViewHolder holder, int position) {
        //get the data from the list, based on position
        Monster monster = monsters.get(position);
        // call the method to set the values in the MonsterViewHolder
        holder.updateMonster(monster);

        holder.bind(monster, onMonsterListener);
    }

    /**
     * @return  returns the total number of the list size. The list values are passed by the constructor
     */
    @Override
    public int getItemCount() {
        return monsters.size();
    }

    /**
     * Add a monster to the list of monsters shown by the Adapter ( monsters )
     * @param monster   Monster to be added to the recyclerView
     */
    public void addItem(Monster monster) {
        monsters.add(monster);
        //notify the recyclerView a new element was added to its source ( monsters list )
        notifyItemInserted(getItemCount());
    }

    public void replaceItem(int position, Monster monster) {
        monsters.set(position, monster);
        notifyItemChanged(position);
    }

    public List<Monster> getMonsters() {
        return monsters;
    }
}
