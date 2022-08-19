package jackli.HttpSender.config;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Form extends ArrayList<Pair<String,String>> {
    public Form() {
        super();
    }

    public Form(List<Pair<String,String>> list) {
        super();
        for (Pair<String,String> p : list) {
            add(new Pair<>(p.first,p.second));
        }
    }

    public Form add(String key,Object value) {
        return add(key,String.valueOf(value));
    }

    public Form add(String key,String value) {
        add(new Pair<>(key,value));
        return this;
    }

    @Override
    public boolean add(Pair<String, String> p) {
        try {
            return super.add(new Pair<>(URLEncoder.encode(p.first,"UTF-8"),URLEncoder.encode(p.second,"UTF-8")));
        } catch (Exception ignore) { }
        return false;
    }

    @Override
    public void add(int index, Pair<String, String> p) {
        try {
            super.add(index,new Pair<>(URLEncoder.encode(p.first,"UTF-8"),URLEncoder.encode(p.second,"UTF-8")));
        } catch (Exception ignore) { }
    }

    @Override
    public String toString() {
        boolean f = true;
        StringBuilder ans = new StringBuilder();
        for (Pair<String,String> p : this) {
            if (f) f = false;
            else ans.append("&");
            ans.append(p.first).append("=").append(p.second);
        }
        return ans.toString();
    }
}
