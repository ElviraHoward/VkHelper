package com.elvira.vkhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

public class MainActivity extends AppCompatActivity {

    private String[] vkscope = new String[]{VKScope.WALL, VKScope.FRIENDS, VKScope.PHOTOS};
    private ListView listViewFriends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKSdk.login(this, vkscope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
               listViewFriends = (ListView) findViewById(R.id.listView);
                // success auth
                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "sex,bdate,city"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList friendsList = (VKList)response.parsedModel;

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, friendsList);
                        listViewFriends.setAdapter(arrayAdapter);
                    }
                });
                Toast.makeText(getApplicationContext(), "Success auth", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                // fail auth
                Toast.makeText(getApplicationContext(), "Failed auth", Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
