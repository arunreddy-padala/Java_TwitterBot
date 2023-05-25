import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class TwitterBot {

  private static final String BASE_URL = "https://api.twitter.com/2";
  private static final String BEARER_TOKEN = "";

  public static void main(String[] args) {

    tweetLines();

  }

  private static void tweetLines() {

    String line;

    try {

      try(
              InputStream fis = new FileInputStream("/Users/Documents/Projects/TwitterBot/TwitterBot/src/main/resources/tweets.txt");
              InputStreamReader isr = new InputStreamReader(fis, Charset.forName("Cp1252"));
              BufferedReader br = new BufferedReader(isr);

              )

      {
        while ((line= br.readLine())!=null) {

          sendTweet(line);
          System.out.println("Tweeting: " + line + "...");

          try {

            System.out.println("Sleeping for 3 minutes: ");
            Thread.sleep(180000);

          } catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        }
        }
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }


    }
  private static void sendTweet(String line) {
    Twitter twitter = TwitterFactory.getSingleton();
    Status status;
    try {
      status = twitter.updateStatus(line);
      System.out.println(status);
    } catch (TwitterException e) {;
      e.printStackTrace();
    }
  }


  private static void postTweet(String tweetText) {
    OkHttpClient client = new OkHttpClient();

    String endpoint = BASE_URL + "/tweets";

    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    String json = "{\"text\":\"" + tweetText + "\"}";

    RequestBody body = RequestBody.create(mediaType, json);

    Request request = new Request.Builder()
            .url(endpoint)
            .header("Authorization", "Bearer " + BEARER_TOKEN)
            .post(body)
            .build();

    try {
      Response response = client.newCall(request).execute();
      System.out.println(response.body().string());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}




