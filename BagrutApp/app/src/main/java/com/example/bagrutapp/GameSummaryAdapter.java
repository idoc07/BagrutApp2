package com.example.bagrutapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GameSummaryAdapter extends BaseAdapter {

    private Context context;
    private List<GameSummary> games;
    private LayoutInflater inflater;

    public GameSummaryAdapter(Context context, List<GameSummary> games) {
        this.context = context;
        this.games = games;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvMatchTitle, tvWinner, tvDuration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_game_summary, parent, false);
            holder = new ViewHolder();
            holder.tvMatchTitle = convertView.findViewById(R.id.tvMatchTitle);
            holder.tvWinner = convertView.findViewById(R.id.tvWinner);
            holder.tvDuration = convertView.findViewById(R.id.tvDuration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GameSummary game = games.get(position);

        holder.tvMatchTitle.setText(game.getPlayer1() + " vs " + game.getPlayer2());
        holder.tvWinner.setText("Winner: " + game.getWinner());
        holder.tvDuration.setText("Duration: " + game.getDuration());

        return convertView;
    }
}
