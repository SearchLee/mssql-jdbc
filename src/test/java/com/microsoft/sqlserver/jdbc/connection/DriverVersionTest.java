/*
 * Microsoft JDBC Driver for SQL Server
 * 
 * Copyright(c) Microsoft Corporation All rights reserved.
 * 
 * This program is made available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */
package com.microsoft.sqlserver.jdbc.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Random;


import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.testframework.AbstractTest;
import com.microsoft.sqlserver.testframework.util.Util;

/**
 * This test validates PR #342. In this PR, DatatypeConverter#parseHexBinary is replaced with type casting. This tests validates if the behavior
 * reminds the same.
 *
 * The valid length of driver version byte array has to be 4. Otherwise, connection won't be established. Therefore, the valid value for the original
 * method is between 0 and 255 (inclusive).
 *
 */
@RunWith(JUnitPlatform.class)
public class DriverVersionTest extends AbstractTest {
    Random rand = new Random();
    int major = rand.nextInt(256);
    int minor = rand.nextInt(256);
    int patch = rand.nextInt(256);
    int build = rand.nextInt(256);

    /**
     * validates version byte array generated by the original method and type casting reminds the same.
     */
    @Test
    public void testConnectionDriver() {
        // the original way to create version byte array
        String interfaceLibVersion = generateInterfaceLibVersion();
        byte originalVersionBytes[] = Util.hexStringToByte(interfaceLibVersion);

        String originalBytes = Arrays.toString(originalVersionBytes);

        // the new way to create version byte array
        byte newVersionBytes[] = {(byte) build, (byte) patch, (byte) minor, (byte) major};

        String newBytes = Arrays.toString(newVersionBytes);

        assertEquals(originalBytes, newBytes, "Original: " + originalBytes + "; New: " + newBytes);
    }

    /**
     * the original method that converts version number to hex string
     * 
     * @return
     */
    private String generateInterfaceLibVersion() {
        StringBuilder outputInterfaceLibVersion = new StringBuilder();

        String interfaceLibMajor = Integer.toHexString(major);
        String interfaceLibMinor = Integer.toHexString(minor);
        String interfaceLibPatch = Integer.toHexString(patch);
        String interfaceLibBuild = Integer.toHexString(build);

        // build the interface lib name
        // 2 characters reserved for build
        // 2 characters reserved for patch
        // 2 characters reserved for minor
        // 2 characters reserved for major
        if (2 == interfaceLibBuild.length()) {
            outputInterfaceLibVersion.append(interfaceLibBuild);
        }
        else {
            outputInterfaceLibVersion.append("0");
            outputInterfaceLibVersion.append(interfaceLibBuild);
        }
        if (2 == interfaceLibPatch.length()) {
            outputInterfaceLibVersion.append(interfaceLibPatch);
        }
        else {
            outputInterfaceLibVersion.append("0");
            outputInterfaceLibVersion.append(interfaceLibPatch);
        }
        if (2 == interfaceLibMinor.length()) {
            outputInterfaceLibVersion.append(interfaceLibMinor);
        }
        else {
            outputInterfaceLibVersion.append("0");
            outputInterfaceLibVersion.append(interfaceLibMinor);
        }
        if (2 == interfaceLibMajor.length()) {
            outputInterfaceLibVersion.append(interfaceLibMajor);
        }
        else {
            outputInterfaceLibVersion.append("0");
            outputInterfaceLibVersion.append(interfaceLibMajor);
        }

        return outputInterfaceLibVersion.toString();
    }
}
