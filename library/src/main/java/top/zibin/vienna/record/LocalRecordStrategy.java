package top.zibin.vienna.record;

import android.media.MediaRecorder;

import java.io.File;

public class LocalRecordStrategy implements BaseRecordStrategy {
  private static final int RECORDING_BITRATE = 32000;
  private static final int RECORDING_SAMPLING = 16000;

  private MediaRecorder mRecorder = new MediaRecorder();
  private File mRecordFile;

  private boolean isRecording = false;
  private long mStartTime;

  @Override public void startRecord() {
    mRecorder.release();
    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    mRecorder.setAudioChannels(1);
  }

  @Override public void stopRecord() {

  }

  @Override public void discardRecord() {

  }

  @Override public String getOutputFile() {
    return null;
  }

  @Override public void setOutputFile(File recordFile) {

  }

  @Override public String getRecordFileName() {
    return null;
  }

  @Override public boolean isRecording() {
    return false;
  }

  @Override public int getMaxAmplitude() {
    return 0;
  }
}
