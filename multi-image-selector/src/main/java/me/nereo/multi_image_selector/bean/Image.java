package me.nereo.multi_image_selector.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image implements Serializable {
    public String path;
    public String name;
    public long time;

    public Image(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return TextUtils.equals(this.path, other.path);
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
