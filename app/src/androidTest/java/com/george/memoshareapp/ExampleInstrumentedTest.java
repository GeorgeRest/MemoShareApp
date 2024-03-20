package com.george.memoshareapp;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.george.memoshareapp.activities.ContactListActivity;
import com.george.memoshareapp.activities.ReleaseActivity;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.manager.RetrofitManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context appContext;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.george.litepaltest1", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule<ContactListActivity> activityScenarioRule = new ActivityScenarioRule<>(ContactListActivity.class);
    public ActivityScenarioRule<ReleaseActivity> activityScenarioRule2 = new ActivityScenarioRule<>(ReleaseActivity.class);
//
//    @Test
//    public void verifyContactUser() {
//        // 模拟用户点击按钮跳转到 AActivity
//        Espresso.onData(anything())
//                .inAdapterView(withId(R.id.lv_contact_list))
//                .atPosition(0) // 0表示第一个位置
//                .perform(click());
//        // 检查result
//        Instrumentation.ActivityResult result = activityScenarioRule2.getScenario().getResult();
//        assertEquals(2,result.getResultCode());
//    }
    @Test
    public void test_getFriendUser() {
        String phoneNumber = "15541871133";
        UserServiceApi apiService = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<HttpListData<User>> friendUserCall;
        friendUserCall = apiService.getFriendUser(phoneNumber);
        friendUserCall.enqueue(new Callback<HttpListData<User>>() {
            @Override
            public void onResponse(Call<HttpListData<User>> call, Response<HttpListData<User>> response) {
                List<User> users = response.body().getItems();
                assertNotNull(users);
            }

            @Override
            public void onFailure(Call<HttpListData<User>> call, Throwable t) {

            }
        });
    }

    @Test
    public void test_displayManager_getLikePost() {

    }
}