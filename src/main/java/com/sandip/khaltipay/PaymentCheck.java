package com.sandip.khaltipay;

import com.google.gson.Gson;
import com.sandip.khaltipay.pojo.Verification;
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
                Gson gson = new Gson();
                Verification verify = gson.fromJson(responseBody, Verification.class);
//                System.out.println(verify.getStatus());
                if ("Completed".equals(verify.getStatus())) {
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
