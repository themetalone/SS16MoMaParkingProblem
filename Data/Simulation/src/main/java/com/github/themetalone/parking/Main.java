package com.github.themetalone.parking;

import com.github.themetalone.parking.core.Simulation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;

/**
 * Main Class for the Simulation
 * Created by steff on 06.07.2016.
 */
public class Main {

    public static void main(String[] args){

        // Try to load external spring config
        ApplicationContext applicationContext;
        applicationContext = new ClassPathXmlApplicationContext("spring.cfg.xml");
        if(args!=null) {
            for (String s : args) {
                if(s.startsWith("cfg=")){
                    String location = s.replaceFirst("cfg=","");
                    if((new File(location)).exists()){
                        // may throw RuntimeException
                        applicationContext = new FileSystemXmlApplicationContext(location);
                    }
                    break;
                }
            }
        }


        Object bean = applicationContext.getBean("Simulation");
        if(!(bean instanceof Simulation)){
            return;
        }
        Simulation simulation = (Simulation) bean;
        simulation.simulate();
    }

}
