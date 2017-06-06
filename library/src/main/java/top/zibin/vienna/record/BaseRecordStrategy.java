package top.zibin.vienna.record;

import java.io.File;

public interface BaseRecordStrategy {
  void startRecord();

  void stopRecord();

  void discardRecord();

  File getOutputFile();

  void setOutputFile(File recordFile);

  String getRecordFileName();

  boolean isRecording();

  int getMaxAmplitude();
}
