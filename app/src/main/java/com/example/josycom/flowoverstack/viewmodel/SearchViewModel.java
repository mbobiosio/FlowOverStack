package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.josycom.flowoverstack.model.SearchResponse;
import com.example.josycom.flowoverstack.repository.SearchRepository;


public class SearchViewModel extends ViewModel {

    private SearchRepository mSearchRepository;
    private MutableLiveData<String> mSearchLiveData = new MutableLiveData<>();
    private LiveData<SearchResponse> mResponseLiveData = Transformations.switchMap(mSearchLiveData, (query) -> mSearchRepository.getResponse(query));

    public SearchViewModel() {
        mSearchRepository = new SearchRepository();
    }

    public LiveData<SearchResponse> getResponseLiveData() {
        return mResponseLiveData;
    }

    public void setQuery(String query) {
        mSearchLiveData.setValue(query);
    }
}
