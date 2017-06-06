package top.zibin.vienna.record;

import android.support.annotation.UiThread;

import java.io.File;

public interface OnRecordListener {

  @UiThread
  void onRelease(File audioFile);

  @UiThread
  void onAmplitudeChange(int amplitude);
}