package com.nesterov.server.service;

import com.nesterov.core.request.ActionType;
import com.nesterov.server.statistics.StatisticsItem;
import com.nesterov.server.statistics.StatisticsStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

//Сервис сохраненя статистки
public class StatistcsService implements Service {
    private final Timer timer;
    private final StatisticsStorage<ActionType> statisticsStorage;
    private final File file;
    private long timeOut;

    public StatistcsService(File file, StatisticsStorage<ActionType> statisticsStorage, long timeout, TimeUnit timeUnit)
    {
        this.statisticsStorage=statisticsStorage;
        this.timer=new Timer(false);
        this.file=file;
        this.timeOut=timeUnit.toMillis(timeout);
    }
    @Override
    public void run() {
        //Каждые N секунд созраняет статистику  в файл
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Calendar calendar=Calendar.getInstance();
                    FileOutputStream fileOutputStream=new FileOutputStream(file,true);
                    fileOutputStream.write(String.format("-----STAT %s-----\n",calendar.getTime()).getBytes());
                StatisticsItem<String,Integer> event=statisticsStorage.getEvent(ActionType.FILE);
                    Set<String> files=event.getKeys();
                    for(String file:files) {
                            fileOutputStream.write(
                                    String.format("*********\n File name:%s\t Downloads count:%d\n", file,event.get(file)).getBytes());

                    }
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, this.timeOut,this.timeOut);
    }

    @Override
    public void stop() {
        this.timer.cancel();
    }
}
