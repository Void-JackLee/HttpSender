package jackli.HttpSender.config;

public class Cookie {
    public String key;
    public String value;

    public String path;
    public String domain;

    public Cookie(String key,String value) {
        this(key,value,"/");
    }

    public Cookie(String key,String value,String path) {
        this(key,value,path,"*");
    }

    public Cookie(String key,String value,String path,String domain) {
        this.key = key;
        this.value = value;
        this.path = path;
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "=> " + key + " = " + value + "\n" +
                "   path = " + path + "\n" +
                "   domain = " + domain;
    }
}
