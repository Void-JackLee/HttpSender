package jackli.HttpSender.config;

import java.util.HashMap;

public class Cookies extends HashMap<String, Cookie>{ // 其实应该根据domain+path+key来作为map的key，但是实际爬虫没有必要，做浏览器时有必要

    public Cookies()
    {
        super();
    }

    public Cookies add(Cookie cookie) {
        put(cookie.key,cookie);
        return this;
    }

    public Cookies add(String key,String value) {
        return add(new Cookie(key,value));
    }

    public Cookies add(String key,String value,String path) {
        return add(new Cookie(key,value,path));
    }

    public Cookies remove(String key) {
        super.remove(key);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        boolean f = true;
        for (Entry<String,Cookie> p : this.entrySet()) {
            if (f) f = false;
            else str.append("; ");
            str.append(p.getKey()).append("=").append(p.getValue().value);
        }
        return str.toString();
    }

    public String toString(String host) {
        // TODO: add filter
        StringBuilder str = new StringBuilder();
        boolean f = true;
        for (Entry<String,Cookie> p : this.entrySet()) {
            if (f) f = false;
            else str.append("; ");
            str.append(p.getKey()).append("=").append(p.getValue().value);
        }
        return str.toString();
    }

    public String getInformation()
    {
        StringBuilder str = new StringBuilder("----- Information of Cookies -----\n");
        for (Cookie cookie : this.values()) {
            str.append(cookie.toString()).append('\n');
        }
        str.append("----------------------------------");
        return str.toString();
    }


}
