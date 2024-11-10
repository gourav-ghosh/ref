package com.devstringx.mytylesstockcheck.steperView;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails.HistoryData;
import com.devstringx.mytylesstockcheck.steperView.interfaces.StepLayoutInterface;




public abstract class StepView extends LinearLayout{

    private TextView tvTitle, originalVal_tv, tvPosition,newVal_tv;
    private LinearLayout  separator;
    private StepLayoutInterface stepLayoutInterface;
    protected int stepNumber;
    public STATE currentState = STATE.WAIT;



    enum STATE {
        WAIT,
        SELECTED,
        RESOLVED
    }

    public StepView(Context context) {
        super(context);
        initView();
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public abstract boolean onStepSuccess();
    public abstract String showTitle();
    public abstract void onStepCancel();
    public abstract String showSelection();
    public abstract void setupViews();


    public void setStepLayoutInterface(StepLayoutInterface stepLayoutInterface) {
        this.stepLayoutInterface = stepLayoutInterface;
    }

    public void setWaitState() {
        currentState = STATE.WAIT;
        tvPosition.setActivated(false);
        tvPosition.setSelected(false);

    }

    public void setResolvedState() {
        currentState = STATE.RESOLVED;
        tvPosition.setSelected(true);

        tvPosition.setText("");

    }

    public void setSelectedState() {
        currentState = STATE.SELECTED;
        tvPosition.setActivated(true);
        tvPosition.setSelected(false);

    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
        tvTitle.setText(showTitle());
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.step_view_layout, this, true);
        tvPosition = (TextView) findViewById(R.id.tvPosition);
        originalVal_tv = (TextView) findViewById(R.id.originalVal_tv);
        newVal_tv = (TextView) findViewById(R.id.newVal_tv);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        separator = (LinearLayout) findViewById(R.id.separator);

        setupViews();

    }
    public void setupData(HistoryData historyData) {
        tvTitle.setText(Html.fromHtml("<font color=#000000><b>"+historyData.getUserName()+"</b> </font> send response as \"" +
                "<font color=#F7941D> <b>"+historyData.getNewValue()+"</b> </font>\"  on "+historyData.getDateTime()));
        originalVal_tv.setText(historyData.getOldValue());
        newVal_tv.setText(historyData.getNewValue());
    }
    public void showSeparator(boolean show) {
        separator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
