package com.devstringx.mytylesstockcheck.datamodal;

import java.util.ArrayList;
import java.util.List;

public class HelpingAgentData {
    private int sr_no;
    private String agentId = "";
    private String expDeliveryDate;
    private String expDeliveryTime;
    private String comment;
    private List<String> selectedHelpingAgentPOList = new ArrayList<>();

    public void setSr_no(int sr_no) {
        this.sr_no = sr_no;
    }

    public int getSr_no() {
        return sr_no;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setExpDeliveryDate(String expDeliveryDate) {
        this.expDeliveryDate = expDeliveryDate;
    }

    public String getExpDeliveryDate() {
        return expDeliveryDate;
    }

    public void setExpDeliveryTime(String expDeliveryTime) {
        this.expDeliveryTime = expDeliveryTime;
    }

    public String getExpDeliveryTime() {
        return expDeliveryTime;
    }

    public void setSelectedHelpingAgentPOList(List<String> selectedHelpingAgentPOList) {
        this.selectedHelpingAgentPOList.addAll(selectedHelpingAgentPOList);
    }

    public List<String> getSelectedHelpingAgentPOList() {
        return selectedHelpingAgentPOList;
    }
    public void removeSelectedPOList(String poId) {
        selectedHelpingAgentPOList.remove(poId);
    }
    public void addSelectedPOList(String poId) {
        selectedHelpingAgentPOList.add(poId);
    }
}
