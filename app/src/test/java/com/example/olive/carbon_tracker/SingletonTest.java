package com.example.olive.carbon_tracker;

import org.junit.Test;

import static org.junit.Assert.*;


public class SingletonTest {
    @Test
    public void testVehicleList() {
        Vehicle testVehicle = new Vehicle();
        Singleton.getCurrInstance().addVehicle(testVehicle);
        Vehicle output = Singleton.getCurrInstance().getVehicle(0);
        assertEquals(testVehicle, output);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testVehicleListException() {
        Vehicle testVehicle = new Vehicle();
        Vehicle output = Singleton.getCurrInstance().getVehicle(-1);
        assertEquals(testVehicle, output);
    }

    @Test
    public void testRouteList() {
        Route testRoute = new Route("Test", 100, 150);
        Singleton.getCurrInstance().addRoute(testRoute);
        Route output = Singleton.getCurrInstance().getRoute(0);
        assertEquals(testRoute, output);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRouteListException() {
        Route testRoute = new Route("Test", 100, 150);
        Route output = Singleton.getCurrInstance().getRoute(-1);
        assertEquals(testRoute, output);
    }
}