package com.example.isslocator;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.isslocator.common.Constants;
import com.example.isslocator.ui.home.HomeActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    private static final String FILE_NAME = "200_response.json";

    private ActivityTestRule<HomeActivity> activityTestRule =
            new ActivityTestRule<>(HomeActivity.class, true, false);

    private MockWebServer mockWebServer;

    @Before
    public void setup() {
        mockWebServer = new MockWebServer();
        try {
            mockWebServer.start();
            mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(TestHelper.getStringFromFile(
                            InstrumentationRegistry.getContext(), FILE_NAME)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constants.BASE_URL = mockWebServer.url("/").toString();
        activityTestRule.launchActivity(new Intent());

    }

    @Test
    public void testLoadingDataSucessfully() {
        CountingIdlingResource countingIdlingResource = activityTestRule.getActivity()
                .getCountingIdlingResource();
        IdlingRegistry.getInstance().register(countingIdlingResource);

        Espresso.onView(ViewMatchers.withId(R.id.rvResults)).check(
                new RecyclerViewItemCountAssertion(5));

        Espresso.onView(ViewMatchers.withId(R.id.rvResults)).check(
                ViewAssertions.matches(atPosition(0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("Duration: 0 Seconds")))));

        Espresso.onView(ViewMatchers.withId(R.id.rvResults)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, ViewActions.click()));
    }

    public static Matcher<View> atPosition(final int position, final Matcher<View> matcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("Has Item at Position: " + position);
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                RecyclerView.ViewHolder viewHolder = recyclerView
                        .findViewHolderForAdapterPosition(position);
                return viewHolder != null && matcher.matches(viewHolder.itemView);
            }
        };
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {

        private final int expectedItems;

        public RecyclerViewItemCountAssertion(int expectedItems) {
            this.expectedItems = expectedItems;
        }


        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            final RecyclerView.Adapter adapter = ((RecyclerView) view)
                    .getAdapter();

            Assert.assertThat(adapter.getItemCount(),
                    org.hamcrest.Matchers.is(expectedItems));
        }
    }
}
