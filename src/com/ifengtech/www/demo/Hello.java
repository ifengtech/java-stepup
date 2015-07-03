package com.ifengtech.www.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * 
 * @author Wang Gensheng
 *
 */


public class Hello {
	
	public static void main(String[] args) {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final String HOST = "http://test.2qianbao.com";
			final String API_PREF = "/index.php/zte/common/";
			final String API_MOTHOD = "register";
			String url = HOST + API_PREF + API_MOTHOD;
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("mobile", "18025423291"));
            nvps.add(new BasicNameValuePair("password", "dashing2315"));
            nvps.add(new BasicNameValuePair("type", "2"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
//                EntityUtils.consume(entity2);
                System.out.println(EntityUtils.toString(entity2));
                
            }
        } catch (Exception e) {
			// TODO: handle exception
        	System.out.println("error");
		}
	}
}
