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

@WebServlet(name = "PaymentCheck", urlPatterns = {"/paydone"})
public class PaymentCheck extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pidx = request.getParameter("pidx");
        String payload = String.format("""
                         {
                             "pidx": "%s"
                         }
                         """, pidx);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestPay = HttpRequest.newBuilder()
                .uri(URI.create("https://dev.khalti.com/api/v2/epayment/lookup/"))
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
                 * 
                 *Success Response
                    {
                       "pidx": "HT6o6PEZRWFJ5ygavzHWd5",
                       "total_amount": 1000,
                       "status": "Completed",
                       "transaction_id": "GFq9PFS7b2iYvL8Lir9oXe",
                       "fee": 0,
                       "refunded": false
                    } 
                 */
                //Here responseBody is the Json string as above
                JSONObject object = new JSONObject(responseBody);
                String status = object.getString("status");
                if ("Completed".equals(status)) {
                    //TODO: Add to database as per requirements
                    response.sendRedirect("paydone.html");
                }
            } else {
                System.out.println("Handle Error! Khalti Server Error!");
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }
}
