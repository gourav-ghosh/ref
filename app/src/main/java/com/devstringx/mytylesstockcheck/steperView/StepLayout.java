package com.devstringx.mytylesstockcheck.steperView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails.HistoryData;
import com.devstringx.mytylesstockcheck.steperView.interfaces.StepLayoutInterface;
import com.devstringx.mytylesstockcheck.steperView.interfaces.StepLayoutResult;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pedrocarrillo on 9/14/16.
 */

public class StepLayout extends LinearLayout implements StepLayoutInterface {

    private List<StepView> stepViews = new ArrayList<>();
    private StepperType stepperType = StepperType.VERTICAL;
    private StepLayoutResult stepLayoutResult;
    private int currentStep = 0;

    enum StepperType {
        VERTICAL
    }

    public void setStepperType(StepperType stepperType) {
        this.stepperType = stepperType;
    }

    public void setStepLayoutResult(StepLayoutResult stepLayoutResult) {
        this.stepLayoutResult = stepLayoutResult;
    }

    public void addStepView(StepView stepView) {
        stepView.setStepLayoutInterface(this);
        stepViews.add(stepView);
        stepView.setStepNumber(stepViews.size());
    }

    public StepLayout(Context context) {
        super(context);
    }

    public StepLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void load(List<HistoryData> data) {
        setOrientation(StepView.VERTICAL);
        for (int i = 0; i < stepViews.size(); i++) {
            StepView stepView = stepViews.get(i);
            addView(stepView);
            stepView.setSelectedState();
            stepView.setupData(data.get(i));
            if((stepViews.size()-1)==i){
                stepView.showSeparator(false);
            }else{
                stepView.showSeparator(true);
            }
        }
    }

    @Override
    public void onStepSuccess() {
        if (currentStep + 1 < stepViews.size()) {
            stepViews.get(currentStep).setResolvedState();
            currentStep++;
            stepViews.get(currentStep).setSelectedState();
        } else {
            if (stepLayoutResult != null) {
                stepViews.get(currentStep).setResolvedState();
                stepLayoutResult.onFinish();
            }
        }
    }

    @Override
    public void onStepCancel() {
        if (currentStep - 1 >= 0) {
            stepViews.get(currentStep).setWaitState();
            currentStep--;
            stepViews.get(currentStep).setStepNumber(currentStep+1);
            stepViews.get(currentStep).setSelectedState();
        } else {
            if (stepLayoutResult != null) {
                stepLayoutResult.onCancel();
            }
        }
    }
}
