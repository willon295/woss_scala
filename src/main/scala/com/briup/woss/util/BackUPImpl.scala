package com.briup.woss.util

import java.io._
import java.util.Properties

class BackUPImpl extends BackUP {
  var logger: Logger = _
  var FILE_PATH = ""

  //key 是文件名 ， 通过文件名获取文件
  override def store(key: String, data: scala.Any, flag: Boolean): Unit = {
    val file = new File(key)
    try {
      val oos = new ObjectOutputStream(new FileOutputStream(file))
      oos.writeObject(data)
    } catch {
      case _ => logger.error("文件备份失败")
    }

  }

  override def load(key: String, flag: Boolean): AnyRef = {

    var loadFile:AnyRef=null

    val file = new File(key)
    if (file.exists()) {
      try {
        val ois = new ObjectInputStream(new FileInputStream(file))
        loadFile = ois.readObject()
      } catch {
        case _ => logger.error("文件读取失败")
      }

    }

    loadFile
  }

  override def init(properties: Properties): Unit = {
    FILE_PATH = properties.getProperty("back-temp")
  }

  override def setConfiguration(configuration: Configuration): Unit = {

    try {
      logger = configuration.getLogger
    } catch {
      case _ => println("BackUP加载logger失败")
    }

  }
}
