package top.zibin.vienna.example;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
  private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x1001;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initPermission();
  }

  @TargetApi(Build.VERSION_CODES.M)
  private void initPermission() {
    if (ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[]{
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.READ_EXTERNAL_STORAGE,
              Manifest.permission.RECORD_AUDIO
          },
          MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }
  }

  @Override public void onBackPressed() {
    android.os.Process.killProcess(android.os.Process.myPid());
  }
}
