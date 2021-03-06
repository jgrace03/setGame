package hu.ait.setgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import hu.ait.setgame.adapter.ListRecyclerAdapter;


public class ScoreBoard extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoreboard);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerItem = (RecyclerView) findViewById(R.id.recyclerItem);
        recyclerItem.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerItem.setLayoutManager(linearLayoutManager);

        ListRecyclerAdapter listRecyclerAdapter = new ListRecyclerAdapter(this,
                ((MainApplication) getApplication()).getRealm());
        recyclerItem.setAdapter(listRecyclerAdapter);
    }
}
