package jackli.HttpSender;

import jackli.HttpSender.config.Cookies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Requester {
    private Site site;

    public Requester(Site site) {
        this.site = site;
    }

    public Page GET() throws Exception {
        Page page = null;
        try {
            HttpURLConnection connection = getConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(site.getConnectTimeout() * 1000);
            connection.setReadTimeout(site.getReadTimeout() * 1000);

            page = getPage(connection);
        } catch (Exception e) {
            throw new Exception("An error occupied when execute GET method on '" + site.getUrl() + "'.",e);
        }

        return page;
    }

    public Page POST(String data) throws Exception
    {
        PrintWriter out = null;
        Page page = null;
        try {
            HttpURLConnection connection = getConnection();

            connection.setDoOutput(true);
            connection.setConnectTimeout(site.getConnectTimeout() * 1000);
            connection.setReadTimeout(site.getReadTimeout() * 1000);
            connection.setRequestMethod("POST");

            out = new PrintWriter(connection.getOutputStream()); // 获取URLConnection对象对应的输出流
            out.print(data);
            out.flush();

            page = getPage(connection);

            out.close();
        } catch (Exception e) {
            if (out != null) out.close();
            throw new Exception("An error occupied when execute POST method on '" + site.getUrl() + "'.",e);
        }
        return page;
    }

    private Page getPage(HttpURLConnection connection) throws IOException {
        String result = "";
        BufferedReader in = null;
        Page page = null;
        String line;
        try {
            page = new Page();

            page.setStatusCode(connection.getResponseCode());
            page.setHeaders(connection.getHeaderFields()); // 获取所有响应头字段

            String location = connection.getHeaderField("Location");
            if (location == null || "".equals(location)) {
                page.setUrl(connection.getURL());
            } else {
                page.setUrl(new URL(connection.getURL().getProtocol() + "://" + connection.getURL().getAuthority() + location));
            }

            in = new BufferedReader(new InputStreamReader(connection.getInputStream())); // 定义 BufferedReader 输入流来读取URL的响应
            while ((line = in.readLine()) != null) result += line;

            page.setContent(result);

            if (site.getAutoUpdateCookie()) {
                page.updateCookie(site);
            }

            in.close();
        } catch (IOException e) {
            if (in != null) in.close();
            throw e;
        }
        return page;
    }

    private HttpURLConnection getConnection() throws Exception
    {
        URL realUrl = new URL(site.getUrl());
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

        Map<String, String> headers = site.getHeaders();
        Cookies cookies = site.getCookies();


        // 设置Cookie
        headers.remove("Cookie");
        String str = "";
        if (!site.getShouldFilterDomain()) str = cookies.toString();
        else str = cookies.toString(realUrl.getHost());
        if (!str.equals("")) headers.put("Cookie",str);

        // 设置头
        for (Map.Entry<String,String> key : headers.entrySet()) {
            connection.setRequestProperty(key.getKey(),key.getValue());
        }

        connection.setInstanceFollowRedirects(false);
        return connection;
    }
}
