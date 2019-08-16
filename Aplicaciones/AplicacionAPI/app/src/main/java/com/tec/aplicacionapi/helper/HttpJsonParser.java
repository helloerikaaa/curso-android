package com.tec.aplicacionapi.helper;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class HttpJsonParser {

    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";
    HttpURLConnection httpURLConnection = null;

    public JSONObject makeHttpRequest
            (String url, String method, Map<String, String> params){

        try {
            Uri.Builder builder = new Uri.Builder();
            URL urlObj;
            String encodeParams = "";
            if(params != null){
                for(Map.Entry<String, String> entry : params.entrySet()){
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            if(builder.build().getEncodedQuery() != null){
                encodeParams = builder.build().getEncodedQuery();
            }
            if("GET".equals(method)){
                url = url + "?" + encodeParams;
                urlObj = new URL(url);
                httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                httpURLConnection.setRequestMethod(method);
            }else{
                urlObj = new URL(url);
                httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                httpURLConnection.setRequestMethod(method);
                httpURLConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length",
                        String.valueOf(encodeParams.getBytes().length));
                httpURLConnection.getOutputStream().
                        write(encodeParams.getBytes());
            }

            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            json = stringBuilder.toString();
            jsonObject = new JSONObject(json);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

}
