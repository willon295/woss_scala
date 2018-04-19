import com.briup.woss.client.{Client, Gather}
import com.briup.woss.util.ConfigurationImpl

class Application {

}


object StartServer extends App {


  val conf = new ConfigurationImpl
  conf.getServer.reciver()

}

object StartClient extends App {
  val conf = new ConfigurationImpl
  val gather: Gather = conf.getGather
  val client: Client = conf.getClient

  client.send(gather.gather())
}