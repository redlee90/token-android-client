package com.bakkenbaeck.token.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakkenbaeck.token.R;
import com.bakkenbaeck.token.databinding.FragmentScannerBinding;
import com.bakkenbaeck.token.presenter.ScannerPresenter;
import com.bakkenbaeck.token.presenter.factory.PresenterFactory;
import com.bakkenbaeck.token.presenter.factory.ScannerPresenterFactory;

public class ScannerFragment extends BasePresenterFragment<ScannerPresenter, ScannerFragment>  {
    private ScannerPresenter presenter;
    private FragmentScannerBinding binding;

    public static Fragment newInstance() {
        return new ScannerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle inState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false);
        return binding.getRoot();
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.presenter != null) {
            this.presenter.setUserVisibleHint(isVisibleToUser);
        }
    }

    public FragmentScannerBinding getBinding() {
        return this.binding;
    }

    @NonNull
    @Override
    protected PresenterFactory<ScannerPresenter> getPresenterFactory() {
        return new ScannerPresenterFactory();
    }

    @Override
    protected void onPresenterPrepared(@NonNull final ScannerPresenter presenter) {
        this.presenter = presenter;
    }

}