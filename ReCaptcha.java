import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class ReCaptcha {
    private String recaptchaEndpoint = "https://www.google.com/recaptcha/api/siteverify";
    private String recaptchaSecret;
    private String recaptchaResponse;

    public ReCaptcha(String recaptchaSecret, String recaptchaResponse) {
        this.recaptchaSecret = recaptchaSecret;
        this.recaptchaResponse = recaptchaResponse;
    }

    public boolean verify() {
        URL url;
        HttpsURLConnection conn;
        String postParams, inputLine;
        StringBuffer response;
        DataOutputStream output;
        BufferedReader input;
        JsonReader jsonReader;
        JsonObject jsonObject;

        try {
            url = new URL(recaptchaEndpoint);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            postParams = "secret=" + this.recaptchaSecret + "&response=" + this.recaptchaResponse;
            conn.setDoOutput(true);
            output = new DataOutputStream(conn.getOutputStream());
            output.writeBytes(postParams);
            output.flush();
            output.close();
            if (conn.getResponseCode() != 200) {
                System.out.println("reCaptcha returned non 200 code");
                return false;
            }
            input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = new StringBuffer();
            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }
            input.close();
            jsonReader = Json.createReader(new StringReader(response.toString()));
            jsonObject = jsonReader.readObject();
            jsonReader.close();
            if (jsonObject.getBoolean("success")) {
                return true;
            } else {
                Log.write("reCaptcha failed", 3);
                return false;
            }
        } catch(Exception e) {
            System.out.println("reCaptcha query failed: " + e.toString());
            return false;
        }
    }
}
