package com.example.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class payJazzCash extends AppCompatActivity {
    private WebView mWebView;
    String postData ="";

    private final String Jazz_MerchantID      = "MC18081";
    private final String Jazz_Password        = "20utf68599";
    private final String Jazz_IntegritySalt   = "f08e152390";

    private static final String paymentReturnUrl="http://sandbox.jazzcash.com.pk/ApplicationAPI/API/Payment/DoTransaction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_jazz_cash);

        mWebView = (WebView) findViewById(R.id.activityPayJazzWebView);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClient());
        webSettings.setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new FormDataInterface(), "FORMOUT");

        Intent intentData = getIntent();
        String amount = intentData.getStringExtra("amount");
        System.out.println("price_before : " +amount);


        String pp_Amount = "amount";
        String pp_AuthCode = "";
        String pp_BankID = "";
        String pp_BillReference = "billRef";
        String pp_Language = "EN";
        String pp_MerchantID = "MC18081";
        String pp_ResponseCode = "157";
        String pp_ResponseMessage = "Transaction is pending";
        String pp_RetreivalReferenceNo = "210505067852";
        String pp_SubMerchantId = "";
        String pp_TxnCurrency = "PKR";
        String pp_TxnDateTime = "20210505150457";
        String pp_TxnRefNo = "T20210505150438";
        String pp_SettlementExpiry = "00010101000000";
        String pp_TxnType = "MWALLET";
        String pp_Version = "1.1";
        String ppmpf_1= "03405542599";
        String ppmpf_2 = "";
        String ppmpf_3 = "";
        String ppmpf_4 = "";
        String ppmpf_5 = "";
        String pp_SecureHash = "6916E7A0BFE546A7C72CF499EBBDE87B8FF3DFD1D5A1CA9DAE9F4FDA72B6B533";


        try {
            postData += URLEncoder.encode("pp_Version", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Version, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnType", "UTF-8")
                    + "=" + pp_TxnType + "&";
            postData += URLEncoder.encode("pp_Language", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Language, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_MerchantID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_MerchantID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_BankID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BankID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnRefNo", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnRefNo, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_Amount", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Amount, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnCurrency", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnCurrency, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnDateTime, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_BillReference", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BillReference, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_SecureHash", "UTF-8")
                    + "=" + pp_SecureHash + "&";
            postData += URLEncoder.encode("ppmpf_1", "UTF-8")
                    + "=" + URLEncoder.encode(ppmpf_1, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_2", "UTF-8")
                    + "=" + URLEncoder.encode(ppmpf_2, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_3", "UTF-8")
                    + "=" + URLEncoder.encode(ppmpf_3, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_4", "UTF-8")
                    + "=" + URLEncoder.encode(ppmpf_4, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_5", "UTF-8")
                    + "=" + URLEncoder.encode(ppmpf_5, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("postData : " +postData);

        mWebView.postUrl("http://sandbox.jazzcash.com.pk/ApplicationAPI/API/Payment/DoTransaction", postData.getBytes());
    }

    private class MyWebViewClient extends WebViewClient {
        private final String jsCode ="" + "function parseForm(form){"+
                "var values='';"+
                "for(var i=0 ; i< form.elements.length; i++){"+
                "   values+=form.elements[i].name+'='+form.elements[i].value+'&'"+
                "}"+
                "var url=form.action;"+
                "console.log('parse form fired');"+
                "window.FORMOUT.processFormData(url,values);"+
                "   }"+
                "for(var i=0 ; i< document.forms.length ; i++){"+
                "   parseForm(document.forms[i]);"+
                "};";

        //private static final String DEBUG_TAG = "CustomWebClient";

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(url.equals(paymentReturnUrl)){
                System.out.println("return url cancelling");
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(url.equals(paymentReturnUrl)){
                return;
            }
            view.loadUrl("javascript:(function() { " + jsCode + "})()");

            super.onPageFinished(view, url);
        }
    }

    private class FormDataInterface {
        @JavascriptInterface
        public void processFormData(String url, String formData) {
            Intent i = new Intent(payJazzCash.this, MainActivity.class);

            System.out.println("Url:" + url + " form data " + formData);
            if (url.equals(paymentReturnUrl)) {
                String[] values = formData.split("&");
                for (String pair : values) {
                    String[] nameValue = pair.split("=");
                    if (nameValue.length == 2) {
                        System.out.println("Name:" + nameValue[0] + " value:" + nameValue[1]);
                        i.putExtra(nameValue[0], nameValue[1]);
                    }
                }

                setResult(RESULT_OK, i);
                finish();

                return;
            }
        }
    }
}