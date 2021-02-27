import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

class ChatPresenter {
    private lateinit var mUsername: String
    private val mListMessages = listOf<String>().toMutableStateList()
    private var mClient: ChatClient? = null
    // содержание текстового поля
    var text by remember { mutableStateOf("") }
    // переменная заполнения строки
    var fill by remember { mutableStateOf(1f) }

    private fun onMessage(message: String) {
        mListMessages.add(message)
    }

    fun isAuthenticated(): Boolean {
        return mClient == null
    }

    fun auth(host: String, username: String): Boolean {
        if (isAuthenticated()) {
            return false
        }
        mUsername = username
        mClient = ChatClient(host, 8006, username) { onMessage(it) }
        return true
    }

    fun send() {
        if (text.isNotEmpty()) {
            fill = 1f
            mClient!!.send(text)
        }
    }

    fun getUsername(): String {
        return mUsername
    }

    fun getListMessages(): SnapshotStateList<String> {
        return mListMessages
    }
}