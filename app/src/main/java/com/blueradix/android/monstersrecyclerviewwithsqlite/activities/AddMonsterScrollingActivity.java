package com.blueradix.android.monstersrecyclerviewwithsqlite.activities;

import android.content.Intent;
import android.os.Bundle;

import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Constants;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.blueradix.android.monstersrecyclerviewwithsqlite.R;

public class AddMonsterScrollingActivity extends AppCompatActivity {



    EditText monsterNameEditText;
    EditText monsterDescriptionEditText;
    SeekBar scarinessSeekBar;
    Button cancelMonsterButton;
    Button addMonsterButton;

    Monster monster;
    Integer scarinessValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monster_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        monsterNameEditText = findViewById(R.id.monsterNameEditTextAddMonster);
        monsterDescriptionEditText = findViewById(R.id.monsterDescriptionEditTextAddMonster);
        scarinessSeekBar = findViewById(R.id.scarinnessSeekBarAddMonster);
        cancelMonsterButton = findViewById(R.id.cancelButtonAddMonster);
        addMonsterButton = findViewById(R.id.addButtonAddMonster);

        cancelMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(v);
            }
        });

        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(v);
            }
        });

        scarinessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scarinessValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void add(View v) {
        //get values from the layout
        String monsterName = monsterNameEditText.getText().toString();
        String monsterDescription = monsterDescriptionEditText.getText().toString();

        if(monsterName.isEmpty()) {
            Snackbar.make(v, getString(R.string.monster_name_is_required), Snackbar.LENGTH_SHORT).show();
            return;
        }
        monster = new Monster();
        monster.setName(monsterName);
        monster.setDescription(monsterDescription);
        monster.setScariness(scarinessValue);

        //set the intent to return the monster to the caller Activity
        Intent goingBack = new Intent();
        goingBack.putExtra(Monster.MONSTER_KEY, monster);
        setResult(RESULT_OK, goingBack);
        finish();

    }

    private void cancel(View v) {
        finish();
    }
}
