package com.devstringx.mytylesstockcheck.webservices;

/**
 * Created by venkatarao on 2/8/2017.
 */

public enum ServiceMethods {
    WS_SECRETKEY,
    WS_LOGINWITHMOBILEPASS,
    WS_GETOTP,
    WS_OTPVERIFICATION,
    WS_RESETPASSWORD,
    WS_CHANGEPASSWORD,
    WS_GETALLLEADS,
    WS_GETLEADSTAGE,
    WS_GETLEADLOSTREASON,
    WS_GETACTIVITYTYPE,
    WS_UPDATELEADSTAGE,
    WS_CREATELEADACTIVITY,
    WS_GETFILTERLEADS,
    WS_CREATELEADTASK,
    WS_LEADDETAILS,
    WS_GETREQUIRENMENTS,
    WS_GETALLOWNERS,
    WS_GETFILTERTASK,
    WS_GETLEADHISTORYBYLEADID,
    WS_GETMARKLEADASSTAR,
    WS_GETLEADTASK,
    WS_GETLEADTASKBYLEADID,
    WS_GETMARKTASKASCOMPLETED,
    WS_GETRESCHEDULETASK,
    WS_GETCITIES,
    WS_GETSTATES,
    WS_CREATELEAD,
    WS_UPDATELEAD,
    WS_GETALLQUOTES,
    WS_ALLPRODUCTINVENTRY,
    WS_GETQUOTESDETAILS,
    WS_UPDATEBILLINGADDRESS,
    WS_ADDBILLINGADDRESS,
    WS_ADDSHIPPINGADDRESS,
    WS_GETVENDORQUOTATIONS,
    WS_GETLEADSHIPPINGADDRESS,
    WS_MAKEDEFAULTSHIPPINGADDRESS,
    WS_REMOVESHIPPINGADDRESS,
    WS_CREATEQUOTATION,
    WS_RECHECKQUOTE,
    WS_DOWNLOADQUOTE,
    WS_EDITQUOTE,
    WS_UPDATEQUOTATIONPRODUCTSTATUS,
    WS_FILTERGETVENDORQUOTATIONS,
    WS_EXPORTQUOTATIONDATA,
    WS_UPLOADATTACHMENT,
    WS_DELETEATTACHMENT,
    WS_DELETEQUOTATION,
    WS_GETUSERPROFILE,
    WS_UPDATEUSERPROFILE,
    WS_UPLOADPROFILEIMAGE,
    WS_REMOVEPROFILEPIC,
    WS_GETVENDORCOMPANYNAME,
    WS_GETROLES,
    WS_CHANGEPASSWORDFORUSER,
    WS_UPDATESTATUS,
    WS_RESENDPASSWORD,
    WS_CREATEUSER,
    WS_UPDATEUSER,
    WS_GETMODULEBYUSER,
    WS_TEMPORARYPASSWORDCHANGE,
    WS_EXPORTUSERDATA,
    WS_EXPORTUSERQUOTATIONDATA,
    WS_EXPORTLEADSDATA,
    WS_ORDERDISPATCHED,
    WS_DASHBOARANALYTICSDDATA,
    WS_ORDERANALYTICSDDATA,
    WS_LEADANALYTICSDATA,
    WS_LEADCONVERSATIONDATA,
    WS_SCANALYTICSDATA,
    WS_EXPORTQUOTATIONANALYTICSDATA,
    WS_EXPORTLEADCONVDATA,
    WS_EXPORTLEADGENDATA,
    WS_GETLEADQUOTATION,
    WS_GETALLCUSTOMERS,
    WS_GETUSERFORSCREEN,
    WS_GETNOTIFICATIONLISTINGFORTASK,
    WS_GETNOTIFICATIONLISTINGFORRESPONSE,
    WS_UPDATENOTIFICATION,
    WS_NOTIFICATIONCOUNT,
    WS_GETBRANCHNAME,
    WS_LOGOUT,
    WS_GETRAZORPAY,
    WS_CREATERAZORPAY,
    WS_DELETERAZORPAY,
    WS_GETUSERSBYROLENAME,
    WS_GETALLCOMPLAINTS,
    WS_CREATECOMPLAINT,
    WS_GETALLARCHITECT,
    WS_CREATEARCHITECT,
    WS_GETARCHITECTDETAILS,
    WS_DELETEARCHITECT,
    WS_CHECKSALESORDERNUMBER,
    WS_GETCOMPLAINTDETAILS,
    WS_DELETECOMPLAINT,
    WS_COMPLAINTPROCESS,
    WS_GETCOMPLAINTFILTERVALUE,
    WS_RESOLVECOMPLAINT,
    WS_EDITCOMPLAINT,
    WS_UPLOADCOMPLAINTMEDIA,
    WS_GENERATEARCHITECTLINK,
    WS_DELETECOMPLAINTMEDIA,
    WS_DELETEARCHITECTMEDIA,
    WS_CREATECOMPLAINTCOMMENT,
    WS_GETCOMPLAINTCOMMENT,
    WS_GETORDERBYARCHITECT,
    WS_GETORDERTABS,
    WS_GETARCHITECTNAMELIST,
    WS_GETALLORDERS,
    WS_GETORDERDETAILS,
    WS_GETALLWORKLOGS,
    WS_ORDERDELETE,
    WS_ORDERCANCEL,
    WS_GET_ALL_TASK,
    WS_ADD_TASK,
    WS_GET_TASK_DETAILS,
    WS_COMPLETE_TASK,
    WS_UPDATEDELIVERYTYPE,
    WS_UPDATEORDERTYPE,
    WS_UPDATEDELIVERYLOCATIONTYPE,
    WS_ORDERRESCHEDULE,
    WS_UPDATEPAYMENT,
    WS_GETORDERFILTERVALUE,
    WS_ASSIGNDELIVERYAGENT,
    WS_UPLOADMULTIPLEORDERFILE,
    WS_DELETEORDERFILE,
    WS_GET_ANALYTICS,
    WS_GET_TRANSACTION_HISTORY,
    WS_SENDORDERCOMMENT,
    WS_GETORDERCOMMENT,
    WS_STARTLOADING,
    WS_CHANGEORDERSTATUS,
    WS_EXPORTORDER,
    WS_GETSINGLEUSERALLWORKLOGS,
    WS_EXPORTWORKLOG,
    WS_GET_ALL_SHIPPING_TAB,
    WS_GET_ALL_SHIPPING_CHARGE,
    WS_APPROVE_SHIPPING_CHARGE,
    WS_REJECT_SHIPPING_CHARGE,
    WS_GETWORKLOGDETAILS,
    WS_DELETEWORKLOG,
    WS_CREATEWORKLOG,
    WS_DELETEWORKLOGFILE,
    WS_EXPORTALLWORKLOGS,
    WS_EXPORTCOMPLAINTS,
    WS_EXPORTSHIPPINGCHARGE,
    WS_GETSHIPPINGCHARGESTOADD,
    WS_ADDSHIPPINGCHARGE,
    WS_UPLOADPOFILE,
    WS_NOTIFICATIONLISTINGFORORDER,
    WS_NOTIFICATIONUPDATE,
    WS_GET_BANK_DETAILS,
    WS_POSTATUSCHANGE,
    WS_POARRANGE,
    WS_UPDATEPOAOTRANSPORTATION,
    WS_GETPOCODES,
    WS_GETPRODUCTDETAILS,
    WS_SETELIGIBLEUSER,
}
