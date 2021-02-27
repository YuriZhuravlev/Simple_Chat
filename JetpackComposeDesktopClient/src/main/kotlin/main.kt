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
    MaterialTheme(colors = colors) {
        if (!chatPresenter.isAuthenticated()) {
            //TODO auth
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
                        itemsIndexed(chatPresenter.getListMessages()) { i: Int, s: String ->
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

/**
 * Вывод строки ввода - EditText + Button, если поле ввода не пусто
 */
@Composable
fun inputLine() {
    Row {
        TextField(
            chatPresenter.text,
            onValueChange = { s: String -> chatPresenter.text = s; chatPresenter.fill = 0.8f },
            modifier = Modifier.fillMaxWidth(chatPresenter.fill)
                .fillMaxHeight()
                .shortcuts {
                    on(Key.Enter) {
                        chatPresenter.send()
                    }
                },
            singleLine = true
        )
        if (chatPresenter.text.isNotEmpty()) {
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