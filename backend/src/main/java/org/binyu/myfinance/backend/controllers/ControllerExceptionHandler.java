/********************************************************************
 * File Name:    ServiceExceptionHandler.java
 *
 * Date Created: Mar 20, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.controllers;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.binyu.myfinance.backend.exceptions.InvalidInputException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Uniform Handler class to handling all our custom checked exceptions and
 * return proper response code and content for controllers.
 */
@ControllerAdvice("org.binyu.myfinance.backend.controllers")
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  /*  
   * For ConstraintViolationException exceptions, return BAD_REQUEST status code
   * 
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler({ ConstraintViolationException.class })
  public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
      ConstraintViolationException ex, HttpServletRequest request)
  {
    return createErrorResponse(HttpStatus.BAD_REQUEST, request, getMessage(ex));
  }

  /**
   * For handleDataIntegrityViolationException , return BAD_REQUEST status code
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler({ DataIntegrityViolationException.class })
  public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex, HttpServletRequest request)
  {
    return createErrorResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
  }

  /**
   * For InvalidInputException , return BAD_REQUEST status code
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler({ InvalidInputException.class })
  public ResponseEntity<Map<String, Object>> handleInvalidInputException(
      InvalidInputException ex, HttpServletRequest request)
  {
    return createErrorResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
  }

  /**
   * For other exceptions, return internal server error
   * 
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler({ Throwable.class })
  public ResponseEntity<Map<String, Object>> handleAllOtherException(
      Exception ex, HttpServletRequest request)
  {
    return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request,
        ex.getMessage());
  }

  private ResponseEntity<Map<String, Object>> createErrorResponse(
      HttpStatus status, HttpServletRequest request, String message)
  {
    Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
    errorAttributes.put("timestamp", new Date());
    errorAttributes.put("status", status.toString());
    errorAttributes.put("message", message);
    errorAttributes.put("path", request.getRequestURI());
    return new ResponseEntity<Map<String, Object>>(errorAttributes, status);
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------
  private String getMessage(ConstraintViolationException e)
  {
    StringBuilder sb = new StringBuilder();
    for (ConstraintViolation<?> violation : e.getConstraintViolations())
    {
      sb.append(violation.getMessage() + ";");
    }
    return sb.toString();
  }
  // ACCESSOR METHODS -----------------------------------------------

}
