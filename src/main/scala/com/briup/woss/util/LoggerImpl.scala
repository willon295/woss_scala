package com.briup.woss.util

import java.util.Properties

import org.apache.log4j

class LoggerImpl extends Logger {

  val logger: log4j.Logger = org.apache.log4j.Logger.getLogger(classOf[LoggerImpl])

  override def debug(msg: String): Unit = {

    logger.debug(msg)
  }

  override def error(msg: String): Unit = {
    logger.error(msg)
  }

  override def fatal(msg: String): Unit = {
    logger.fatal(msg)
  }

  override def warn(msg: String): Unit = {
    logger.warn(msg)
  }

  override def info(msg: String): Unit = {
    logger.info(msg)
  }

  override def init(properties: Properties): Unit = {


  }

  override def setConfiguration(configuration: Configuration): Unit = {


  }
}
