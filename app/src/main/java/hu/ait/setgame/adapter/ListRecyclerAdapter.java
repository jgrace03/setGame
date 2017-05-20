package hu.ait.setgame.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import hu.ait.setgame.R;
import hu.ait.setgame.data.Game;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    private List<Game> gameList;
    private Context context;
    private Realm realmGame;

    public ListRecyclerAdapter(Context context, Realm realm) {
        this.context = context;
        this.realmGame = realm;

        RealmResults<Game> itemResult = realmGame.where(Game.class)
                .findAll()
                .sort("time", Sort.ASCENDING);

        gameList = new ArrayList<Game>();
        for (int i = 0; i < itemResult.size(); i++) {
            gameList.add(itemResult.get(i));
        }
    }

    @Override
    public ListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.score_row, parent, false
        );
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ListRecyclerAdapter.ViewHolder holder, int position) {

        holder.tvUsername.setText(gameList.get(position).getUserName());
        holder.tvScore.setText(String.valueOf(gameList.get(position).getTime()) + "s");
    }

    @Override
    public int getItemCount() {
        if (gameList != null) {
            return gameList.size();
        } else {
            return 0;
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private TextView tvScore;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvScore = (TextView) itemView.findViewById(R.id.tvScore);
        }
    }
}
