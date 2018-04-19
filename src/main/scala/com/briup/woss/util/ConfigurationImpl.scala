package com.briup.woss.util

import java.util.Properties

import com.briup.woss.client.{Client, Gather, GatherImpl}
import com.briup.woss.common.{ConfigurationAWare, WossModule}
import com.briup.woss.server.{DBStore, Server}
import org.dom4j.io.SAXReader
import org.dom4j.{Document, Element}

import scala.collection.JavaConverters._
import scala.collection.mutable

class ConfigurationImpl extends Configuration {
  var map: mutable.HashMap[String, WossModule] = new mutable.HashMap[String, WossModule]()
  val reader = new SAXReader
  val document: Document = reader.read("src/main/resources/conf.xml")
  val root: Element = document.getRootElement
  val list1 = root.elements().asScala
  for (a1: Any <- list1) {
    val e1: Element = a1.asInstanceOf[Element]
    val className: String = e1.attributeValue("class")

    var module: WossModule = Class.forName(className).newInstance().asInstanceOf[WossModule]
    var properties: Properties = new Properties()
    val list2 = e1.elements().asScala
    for (a2: Any <- list2) {
      val e2 = a2.asInstanceOf[Element]
      var key = e2.getName
      var value = e2.getText
      properties.put(key, value)

    }

    module.init(properties)
    map.put(e1.getName, module)
    //将属性注入 ， 初始化
    for (ooo <- map.values) {
      ooo match {
        case ware: ConfigurationAWare =>
          ware.setConfiguration(this)
        case _ => println("do nothing")
      }
    }
  }
  override def getServer: Server = {
    map("server").asInstanceOf[Server]
  }

  override def getGather: Gather = {
    map("gather").asInstanceOf[Gather]
  }

  override def getClient: Client = {
    map("client").asInstanceOf[Client]
  }

  override def getBackup: BackUP = {
    map("backup").asInstanceOf[BackUP]
  }

  override def getDbStore: DBStore = {
    map("dbstore").asInstanceOf[DBStore]
  }

  override def getLogger: Logger = {
    map("logger").asInstanceOf[Logger]
  }
}
