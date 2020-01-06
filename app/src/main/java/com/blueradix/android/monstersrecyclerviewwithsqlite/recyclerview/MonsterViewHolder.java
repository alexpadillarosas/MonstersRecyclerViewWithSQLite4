package com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.blueradix.android.monstersrecyclerviewwithsqlite.R;
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
    public final RatingBar ratingBar;

    private OnMonsterListener onMonsterListener;

    public MonsterViewHolder(@NonNull View itemView, OnMonsterListener onMonsterListener) {
        super(itemView);

        monsterImageView = itemView.findViewById(R.id.monsterImageViewAddMonster);
        monsterName = itemView.findViewById(R.id.monsterNameRecyclerView);
        monsterDescription = itemView.findViewById(R.id.monsterDescriptionRecyclerView);
        ratingBar = itemView.findViewById(R.id.ratingBarMonsterRecyclerView);

        this.onMonsterListener = onMonsterListener;

    }

    /**
     * Method used to update the data of the ViewHolder of a particular monster
     * @param monster       The monster object containing the data to populate the correspondent MonsterViewHolder
     */
    public void updateMonster(final Monster monster){

        Picasso.get().load("file:///android_asset/monsters/" + monster.getImageFileName().substring(3) + ".png").into(monsterImageView);
        this.monsterName.setText(monster.getName());
        //Tags are essentially an extra piece of information that can be associated with a view.
        //They are most often used as a convenience to store data related to views in the views
        //themselves rather than by putting them in a separate structure.
        this.monsterName.setTag(monster.getId());
        this.monsterDescription.setText(monster.getDescription());
        float rate;
        if(monster.getVotes() > 0)
            rate = 1.0f * monster.getStars() / monster.getVotes();
        else
            rate = 0.0f;
        this.ratingBar.setRating(rate);
    }


    /**
     * Bind every monster with a listener, to be used when the user clicks a particular monster in the recyclerView
     * @param monster               monster in the view
     * @param onMonsterListener     listener to be called
     */
    public void bind(final Monster monster, final OnMonsterListener onMonsterListener) {
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMonsterListener.onMonsterClick(monster);
            }
        });
    }

}
