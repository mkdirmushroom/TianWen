package me.jeremy.ccst.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;

import me.jeremy.ccst.R;

/**
 * Created by qiugang on 2014/10/15.
 */
public class LoadingFooter {
    protected View mLoadingFooter;

    protected TextView mLoadingText;

    protected State mState = State.Idle;

    private ProgressBar mProgress;

    private long mAnimationDuration;

    public static enum State {
        Idle, TheEnd, Loading
    }

    public LoadingFooter(Context context) {
        mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
        mLoadingFooter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        mProgress = (ProgressBar) mLoadingFooter.findViewById(R.id.footer_progressBar);
        mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.footer_textView);
        mAnimationDuration = context.getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mProgress.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(context).build());
        setState(State.Idle);
    }

    public View getView() {
        return mLoadingFooter;
    }

    public State getState() {
        return mState;
    }

    public void setState(final State state, long delay) {
        mLoadingFooter.postDelayed(new Runnable() {

            @Override
            public void run() {
                setState(state);
            }
        }, delay);
    }

    public void setmLoadingText(String text) {
        mLoadingText.setText(text);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setState(State status) {
        if (mState == status) {
            return;
        }
        mState = status;

        mLoadingFooter.setVisibility(View.VISIBLE);

        switch (status) {
            case Loading:
                mLoadingText.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                break;
            case TheEnd:
                mLoadingText.setVisibility(View.VISIBLE);
                mLoadingText.animate().withLayer().alpha(1).setDuration(mAnimationDuration);
                mProgress.setVisibility(View.GONE);
                break;
            default:
                mLoadingFooter.setVisibility(View.GONE);
                break;
        }
    }

}
