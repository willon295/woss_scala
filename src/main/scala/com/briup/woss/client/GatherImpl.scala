package com.briup.woss.client

import java.sql.Timestamp
import java.util
import java.util.Properties

import com.briup.woss.bean.BIDR
import com.briup.woss.util.{Configuration, Logger}

import scala.io.Source

class GatherImpl extends Gather {
  var logger:Logger=_
  var RAW_FILE_PATH:String=_
  override def gather(): util.Collection[BIDR] = {
    var login_user_map = new util.HashMap[String, BIDR]()
    var userList = new util.ArrayList[BIDR]()
    val strings: Iterator[String] = Source.fromInputStream(ClassLoader.getSystemResourceAsStream(RAW_FILE_PATH)).getLines()
    for (line <- strings) {
      val datas = line.split("\\|")
      val user_name: String = datas(0)
      val nsa_ip: String = datas(1)
      val action_type: String = datas(2)
      val time_stap: Long = java.lang.Long.parseLong(datas(3)) * 1000
      val user_ip: String = datas(4)

      //将数据进行封装
      val b = new BIDR()
      b.setLogin_ip(user_ip)
      b.setNAS_ip(nsa_ip)

      if (action_type.equals("7")) {
        b.setAAA_login_name(user_name)
        b.setLogin_date(new Timestamp(time_stap))
        login_user_map.put(b.getLogin_ip, b)
      } else {
        var b2 = login_user_map.get(b.getLogin_ip)
        b.setAAA_login_name(b2.getAAA_login_name)
        b.setLogin_date(b2.getLogin_date)
        b.setLogout_date(new Timestamp(time_stap))
        b.setTime_duration(b.getLogout_date.getTime.asInstanceOf[Int] - b.getLogin_date.getTime.asInstanceOf[Int])
        userList.add(b)
        login_user_map.remove(b.getLogin_ip)
      }
    }
    userList
  }
  override def setConfiguration(configuration: Configuration): Unit = {
    try {
      logger = configuration.getLogger
    } catch {
      case _ => println("Gather加载logger失败")
    }
  }

  override def init(properties: Properties): Unit = {
    RAW_FILE_PATH=properties.getProperty("src-file")
  }
}

