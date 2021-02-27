import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

class ChatPresenter {
    private lateinit var mUsername: String
    private val mListMessages = listOf<String>().toMutableStateList()
    private var mClient: ChatClient? = null
    var isAuthenticated = mutableStateOf(false)
    // содержание текстового поля
    var text = mutableStateOf("")
    // переменная заполнения строки
    var fill = mutableStateOf(1f)

    private fun onMessage(message: String) {
        mListMessages.add(message)
    }


    fun auth(host: String, username: String): Boolean {
        if (isAuthenticated.value) {
            return false
        }
        mUsername = username
        mClient = ChatClient(host, 8006, username) { onMessage(it) }
        isAuthenticated.value = true
        return true
    }

    fun send() {
        if (isAuthenticated.value && text.value.isNotEmpty()) {
            fill.value = 1f
            mClient!!.send(text.value)
        }
    }

    fun getUsername(): String {
        return mUsername
    }

    fun getListMessages(): SnapshotStateList<String> {
        return mListMessages
    }
}