package com.demo.generator.impl;

import com.demo.generator.Counter;
import com.demo.generator.NoDigitsConfigFactory;
import com.demo.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *   assoNo rules:  20240617 18976   at 20240617 the 18976th auths grant
 *       digits means resourceId  represent the sequence that today grant
 */
@Slf4j
public class AuthAssoNoGenerator {

    private static final String VERSION="1";

    private static int DEFAULT_NO_DIGITS= NoDigitsConfigFactory.getDigits(
            NoDigitsConfigFactory.AUTH_ASSO_PREFIX);

    private static Counter counter=new AssoNoCounter(DEFAULT_NO_DIGITS);

    private static volatile boolean isBeingReset=false;

    public static String generateAssoNo(){
        if(!isBeingReset) {
            return generateAssoNoInternal();
        }
        synchronized (counter){
            // second check if reset has completed
            // casue our business ThreadPool may have many thread to excute genAssoNo
            if(!isBeingReset)
                return generateAssoNoInternal();
            try {
                counter.wait();
            } catch (InterruptedException e) {
                log.error("Thread was interrupt when waiting counter set");
                throw new RuntimeException("generate authassociationFailed",e);
            }
        }
        return generateAssoNoInternal();
    }

    private static String generateAssoNoInternal(){
        String date = VERSION+DateUtils.getCurrentDateSecondsStr().replaceAll("-","");
        String recouseId = String.valueOf(counter.getNextCount());
        return date.concat(recouseId);
    }

    public static void resetAssoNo(){
        if(isBeingReset)
            return;
        synchronized (counter){
            // no need double check as in system only timedTask thread can trigger this
            isBeingReset=true;
            counter.reset();
            // after reset the thread dont held object monitor lock can execute as flag change
            isBeingReset=false;
            // to wake up the threads waiting on this lock
            counter.notifyAll();
        }
    }

    //inner static class
    private static class AssoNoCounter implements Counter<Integer>{

        // actually if mutiple threads try to access and get Count using CAS is not a good idea
        // we need block thread using other lock
        // this is for demostration
        private AtomicInteger assoNo;

        private int initalAssoNo;

        public AssoNoCounter(int digits){
            initalAssoNo=(int)Math.pow(10,digits-1);
            assoNo=new AtomicInteger(initalAssoNo);
        }

        @Override
        public Integer getNextCount() {
            return assoNo.getAndIncrement();
        }

        @Override
        public void reset() {
          assoNo.set(initalAssoNo);
        }
    }
}
