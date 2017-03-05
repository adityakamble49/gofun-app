package com.adityakamble49.ttl.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.services.CountDownService;
import com.adityakamble49.ttl.utils.Constants;
import com.adityakamble49.ttl.utils.DateTimeUtil;
import com.adityakamble49.ttl.utils.ServiceUtils;
import com.adityakamble49.ttl.utils.SharedPrefUtils;
import com.adityakamble49.ttl.utils.TimerManager;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class TimerFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView mTimerTextView;
    private String mDefaultTimer;
    private TextView mTimeInTextView;
    private CircularProgressBar mCircularProgress;
    private TextView mTimeOutTextView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.Timer.KEY_COUNTDOWN)) {
                long millisUntilFinished = intent.getLongExtra(Constants.Timer.KEY_COUNTDOWN, 0);
                String timeLeft = DateTimeUtil.getHrsMinSecFromMillis(millisUntilFinished);
                mTimerTextView.setText(timeLeft);
                float percentLeft = TimerManager.getInstance(getContext()).getPercentLeft
                        (millisUntilFinished);
                updateCircularProgress(percentLeft);
                mTimeInTextView.setText(DateTimeUtil.getTimeFromMillis(TimerManager.getInstance
                        (getContext()).getTimeInMillis()));
                mTimeOutTextView.setText(DateTimeUtil.getTimeFromMillis(TimerManager.getInstance
                        (getContext()).getTimeOutMillis()));
            }
        }
    };

    public TimerFragment() {
        // Required empty public constructor
    }

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        mTimerTextView = (TextView) rootView.findViewById(R.id.v_tv_time_left);
        mTimeInTextView = (TextView) rootView.findViewById(R.id.v_tv_time_in);
        mTimeOutTextView = (TextView) rootView.findViewById(R.id.v_tv_time_out);
        mCircularProgress = (CircularProgressBar) rootView.findViewById(R.id.v_cpb_circular_timer);
        updateCircularProgress(100);
        startCountDownTimer();
        return rootView;
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(broadcastReceiver, new IntentFilter(Constants
                .BroadcastIntents.COUNTDOWN_SR));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    /**
     * Start CountDown Timer if not completed for day
     * In case User force stops app, CountDownService stops and lose timer
     * So to restart CountDown Timer with actual time left
     */
    private void startCountDownTimer() {
        if (SharedPrefUtils.getLongFromPreferences(getContext(), Constants.Timer.KEY_IN_TIME,
                Constants.Timer.IN_TIME_EMPTY) != Constants.Timer.IN_TIME_EMPTY) {
            if (!ServiceUtils.getInstance(getContext().getApplicationContext()).isMyServiceRunning
                    (CountDownService.class)) {
                getContext().startService(new Intent(getContext(), CountDownService.class));
            }
        }
    }

    private void updateCircularProgress(float percentage) {
        int animationDuration = 2500;
        mCircularProgress.setProgressWithAnimation(percentage, animationDuration);
    }
}
