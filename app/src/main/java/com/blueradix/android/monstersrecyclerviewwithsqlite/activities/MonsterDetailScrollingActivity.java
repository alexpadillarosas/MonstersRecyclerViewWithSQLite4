package com.blueradix.android.monstersrecyclerviewwithsqlite.activities;

import android.content.Intent;
import android.os.Bundle;

import com.blueradix.android.monstersrecyclerviewwithsqlite.database.MonsterDatabaseHelper;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueradix.android.monstersrecyclerviewwithsqlite.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;

public class MonsterDetailScrollingActivity extends AppCompatActivity {

    private static final String TAG = MonsterDetailScrollingActivity.class.getName();
    private RatingBar ratingBar;
    private Integer rate = 0;
    private Monster monster = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_detail_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intentThatCalled = getIntent();

        if(intentThatCalled.hasExtra(Monster.MONSTER_KEY)) {
            monster = (Monster)intentThatCalled.getSerializableExtra(Monster.MONSTER_KEY);
        }else{
            Log.e(TAG, "Empty monster was passed");
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        ImageView monsterImageViewDetailActivity = findViewById(R.id.monsterImageViewDetailActivity);
        TextView monsterNameTextViewDetailActivity = findViewById(R.id.monsterNameTextViewDetailActivity);

        Picasso.get().load("file:///android_asset/monsters/" + monster.getImageFileName().substring(3) + ".png").into(monsterImageViewDetailActivity);
        monsterNameTextViewDetailActivity.setText(monster.getName());

        ratingBar = findViewById(R.id.monsterRatingBarDetailActivity);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = (int) ratingBar.getRating();
            }
        });



    }

    @Override
    public void onBackPressed() {
        setData();
        super.onBackPressed();
    }

    private void setData(){

        Intent goingBack = new Intent();
        goingBack.putExtra(Monster.MONSTER_KEY, monster);
        goingBack.putExtra(Monster.MONSTER_STARS, rate);
        goingBack.putExtra(Monster.MONSTER_ID, monster.getId());
        setResult(RESULT_OK, goingBack);
        finish();

    }
}
