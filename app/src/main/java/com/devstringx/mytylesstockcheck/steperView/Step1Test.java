package com.devstringx.mytylesstockcheck.steperView;

import android.content.Context;


public class Step1Test extends StepView {

    public Step1Test(Context context) {
        super(context);
    }

    @Override
    public boolean onStepSuccess() {
        return true;
    }

    @Override
    public void setupViews() {
        // find my views.
    }

    @Override
    public String showTitle() {
        return "Step "+stepNumber;
    }

    @Override
    public void onStepCancel() {

    }

    @Override
    public String showSelection() {
        return "selected item 1";
    }

}
