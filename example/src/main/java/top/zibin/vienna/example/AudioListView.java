package top.zibin.vienna.example;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.vienna.Vienna;
import top.zibin.vienna.record.OnRecordListener;

public class AudioListView extends RelativeLayout {
  private static final String TAG = "Vienna";

  private RecyclerView recyclerView;
  private Button control;
  private ProgressBar progressBar;
  private TextView volume;

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
    volume = (TextView) this.findViewById(R.id.volume);

    mAdapter = new AudioAdapter(mAudios);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(mAdapter);

    control.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (Vienna.INSTANCE.isRecording()) {
          stopRecord();
        } else {
          startRecord();
        }
      }
    });
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    Vienna.INSTANCE.destroy();
  }

  public void startRecord() {
    Vienna.INSTANCE.setSectionTime(10);
    Vienna.INSTANCE.setMaxVol(10);
    Vienna.INSTANCE.startRecord(getContext());
    Vienna.INSTANCE.setOnRecordListener(new OnRecordListener() {
      @Override public void onRelease(File audioFile) {
        mAudios.add(new AudioInfo(audioFile.getAbsolutePath(), false));
        mAdapter.notifyDataSetChanged();
      }

      @Override public void onAmplitudeChange(int amplitude) {
        volume.setText(getResources().getString(R.string.amplitude, amplitude));
        Log.d(TAG, String.valueOf(amplitude));
      }
    });

    progressBar.setVisibility(VISIBLE);
    control.setText(R.string.stop);
  }

  public void stopRecord() {
    Vienna.INSTANCE.stopRecord();

    progressBar.setVisibility(GONE);
    control.setText(R.string.begin);
  }
}
