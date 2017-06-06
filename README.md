# Vienna

`Vienna` 是一款安卓音频操作工具，主要提供音频录制与播放的功能。提供简洁的接口降低音频开发的成本。

## 主要功能

* 音频录制提供了分段存储功能：录音时每隔一段时间输出该时段音频。（适合目前一些讲课软件app讲师录音的时候不间断讲课时录制的音频文件太大时间太长的问题）；

## 使用

* 开始录音

```java
// 设置录音时每隔多长输出文件（单位/s），不设置则不开启分段录音
Vienna.INSTANCE.setSectionTime(10);
// 设置最大音量标识（例如微信录音时弹出的录音标识有8个格子）
Vienna.INSTANCE.setMaxVol(10);
// 开始录音
Vienna.INSTANCE.startRecord(getContext());
// 监听录音过程的各种状态
Vienna.INSTANCE.setOnRecordListener(new OnRecordListener() {
  @Override public void onRelease(File audioFile) {
    // vienna 所有录音文件都通过该回调方法输出
  }

  @Override public void onAmplitudeChange(int amplitude) {
    // 录音音量变化的回调方法
  }
});
```

* 结束录音
```java
Vienna.INSTANCE.stopRecord();
```

* 销毁资源 在app生命周期合适的位置调用该方法销毁计时线程等资源
```java
Vienna.INSTANCE.destroy();
```

#License

    Copyright 2017 Zheng Zibin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
