import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.unit.dp

lateinit var chatPresenter: ChatPresenter

fun main() = Window {
    chatPresenter = ChatPresenter()
    val isAuthenticated by remember { chatPresenter.isAuthenticated }
    val messages = chatPresenter.getListMessages()
    MaterialTheme(colors = colors) {
        if (!isAuthenticated) {
            authWindow()
        } else {
            Row(
                modifier = Modifier.fillMaxSize()
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.7f)
                        .padding(20.dp)
                ) {
                    LazyColumn(Modifier.fillMaxHeight(0.88f).fillMaxWidth()) {
                        itemsIndexed(messages) { i: Int, s: String ->
                            messageItem(i, s)
                        }
                    }
                    inputLine()
                }
                Column {
                    Text("Username: ${chatPresenter.getUsername()}")
                    Box(
                        modifier = Modifier.size(160.dp)
                            .align(Alignment.End)
                            .padding(10.dp)
                            .background(Color.Cyan)
                    )
                }
            }
        }
    }
}

@Composable
fun authWindow() {
    Column(modifier = Modifier.padding(10.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        var host by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var hideError by remember { mutableStateOf(true) }
        TextField(
            value = host, onValueChange = { s: String -> host = s },
            modifier = Modifier.padding(5.dp)
                .shortcuts {
                    on(Key.Enter) {
                        hideError = chatPresenter.validateInput(host, username)
                        if (hideError) {
                            chatPresenter.auth(host, username)
                        }
                    }
                },
            singleLine = true, isError = !hideError, label = {Text("IP-address")}
        )
        TextField(
            value = username, onValueChange = { s: String -> username = s },
            modifier = Modifier.padding(5.dp)
                .shortcuts {
                    on(Key.Enter) {
                        hideError = chatPresenter.validateInput(host, username)
                        if (hideError) {
                            chatPresenter.auth(host, username)
                        }
                    }
                },
            singleLine = true, isError= !hideError , label = {Text("Username")}
        )
        if (!hideError) {
            Text("Error!", color = colors.error)
        }
    }
}

/**
 * Вывод строки ввода - EditText + Button, если поле ввода не пусто
 */
@Composable
fun inputLine() {
    var fill by remember { chatPresenter.fill }
    var text by remember { chatPresenter.text }
    Row {
        TextField(
            text,
            onValueChange = { s: String -> text = s; fill = 0.8f },
            modifier = Modifier.fillMaxWidth(fill)
                .fillMaxHeight()
                .shortcuts {
                    on(Key.Enter) {
                        chatPresenter.send()
                    }
                },
            singleLine = true
        )
        if (text.isNotEmpty()) {
            Button(modifier = Modifier.fillMaxSize(), onClick = {
                chatPresenter.send()
            }) {
                Text("Send")
            }
        }
    }
}

/**
 * Отображение сообщения из массива
 *
 * @param i индекс массива
 * @param s строка сообщения
 */
@Composable
private fun messageItem(i: Int, s: String) {
    val color = if (i % 2 == 0) {
        colors.secondaryVariant
    } else {
        Color.White
    }
    Text(
        text = s,
        modifier = Modifier.background(color).fillMaxWidth().padding(PaddingValues(top = 1.dp))
    )
}