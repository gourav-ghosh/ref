package com.devstringx.mytylesstockcheck.interfaces;

public interface LeadTask {
    void createTaskListner(String id,String follow_up_date,String follow_up_time,String subject,String task_details,boolean reminder);
    void rescheduleTaskListner(int id,String follow_up_date,String follow_up_time,String subject,String task_details,boolean reminder);

}
