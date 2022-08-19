# HttpSender
A library of java to send HTTP request based on HttpURLConnection.

# Installation
Download the code and run command below the directory:
```shell
mvn clean install
```

Configure `pom.xml` in your project.
```xml
<dependencies>
	
	<!--...-->

	<dependency>
    	<groupId>jackli</groupId>
        <artifactId>HttpSender</artifactId>
        <version>1.2</version>
    </dependency>

    <!--...-->

</dependencies>
```

# Usage
Initial a object of Site to indicate the url which you want to request.
```java
Site site = new Site("http://yourURL");
```
A site can simulate some browser action, including 'Referer' and 'Origin', trigering by `setUrl` or `ajax` function.

Simple example of request.
```java
Map<String,Object> mp = new HashMap<>();
mp.put("id","hello");
mp.put("usr","jack");
mp.put("psd","123456");

Page res = site.setAjaxHeader()
               .POSTJson(mp);

// page object is used for handling the result of request
System.out.println(res);
```

