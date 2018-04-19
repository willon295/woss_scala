package com.briup.woss.server

import java.io.ObjectInputStream
import java.net.{ServerSocket, Socket}
import java.util
import java.util.Properties

import com.briup.woss.bean.BIDR
import com.briup.woss.util.{Configuration, ConfigurationImpl, Logger}

class ServerImpl extends Server {
  var LISTEN_PORT = 0
  var logger: Logger = _

  override def reciver(): Unit = {
    var serverSocket: ServerSocket = null
    try {
      serverSocket = new ServerSocket(LISTEN_PORT)
      logger.info("服务器监听端口" + LISTEN_PORT)
    } catch {
      case _ => println("服务器启动失败")
    }

    var ds: DBStoreImpl = new DBStoreImpl
    var server: Socket = null
    var bidrs: util.Collection[BIDR] = null
    while (true) {
      server = serverSocket.accept()
      val ois: ObjectInputStream = new ObjectInputStream(server.getInputStream)
      try {
        bidrs = ois.readObject().asInstanceOf[util.Collection[BIDR]]
        logger.info("接受Client对象大小：" + bidrs.size())
      } catch {
        case _ => logger.error("接受对象失败")
      }
      try {
        ds.saveToDB(bidrs)
      } catch {
        case _ => logger.error("入库失败")
      }
    }


  }

  override def shutDown(): Unit = {}

  override def init(properties: Properties): Unit = {
    LISTEN_PORT = properties.getProperty("port").toInt
  }

  override def setConfiguration(configuration: Configuration): Unit = {

    try {
      logger = configuration.getLogger
    } catch {
      case _ => println("Server加载logger失败")
    }
  }
}


object ServerImpl {
  def main(args: Array[String]): Unit = {
    val conf = new ConfigurationImpl
    conf.getServer.reciver()
  }
}
