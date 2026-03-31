# KhaltiPay

*Extra tools and libraries:*
1. Gson and POJO class not needed
2. Convert response to JSONObject and directly read the key-value data

**Main Documentation: https://docs.khalti.com/khalti-epayment/**

General Steps:
1. Create test account and live secret key is required from setting > Keys
2. From sample request object, modify according to project requirements.

```java
    //pay is value sent from client request
    Integer pay = Integer.valueOf(request.getParameter("pay"));
    //Sample data from our checkout or payment
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
                         """,pay);
```

                        
==Here, String format is used with multiline string==

3. Now, we create new request, where our server acts as client
 and Khalti as server: HttpClient, HttpRequest and HttpResponse from java.net.http

```java
 HttpClient client = HttpClient.newHttpClient();
// Create request header and body according to Khalti documentation
//Server: https://dev.khalti.com/api/v2/epayment/initiate/
//have to add authorization and content type in request header
 HttpRequest requestPay = HttpRequest.newBuilder()
        .uri(URI.create("https://dev.khalti.com/api/v2/epayment/initiate/"))
        .header("Content-Type", "application/json")
        .header("Authorization", "key live_secret_key")
        .POST(HttpRequest.BodyPublishers.ofString(payload)) // JSON body
        .build();
```

4. Send request and then khalti will send us response that contains payment url, 
we redirect to it.

Sample response body from Khalti
```json
{
  "pidx": "bZQLD9wRVWo4CdESSfuSsB",
  "payment_url": "https://test-pay.khalti.com/?pidx=bZQLD9wRVWo4CdESSfuSsB",
  "expires_at": "2023-05-25T16:26:16.471649+05:45",
  "expires_in": 1800
}
```

```java
HttpResponse<String> responsePay;
    try {
        responsePay = client.send(requestPay,HttpResponse.BodyHandlers.ofString());
		String responseBody = responsePay.body();
		int statusCode = responsePay.statusCode();
//            System.out.println("Status Code: "+statusCode);
		//Open link provided by Khalti if response status is OK
		if (statusCode == 200) {
			JSONObject object = new JSONObject(responseBody);
                        String payment_url = object.getString("payment_url");
                        response.sendRedirect(payment_url);
//                System.out.println(responseBody);
		}else{
			System.out.println("Handle Error! Khalti Server Error!");
			//We can create error based POJO and handle error as above using Gson
		}
	} catch (InterruptedException ex) {
		System.out.println(ex.getLocalizedMessage());
	}
```

5. In Khalti payment website, make khalti wallet app with:

> [!NOTE]
> Test Khalti ID for 980000000(0–5) e.g. 9800000005
> Test MPIN 1111
> Test OTP 987654

6. After payment is handled; either failure or success: Khalti sends get request according
to the return_url in initial request (requestPay)
```html
    http://localhost:8080/KhaltiServlet/paydone
    ?pidx=ntDfgKyydLdPtd9TymrMdm
    &transaction_id=UXMazzmAEosnbgoefgNFhT
    &tidx=UXMazzmAEosnbgoefgNFhT
    &txnId=UXMazzmAEosnbgoefgNFhT
    &amount=1000
    &total_amount=1000
    &mobile=98XXXXX005
    &status=Completed
    &purchase_order_id=Order01
    &purchase_order_name=test
```

Now, we can again read this via Servlet and according to status and tidx parameter 
you can verify transactions and update database.

In PaymentCheck Servlet, we will check status and can store status, transaction id 
and pidx to order table.

Khalti provides us lookup url for verification of payment according to pidx.
