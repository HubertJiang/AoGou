package com.kui.gou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangkuiyuan on 16/9/10.
 */
public class User implements Serializable {
    public String id;
    public String username;
    public long ttl;
    public String userId;
    public String nickname;
    public String address;
    public String gender;
    public Image avatar;
    public Image email;
    public List<String> collection;
}
