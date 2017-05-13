package top.zibin.vienna.example;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
  private List<AudioInfo> mAudioInfoList = new ArrayList<>();

  public AudioAdapter(List<AudioInfo> audioInfoList) {
    mAudioInfoList = audioInfoList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.playControl.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
  }

  @Override public int getItemCount() {
    return mAudioInfoList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView playControl;

    public ViewHolder(View itemView) {
      super(itemView);
      playControl = (ImageView) itemView.findViewById(R.id.play_control);
    }
  }
}
