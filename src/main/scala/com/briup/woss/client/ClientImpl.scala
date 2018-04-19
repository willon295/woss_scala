package com.briup.woss.client

import java.io.ObjectOutputStream
import java.net.Socket
import java.util
import java.util.Properties

import com.briup.woss.bean.BIDR
import com.briup.woss.util.{Configuration, ConfigurationImpl, Logger}

class ClientImpl extends Client {
  var SERVER_IP = ""
  var SERVER_PORT = 0
  var logger: Logger = _

  override def send(list: util.Collection[BIDR]): Unit = {
    var socket: Socket = null
    try {
      socket = new Socket(SERVER_IP, SERVER_PORT)
    } catch {
      case _ => logger.error("服务器连接失败")
    }
    var oos: ObjectOutputStream = null
    try {
      oos = new ObjectOutputStream(socket.getOutputStream)
    } catch {
      case _ => logger.error("获取流失败")
    }
    try {
      oos.writeObject(list)
    } catch {
      case _ => logger.error("数据发送失败")
    }
    oos.flush()
  }

  override def setConfiguration(configuration: Configuration): Unit = {
    try {
      logger = configuration.getLogger
    } catch {
      case _ => println("Client加载logger失败")
    }
  }

  override def init(properties: Properties): Unit = {
    SERVER_IP = properties.getProperty("ip")
    SERVER_PORT = properties.getProperty("port").toInt
  }
}


object ClientImpl {
  def main(args: Array[String]): Unit = {
    val conf = new ConfigurationImpl
    val gather: Gather = conf.getGather
    val client: Client = conf.getClient
    client.send(gather.gather())
  }
}