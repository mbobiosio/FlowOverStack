package com.josycom.mayorjay.flowoverstack.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.adapters.QuestionAdapter;
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsByCreationBinding;
import com.josycom.mayorjay.flowoverstack.model.Owner;
import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.util.DateUtil;
import com.josycom.mayorjay.flowoverstack.util.StringConstants;
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomQuestionViewModelFactory;
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionViewModel;

import org.jetbrains.annotations.NotNull;

import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_AVATAR_ADDRESS;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_ANSWERS_COUNT;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_DATE;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_FULL_TEXT;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_ID;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_NAME;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_OWNER_LINK;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_TITLE;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_VOTES_COUNT;

/**
 * This fragment houses the Recent Questions
 */
public class QuestionsByCreationFragment extends Fragment {

    private FragmentQuestionsByCreationBinding mFragmentQuestionsByCreationBinding;
    private PagedList<Question> mQuestions;
    private View.OnClickListener mOnClickListener;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentQuestionsByCreationBinding = FragmentQuestionsByCreationBinding.inflate(inflater, container, false);
        mFragmentQuestionsByCreationBinding.creationSwipeContainer.setColorSchemeResources(R.color.colorPrimaryLight);
        mFragmentQuestionsByCreationBinding.creationScrollUpFab.setVisibility(View.INVISIBLE);

        mFragmentQuestionsByCreationBinding.creationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mFragmentQuestionsByCreationBinding.creationScrollUpFab.setVisibility(View.VISIBLE);
                } else {
                    mFragmentQuestionsByCreationBinding.creationScrollUpFab.setVisibility(View.INVISIBLE);
                }
            }
        });
        mFragmentQuestionsByCreationBinding.creationScrollUpFab.setOnClickListener(v ->
                mFragmentQuestionsByCreationBinding.creationRecyclerView.scrollToPosition(0));

        mOnClickListener = v -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Intent answerActivityIntent = new Intent(getContext(), AnswerActivity.class);
            Question currentQuestion = mQuestions.get(position);
            assert currentQuestion != null;
            Owner questionOwner = currentQuestion.getOwner();

            answerActivityIntent.putExtra(EXTRA_QUESTION_TITLE, currentQuestion.getTitle());
            answerActivityIntent.putExtra(EXTRA_QUESTION_NAME, questionOwner.getDisplayName());
            answerActivityIntent.putExtra(EXTRA_QUESTION_DATE,
                    DateUtil.toNormalDate(currentQuestion.getCreationDate()));
            answerActivityIntent.putExtra(EXTRA_QUESTION_FULL_TEXT, currentQuestion.getBody());
            answerActivityIntent.putExtra(EXTRA_AVATAR_ADDRESS, questionOwner.getProfileImage());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.getAnswerCount());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ID, currentQuestion.getQuestionId());
            answerActivityIntent.putExtra(EXTRA_QUESTION_VOTES_COUNT, currentQuestion.getScore());
            answerActivityIntent.putExtra(EXTRA_QUESTION_OWNER_LINK, questionOwner.getLink());

            startActivity(answerActivityIntent);
            requireActivity().overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
        };
        handleRecyclerView();
        return mFragmentQuestionsByCreationBinding.getRoot();
    }

    private void handleRecyclerView() {
        final QuestionAdapter questionAdapter = new QuestionAdapter();
        mFragmentQuestionsByCreationBinding.creationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFragmentQuestionsByCreationBinding.creationRecyclerView.setItemAnimator(new DefaultItemAnimator());

        QuestionViewModel questionViewModel = new ViewModelProvider(this, new CustomQuestionViewModelFactory(StringConstants.FIRST_PAGE,
                StringConstants.PAGE_SIZE,
                StringConstants.ORDER_DESCENDING,
                StringConstants.SORT_BY_CREATION,
                StringConstants.SITE,
                StringConstants.QUESTION_FILTER,
                StringConstants.API_KEY)).get(QuestionViewModel.class);

        questionViewModel.getNetworkState().observe(getViewLifecycleOwner(), s -> {
            switch (s) {
                case StringConstants.LOADING:
                    onLoading();
                    break;
                case StringConstants.LOADED:
                    onLoaded();
                    break;
                case StringConstants.FAILED:
                    onError();
                    break;
            }
        });
        questionViewModel.getQuestionPagedList().observe(getViewLifecycleOwner(), questions -> {
            mQuestions = questions;
            questionAdapter.submitList(questions);
        });
        mFragmentQuestionsByCreationBinding.creationRecyclerView.setAdapter(questionAdapter);
        questionAdapter.setOnClickListener(mOnClickListener);
        mFragmentQuestionsByCreationBinding.creationSwipeContainer.setOnRefreshListener(() -> {
            questionViewModel.refresh();
            mFragmentQuestionsByCreationBinding.creationSwipeContainer.setRefreshing(false);
        });
    }

    private void onLoaded() {
        mFragmentQuestionsByCreationBinding.creationPbFetchData.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByCreationBinding.creationRecyclerView.setVisibility(View.VISIBLE);
        mFragmentQuestionsByCreationBinding.creationTvError.setVisibility(View.INVISIBLE);
    }

    private void onError() {
        mFragmentQuestionsByCreationBinding.creationPbFetchData.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByCreationBinding.creationRecyclerView.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByCreationBinding.creationTvError.setVisibility(View.VISIBLE);
    }

    private void onLoading() {
        mFragmentQuestionsByCreationBinding.creationPbFetchData.setVisibility(View.VISIBLE);
        mFragmentQuestionsByCreationBinding.creationRecyclerView.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByCreationBinding.creationTvError.setVisibility(View.INVISIBLE);
    }
}