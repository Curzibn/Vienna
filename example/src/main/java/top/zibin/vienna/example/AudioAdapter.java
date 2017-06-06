package top.zibin.vienna.example;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioHolder> {
  private Context mContext;
  private MediaPlayer mPlayer;

  private List<AudioInfo> mAudioInfoList = new ArrayList<>();

  public AudioAdapter(List<AudioInfo> audioInfoList) {
    mAudioInfoList = audioInfoList;
  }

  @Override public AudioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    return new AudioHolder(LayoutInflater.from(mContext).inflate(R.layout.item_audio, parent, false));
  }

  @Override public void onBindViewHolder(AudioHolder holder, int position) {
    holder.bindData(mAudioInfoList.get(position));
  }

  @Override public int getItemCount() {
    return mAudioInfoList.size();
  }

  class AudioHolder extends RecyclerView.ViewHolder {
    private ImageView playControl;

    AudioHolder(View itemView) {
      super(itemView);
      playControl = (ImageView) itemView.findViewById(R.id.play_control);
    }

    void bindData(final AudioInfo audioInfo) {
      playControl.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          startPlay(audioInfo.file);
        }
      });
    }

    private void startPlay(String audio) {
      if (mPlayer == null) {
        mPlayer = new MediaPlayer();

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);
        audioManager.setMode(AudioManager.MODE_NORMAL);
      }

      try {
        mPlayer.reset();
        mPlayer.setDataSource(audio);
        mPlayer.prepare();
        mPlayer.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
