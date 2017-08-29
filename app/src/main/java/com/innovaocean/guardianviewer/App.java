package com.innovaocean.guardianviewer;

import android.app.Application;

public class App extends Application {

    private GuardianRepository guardianRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        guardianRepository = new GuardianRepository();
    }

    public void setGuardianRepository(GuardianRepository repository) {
        this.guardianRepository = repository;
    }

    public GuardianRepository getGuardianRepository() {
        return guardianRepository;
    }
}
