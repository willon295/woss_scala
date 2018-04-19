package com.briup.woss.server

import java.sql.{Connection, Date, DriverManager}
import java.util.Properties
import java.{sql, util}

import com.briup.woss.bean.BIDR
import com.briup.woss.util.{Configuration, Logger}

class DBStoreImpl extends DBStore {

  var logger:Logger=_
  var conn: Connection = _
  var URL:String = _
  var USERNAME:String = _
  var PASSWORD:String = _
  var DRIVER:String = _
  var BATCH_SIZE:Int=_

  override def saveToDB(collection: util.Collection[BIDR]): Unit = {
    conn=getConn
    var mystring:String = "INSERT INTO bidrs  (username,login_date,logout_date,login_ip,nas_ip,time_duration) VALUES (?,?,?,?,?,?)"
    var statement: sql.PreparedStatement = conn.prepareStatement(mystring)
    var count = 0
    val value = collection.iterator()
    while (value.hasNext){
      val b: BIDR = value.next()
      statement.setString(1,b.getAAA_login_name)
      statement.setDate(2,new Date(b.getLogin_date.getTime))
      statement.setDate(3,new Date(b.getLogout_date.getTime))
      statement.setString(4,b.getLogin_ip)
      statement.setString(5,b.getNAS_ip)
      statement.setInt(6,b.getTime_duration)
      count+=1
      if((count%BATCH_SIZE) == 0  || count ==collection.size()   ){
        statement.executeBatch()
        statement.execute()
      }
    }

  }

  override def init(properties: Properties): Unit = {
    USERNAME = properties.getProperty("userName")
    PASSWORD = properties.getProperty("passWord")
    URL = properties.getProperty("url")
    DRIVER = properties.getProperty("driver")
    BATCH_SIZE=properties.getProperty("batch-size").toInt
    try {
      Class.forName(DRIVER)
    } catch {
      case _ => logger.error("数据库驱动加载失败")
    }
  }

  override def setConfiguration(configuration: Configuration): Unit = {

    try {
      logger = configuration.getLogger
    } catch {
      case _ => println("DBStore加载logger失败")
    }
  }

  def getConn: Connection = {

    if (conn == null) {
      try {
        conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)
      } catch {
        case _ => logger.error("数据库连接失败")
      }
      conn
    }
    conn

  }
}
