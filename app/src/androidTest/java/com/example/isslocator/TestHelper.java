package com.example.isslocator;

import android.content.Context;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestHelper {

    static String getStringFromFile(Context context, String filepath)
            throws IOException {
        InputStream inputStream = context.getResources().getAssets()
                .open(filepath);
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();

        String line;

        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();
        inputStream.close();

        return stringBuilder.toString();
    }



}
