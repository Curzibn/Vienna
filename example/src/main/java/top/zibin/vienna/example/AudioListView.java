package top.zibin.vienna.example;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AudioListView extends RelativeLayout {
  private RecyclerView recyclerView;
  private Button control;
  private ProgressBar progressBar;
  private TextView time;

  private AudioAdapter mAdapter;
  private List<AudioInfo> mAudios = new ArrayList<>();

  public AudioListView(Context context) {
    super(context);
  }

  public AudioListView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public AudioListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
    control = (Button) this.findViewById(R.id.record_control);
    progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
    time = (TextView) this.findViewById(R.id.time);

    mAdapter = new AudioAdapter(mAudios);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(mAdapter);
  }
}
