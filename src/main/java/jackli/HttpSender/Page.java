package jackli.HttpSender;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jackli.HttpSender.config.Cookie;
import jackli.HttpSender.config.Cookies;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class Page {
    private URL url;
    private String content;
    private Map<String,List<String>> headers;
    private int statusCode = 200;

    public Page() { }
    public Page(Map<String, List<String>> responseHeaders,String content)
    {
        this.headers = responseHeaders;
        this.content = content;
    }

    public Page updateCookie(Site site) {
        Cookies cookies = getNewCookies();
        for (Cookie c : cookies.values()) {
            site.addCookie(c);
        }
        return this;
    }

    public Cookies getNewCookies()
    {
        Cookies cookies = new Cookies();
        int size;
        String name;
        String value;
        Cookie cookie;
        int tp;
        boolean f;
        char c;
        List<String> list = headers.get("Set-Cookie");
        if (list != null) {
            for (String str : list) {
                size = str.length();
                name = "";
                value = "";
                tp = 0;
                f = true;
                cookie = new Cookie("nothing","nothing");
                if (str.charAt(size - 1) != ';') {str += ';'; size ++;}
                for (int i = 0;i < size;i ++) {
                    c = str.charAt(i);
                    if (c == '=') {
                        tp = 1;
                    } else if (c == ';') {
                        if (f) {
                            cookie.key = name.trim();
                            cookie.value = value.trim();
                        } else {
                            if ("path".equals(name.trim())) {
                                cookie.path = value.trim();
                            } else if ("domain".equals(name.trim())) {
                                cookie.domain = value.trim();
                            }
                        }
                        tp = 0;
                        f = false;
                        name = "";
                        value = "";
                    } else {
                        if (tp == 0) name += c;
                        else value += c;
                    }
                }
                cookies.add(cookie);
            }
        }
        return cookies;
    }

    @Override
    public String toString() {
        return "url=" + url +"\n" +
                "statusCode=" + statusCode + "\n" +
                getNewCookies().getInformation() + "\n" +
                content;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getContent() {
        return content;
    }

    public JsonElement toJson() {
        return new JsonParser().parse(content);
    }
}
