package com.sandip.khaltipay;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

@WebServlet(asyncSupported = true, name = "PaymentServlet", urlPatterns = {"/payment"})
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer pay = Integer.valueOf(request.getParameter("pay"));
        String payload = String.format("""
                         {
                             "return_url": "http://localhost:8080/KhaltiServlet/paydone",
                             "website_url": "http://localhost:8080/KhaltiServlet",
                             "amount": "%d",
                             "purchase_order_id": "Order01",
                             "purchase_order_name": "test",
                             "customer_info": {
                             "name": "Ram Bahadur",
                             "email": "test@khalti.com",
                             "phone": "9800000001"
                             }
                         }
                         """, pay);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestPay = HttpRequest.newBuilder()
                .uri(URI.create("https://dev.khalti.com/api/v2/epayment/initiate/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "key live_secret_key")
                .POST(HttpRequest.BodyPublishers.ofString(payload)) // JSON body
                .build();

        HttpResponse<String> responsePay;
        try {
            responsePay = client.send(requestPay, HttpResponse.BodyHandlers.ofString());
            String responseBody = responsePay.body();
            int statusCode = responsePay.statusCode();
//            System.out.println("Status Code: "+statusCode);
            if (statusCode == 200) {
        /**
         * Sample response from khalti; redirect to the provided url
         *   {
                "pidx": "bZQLD9wRVWo4CdESSfuSsB",
                "payment_url": "https://test-pay.khalti.com/?pidx=bZQLD9wRVWo4CdESSfuSsB",
                "expires_at": "2023-05-25T16:26:16.471649+05:45",
                "expires_in": 1800
            }
         */
                JSONObject object = new JSONObject(responseBody);
                String payment_url = object.getString("payment_url");
                response.sendRedirect(payment_url);
            }else{
                System.out.println("Handle Error! Khalti Server Error!");
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }
}
