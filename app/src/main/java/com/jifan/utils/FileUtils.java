package com.jifan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;

public class FileUtils {
    // 得到当前外部存储设备的目录( /SDCARD )
    private static String SDPATH = Environment.getExternalStorageDirectory().getPath() + "/";

    private static int FILESIZE = 4 * 1024;

    public String getSDPATH() {
        return SDPATH;
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return
     */
    public static void createSDDir(String dirName) {
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + dirName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 判断SD卡上的文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();

    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public static File write2SDFromInput(String path, String fileName,
                                         InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];

			/*
             * 真机测试，这段可能有问题，请采用下面网友提供的 while((input.read(buffer)) != -1){
			 * output.write(buffer); }
			 */

			/* 网友提供 begin */
            int length;
            while ((length = (input.read(buffer))) > 0) {
                output.write(buffer, 0, length);
            }
            /* 网友提供 end */

            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String readTxtFromFile(String fileName) throws IOException {

        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file);

        int length = fis.available();

        byte[] buffer = new byte[length];
        fis.read(buffer);

        String res = new String(buffer, "UTF-8");

        fis.close();
        return res;
    }

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String fileName) {


        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            //生成文件夹之后，再生成文件，不然会出错
            createSDFile(fileName);

            File file = new File(fileName);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + fileName);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes("UTF-8"));
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }



    /**
     * 对象本地化<br>
     * 把对象的各个字段写入SharedPreferences
     *
     * @param context
     * @param model
     * @param shareKey SharedPreferences中的键
     */
    public static void modelToShare(Context context, Object model,
                                    String shareKey) {
        SharedPreferences sp = context.getSharedPreferences(shareKey,
                Context.MODE_APPEND);
        Editor editor = sp.edit();

        if (model instanceof String) {
            editor.putString(shareKey, (String) model).commit();
        } else if (model instanceof Integer) {
            editor.putInt(shareKey, (Integer) model).commit();
        } else if (model instanceof Boolean) {
            editor.putBoolean(shareKey, (Boolean) model).commit();
        } else if (model instanceof Long) {
            editor.putLong(shareKey, (Long) model).commit();
        } else if (model instanceof Float) {
            editor.putFloat(shareKey, (Float) model).commit();
        } else {
            Field[] fields = model.getClass().getDeclaredFields();
            for (Field field : fields) {

                field.setAccessible(true);
                String key = field.getName();
                String type = field.getType().getSimpleName();
                try {
                    if ("String".equals(type)) {
                        String value = String.valueOf(field.get(model));
                        editor.putString(key, value);
                    } else if ("float".equals(type)) {
                        editor.putFloat(key, field.getFloat(model));
                    } else if ("int".equals(type)) {
                        editor.putInt(key, field.getInt(model));
                    } else if ("long".equals(type)) {
                        editor.putLong(key, field.getLong(model));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            editor.commit();
        }
    }

    /**
     * SharedPreferences对象化<br>
     * 把SharedPreferences中的某个文件对象化
     *
     * @param context
     * @param modelClass
     * @param shareKey   SharedPreferences中的键
     */
    public static <M> M shareToModel(Context context, Class<M> modelClass,
                                     String shareKey) {
        M model = null;


        SharedPreferences sp = context.getSharedPreferences(shareKey,
                Context.MODE_APPEND);
        if (modelClass.getSimpleName().equals("String")) {
            String str = sp.getString(shareKey, "");
            model = (M) str;
        } else if (modelClass.getSimpleName().equals("Integer")) {
            Integer in = sp.getInt(shareKey, 0);
            model = (M) in;
        } else if (modelClass.getSimpleName().equals("Long")) {
            Long lon = sp.getLong(shareKey, 0);
            model = (M) lon;
        } else if (modelClass.getSimpleName().equals("Float")) {
            Float fl = sp.getFloat(shareKey, 0);
            model = (M) fl;
        } else if (modelClass.getSimpleName().equals("boolean")) {
            Boolean bl = sp.getBoolean(shareKey, false);
            model = (M) bl;
        } else {
            try {
                model = modelClass.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            Field[] fields = modelClass.getDeclaredFields();
            for (Field field : fields) {

                field.setAccessible(true);
                String key = field.getName();
                String type = field.getType().getSimpleName();
                try {
                    if ("String".equals(type)) {
                        field.set(model, sp.getString(key, null));
                    } else if ("float".equals(type)) {
                        field.setFloat(model, sp.getFloat(key, 0));
                    } else if ("int".equals(type)) {
                        field.setInt(model, sp.getInt(key, 0));
                    } else if ("long".equals(type)) {
                        field.setLong(model, 0);
                    } else {
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return model;
    }


    public static void removekey(Context context, String shareKey) {
        SharedPreferences sp = context.getSharedPreferences(shareKey,
                Context.MODE_APPEND);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}