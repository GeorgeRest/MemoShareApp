package com.george.memoshareapp.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Recordings;

import java.io.IOException;
import java.util.List;

public class GroupChatRecordAdapter extends RecyclerView.Adapter<GroupChatRecordAdapter.RecordViewHolder> {
    private List<Recordings> recordingsList;
    private Context context;

    public GroupChatRecordAdapter(Context context, List<Recordings> recordingsList) {
        this.recordingsList = recordingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
        return new RecordViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Recordings recordings = recordingsList.get(position);

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(recordings.getRecordCachePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                } catch (IOException e) {
                    Toast.makeText(context, "播放失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 设置其他视图的信息，例如录音的时长，文件大小等
        holder.record_time.setText((recordings.getRecordTime())+"'");

    }


    @Override
    public int getItemCount() {
        return recordingsList.size();
    }




    class RecordViewHolder extends RecyclerView.ViewHolder {
        Button playButton;
        TextView record_time;
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            playButton = itemView.findViewById(R.id.play_button); // 需要在你的布局文件中有一个Button，并且它的id是play_button
            record_time = itemView.findViewById(R.id.record_time);
        }
    }
}
