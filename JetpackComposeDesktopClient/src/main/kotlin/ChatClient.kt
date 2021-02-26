import kotlinx.coroutines.*
import java.io.*
import java.net.Socket


class ChatClient(host: String, port: Int, username: String, onMessage: (String) -> Unit) {
    private lateinit var mSocket: Socket
    private lateinit var mInputStream: BufferedReader
    private lateinit var mOutputStream: OutputStream

    init {
        try {
            this.mSocket = Socket(host, port)

            mInputStream = mSocket.getInputStream().bufferedReader()
            mOutputStream = mSocket.getOutputStream()
            mOutputStream.write(username.encodeToByteArray())
            GlobalScope.launch {
                listen()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun listen() {
        try {
            while (mSocket.isConnected) {
                val data = mInputStream.readLine()
                if (data.isNotEmpty()) {
                    onMessage(data)
                }
            }
        } catch (e: Exception) {
            print(e.printStackTrace())
        }
    }


    fun send(message: String) {
        try {
            GlobalScope.launch {
                mOutputStream.write((message).encodeToByteArray())
            }
        } catch (e: Exception) {
            print(e.printStackTrace())
        }
    }
}