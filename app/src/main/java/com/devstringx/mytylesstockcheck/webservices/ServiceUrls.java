package com.devstringx.mytylesstockcheck.webservices;

import com.devstringx.mytylesstockcheck.BuildConfig;

public class ServiceUrls {

    private static ServiceUrls INSTANCE;

    public static ServiceUrls getInstace() {
        if (INSTANCE == null)
            init();
        return INSTANCE;
    }

    private String HOSTURL = BuildConfig.BASE_URL;//"https://agileatoz.com/api/v1/";

    public String getMETHODNAME(ServiceMethods serviceMethods) {
        switch (serviceMethods) {
            case WS_LOGINWITHMOBILEPASS:
                return HOSTURL + NKeys.LOGIN;
            case WS_GETALLLEADS:
                return HOSTURL + NKeys.GETALLLEADS;
            case WS_GETFILTERLEADS:
                return HOSTURL + NKeys.GETFILTERLEADS;
            case WS_GETOTP:
                return HOSTURL + NKeys.GETOTP;
            case WS_OTPVERIFICATION:
                return HOSTURL + NKeys.OTPVERIFICATION;
            case WS_RESETPASSWORD:
                return HOSTURL + NKeys.RESETPASSWORD;
            case WS_CHANGEPASSWORD:
                return HOSTURL + NKeys.CHANGEPASSWORD;
            case WS_GETLEADSTAGE:
                return HOSTURL + NKeys.GETLEADSTAGE;
            case WS_GETACTIVITYTYPE:
                return HOSTURL + NKeys.GETACTIVITYTYPE;
            case WS_UPDATELEADSTAGE:
                return HOSTURL + NKeys.UPDATELEADSTAGE;
            case WS_GETLEADLOSTREASON:
                return HOSTURL + NKeys.GETLEADLOSTREASONS;
            case WS_CREATELEADACTIVITY:
                return HOSTURL + NKeys.CREATELEADACTIVITY;
            case WS_CREATELEADTASK:
                return HOSTURL + NKeys.CREATELEADTASK;
            case WS_LEADDETAILS:
                return HOSTURL + NKeys.LEADDETAILS;
            case WS_GETREQUIRENMENTS:
                return HOSTURL + NKeys.GETREQUIRENMENTS;
            case WS_GETALLOWNERS:
                return HOSTURL + NKeys.GETALLOWNERS;
            case WS_GETMARKLEADASSTAR:
                return HOSTURL + NKeys.GETMARKLEADASSTAR;
            case WS_GETLEADTASK:
                return HOSTURL + NKeys.GETLEADTASK;
            case WS_GETLEADTASKBYLEADID:
                return HOSTURL + NKeys.GETLEADTASKBYLEADID;
            case WS_GETMARKTASKASCOMPLETED:
                return HOSTURL + NKeys.GETMARKTASKASCOMPLETED;
            case WS_GETRESCHEDULETASK:
                return HOSTURL + NKeys.GETRESCHEDULETASK;
            case WS_GETFILTERTASK:
                return HOSTURL + NKeys.GETFILTERTASK;
            case WS_GETCITIES:
                return HOSTURL + NKeys.GETCITIES;
            case WS_GETLEADHISTORYBYLEADID:
                return HOSTURL + NKeys.GETLEADHISTORYBYLEADID;
            case WS_GETSTATES:
                return HOSTURL + NKeys.GETSTATES;
            case WS_CREATELEAD:
                return HOSTURL + NKeys.CREATELEAD;
            case WS_UPDATELEAD:
                return HOSTURL + NKeys.UPDATELEAD;
            case WS_GETQUOTESDETAILS:
            case WS_GETALLQUOTES:
                return HOSTURL + NKeys.GETALLQUOTES;
            case WS_ALLPRODUCTINVENTRY:
                return HOSTURL + NKeys.GETALLPRODUCTINVENTRY;
            case WS_UPDATEBILLINGADDRESS:
                return HOSTURL + NKeys.UPDATEBILLINGADDRESS;
            case WS_ADDBILLINGADDRESS:
                return HOSTURL + NKeys.ADDBILLINGADDRESS;
            case WS_ADDSHIPPINGADDRESS:
                return HOSTURL + NKeys.ADDSHIPPINGADDRESS;
            case WS_FILTERGETVENDORQUOTATIONS:
            case WS_GETVENDORQUOTATIONS:
                return HOSTURL + NKeys.GETVENDORQUOTATIONS;
            case WS_GETLEADSHIPPINGADDRESS:
                return HOSTURL + NKeys.GETLEADSHIPPINGADDRESS;
            case WS_MAKEDEFAULTSHIPPINGADDRESS:
                return HOSTURL + NKeys.MAKEDEFAULTSHIPPINGADDRESS;
            case WS_CREATEQUOTATION:
                return HOSTURL + NKeys.CREATEQUOTATION;
            case WS_RECHECKQUOTE:
                return HOSTURL + NKeys.RECHECKQUOTE;
            case WS_DOWNLOADQUOTE:
                return HOSTURL + NKeys.DOWNLOADQUOTE;
            case WS_EDITQUOTE:
                return HOSTURL + NKeys.EDITQUOTE;
            case WS_UPDATEQUOTATIONPRODUCTSTATUS:
                return HOSTURL + NKeys.UPDATEQUOTATIONPRODUCTSTATUS;
            case WS_EXPORTQUOTATIONDATA:
                return HOSTURL + NKeys.EXPORTQUOTATIONDATA;
            case WS_UPLOADATTACHMENT:
                return HOSTURL + NKeys.UPLOADATTACHMENT;
            case WS_DELETEATTACHMENT:
                return HOSTURL + NKeys.DELETEATTACHMENT;
            case WS_DELETEQUOTATION:
                return HOSTURL + NKeys.DELETEQUOTATION;
            case WS_GETUSERPROFILE:
                return HOSTURL + NKeys.GETUSERPROFILE;
            case WS_UPDATEUSERPROFILE:
                return HOSTURL + NKeys.UPDATEUSERPROFILE;
            case WS_UPLOADPROFILEIMAGE:
                return HOSTURL + NKeys.UPLOADPROFILEIMAGE;
            case WS_REMOVEPROFILEPIC:
                return HOSTURL + NKeys.REMOVEPROFILEPIC;
            case WS_GETVENDORCOMPANYNAME:
                return HOSTURL + NKeys.GETVENDORCOMPANYNAME;
            case WS_GETROLES:
                return HOSTURL + NKeys.GETROLES;
            case WS_CHANGEPASSWORDFORUSER:
                return HOSTURL + NKeys.CHANGEPASSWORDFORUSER;
            case WS_UPDATESTATUS:
                return HOSTURL + NKeys.UPDATESTATUS;
            case WS_RESENDPASSWORD:
                return HOSTURL + NKeys.RESENDPASSWORD;
            case WS_CREATEUSER:
                return HOSTURL + NKeys.CREATEUSER;
            case WS_UPDATEUSER:
                return HOSTURL + NKeys.UPDATEUSER;
            case WS_GETMODULEBYUSER:
                return HOSTURL + NKeys.GETMODULEBYUSER;
            case WS_REMOVESHIPPINGADDRESS:
                return HOSTURL + NKeys.REMOVE_ADDRESS;
            case WS_TEMPORARYPASSWORDCHANGE:
                return HOSTURL + NKeys.TEMPORARYPASSWORDCHANGE;
            case WS_EXPORTUSERDATA:
                return HOSTURL + NKeys.EXPORTUSERDATA;
            case WS_EXPORTUSERQUOTATIONDATA:
                return HOSTURL + NKeys.EXPORTUSERQUOTATIONDATA;
            case WS_EXPORTLEADSDATA:
                return HOSTURL + NKeys.EXPORTLEADSDATA;
            case WS_ORDERDISPATCHED:
                return HOSTURL + NKeys.ORDERDISPATCHED;
            case WS_DASHBOARANALYTICSDDATA:
                return HOSTURL + NKeys.DASHBOARANALYTICSDDATA;
            case WS_ORDERANALYTICSDDATA:
                return HOSTURL + NKeys.ORDERANALYTICSDDATA;
            case WS_LEADANALYTICSDATA:
                return HOSTURL + NKeys.LEADANALYTICSDATA;
            case WS_LEADCONVERSATIONDATA:
                return HOSTURL + NKeys.LEADCONVERSATIONDATA;
            case WS_SCANALYTICSDATA:
                return HOSTURL + NKeys.SCANALYTICSDATA;

            case WS_EXPORTQUOTATIONANALYTICSDATA:
                return HOSTURL + NKeys.EXPORTQUOTATIONANALYTICSDATA;
            case WS_EXPORTLEADCONVDATA:
                return HOSTURL + NKeys.EXPORTLEADCONVDATA;
            case WS_EXPORTLEADGENDATA:
                return HOSTURL + NKeys.EXPORTLEADGENDATA;
            case WS_GETLEADQUOTATION:
                return HOSTURL + NKeys.GETLEADQUOTATION;
            case WS_GETUSERFORSCREEN:
                return HOSTURL + NKeys.GETUSERFORSCREEN;
            case WS_GETNOTIFICATIONLISTINGFORTASK:
                return HOSTURL + NKeys.GETNOTIFICATIONLISTINGFORTASK;
            case WS_GETNOTIFICATIONLISTINGFORRESPONSE:
                return HOSTURL + NKeys.GETNOTIFICATIONLISTINGFORRESPONSE;
            case WS_UPDATENOTIFICATION:
                return HOSTURL + NKeys.UPDATENOTIFICATION;
            case WS_NOTIFICATIONCOUNT:
                return HOSTURL + NKeys.NOTIFICATIONCOUNT;
            case WS_GETBRANCHNAME:
                return HOSTURL + NKeys.GETBRANCHNAME;
            case WS_GETRAZORPAY:
                return HOSTURL + NKeys.GETRAZORPAY;
            case WS_CREATERAZORPAY:
                return HOSTURL + NKeys.CREATERAZORPAY;
            case WS_DELETERAZORPAY:
                return HOSTURL + NKeys.DELETERAZORPAY;
            case WS_GETUSERSBYROLENAME:
                return HOSTURL + NKeys.GETUSERSBYROLENAME;
            case WS_GETALLCOMPLAINTS:
                return HOSTURL + NKeys.GETALLCOMPLAINTS;
            case WS_CREATECOMPLAINT:
                return HOSTURL + NKeys.CREATECOMPLAINT;
            case WS_GETALLCUSTOMERS:
                return HOSTURL + NKeys.GETALLCUSTOMERS;
            case WS_GETALLARCHITECT:
                return HOSTURL + NKeys.GETALLARCHITECT;
            case WS_GETARCHITECTDETAILS:
                return HOSTURL + NKeys.GETARCHITECTDETAILS;
            case WS_DELETEARCHITECT:
                return HOSTURL + NKeys.DELETEARCHITECT;
            case WS_GETORDERBYARCHITECT:
                return HOSTURL + NKeys.GETORDERBYARCHITECT;
            case WS_CHECKSALESORDERNUMBER:
                return HOSTURL + NKeys.CHECKSALESORDERNUMBER;
            case WS_GETCOMPLAINTDETAILS:
                return HOSTURL + NKeys.GETCOMPLAINTDETAILS;
            case WS_DELETECOMPLAINT:
                return HOSTURL + NKeys.DELETECOMPLAINT;
            case WS_COMPLAINTPROCESS:
                return HOSTURL + NKeys.COMPLAINTPROCESS;
            case WS_GETCOMPLAINTFILTERVALUE:
                return HOSTURL + NKeys.GETCOMPLAINTFILTERVALUE;
            case WS_EDITCOMPLAINT:
                return HOSTURL + NKeys.EDITCOMPLAINT;
            case WS_GENERATEARCHITECTLINK:
                return HOSTURL + NKeys.GENERATEARCHITECTLINK;
            case WS_DELETECOMPLAINTMEDIA:
                return HOSTURL + NKeys.DELETECOMPLAINTMEDIA;
            case WS_DELETEARCHITECTMEDIA:
                return HOSTURL + NKeys.DELETEARCHITECTMEDIA;
            case WS_CREATECOMPLAINTCOMMENT:
                return HOSTURL + NKeys.CREATECOMPLAINTCOMMENT;
            case WS_GETCOMPLAINTCOMMENT:
                return HOSTURL + NKeys.GETCOMPLAINTCOMMENT;
            case WS_GETORDERTABS:
                return HOSTURL + NKeys.GETORDERTAB;
            case WS_GETALLORDERS:
                return HOSTURL + NKeys.GETALLORDERS;
            case WS_GETORDERDETAILS:
                return HOSTURL + NKeys.GETORDERDETAILS;
            case WS_GETARCHITECTNAMELIST:
                return HOSTURL + NKeys.GETARCHITECTNAMELIST;
            case WS_GETALLWORKLOGS:
                return HOSTURL + NKeys.GETALLWORKLOGS;
            case WS_ORDERDELETE:
                return HOSTURL + NKeys.ORDERDELETE;
            case WS_ORDERCANCEL:
                return HOSTURL + NKeys.ORDERCANCEL;
            case WS_UPDATEDELIVERYTYPE:
                return HOSTURL + NKeys.UPDATEDELIVERYTYPE;
            case WS_UPDATEORDERTYPE:
                return HOSTURL + NKeys.UPDATEORDERTYPE;
            case WS_UPDATEDELIVERYLOCATIONTYPE:
                return HOSTURL + NKeys.UPDATEDELIVERYLOCATIONTYPE;
            case WS_ORDERRESCHEDULE:
                return HOSTURL + NKeys.ORDERRESCHEDULE;
            case WS_UPDATEPAYMENT:
                return HOSTURL + NKeys.UPDATEPAYMENT;
            case WS_GETORDERFILTERVALUE:
                return HOSTURL + NKeys.GETORDERFILTERVALUE;
            case WS_GET_BANK_DETAILS:
                return HOSTURL + NKeys.GET_BANK_DETAILS;
            case WS_LOGOUT:
                return HOSTURL + NKeys.LOGOUT;
            case WS_GET_ALL_TASK:
                return HOSTURL + NKeys.GETALLTASK;
            case WS_ADD_TASK:
                return HOSTURL + NKeys.ADDTASK;
            case WS_GET_TASK_DETAILS:
                return HOSTURL + NKeys.GET_TASK_DETAILS;
            case WS_COMPLETE_TASK:
                return HOSTURL + NKeys.COMPLETE_TASK;
            case WS_ASSIGNDELIVERYAGENT:
                return HOSTURL + NKeys.ASSIGNDELIVERYAGENT;
            case WS_UPLOADMULTIPLEORDERFILE:
                return HOSTURL + NKeys.UPLOADMULTIPLEORDERFILE;
            case WS_DELETEORDERFILE:
                return HOSTURL + NKeys.DELETEORDERFILE;
            case WS_GETORDERCOMMENT:
                return HOSTURL + NKeys.GETORDERCOMMENT;
            case WS_SENDORDERCOMMENT:
                return HOSTURL + NKeys.SENDORDERCOMMENT;
            case WS_STARTLOADING:
                return HOSTURL + NKeys.STARTLOADING;
            case WS_CHANGEORDERSTATUS:
                return HOSTURL + NKeys.CHANGEORDERSTATUS;
            case WS_GET_ANALYTICS:
                return HOSTURL + NKeys.GET_ANALYTICS;
            case WS_GET_TRANSACTION_HISTORY:
                return HOSTURL + NKeys.GET_TRANSACTION_HISTORY;
            case WS_EXPORTORDER:
                return HOSTURL + NKeys.EXPORTORDER;
            case WS_GETSINGLEUSERALLWORKLOGS:
                return HOSTURL + NKeys.GETSINGLEUSERALLWORKLOGS;
            case WS_EXPORTWORKLOG:
                return HOSTURL + NKeys.EXPORTWORKLOG;
            case WS_GET_ALL_SHIPPING_TAB:
                return HOSTURL + NKeys.GET_ALL_SHIPPING_TAB;
            case WS_GET_ALL_SHIPPING_CHARGE:
                return HOSTURL + NKeys.GET_ALL_SHIPPING_CHARGE;
            case WS_APPROVE_SHIPPING_CHARGE:
                return HOSTURL + NKeys.APPROVE_SHIPPING_CHARGE;
            case WS_REJECT_SHIPPING_CHARGE:
                return HOSTURL + NKeys.REJECT_SHIPPING_CHARGE;
            case WS_GETWORKLOGDETAILS:
                return HOSTURL + NKeys.GETWORKLOGDETAILS;
            case WS_DELETEWORKLOG:
                return HOSTURL + NKeys.DELETEWORKLOG;
            case WS_CREATEWORKLOG:
                return HOSTURL + NKeys.CREATEWORKLOG;
            case WS_DELETEWORKLOGFILE:
                return HOSTURL + NKeys.DELETEWORKLOGFILE;
            case WS_EXPORTALLWORKLOGS:
                return HOSTURL + NKeys.EXPORTALLWORKLOGS;
            case WS_EXPORTCOMPLAINTS:
                return HOSTURL + NKeys.EXPORTCOMPLAINTS;
            case WS_EXPORTSHIPPINGCHARGE:
                return HOSTURL + NKeys.EXPORTSHIPPINGCHARGE;
            case WS_GETSHIPPINGCHARGESTOADD:
                return HOSTURL + NKeys.GETSHIPPINGCHARGESTOADD;
            case WS_ADDSHIPPINGCHARGE:
                return HOSTURL + NKeys.ADDSHIPPINGCHARGE;
            case WS_UPLOADPOFILE:
                return HOSTURL + NKeys.UPLOADPOFILE;
            case WS_NOTIFICATIONLISTINGFORORDER:
                return HOSTURL + NKeys.NOTIFICATIONLISTINGFORORDER;
            case WS_NOTIFICATIONUPDATE:
                return HOSTURL + NKeys.NOTIFICATIONUPDATE;
            case WS_POSTATUSCHANGE:
                return HOSTURL + NKeys.POSTATUSCHANGE;
            case WS_POARRANGE:
                return HOSTURL + NKeys.POARRANGE;
            case WS_UPDATEPOAOTRANSPORTATION:
                return HOSTURL + NKeys.UPDATEPOAOTRANSPORTATION;
            case WS_GETPOCODES:
                return HOSTURL + NKeys.GETPOCODES;
            case WS_GETPRODUCTDETAILS:
                return HOSTURL + NKeys.GETPRODUCTDETAILS;
            case WS_SETELIGIBLEUSER:
                return HOSTURL + NKeys.SETELIGIBLEUSER;
            default:
                return HOSTURL;
        }
    }

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceUrls();
        }
    }


}
