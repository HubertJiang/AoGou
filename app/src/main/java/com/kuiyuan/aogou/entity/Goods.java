package com.kuiyuan.aogou.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class Goods extends BmobObject {
    public String name;
    public String content;
    public BmobFile image,image1,image2,image3;
    public Double price;
}
