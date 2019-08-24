package edu.caltech.cs2.lab07;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class GerrymanderingTests {
    public void testGerrymandering(int[] precinctsAVotes,
                                   int[] precinctsBVotes, boolean actual) {
        assertEquals(Gerrymandering.canGerrymander(precinctsAVotes,
                precinctsBVotes), actual);
    }

    @Order(0)
    @DisplayName("Testing A Wins All Precincts")
    @Test
    public void testAWinsAllPrecincts() {
        testGerrymandering(new int[]{55, 45, 67, 54}, new int[]{40, 40, 40, 40}, true);
    }

    @Order(1)
    @DisplayName("Testing A Loses All Precincts")
    @Test
    public void testALosesAllPrecincts() {
        testGerrymandering(new int[]{40, 40, 40, 40}, new int[]{55, 45, 67, 54}, false);
    }

    @Order(2)
    @DisplayName("Testing A Ties All Precincts")
    @Test
    public void testATiesAllPrecincts() {
        testGerrymandering(new int[]{40, 40, 40, 40}, new int[]{40, 40, 40, 40}, false);
    }

    @Order(3)
    @DisplayName("Testing Simple One Way A Wins")
    @Test
    public void testSimpleCase() {
        testGerrymandering(new int[]{9, 12, 10, 11}, new int[]{10, 10, 10, 10}, true);
    }

    @Order(4)
    @DisplayName("Testing One Way A Can Win Both Districts")
    @Test
    public void testOneWayAWins() {
        testGerrymandering(new int[]{55, 43, 60, 47}, new int[]{45, 57, 40, 40}, true);
    }

    @Order(5)
    @DisplayName("Testing A Can Only Win 1 District")
    @Test
    public void testOneDistrictWin() {
        testGerrymandering(new int[]{55, 43, 60, 47}, new int[]{45, 64, 40, 40}, false);
    }

    @Order(6)
    @DisplayName("Testing A Can Only Win 1 District, Can Tie Other")
    @Test
    public void testOneDistrictWinOneTie() {
        testGerrymandering(new int[]{50, 40, 45, 45}, new int[]{40, 50, 43, 43}, false);
    }

    @Order(7)
    @DisplayName("Testing Gerrymandering More Precincts")
    @Test
    public void testMorePrecincts() {
        testGerrymandering(new int[]{95, 45, 64, 75, 40, 46, 25, 36, 64, 65},
                new int[]{36, 75, 43, 65, 74, 34, 23, 85, 93, 23}, true);
    }
}