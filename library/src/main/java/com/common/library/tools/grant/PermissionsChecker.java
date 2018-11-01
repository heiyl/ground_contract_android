package com.common.library.tools.grant;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.logging.LogManager;


/**
 * Created by 太极 on 2017/9/30.
 */

public class PermissionsChecker {

    public static boolean isPermissionGranted(Activity activity, String permission) {
        try {
            switch (permission) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage(activity);
                case Manifest.permission.RECORD_AUDIO:
                    return checkRecordAudio(activity);
                case Manifest.permission.CAMERA:
                    return checkCamera(activity);
                default:
                    return ActivityCompat.checkSelfPermission(activity, permission)
                            == PackageManager.PERMISSION_GRANTED;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkCamera(Activity activity) {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        } finally {
            if (mCamera != null) {
                try {
                    mCamera.stopPreview();
                    mCamera.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return isCanUse;
    }

    private static boolean checkRecordAudio(Activity activity) {
        int audioSource = MediaRecorder.AudioSource.MIC;
        // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
        int sampleRateInHz = 44100;
        // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        // 缓冲区字节大小
        int bufferSizeInBytes = 0;
        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING
                ) {
            // && audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED
            return false;
        }
//        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
//            //如果短时间内频繁检测，会造成audioRecord还未销毁完成，此时检测会返回RECORDSTATE_STOPPED状态，再去read，会读到0的size，所以此时默认权限通过
//            return true;
//        }
        byte[] bytes = new byte[1024];
        int readSize = audioRecord.read(bytes, 0, 1024);
        if (readSize == AudioRecord.ERROR_INVALID_OPERATION || readSize <= 0) {
            return false;
        }
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;

        return true;
    }

    private static boolean checkWriteStorage(Activity activity) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "qmtj_permi");
                if (!file.exists()) {
                    return file.createNewFile();
                } else {
                    return file.delete();
                }
            } else {//非挂载状态,小于6.0都返回true,6.0+正常
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return true;
                } else {
                    return (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

}
