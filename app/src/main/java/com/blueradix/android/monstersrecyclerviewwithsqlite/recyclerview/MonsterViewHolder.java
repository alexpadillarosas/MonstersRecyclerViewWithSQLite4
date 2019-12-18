package com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueradix.android.monstersrecyclerviewwithsqlite.activities.AddMonsterScrollingActivity;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.blueradix.android.monstersrecyclerviewwithsqlite.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;




/**
 * We create this view holder representing the recycler_item_view.xml
 * The idea of this class is to create a class that can manipulate the view
 */
public class MonsterViewHolder extends RecyclerView.ViewHolder {

//    private Monster currentMonster;

    //binding recycler_item_view.xml elements

    public final ImageView monsterImageView;
    public final TextView monsterName;
    public final TextView monsterDescription;

    public MonsterViewHolder(@NonNull View itemView) {
        super(itemView);

        monsterImageView = itemView.findViewById(R.id.monsterImageViewAddMonster);
        monsterName = itemView.findViewById(R.id.monsterName);
        monsterDescription = itemView.findViewById(R.id.monsterDescription);

        //to do something when touching the cardview
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here call the details activity
                Snackbar.make(v, "this Monster is at position " + getAdapterPosition(), Snackbar.LENGTH_SHORT).show();
//                callAddMonsterScrollingActivityToModify(v, getAdapterPosition(), currentMonster);

            }
        });

    }

    private void callAddMonsterScrollingActivityToModify(View v, int adapterPosition, Monster monster) {
        Intent goToModifyMonster = new Intent( v.getContext(), AddMonsterScrollingActivity.class);


//        goToModifyMonster.putExtra(cu)

    }

    /**
     * Method used to update the data of the ViewHolder of a particular monster
     * @param monster       The monster object containing the data to populate the correspondent MonsterViewHolder
     */
    public void updateMonster(Monster monster){

        Picasso.get().load("file:///android_asset/monsters/" + monster.getImageFileName().substring(3) + ".png").into(monsterImageView);
        this.monsterName.setText(monster.getName());
        //Tags are essentially an extra piece of information that can be associated with a view.
        //They are most often used as a convenience to store data related to views in the views
        //themselves rather than by putting them in a separate structure.
        this.monsterName.setTag(monster.getId());
        this.monsterDescription.setText(monster.getDescription());
    }

//    public void bind(Monster monster, OnItemClickListener listener){
//        this.
//    }


}
