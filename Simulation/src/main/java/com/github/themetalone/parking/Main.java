package com.github.themetalone.parking;

import com.github.themetalone.parking.core.Simulation;
import com.sun.deploy.appcontext.AppContext;
import com.sun.glass.ui.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by steff on 06.07.2016.
 */
public class Main {

    public static void main(String[] args){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.cfg.xml");
        Object bean = applicationContext.getBean("Simulaation");
        if(!(bean instanceof Simulation)){
            return;
        }
        Simulation simulation = (Simulation) bean;
        simulation.simulate(10000);
    }

}
