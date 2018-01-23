package com.electroninc.quizapp;

/*
 * Created by D A Victor on 16-Jan-18.
 * Utilities for querying the API.
 */

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

final class QueryUtils {

    private QueryUtils() {
    }

    static List<Question> fetchQuestions(Context context, String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Toast.makeText(context, "Error in fetching questions", Toast.LENGTH_LONG).show();
        }

        QuizActivity.currentPage = 0;
        QuizActivity.score = 0;
        return extractFeatureFromJson(jsonResponse);
    }

    private static List<Question> extractFeatureFromJson(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }

        List<Question> questions = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonData);
            JSONArray questionsArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject currentQuestion = questionsArray.getJSONObject(i);

                String questionString = decodeHtmlEntities(currentQuestion.getString("question"));
                String correctAnswer = decodeHtmlEntities(currentQuestion.getString("correct_answer"));
                JSONArray incorrectAnswers = currentQuestion.getJSONArray("incorrect_answers");

                ArrayList<String> incorrectAnswersArrayList = new ArrayList<>();
                for (int j = 0; j < incorrectAnswers.length(); j++) {
                    incorrectAnswersArrayList.add(decodeHtmlEntities(incorrectAnswers.getString(j)));
                }

                Question question = new Question(questionString, correctAnswer, incorrectAnswersArrayList);
                questions.add(question);
            }

        } catch (JSONException ignored) {
        }

        return questions;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException ignored) {
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(30000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException ignored) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String decodeHtmlEntities(String s) {
        return Html.fromHtml(s).toString();
    }

}
