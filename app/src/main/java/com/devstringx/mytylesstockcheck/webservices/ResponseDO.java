package com.devstringx.mytylesstockcheck.webservices;

public class ResponseDO {
    private ServiceMethods serviceMethods;
    private String response;
    private int totalPages=1;
    private boolean isError = false;
    private int errorCode = -1;
    private int code = -1;
    private String section_type;
    private String apiEndPoint="";
    private String api_base_url="";
    public ServiceMethods getServiceMethods() {
        return serviceMethods;
    }

    public void setServiceMethods(ServiceMethods serviceMethods) {
        this.serviceMethods = serviceMethods;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSection_type() {
        return section_type;
    }

    public void setSection_type(String section_type) {
        this.section_type = section_type;
    }

    public String getApi_base_url() {
        return api_base_url;
    }

    public void setApi_base_url(String api_base_url) {
        this.api_base_url = api_base_url;
    }

    public String getApiEndPoint() {
        return apiEndPoint;
    }

    public void setApiEndPoint(String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
    }
}
