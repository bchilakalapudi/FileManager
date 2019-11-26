package com.bchilakalapudi.filemanager.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bchilakalapudi.filemanager.ListAdapter;
import com.bchilakalapudi.filemanager.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;



    public HomeViewModel() {
      //  mText = new MutableLiveData<>();
       // mText.setValue("This is home fragment");




    }

    public LiveData<String> getText() {
        return mText;
    }
}