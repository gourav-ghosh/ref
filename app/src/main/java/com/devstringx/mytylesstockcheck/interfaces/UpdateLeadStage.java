package com.devstringx.mytylesstockcheck.interfaces;

import java.util.List;

public interface UpdateLeadStage{
    void upldateLeadStageListner(String id,String StageName,String comment);
    void upldateLeadStageListnerLost(String id, String StageName, String comment, List<String> stringList);
}
