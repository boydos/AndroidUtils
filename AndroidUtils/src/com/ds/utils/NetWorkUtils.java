package com.ds.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.ParseException;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NetWorkUtils extends BaseCloseUtils {
	public static final String DEFAULT_CHARSET="UTF-8";
	public static final int DEFAULT_TIMEOUT=5000;
	public static String getByHttpURL(String url) {
		return getByHttpClient(url,DEFAULT_CHARSET);
	}
	public static String getByHttpURL(String url,String charset) {
	    BufferedReader in = null;        
	    StringBuilder result = new StringBuilder(); 
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(DEFAULT_TIMEOUT);
			conn.setRequestMethod("GET");
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			if(conn.getResponseCode()!=HttpURLConnection.HTTP_OK) {
				throw new IOException("Can't Connect Server");
			}
			String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
	           closeResources(in);
	    }
		return result.toString();
	}
	public static String postByHttpURL(String url, Map<String, String> params)  {
		return postByHttpURL(url,DEFAULT_CHARSET, params);
	}
	public static String postByHttpURL(String url,String charset, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;        
        StringBuilder result = new StringBuilder(); 
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), charset);
            // 发送请求参数            
            if (params != null) {
		          StringBuilder param = new StringBuilder(); 
		          for (Map.Entry<String, String> entry : params.entrySet()) {
		        	  if(param.length()>0){
		        		  param.append("&");
		        	  }	        	  
		        	  param.append(entry.getKey());
		        	  param.append("=");
		        	  param.append(entry.getValue());		        	  
		        	  //System.out.println(entry.getKey()+":"+entry.getValue());
		          }
		          //System.out.println("param:"+param.toString());
		          out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {            
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
           closeResources(out,in);
        }
        return result.toString();
    }
	
	
	public static String postByHttpClient(String url, Map<String,String> paras) {
		return postByHttpClient(url,null,paras);
	}
	public static String postByHttpClient(String url, NameValuePair ...paras) {
		return postByHttpClient(url,null,paras);
	}
	public static String postByHttpClient(String url,String charset, Map<String,String> paras) {
		return postByHttpClient(url,charset,getNameValues(paras));
	}
	public static String postByHttpClient(String url,String charset, NameValuePair ...paras) {
		if(StringUtils.isEmpty(url)) return "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if(paras!=null) {
			for(NameValuePair nvp:paras) {
				formparams.add(nvp);
			}
		}
		UrlEncodedFormEntity uefEntity;
		CloseableHttpResponse response=null;
		try {
			if(charset==null) {
				uefEntity=new UrlEncodedFormEntity(formparams);
			} else {
				uefEntity=new UrlEncodedFormEntity(formparams,charset);
			}
			httppost.setEntity(uefEntity);
			response = httpclient.execute(httppost);
			HttpEntity result= response.getEntity();
			if(result!=null) {
				if(charset==null) {
					return EntityUtils.toString(result);
				} else {
					return EntityUtils.toString(result,charset);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			closeResources(response,httpclient);
		}
		return "";
	}
	
	public static String getByHttpClient(String url) {
		return getByHttpClient(url,null);
	}
	public static String getByHttpClient(String url,String charset) {
		if(StringUtils.isEmpty(url)) return null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget= new HttpGet(url);
		CloseableHttpResponse response=null;
		try {
			response = httpclient.execute(httpget);
			HttpEntity result= response.getEntity();
			if(StringUtils.isEmpty(charset)) {
				charset = getContentCharSet(result);
			}
			return formatString(result,charset);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResources(response,httpclient);
		}
		return null;
	}

	public static String formatString(HttpEntity entity) {
		return formatString(entity,null);
	}
	public static String formatString(HttpEntity entity,String charset) {
		try {
			if(StringUtils.isEmpty(charset)) {
				return entity!=null? EntityUtils.toString(entity):""; 
			}
			return entity!=null? EntityUtils.toString(entity,charset):"";
		} catch (org.apache.http.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String getContentCharSet(final HttpEntity entity) throws ParseException {   
        if (entity == null) {   
            throw new IllegalArgumentException("HTTP entity may not be null");   
        }   
        String charset = null;   
        if (entity.getContentType() != null) {    
            HeaderElement values[] = entity.getContentType().getElements();   
            if (values.length > 0) {   
                NameValuePair param = values[0].getParameterByName("charset" );   
                if (param != null) {   
                    charset = param.getValue();   
                }   
            }   
        }   
         
        if(StringUtils.isEmpty(charset)){  
            charset = DEFAULT_CHARSET;  
        }  
        return charset;   
   }
   public static NameValuePair [] getNameValues(Map<String,String>map) {
		if(map ==null) return null;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Set<String>keys = map.keySet();
		for(String key:keys) {
			list.add(new BasicNameValuePair(key, map.get(key)));
		}
		NameValuePair [] name = new BasicNameValuePair[list.size()];
		return list.toArray(name);
		
	}
}
