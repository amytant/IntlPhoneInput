package net.rimoto.wheninroam.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import net.rimoto.wheninroam.PagerIndicator.LoginAnimationIndicator;
import net.rimoto.wheninroam.PagerIndicator.SimpleTitleIndicator;
import net.rimoto.wheninroam.R;
import net.rimoto.wheninroam.PagerIndicator.IndicatorAggregator;
import net.rimoto.wheninroam.utils.RimotoCore.Login;
import net.rimoto.wheninroam.utils.RimotoCore.RimotoException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import net.rimoto.wheninroam.adapter.LoginFragmentAdapter;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {
    @ViewById(R.id.pager)
    protected ViewPager mPager;

    @ViewById(R.id.titles)
    protected SimpleTitleIndicator mTitleIndicator;

    @ViewById(R.id.indicator)
    protected CirclePageIndicator mCircleIndicator;

    @AfterViews
    protected void pagerAdapter() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new LoginFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        IndicatorAggregator indicatorAggregator = new IndicatorAggregator();
        indicatorAggregator.addIndicator(mTitleIndicator);
        indicatorAggregator.addIndicator(mCircleIndicator);
        indicatorAggregator.addIndicator(new LoginAnimationIndicator());
        indicatorAggregator.setViewPager(mPager);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Click(R.id.login_btn)
    protected void loginClick() {
        try {
            Login.getInstance().auth(this, (token, error) -> {
                if(error==null) {
                    getVPNConfig();
                    startWizard();
                }
            });
        } catch (RimotoException e) {
            e.printStackTrace();
        }
    }

    private void getVPNConfig() {

    }
    private void startWizard() {
        Intent intent = new Intent(this, WizardActivity_.class);
        startActivity(intent);
        finish();
    }
}