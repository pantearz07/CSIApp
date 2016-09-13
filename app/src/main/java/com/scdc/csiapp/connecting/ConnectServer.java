package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.scdc.csiapp.main.WelcomeActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Pantearz07 on 18/10/2558.
 */
public class ConnectServer {
    //private static PreferenceData mManager;
   // Context context;

   // static String ipvalue="";
   // public static final String urlWebIP ="/csi/";
  //  public static final String urlIP ="/csi/mobile/";
      public static String urlWebIP = "http://192.168.4.101/csi/";
    //public static final String urlWebIP2 ="http://04.csi/";
      public static String urlIP ="http://192.168.4.101/csi/mobile/";
    //public static final String urlIP2 ="http://04.csi/mobile/";
   //public static final String urlWebIP = "http://192.168.0.151/CSIReport/";
  //  public static final String urlIP ="http://192.168.0.151/CSIReport/mobile/";
                   // "http://10.199.120.140/CSIReport/mobile/";
    //http://180.183.251.32:8080/csi/
    //"http://dna04.no-ip.biz:8080/csi/mobile/" http://04.csi/mobile/

    public static void updateIP(){
        Context context = WelcomeActivity.mContext;
        SharedPreferences sp = context.getSharedPreferences(PreferenceData.KEY_PREFS,context.MODE_PRIVATE);

        String IP = sp.getString(PreferenceData.KEY_IP,"192.168.4.101");
        urlIP = "http://"+IP+"/csi/mobile/";
        urlWebIP= "http://"+IP+"/csi/";
        Log.d("urlIP", urlIP);
        Log.d("urlWebIP", urlWebIP);
    }

/*
    public static String getIPValue() {
        mManager = new PreferenceData(this);
        Boolean pret_IP_status = mManager.getPreferenceDataBoolean(mManager.PREF_IP);

        if (pret_IP_status) {
            ipvalue = mManager.getPreferenceData(mManager.KEY_IP);

            return ipvalue;
        }else{
            return null;
        }
    }*/
   /* public static String getLocalIpAddressMobile() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("IP address", "***** IP="+ ipaddress);
                        String[] arrIP = ipaddress.split("\\.");
                        Log.i("IP address",  String.valueOf(arrIP.length)+"*****2 IP=" + ipaddress );

                        if(arrIP[0].equals("10")){
                            if(arrIP[1].equals("5")){
                                if(arrIP[2].equals("9") || arrIP[2].equals("10")){
                                    return urlIP2;
                                }else {
                                    return urlIP;
                                }
                            }else {
                                return urlIP;
                            }
                        }else{
                            return urlIP;
                        }

                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP address", ex.toString());
        }
        return null;
    }
    public static String getLocalIpAddressWeb() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("IP address", "***** IP="+ ipaddress);
                        String[] arrIP = ipaddress.split("\\.");
                        Log.i("IP address",  String.valueOf(arrIP.length)+"*****2 IP=" + ipaddress );

                        if(arrIP[0].equals("10")){
                            if(arrIP[1].equals("5")){
                                if(arrIP[2].equals("9") || arrIP[2].equals("10")){
                                    return urlWebIP2;
                                }else {
                                    return urlWebIP;
                                }
                            }else {
                                return urlWebIP;
                            }
                        }else{
                            return urlWebIP;
                        }

                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP address", ex.toString());
        }
        return null;
    }*/
    public static String getHttpGet(String PhpFileName){
        updateIP();

       // String urlIP = "http://"+getIPValue()+"/csi/mobile/";
        String urlforConnect = urlIP + PhpFileName+".php";
        String result = "";
        try {

            HttpGet httpGet = new HttpGet(urlforConnect);
            HttpClient client = new DefaultHttpClient();

            HttpResponse response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
            }

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        return result;
    }

    public static String getJsonGet(String PhpFileName){
        updateIP();

       // String urlIP = "http://"+getIPValue()+"/csi/mobile/";

        String urlforConnect =  urlIP + PhpFileName+".php";


        Log.i("urlforConnect",  urlforConnect);

        StringBuilder str = new StringBuilder();
        try {

            HttpGet httpGet = new HttpGet(urlforConnect);
            HttpClient client = new DefaultHttpClient();

            HttpResponse response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            }else {
                Log.e("Log", "Failed to download result..");
            }

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        return str.toString();
    }
    public static String getJsonPost(List<NameValuePair> nameValuePairs, String PhpFileName){
        updateIP();

        //String urlIP = "http://"+getIPValue()+"/csi/mobile/";
        String urlforConnect = urlIP + PhpFileName+".php";
        StringBuilder str = new StringBuilder();
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlforConnect);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();


        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        return str.toString();
    }
    public static String getJsonPostGet(List<NameValuePair> nameValuePairs, String PhpFileName) {
        updateIP();

        // String url = getLocalIpAddressMobile();
       //String urlIP = "http://10.199.120.77/csi/mobile/";
//urlIP
        String urlforConnect =  urlIP + PhpFileName+".php";


         Log.i("urlforConnect",  urlforConnect);

        StringBuilder str = new StringBuilder();
//IP=211.204.171.85
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlforConnect);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Response ","Status line : "+ response.getStatusLine().toString());
            int statusCode = response.getStatusLine().getStatusCode();
          Log.d("statusCode", String.valueOf(statusCode));
            if (statusCode == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            }else {
                Log.e("Log", "Failed to download result..");
            }


        } catch (ClientProtocolException e) {

        } catch (IOException e) {
            Log.d("error", "connect");
            return "error";
        }
        return str.toString();
    }
}
