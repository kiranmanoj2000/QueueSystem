package com.hackathon.hackathon;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class JobSchedulerService extends JobService {

    private boolean jobWorking = false;
    public boolean jobCancel = false;

    @Override
    public boolean onStartJob(JobParameters parameters){
        jobWorking = true;
        // job logic

        return jobWorking;

    }

    @Override
    public boolean onStopJob(JobParameters parameters){
        jobCancel = true;
        // if the job is done, it does not need to be rescheduled and vice versa
        boolean reschedule = jobWorking;
        // let the system know the job has been finished
        jobFinished(parameters, reschedule);
        // let the system know if it needs to be reschedueled
        return reschedule;
    }
}

