package com.github.themetalone.parking;

import com.github.themetalone.parking.core.simulations.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Main Class for the Simulation
 * Created by steff on 06.07.2016.
 */
public class Main {

    private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String externalConfig = System.getProperty("cfg");
        ApplicationContext applicationContext;
        if (externalConfig == null) {
            LOG.info("Loading internal configuration");
            applicationContext = new ClassPathXmlApplicationContext("spring-config2.xml");
        } else {
            LOG.info("Loading external configuration file {}", externalConfig);
            applicationContext = new FileSystemXmlApplicationContext(externalConfig);
        }

        Object bean = applicationContext.getBean("Simulation");
        if (!(bean instanceof Simulation)) {
            LOG.error("Simulation bean is not instanceof Simulation but of {}", bean.getClass().getName());
            return;
        }
        Simulation simulation = (Simulation) bean;
        simulation.simulate();
    }

}
