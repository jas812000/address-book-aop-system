
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_aspects;

import utilities.LogUtil;

/**
 * Aspect for logging file and directory resolution activity in the AppPaths class.
 * 
 * This aspect intercepts calls to:
 * - AppPaths.getFile(String)
 * - AppPaths.getSubDirectory(String)
 * 
 * It logs these method invocations to track usage of dynamic path resolution within the app.
 */
public aspect AppPathsLoggingAspect {

    /**
     * Pointcut that matches any call to AppPaths.getFile(..) or AppPaths.getSubDirectory(..).
     */
    pointcut appPathsMethodCall(): 
        execution(* io.AppPaths.getFile(..)) || execution(* io.AppPaths.getSubDirectory(..));

    /**
     * After advice that logs usage of AppPaths path-resolving methods.
     * The message includes a standard "[AppPaths]" prefix for filtering.
     */
    after(): appPathsMethodCall() {
        LogUtil.logToFile("INFO", "[AppPaths] Method called");
    }
}
