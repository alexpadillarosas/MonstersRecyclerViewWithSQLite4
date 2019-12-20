package com.blueradix.android.monstersrecyclerviewwithsqlite;

import android.content.Intent;
import android.os.Bundle;

import com.blueradix.android.monstersrecyclerviewwithsqlite.activities.AddMonsterScrollingActivity;
import com.blueradix.android.monstersrecyclerviewwithsqlite.activities.MonsterDetailScrollingActivity;
import com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview.OnMonsterListener;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.blueradix.android.monstersrecyclerviewwithsqlite.database.MonsterDatabaseHelper;
import com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview.MonsterRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import static com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Constants.ADD_MONSTER_ACTIVITY_CODE;
import static com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Constants.VIEW_DETAILS_ACTIVITY_CODE;

//find Material design icons at:  https://material.io/resources/icons/?icon=cancel&style=baseline

public class MainActivity extends AppCompatActivity implements OnMonsterListener {

    List<Monster> monsters;
    MonsterRecyclerViewAdapter adapter;
    MonsterDatabaseHelper database;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //we set back the original AppTheme, to stop showing the splash as background image of this activity
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add a new Monster
                addNewMonster();
            }
        });

        rootView = findViewById(android.R.id.content).getRootView();

        RecyclerView monstersRecyclerView = findViewById(R.id.monstersRecyclerView);

        //set the layout manager
        //monstersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        monstersRecyclerView.setLayoutManager(gridLayoutManager);

        //monstersRecyclerView.setLayoutManager(linearLayoutManager);

        //Load Data from the database
        database = MonsterDatabaseHelper.getInstance(this);
        monsters = database.getMonsters();
        //create adapter passing the data, and the context
        adapter = new MonsterRecyclerViewAdapter(monsters, this, this);
        //attach the adapter to the Recyclerview
        monstersRecyclerView.setAdapter(adapter);

    }

    private void addNewMonster() {
        Intent goToCreateMonster = new Intent(this, AddMonsterScrollingActivity.class);
        //open an activity waiting for a result
        startActivityForResult(goToCreateMonster, ADD_MONSTER_ACTIVITY_CODE);

    }

    private void showMonsterDetail(Monster monster){
        //here redirect to the new activity and pass the monster
        Intent goToMonsterDetail = new Intent(this, MonsterDetailScrollingActivity.class);

        goToMonsterDetail.putExtra(Monster.MONSTER_KEY, monster);
        //open an activity
        startActivityForResult(goToMonsterDetail, VIEW_DETAILS_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String message;
        if(requestCode == ADD_MONSTER_ACTIVITY_CODE){
            if(resultCode == RESULT_OK){
                addMonster(data);
            }
        }else if (requestCode == VIEW_DETAILS_ACTIVITY_CODE){
            if(resultCode == RESULT_OK){
                modifyMonster(data);
            }
        }

    }

    private void addMonster(@Nullable Intent data) {
        String message;
        Monster monster = (Monster) data.getSerializableExtra(Monster.MONSTER_KEY);
        //insert the monster into the DB

        Long result =  database.insert(monster.getName(), monster.getDescription(), monster.getScariness());
        //result holds the autogenerated id in the table
        if(result != -1) {
            monster = database.getMonster(result);
            //try Always to use the adapter to modify the elements of your RecyclerView
            adapter.addItem(monster);
            message = getString(R.string.create_monster_succeeded_message);
        }else{
            message = getString(R.string.create_monster_failed_message);
        }
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    private void modifyMonster(@Nullable Intent data) {
        Integer stars;
        Long id;
        if(data.hasExtra(Monster.MONSTER_STARS) && data.hasExtra(Monster.MONSTER_ID)) {
            Monster monster = (Monster) data.getSerializableExtra(Monster.MONSTER_KEY);
            stars = data.getExtras().getInt(Monster.MONSTER_STARS);
            id = data.getExtras().getLong(Monster.MONSTER_ID);
            if(stars > 0){
                //update the stars for the monster
                boolean result = database.rateMonster(id, stars );
                //find the monster in the list
                int position = adapter.getMonsters().indexOf(monster);
                //if not found it, position is -1
                if(position >= 0) {
                    //load monster from database
                    monster = database.getMonster(id);
                    //replace the old monster with the new one, having the data modified
                    adapter.replaceItem(position, monster);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMonsterClick(Monster monster) {

        showMonsterDetail(monster);
    }
}
