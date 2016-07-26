package cn.ittiger.demo.bean;

/**
 * @author: laohu on 2016/7/23
 * @site: http://ittiger.cn
 */
public class User  {
    private String name;
    private String msg;

    public User() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {

        this.msg = msg;
    }

    @Override
    public String toString() {

        return "{name=" + name + ",msg=" + msg + "}";
    }
}
