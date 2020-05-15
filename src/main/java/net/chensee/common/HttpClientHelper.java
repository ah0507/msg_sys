package net.chensee.common;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @author ah
 * @title: HttpClientHelper
 * @date 2019/12/2 9:51
 */
public class HttpClientHelper {

    public static Object sendPost(String url, Object obj) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
//        httpPost.addHeader("Authorization","cs_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGkiLCJ1c2VyX2lkIjo0LCJ0eXBlIjoiYWNjIiwiZXhwIjoxNTc1MzM5NzgyLCJpYXQiOjE1NzUzMzYxODIsImp0aSI6ImM0NDFmMjFkLTMwMzQtNDAwZC04MWRkLTIzNGNmOWRkYzhjNyJ9.uFIdG5GLmdJFVO_g4pTflzYPC2IkvrJSX8lxxw4fJsU");
        StringEntity entity = new StringEntity(JSONObject.fromObject(obj).toString(), "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        return returnResp(response);
    }

    private static Object returnResp(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == 200) {
            // 返回响应体的内容
            String data = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (data.isEmpty() || data == null) {
                return null;
            }
            JSONObject jsonObject = JSONObject.fromObject(data);
            return jsonObject.get("data");
        }
        return null;
    }

    public static Object sendGet(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
//        httpGet.addHeader("Authorization","cs_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGkiLCJ1c2VyX2lkIjo0LCJ0eXBlIjoiYWNjIiwiZXhwIjoxNTc1MzM5NzgyLCJpYXQiOjE1NzUzMzYxODIsImp0aSI6ImM0NDFmMjFkLTMwMzQtNDAwZC04MWRkLTIzNGNmOWRkYzhjNyJ9.uFIdG5GLmdJFVO_g4pTflzYPC2IkvrJSX8lxxw4fJsU");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return returnResp(response);
    }

    public static Object sendGetByParam(String url, Map<String,Object> paramMap) throws Exception {
        URI uri = getParamUri(url,paramMap);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
//        httpGet.addHeader("Authorization","cs_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGkiLCJ1c2VyX2lkIjo0LCJ0eXBlIjoiYWNjIiwiZXhwIjoxNTc1MzM5NzgyLCJpYXQiOjE1NzUzMzYxODIsImp0aSI6ImM0NDFmMjFkLTMwMzQtNDAwZC04MWRkLTIzNGNmOWRkYzhjNyJ9.uFIdG5GLmdJFVO_g4pTflzYPC2IkvrJSX8lxxw4fJsU");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return returnResp(response);
    }

    private static URI getParamUri(String url, Map<String, Object> paramMap) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (paramMap != null) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        return uriBuilder.build();
    }
}
