package com.company;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

class Text {
    String Text;
}

class Translated{
    Translations[] translations;
}

class Translations{
    String text;
}

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        System.out.println("Введите текст для перевода");
        Text t = new Text();
        t.Text = input.nextLine();

        System.out.println("Введите язык перевода. Например: en, es, uk");
        String lang = input.next();
        String API_URL = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=" + lang;

        Gson g = new Gson();
        List Data = List.of(g.toJson(t, Text.class));
        String POSTData = Data.toString();


        URL url = new URL(API_URL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "82e622ccc27b4ad0af0918182329a742");
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Region", "westeurope");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setDoOutput(true);

        OutputStream out = urlConnection.getOutputStream();
        out.write(POSTData.getBytes());

        Scanner in = new Scanner(urlConnection.getInputStream());
        if (in.hasNext()) {
            String result =  in.nextLine();
            result = result.substring(1, result.length() - 1);
            Translated tr = g.fromJson(result, Translated.class);
            System.out.println(tr.translations[0].text);
        } else System.out.println("No output returned");
        urlConnection.disconnect();
    }
}
