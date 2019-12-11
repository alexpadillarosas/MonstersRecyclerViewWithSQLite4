package com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueradix.android.monstersrecyclerviewwithsqlite.Entities.Monster;
import com.blueradix.android.monstersrecyclerviewwithsqlite.R;
import com.squareup.picasso.Picasso;



/**
 * We create this view holder representing the recycler_item_view.xml
 * The idea of this class is to create a class that can manipulate the view
 */
public class MonsterViewHolder extends RecyclerView.ViewHolder {

    //bind the recycler_item_view.xml elements
    public final ImageView monsterImageView;
    public final TextView monsterName;
    public final TextView monsterDescription;

    public MonsterViewHolder(@NonNull View itemView) {
        super(itemView);

        monsterImageView = itemView.findViewById(R.id.monsterImageView);
        monsterName = itemView.findViewById(R.id.monsterName);
        monsterDescription = itemView.findViewById(R.id.monsterDescription);

    }

    public void updateMonster(Monster monster){

        Picasso.get().load("file:///android_asset/monsters/" + monster.imageFileName.substring(3) + ".png").into(monsterImageView);
        this.monsterName.setText(monster.getName());
        this.monsterDescription.setText(monster.getDescription());
    }


}
