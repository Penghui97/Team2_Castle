package com.example.mywork2.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**@author Jing
 * function: this class is for connecting the horsePay api
 * using http protocol connection
 */
public class PayUtil {

    //the pay method
    public static boolean pay(String customerId, String transactionAmount)  {
        String date = getCurDate();
        String time = getCurTime();
        String result = "";
        boolean isSuccess = false;

        InputStreamReader is = null;
        HttpURLConnection conn = null;

        try {
            //the url used to connect
            URL url = new URL("http://homepages.cs.ncl.ac.uk/daniel.nesbitt/CSC8019/HorsePay/HorsePay.php");
            conn = (HttpURLConnection) url.openConnection();
            //set the request method
            conn.setRequestMethod("POST");
            //allowed to input and output json
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //set the header
            conn.setRequestProperty("Content-Type", "application/json");
            //set the status will be true all the time
            String data = "{\n" +
                    "  \"storeID\":\"Team02\",\n" +
                    "  \"customerID\":\"" + customerId + "\",\n" +
                    "  \"date\":\"" + date + "\",\n" +
                    "  \"time\":\"" + time + "\",\n" +
                    "  \"timeZone\":\"GMT\",\n" +
                    "  \"transactionAmount\":\"" + transactionAmount + "\",\n" +
                    "  \"currencyCode\":\"GBP\",\n" +
                    "  \"forcePaymentSatusReturnType\":\"true\"\n" +
                    "}";
            //get the output stream
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            //get the input stream
            is = new InputStreamReader(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(is);
            String line = null;
            //read the message
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }finally {
            //close the resources
            try {
                if(is != null)
                is.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            conn.disconnect();
        }
        //get the payment result
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject paymetSuccess = new JSONObject(jsonObject.getString("paymetSuccess"));
            result = paymetSuccess.getString("Status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "true".equals(result);
    }

    //get the current date as the format of dd/MM/yyyy
    public static String getCurDate(){
        Date nowTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(nowTime);
    }

    //get the current time as the format of HH:mm
    public static String getCurTime(){
        Date nowTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(nowTime);
    }
}
//the parameters of JSON
//{
//	"storeID":"Team02",
//	"customerID":"C667835",
//	"date":"03/04/2022",
//	"time":"14:01",
//	"timeZone":"GMT",
//	"transactionAmount":"5",
//	"currencyCode":"GBP",
//	"forcePaymentSatusReturnType":"true"
//}
