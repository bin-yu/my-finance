/********************************************************************
 * File Name:    CompositeTriggeringPolicy.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.util.logback;

import java.io.File;

import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.util.FileSize;

/**
 * A composite triggering policy to roll based on both log file size and startup.
 */
public class CompositeTriggeringPolicy<E> implements TriggeringPolicy<E>
{
  private static boolean isStartupRollingDone = false;
  private boolean isStarted = false;

  FileSize maxFileSize;
  private SizeBasedTriggeringPolicy<E> sizedPolicy = new SizeBasedTriggeringPolicy<E>();

  /**
   * 
   */
  public CompositeTriggeringPolicy()
  {
    // TODO Auto-generated constructor stub
  }

  // CONSTANTS ------------------------------------------------------

  @Override
  public void start()
  {
    isStarted = true;
  }

  @Override
  public void stop()
  {

  }

  @Override
  public boolean isStarted()
  {
    return isStarted;
  }

  @Override
  public boolean isTriggeringEvent(File activeFile, E event)
  {
    boolean isTriggered = false;
    if (!isStartupRollingDone)
    {
      isStartupRollingDone = true;
      isTriggered = true;
    }
    return isTriggered || sizedPolicy.isTriggeringEvent(activeFile, event);
  }

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------
  public void setMaxFileSize(String maxFileSize)
  {
    sizedPolicy.setMaxFileSize(maxFileSize);
  }
}
