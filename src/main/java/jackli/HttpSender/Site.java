package jackli.HttpSender;

import com.google.gson.Gson;
import jackli.HttpSender.config.Cookie;
import jackli.HttpSender.config.Cookies;
import jackli.HttpSender.config.Form;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class Site {

    private String url;
    private Map<String,String> headers;
    private Cookies cookies;

    private boolean shouldFilterDomain = false;
    private boolean autoUpdateCookie = true;
    private boolean autoAddReferer = true;
    private boolean autoAddOrigin = true;
    private boolean autoRedirect = false;

    private int connectTimeout = 10;
    private int readTimeout = 10;

    public Site(String url) {
        this.url = url;
        this.headers = new LinkedHashMap<>();
        this.cookies = new Cookies();
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true"); // 去除安全机制，干坏事必须加
    }

    public Site ajax(String url) throws Exception {
        Site ajax = new Site(this.url);
        return ajax.setAjaxHeader()
                .setUrl(url)
                .setCookies(cookies)
                .setAutoAddOrigin(autoAddOrigin)
                .setAutoAddReferer(autoAddReferer);
    }

    private void redirectURL(String url) throws Exception
    {
        this.url = url;
        putHeader("Host",new URL(url).getAuthority());
    }

    public Site setUrl(String url) throws Exception { // 带referer
        if (autoAddReferer || autoAddOrigin) {
            URL u1 = new URL(this.url);
            URL u2 = new URL(url);
            if (u1.getAuthority().equals(u2.getAuthority())) {
                if (autoAddReferer) putHeader("Referer", this.url);
                if (autoAddOrigin) putHeader("Origin", u1.getProtocol() + "://" + u1.getAuthority());
            } else {
                if (autoAddReferer) putHeader("Referer", u1.getProtocol() + "://" + u1.getAuthority() + "/");
            }
        }
        redirectURL(url);
        return this;
    }

    private Page redirect(Page page) throws Exception
    {
        if (autoRedirect && page != null) {
            redirectURL(page.getUrl().toString());
            page = new Requester(this).GET();
        }
        return page;
    }

    public Page GET() throws Exception
    {
        return redirect(new Requester(this).GET());
    }


    /*----- POST BEGIN -----*/
    public Page POST(String data) throws Exception {
        return redirect(new Requester(this).POST(data));
    }

    public Page POSTJson(Object obj) throws Exception {
        return POSTJson(new Gson().toJson(obj));
    }

    public Page POSTJson(String data) throws Exception {
        setContentType("application/json");
//        setContentType("application/json; charset=utf-8");
        Page page = POST(data);
        removeContentType();
        return page;
    }

    public Page POSTForm(Form form) throws Exception
    {
        return POSTForm(form.toString());
    }

    public Page POSTForm(String data) throws Exception
    {
        setContentType("application/x-www-form-urlencoded");
        Page page = POST(data);
        removeContentType();
        return page;
    }
    /*----- POST END -----*/

    // ----- getter/setter BEGIN -----
    public Site putHeader(String key,String value) throws Exception
    {
        headers.put(key,value);
        if ("Cookie".equals(key)) throw new Exception("Cookie not allowed set here!");
        return this;
    }

    public Site removeHeader(String key)
    {
        headers.remove(key);
        return this;
    }

    public Site setHeaders(Map<String, String> headers) throws Exception {
        for (String key : headers.keySet()) {
            if ("Cookie".equals(key)) throw new Exception("Cookie not allowed set here!");
        }
        this.headers = headers;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Cookies getCookies() {
        return cookies;
    }

    public Site setShouldFilterDomain(boolean shouldFilterDomain) {
        this.shouldFilterDomain = shouldFilterDomain;
        return this;
    }

    public boolean getShouldFilterDomain() {
        return shouldFilterDomain;
    }

    public Site setAutoUpdateCookie(boolean autoUpdateCookie) {
        this.autoUpdateCookie = autoUpdateCookie;
        return this;
    }

    public boolean getAutoUpdateCookie() {
        return this.autoUpdateCookie;
    }

    public Site setAutoAddReferer(boolean autoAddReferer) {
        this.autoAddReferer = autoAddReferer;
        return this;
    }

    public boolean getAutoAddReferer() {
        return this.autoAddReferer;
    }

    public Site setAutoAddOrigin(boolean autoAddOrigin) {
        this.autoAddOrigin = autoAddOrigin;
        return this;
    }

    public boolean getAutoAddOrigin() {
        return this.autoAddOrigin;
    }

    public Site setCookies(Cookies cookies) {
        this.cookies = cookies;
        return this;
    }

    public Site addCookie(Cookie cookie) {
        cookies.add(cookie);
        return this;
    }

    public Site addCookie(String key,String value) {
        return addCookie(new Cookie(key,value));
    }

    public Site addCookie(String key,String value,String path) {
        return addCookie(new Cookie(key,value,path));
    }

    public Site setContentType(String val)
    {
        try {
            putHeader("Content-Type",val);
        } catch (Exception ignore) { }
        return this;
    }

    public Site removeContentType() {
        removeHeader("Content-Type");
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Site setAutoRedirect(boolean autoRedirect) {
        this.autoRedirect = autoRedirect;
        return this;
    }

    public boolean getAutoRedirect() {
        return this.autoRedirect;
    }

    public Site setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public Site setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    // ----- getter/setter END -----

    public Site setDefaultHeader()
    {
        try {
            putHeader("Host",new URL(url).getAuthority())
                    .putHeader("Connection","keep-alive")
                    .putHeader("Upgrade-Insecure-Requests","1")
                    .putHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36")
                    .putHeader("Sec-Fetch-Dest","document") // 反爬虫
                    .putHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .putHeader("Accept-Language","zh-CN,zh;q=0.9");
        } catch (Exception ignore){}
        return this;
    }

    public Site setAjaxHeader()
    {
        try {
            putHeader("Host",new URL(url).getAuthority())
                    .putHeader("Connection","keep-alive")
                    .putHeader("Upgrade-Insecure-Requests","1")
                    .putHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36")
                    .putHeader("Sec-Fetch-Dest","empty") // 反爬虫
                    .putHeader("Accept","*/*")
                    .putHeader("Accept-Language","zh-CN,zh;q=0.9");
        } catch (Exception ignore){}
        return this;
    }
}
