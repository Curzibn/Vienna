package top.zibin.vienna;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

import top.zibin.vienna.record.BaseRecordStrategy;
import top.zibin.vienna.record.LocalRecordStrategy;

public enum Vienna {
  INSTANCE();

  private static final String DEFAULT_DISK_CACHE_DIR = "audio_manager_disk_cache";
  private static final String TAG = "Vienna";

  private int mSectionTime = 0;
  private BaseRecordStrategy mRecorder;

  Vienna() {
    this.mRecorder = new LocalRecordStrategy();
  }

  /**
   * Returns a directory with a default name in the private cache directory of the application to
   * use to store retrieved audio.
   *
   * @param context A context.
   * @see #getAudioCacheDir(android.content.Context, String)
   */
  @Nullable
  public static File getAudioCacheDir(Context context) {
    return getAudioCacheDir(context, DEFAULT_DISK_CACHE_DIR);
  }

  /**
   * Returns a directory with the given name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context   A context.
   * @param cacheName The name of the subdirectory in which to store the cache.
   * @see #getAudioCacheDir(android.content.Context)
   */
  @Nullable
  public static File getAudioCacheDir(Context context, String cacheName) {
    File cacheDir = context.getCacheDir();
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
   * set up the section duration of record length
   */
  public void setSectionTime(int sectionTime) {
    this.mSectionTime = sectionTime;
  }

  /**
   * starting record
   */
  public void startRecord(Context context) {
    if (!mRecorder.isRecording()) {
      mRecorder.setOutputFile(getAudioCacheDir(context));
      mRecorder.startRecord();
    }
  }
}
