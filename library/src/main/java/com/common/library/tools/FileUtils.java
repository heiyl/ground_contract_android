package com.common.library.tools;

import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static String getAuthPath(Context context) {
        return context.getFilesDir() + "/auth_info.property";
    }

    /**
     * 递归删除文件。
     *
     * @param file
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 递归删除子文件。
     *
     * @param file
     */
    public static void RecursionDeleteFileChild(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }

        }
    }

    /**
     * 从url获取 如果url为空，则文件名为当前时间毫秒值
     *
     * @param url download file pkgUrl
     * @return file name
     */
    public static String getFileNameFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return System.currentTimeMillis() + "";
    }


    public static boolean deleteFile(Context context, String fileName) {
        return context.deleteFile(fileName);
    }

    public static boolean exists(Context context, String fileName) {
        return (new File(context.getFilesDir(), fileName)).exists();
    }

    public static boolean writeFile(Context context, String fileName, String content) {
        boolean success = false;
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, 0);
            byte[] e = content.getBytes();
            fos.write(e);
            success = true;
        } catch (FileNotFoundException var16) {
            var16.printStackTrace();
        } catch (IOException var17) {
            var17.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

        return success;
    }

    public static boolean writeFile(String filePath, String content) {
        boolean success = false;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(filePath);
            byte[] e = content.getBytes();
            fos.write(e);
            success = true;
        } catch (FileNotFoundException var15) {
            var15.printStackTrace();
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

        return success;
    }

    public static String readFile(Context context, String fileName) {
        if (!exists(context, fileName)) {
            return null;
        } else {
            FileInputStream fis = null;
            String content = null;

            try {
                fis = context.openFileInput(fileName);
                if (fis != null) {
                    byte[] e = new byte[1024];
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

                    while (true) {
                        int readLength = fis.read(e);
                        if (readLength == -1) {
                            fis.close();
                            arrayOutputStream.close();
                            content = new String(arrayOutputStream.toByteArray());
                            break;
                        }

                        arrayOutputStream.write(e, 0, readLength);
                    }
                }
            } catch (FileNotFoundException var17) {
                var17.printStackTrace();
            } catch (IOException var18) {
                var18.printStackTrace();
                content = null;
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException var16) {
                    var16.printStackTrace();
                }

            }

            return content;
        }
    }

    public static String readFile(String filePath) {
        if (filePath != null && (new File(filePath)).exists()) {
            FileInputStream fis = null;
            String content = null;

            try {
                fis = new FileInputStream(filePath);
                if (fis != null) {
                    byte[] e = new byte[1024];
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

                    while (true) {
                        int readLength = fis.read(e);
                        if (readLength == -1) {
                            fis.close();
                            arrayOutputStream.close();
                            content = new String(arrayOutputStream.toByteArray());
                            break;
                        }

                        arrayOutputStream.write(e, 0, readLength);
                    }
                }
            } catch (FileNotFoundException var16) {
                var16.printStackTrace();
            } catch (IOException var17) {
                var17.printStackTrace();
                content = null;
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException var15) {
                    var15.printStackTrace();
                }

            }

            return content;
        } else {
            return null;
        }
    }

    public static String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;

        try {
            is = context.getAssets().open(fileName);
            if (is != null) {
                byte[] e = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

                while (true) {
                    int readLength = is.read(e);
                    if (readLength == -1) {
                        is.close();
                        arrayOutputStream.close();
                        content = new String(arrayOutputStream.toByteArray());
                        break;
                    }

                    arrayOutputStream.write(e, 0, readLength);
                }
            }
        } catch (FileNotFoundException var17) {
            var17.printStackTrace();
        } catch (IOException var18) {
            var18.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

        return content;
    }

    public static boolean writeParcelable(Context context, String fileName, Parcelable parcelObject) {
        boolean success = false;
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, 0);
            Parcel e = Parcel.obtain();
            e.writeParcelable(parcelObject, 1);
            byte[] data = e.marshall();
            fos.write(data);
            success = true;
        } catch (FileNotFoundException var17) {
            var17.printStackTrace();
        } catch (IOException var18) {
            var18.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

        return success;
    }

    public static boolean writeParcelableList(Context context, String fileName, List<Parcelable> list) {
        boolean success = false;
        FileOutputStream fos = null;

        try {
            if (list instanceof List) {
                fos = context.openFileOutput(fileName, 0);
                Parcel e = Parcel.obtain();
                e.writeList(list);
                byte[] data = e.marshall();
                fos.write(data);
                success = true;
            }
        } catch (FileNotFoundException var17) {
            var17.printStackTrace();
        } catch (IOException var18) {
            var18.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

        return success;
    }

    public static Parcelable readParcelable(Context context, String fileName, ClassLoader classLoader) {
        Parcelable parcelable = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = context.openFileInput(fileName);
            if (fis != null) {
                bos = new ByteArrayOutputStream();
                byte[] e = new byte[4096];

                int bytesRead;
                while ((bytesRead = fis.read(e)) != -1) {
                    bos.write(e, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                parcelable = parcel.readParcelable(classLoader);
            }
        } catch (FileNotFoundException var25) {
            var25.printStackTrace();
        } catch (IOException var26) {
            var26.printStackTrace();
            parcelable = null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var24) {
                    var24.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var23) {
                    var23.printStackTrace();
                }
            }

        }

        return parcelable;
    }

    public static List<Parcelable> readParcelableList(Context context, String fileName, ClassLoader classLoader) {
        ArrayList results = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = context.openFileInput(fileName);
            if (fis != null) {
                bos = new ByteArrayOutputStream();
                byte[] e = new byte[4096];

                int bytesRead;
                while ((bytesRead = fis.read(e)) != -1) {
                    bos.write(e, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                results = parcel.readArrayList(classLoader);
            }
        } catch (FileNotFoundException var25) {
            var25.printStackTrace();
        } catch (IOException var26) {
            var26.printStackTrace();
            results = null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var24) {
                    var24.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var23) {
                    var23.printStackTrace();
                }
            }

        }

        return results;
    }

    public static boolean saveSerializable(Context context, String fileName, Serializable data) {
        boolean success = false;
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(context.openFileOutput(fileName, 0));
            oos.writeObject(data);
            success = true;
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

        }

        return success;
    }

    public static Serializable readSerialLizable(Context context, String fileName) {
        Serializable data = null;
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(context.openFileInput(fileName));
            data = (Serializable) ois.readObject();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return data;
    }

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader e = new InputStreamReader(context.getResources().getAssets().open(fileName), "utf-8");
            BufferedReader bufReader = new BufferedReader(e);
            String line = "";

            String Result;
            for (Result = ""; (line = bufReader.readLine()) != null; Result = Result + line) {
                ;
            }

            return Result;
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static BufferedReader getBRFromAssets(Context context, String fileName) {
        try {
            InputStreamReader e = new InputStreamReader(context.getResources().getAssets().open(fileName), "utf-8");
            BufferedReader bufReader = new BufferedReader(e);
            return bufReader;
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static BufferedReader getBRFromFile(String fileName) {
        try {
            InputStreamReader e = new InputStreamReader(new FileInputStream(fileName), "utf-8");
            return new BufferedReader(e);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static boolean copy(String srcFile, String dstFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            File e = new File(dstFile);
            if (!e.getParentFile().exists()) {
                e.getParentFile().mkdirs();
            }

            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(dstFile);
            byte[] buffer = new byte[1024];
            boolean len = false;

            int len1;
            while ((len1 = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }

            return true;
        } catch (Exception var19) {
            var19.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

        }

        return false;
    }

    public static boolean writeFile(byte[] buffer, String folder, String fileName) {
        boolean writeSucc = false;
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        String folderPath = "";
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory() + File.separator + folder + File.separator;
        } else {
            writeSucc = false;
        }

        File fileDir = new File(folderPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(folderPath + fileName);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            writeSucc = true;
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            }

        }

        return writeSucc;
    }

    public static String getFileName(String filePath) {
        return cn.finalteam.toolsfinal.StringUtils.isEmpty(filePath) ? "" : filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    public static String getFileNameNoFormat(String filePath) {
        if (cn.finalteam.toolsfinal.StringUtils.isEmpty(filePath)) {
            return "";
        } else {
            int point = filePath.lastIndexOf(46);
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1, point);
        }
    }

    public static String getFileFormat(String fileName) {
        if (cn.finalteam.toolsfinal.StringUtils.isEmpty(fileName)) {
            return "";
        } else {
            int point = fileName.lastIndexOf(46);
            return fileName.substring(point + 1);
        }
    }

    public static long getFileSize(String filePath) {
        long size = 0L;
        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }

        return size;
    }

    public static String getFileSize(long size) {
        if (size <= 0L) {
            return "0MB";
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            return (df.format(((double) size / 1048576.0D)) + "MB");
        }
    }

    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS == 0) {
            fileSizeString = "0B";
        } else if (fileS < 1024L) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576L) {
            fileSizeString = df.format((double) fileS / 1024.0D) + "KB";
        } else if (fileS < 1073741824L) {
            fileSizeString = df.format((double) fileS / 1048576.0D) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1.073741824E9D) + "G";
        }

        return fileSizeString;
    }

    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0L;
        } else if (!dir.isDirectory()) {
            return 0L;
        } else {
            long dirSize = 0L;
            File[] files = dir.listFiles();
            File[] var7 = files;
            int var6 = files.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                File file = var7[var5];
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    dirSize += file.length();
                    dirSize += getDirSize(file);
                }
            }

            return dirSize;
        }
    }

    public long getFileList(File dir) {
        long count = 0L;
        File[] files = dir.listFiles();
        count = (long) files.length;
        File[] var8 = files;
        int var7 = files.length;

        for (int var6 = 0; var6 < var7; ++var6) {
            File file = var8[var6];
            if (file.isDirectory()) {
                count += this.getFileList(file);
                --count;
            }
        }

        return count;
    }

    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }

        byte[] buffer = out.toByteArray();
        out.close();
        return buffer;
    }

    public static boolean checkFileExists(String name) {
        boolean status;
        if (!name.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + name);
            status = newPath.exists();
        } else {
            status = false;
        }

        return status;
    }

    public static boolean checkFilePathExists(String path) {
        return (new File(path)).exists();
    }

    public static long getFreeDiskSpace() {
        String status = Environment.getExternalStorageState();
        long freeSpace = 0L;
        if (status.equals("mounted")) {
            try {
                File e = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(e.getPath());
                long blockSize = (long) stat.getBlockSize();
                long availableBlocks = (long) stat.getAvailableBlocks();
                freeSpace = availableBlocks * blockSize / 1024L;
            } catch (Exception var9) {
                var9.printStackTrace();
            }

            return freeSpace;
        } else {
            return -1L;
        }
    }

    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            status = newPath.mkdir();
            status = true;
        } else {
            status = false;
        }

        return status;
    }

    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        if (sDCardStatus.equals("mounted")) {
            status = true;
        } else {
            status = false;
        }

        return status;
    }

    public static boolean checkExternalSDExists() {
        Map evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    public static boolean deleteDirectory(String fileName) {
        SecurityManager checker = new SecurityManager();
        boolean status;
        if (!fileName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();

                try {
                    for (int e = 0; e < listfile.length; ++e) {
                        File deletedFile = new File(newPath.toString() + "/" + listfile[e].toString());
                        deletedFile.delete();
                    }

                    newPath.delete();
                    status = true;
                } catch (Exception var8) {
                    var8.printStackTrace();
                    status = false;
                }
            } else {
                status = false;
            }
        } else {
            status = false;
        }

        return status;
    }

    public static boolean deleteFile(String fileName) {
        SecurityManager checker = new SecurityManager();
        boolean status;
        if (!fileName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    newPath.delete();
                    status = true;
                } catch (SecurityException var6) {
                    var6.printStackTrace();
                    status = false;
                }
            } else {
                status = false;
            }
        } else {
            status = false;
        }

        return status;
    }

    public static int deleteBlankPath(String path) {
        File f = new File(path);
        return !f.canWrite() ? 1 : (f.list() != null && f.list().length > 0 ? 2 : (f.delete() ? 0 : 3));
    }

    public static boolean reNamePath(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }

    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            f.delete();
            return true;
        } else {
            return false;
        }
    }

    public static void clearFileWithPath(String filePath) {
        List files = listPathFiles(filePath);
        if (files == null || !files.isEmpty()) {
            Iterator var3 = files.iterator();

            while (var3.hasNext()) {
                File f = (File) var3.next();
                if (f.isDirectory()) {
                    clearFileWithPath(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }

        }
    }

    public static String getSDRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getExternalSDRoot() {
        Map evn = System.getenv();
        return (String) evn.get("SECONDARY_STORAGE");
    }

    public static List<String> listPath(String root) {
        ArrayList allDir = new ArrayList();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        if (path.isDirectory()) {
            File[] var7;
            int var6 = (var7 = path.listFiles()).length;

            for (int var5 = 0; var5 < var6; ++var5) {
                File f = var7[var5];
                if (f.isDirectory() && !f.getName().startsWith(".")) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }

        return allDir;
    }

    public static List<File> listPathFiles(String root) {
        ArrayList allDir = new ArrayList();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        File[] files = path.listFiles();
        File[] var8 = files;
        int var7 = files.length;

        for (int var6 = 0; var6 < var7; ++var6) {
            File f = var8[var6];
            if (f.isFile()) {
                allDir.add(f);
            } else {
                listPath(f.getAbsolutePath());
            }
        }

        return allDir;
    }

    public static FileUtils.PathStatus createPath(String newPath) {
        File path = new File(newPath);
        return path.exists() ? FileUtils.PathStatus.EXITS : (path.mkdir() ? FileUtils.PathStatus.SUCCESS : FileUtils.PathStatus.ERROR);
    }

    public static String getPathName(String absolutePath) {
        int start = absolutePath.lastIndexOf(File.separator) + 1;
        int end = absolutePath.length();
        return absolutePath.substring(start, end);
    }

    public static String getAppCache(Context context, String dir) {
        String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir + "/";
        File savedir = new File(savePath);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }

        savedir = null;
        return savePath;
    }

    public static enum PathStatus {
        SUCCESS,
        EXITS,
        ERROR;

        private PathStatus() {
        }
    }


    /**
     * 获取目录
     *
     * @param name
     * @return
     */
    public static File getDir(Context context, String name) {
        File file = new File(getCacheDir(context), name);
        if (file.exists()) {
            return file;
        } else {
            file.mkdirs();
            return file;
        }

    }

    /**
     * 获取缓存目录 sd卡存在就用sd中的。
     *
     * @param context
     * @return
     */
    public static File getCacheDir(Context context) {

        File CacheDir = context.getExternalCacheDir();
        if (CacheDir != null) {
            return CacheDir;
        } else {

            return context.getCacheDir();
        }

    }

    /**
     * 文件是否存在。
     *
     * @return
     */
    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static File[] getFolderAllFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return files;
    }
}
