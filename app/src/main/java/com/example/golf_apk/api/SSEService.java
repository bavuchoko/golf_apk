package com.example.golf_apk.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SSEService {
    private final ApiService api;
    private final MutableLiveData<String> eventLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> failureLiveData = new MutableLiveData<>();

    public SSEService(ApiService api) {
        this.api = api;
    }

    public void connectToSSE(String id) {
        Call<ResponseBody> call = api.getPracticeEvents(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String line = response.body().string();
                        eventLiveData.postValue(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                        failureLiveData.postValue(e);
                    }
                } else {
                    failureLiveData.postValue(new Exception("Error during SSE request"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                failureLiveData.postValue(t);
            }
        });
    }
    public LiveData<String> getEventLiveData() {
        return eventLiveData;
    }

    public LiveData<Throwable> getFailureLiveData() {
        return failureLiveData;
    }


}
