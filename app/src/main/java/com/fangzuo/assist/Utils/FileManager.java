package com.fangzuo.assist.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.fangzuo.assist.Beans.ImgFolder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileManager {

    private static FileManager mInstance;
    private static Context mContext;
    private static ContentResolver mContentResolver;

    public static FileManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FileManager.class) {
                if (mInstance == null) {
                    mInstance = new FileManager();
                    mContext = context;
                    mContentResolver = context.getContentResolver();
                }
            }
        }
        return mInstance;
    }

    /**
     * 得到图片文件夹集合
     */
    public List<ImgFolder> getImageFolders() {

        List<ImgFolder> folders = new ArrayList<ImgFolder>();
        // 扫描图片
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                    new String[]{"image/jpeg", "image/png"}, "date_modified desc");
            List<String> mDirs = new ArrayList<String>();//用于保存已经添加过的文件夹目录
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));// 路径

                File parentFile = new File(path).getParentFile();
                if (parentFile == null)
                    continue;

                String dir = parentFile.getAbsolutePath();
                if (mDirs.contains(dir)) {//如果已经添加过
                    //追加三张图片
                    for (ImgFolder folder : folders) {
                        if (folder.getDir().equals(dir)) {//取出当前文件夹
                            String[] imgPaths = folder.getPreFourImgPath().split(",");
                            if (imgPaths.length >= 4) {
                                break;
                            } else {
                                folder.setPreFourImgPath(folder.getPreFourImgPath() + path + ",");
                            }
                        }
                    }
                    continue;
                }

                mDirs.add(dir);//添加到保存目录的集合中
                ImgFolder folderBean = new ImgFolder();
                folderBean.setDir(dir);
                folderBean.setPreFourImgPath(path + ",");
                if (parentFile.list() == null)
                    continue;
                int count = parentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                }).length;

                folderBean.setCount(count);
                folders.add(folderBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return folders;
    }

    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    public List<String> getImgListByDir(String dir) {
        ArrayList<String> imgPaths = new ArrayList<>();
        File directory = new File(dir);
        if (directory == null || !directory.exists()) {
            return imgPaths;
        }
        File[] files = directory.listFiles();
        //对文件进行排序
        if (null==files || files.length<= 0)return imgPaths;
        Arrays.sort(files, new FileComparator());
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (FileManager.isPicFile(path)) {
                if (path.contains("camera")){
                    imgPaths.add(path);
                }
            }
        }
        return imgPaths;
    }

    /**
     * 是否是图片文件
     */
    public static boolean isPicFile(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
            return true;
        }
        return false;
    }

    public void deletePic(String dir){
        File f = new File(dir);
        if (f.exists()) {
            f.delete();
        }
    }



    class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.lastModified() < rhs.lastModified()) {
                return 1;
            } else {
                return -1;
            }
        }
    }



}
