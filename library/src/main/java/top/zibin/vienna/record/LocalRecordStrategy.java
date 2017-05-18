package top.zibin.vienna.record;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class LocalRecordStrategy implements BaseRecordStrategy {
  private static final int RECORDING_BITRATE = 32000;
  private static final int RECORDING_SAMPLING = 16000;

  private MediaRecorder mRecorder = new MediaRecorder();
  private File mRecordFile;

  private boolean isRecording = false;
  private long mStartTime;

  @Override public void startRecord() {
    try {
      if (mRecorder != null) {
        mRecorder.release();
        mRecorder = null;
      }
      mRecorder = new MediaRecorder();
      mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
      mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
      mRecorder.setAudioChannels(1);
      mRecorder.setAudioSamplingRate(RECORDING_SAMPLING);
      mRecorder.setAudioEncodingBitRate(RECORDING_BITRATE);

      mRecorder.setOutputFile(mRecordFile.getAbsolutePath());
      mRecorder.prepare();
      isRecording = true;
      mRecorder.start();
      mStartTime = System.currentTimeMillis();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override public void stopRecord() {
    if (mRecorder == null) throw new IllegalStateException("Most init media recorder first!");

    mRecorder.stop();
    mRecorder.release();
    isRecording = false;
    if (mRecordFile != null && mRecordFile.exists() &&
        System.currentTimeMillis() - mStartTime < 899 &&
        mRecordFile.delete()) {
      isRecording = false;
    }
  }

  @Override public void discardRecord() {
    if (mRecorder == null) throw new IllegalStateException("Most init the media recorder first!");

    mRecorder.stop();
    mRecorder.release();
    if (mRecordFile != null && mRecordFile.exists() &&
        mRecordFile.delete()) {
      isRecording = false;
    }
  }

  @Override public File getOutputFile() {
    return mRecordFile;
  }

  @Override public void setOutputFile(File recordFile) {
    this.mRecordFile = recordFile;
  }

  @Override public String getRecordFileName() {
    return "Vienna_" + System.currentTimeMillis() + ".aac";
  }

  @Override public boolean isRecording() {
    return isRecording;
  }

  @Override public int getMaxAmplitude() {
    if (mRecorder == null) return 0;
    return mRecorder.getMaxAmplitude();
  }
}
