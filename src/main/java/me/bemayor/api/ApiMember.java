package me.bemayor.api;

public class ApiMember {
    protected final ApiManagement apiManager;

    public ApiMember(ApiManagement apiManagement) {
        apiManager = apiManagement;
    }

    public ApiManagement getApiManager() {
        return apiManager;
    }
}
