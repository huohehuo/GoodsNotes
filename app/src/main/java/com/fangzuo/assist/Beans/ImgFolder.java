package com.fangzuo.assist.Beans;

public class ImgFolder {
    /**当前文件夹的路径*/
    private String dir;
    /**前四张图片的路径，用逗号分隔*/
    private String preFourImgPath;
    /**文件夹名*/
    private String name;
    /**文件夹中图片的数量*/
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndex = dir.lastIndexOf("/");
        this.name = dir.substring(lastIndex + 1);
    }

    public String getPreFourImgPath() {
        return preFourImgPath;
    }

    public void setPreFourImgPath(String preFourImgPath) {
        this.preFourImgPath = preFourImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void setName(String name) {
        this.name = name;
    }

    public ImgFolder() {
    }

    public ImgFolder(String dir, String preFourImgPath, String name, int count) {
        this.dir = dir;
        this.preFourImgPath = preFourImgPath;
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImgFolder{" +
                "dir='" + dir + '\'' +
                ", preFourImgPath='" + preFourImgPath + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
