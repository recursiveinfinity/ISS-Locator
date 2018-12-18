package com.example.isslocator.ui.home;

import android.support.annotation.NonNull;

import com.example.isslocator.model.Result;

import java.util.List;

public interface HomeContract {

    interface View {
        void showResults(@NonNull List<Result> results);
        void showError(@NonNull String message);
        void showProgress();
        void hideProgress();
        void showList(List<Integer> numbers);
    }

    interface Presenter {
        void stop();
        void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
        void getLocation();
    }
}
