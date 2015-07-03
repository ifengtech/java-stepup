## HTTP请求模块
采用httpclient框架请求服务器，可用于Java项目调试。

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
			// EntityUtils.consume(entity2);
            System.out.println(EntityUtils.toString(entity2));
            
        }
    } catch (Exception e) {
    	e.printStackTrace();
	}