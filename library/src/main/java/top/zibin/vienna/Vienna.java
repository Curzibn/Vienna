package top.zibin.vienna;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import top.zibin.vienna.record.BaseRecordStrategy;
import top.zibin.vienna.record.LocalRecordStrategy;
import top.zibin.vienna.record.OnRecordListener;

public enum Vienna {
  INSTANCE();

  private static final String DEFAULT_DISK_CACHE_DIR = "audio_manager_disk_cache";
  private static final String TAG = "Vienna";

  private OnRecordListener mListener;

  private BaseRecordStrategy mRecorder;

  private int mSectionTime = 0;
  private int mSplitCount = 0;
  private int mMaxVol = 0x7FFF;

  private long lastPosition = 0;
  private Timer mTimer = null;
  private Handler mHandler = new Handler();

  Vienna() {
    this.mRecorder = new LocalRecordStrategy();
  }

  /**
   * Returns a file with a cache audio name in the private cache directory.
   *
   * @param context
   *     A context.
   */
  public File getAudioCacheFile(Context context) {
    if (getAudioCacheDir(context) != null) {
      return new File(getAudioCacheDir(context) + "/" + mRecorder.getRecordFileName());
    }
    return null;
  }

  /**
   * Returns a directory with a default name in the private cache directory of the application to
   * use to store retrieved audio.
   *
   * @param context
   *     A context.
   *
   * @see #getAudioCacheDir(android.content.Context, String)
   */
  @Nullable
  private File getAudioCacheDir(Context context) {
    return getAudioCacheDir(context, DEFAULT_DISK_CACHE_DIR);
  }

  /**
   * Returns a directory with the given name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context
   *     A context.
   * @param cacheName
   *     The name of the subdirectory in which to store the cache.
   *
   * @see #getAudioCacheDir(android.content.Context)
   */
  @Nullable
  private File getAudioCacheDir(Context context, String cacheName) {
    File cacheDir = context.getExternalCacheDir();
    if (cacheDir != null) {
      File result = new File(cacheDir, cacheName);
      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        // File wasn't able to create a directory, or the result exists but not a directory
        return null;
      }
      return result;
    }
    if (Log.isLoggable(TAG, Log.ERROR)) {
      Log.e(TAG, "default disk cache dir is null");
    }
    return null;
  }

  /**
   * Set up the section duration of record length
   *
   * @param sectionTime
   *     the section duration in seconds
   */
  public void setSectionTime(int sectionTime) {
    if (sectionTime < 0) throw new IllegalStateException("section duration can't less than 0");

    this.mSectionTime = sectionTime;
  }

  /**
   * Set up the max grade of your volume marker
   *
   * @param vol
   *     the max grade
   */
  public void setMaxVol(int vol) {
    if (mMaxVol < 0) throw new IllegalStateException("max volume can't less than 0");

    this.mMaxVol = vol;
  }

  private File splitAudio(long begin, long end) {
    try {
      String fileName = mRecorder.getOutputFile().getAbsolutePath();
      fileName = fileName.substring(0, fileName.lastIndexOf(".")) +
          "_" + ++mSplitCount +
          fileName.substring(fileName.lastIndexOf("."), fileName.length());
      final File result = new File(fileName);

      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(result));
      BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(mRecorder.getOutputFile()));

      int size = (int) (end - begin);
      byte[] buffer = new byte[size];

      if (inputStream.skip(begin) >= 0 && inputStream.read(buffer, 0, size) > 0) {
        outputStream.write(buffer);
        mHandler.post(new Runnable() {
          @Override public void run() {
            if (mListener != null) {
              mListener.onRelease(result);
            }
          }
        });
      }

      outputStream.close();
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * starting record
   */
  public void startRecord(Context context) {
    if (mRecorder.isRecording()) return;

    mRecorder.setOutputFile(getAudioCacheFile(context));
    mRecorder.startRecord();

    if (mTimer == null) initTimer();
  }

  private int time;

  private void initTimer() {
    TimerTask mTimerTask = new TimerTask() {
      @Override public void run() {
        time++;

        if (!isRecording()) return;

        if (time % (10 * mSectionTime) == 0) {
          if (mRecorder.getOutputFile() == null || !mRecorder.getOutputFile().exists()) {
            return;
          }

          long prevPosition = lastPosition;
          lastPosition = mRecorder.getOutputFile().length();
          splitAudio(prevPosition, lastPosition);
        }

        mHandler.post(new Runnable() {
          @Override public void run() {
            if (mListener != null) {
              mListener.onAmplitudeChange(mRecorder.getMaxAmplitude() * mMaxVol / 0x7FFF);
            }
          }
        });
      }
    };

    mTimer = new Timer();
    mTimer.schedule(mTimerTask, 100, 100);
  }

  /**
   * @return <code>true</code> if and only if the microphone was recording;
   * <code>false</code> otherwise
   */
  public boolean isRecording() {
    return mRecorder.isRecording();
  }

  public void stopRecord() {
    if (!isRecording()) return;

    mRecorder.stopRecord();

    if (mSectionTime > 0 && mListener != null) {
      mListener.onRelease(mRecorder.getOutputFile());
    } else {
      splitAudio(lastPosition, mRecorder.getOutputFile().length());
    }
  }

  public void discardRecord() {
    if (isRecording()) {
      mRecorder.discardRecord();
    }
  }

  public void setOnRecordListener(OnRecordListener mListener) {
    this.mListener = mListener;
  }

  public void destroy() {
    mListener = null;
    mTimer.cancel();
  }
}
