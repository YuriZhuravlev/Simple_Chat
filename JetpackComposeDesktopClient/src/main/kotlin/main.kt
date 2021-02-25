import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.ExperimentalKeyInput
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.unit.dp

val listMessages = listOf("456", "648", "hello world!").toMutableStateList()
val colors = Colors(
    primary = Color(0xFF3B4252),
    primaryVariant = Color(0xFF2E3440),
    secondary = Color(0xFFE5E9F0),
    secondaryVariant = Color(0xFFD8DEE9),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    isLight = true
)

@ExperimentalKeyInput
fun main() = Window {
    val username = "test_user"

    MaterialTheme(colors = colors) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(5.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f)
                    .padding(20.dp)
            ) {
                LazyColumn(Modifier.fillMaxHeight(0.88f).fillMaxWidth()) {
                    itemsIndexed(listMessages) { i: Int, s: String ->
                        messageItem(i, s)
                    }
                }
                inputLine()
            }
            Column {
                Text("Username: $username")
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

/**
 * Вывод строки ввода - EditText + Button, если поле ввода не пусто
 */
@ExperimentalKeyInput
@Composable
fun inputLine() {
    // содержание текстового поля
    var text by remember { mutableStateOf("") }
    // переменная заполнения строки
    var fill by remember { mutableStateOf(1f) }
    Row {
        TextField(
            text,
            onValueChange = { s: String -> text = s; fill = 0.8f },
            modifier = Modifier.fillMaxWidth(fill)
                .shortcuts {
                    on(Key.Enter) {
                        if (text.isNotEmpty()) {
                            listMessages.add(text)
                            text = ""
                            fill = 1f
                        }
                    }
                },
            singleLine = true
        )
        if (text.isNotEmpty()) {
            Button(modifier = Modifier.fillMaxSize(), onClick = {
                listMessages.add(text)
                text = ""
                fill = 1f
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