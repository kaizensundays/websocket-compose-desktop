import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep

/**
 * Created: Sunday 6/27/2021, 1:51 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class QuoteView : QuoteListener {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val client = WebSocketClient()

    private val handler = QuoteMessageHandler(this)

    private val subscriptionDelayMillis = 700L

    private val symbols = listOf("AMZN", "AAPL", "ORCL")

    private val data = symbols.map { symbol -> symbol to Quote(symbol) }.toMap()

    private val sequenceStateMap = mutableMapOf<String, MutableState<String>>()
    private val bidStateMap = mutableMapOf<String, MutableState<String>>()
    private val askStateMap = mutableMapOf<String, MutableState<String>>()

    private fun formatLong(value: Long) = String.format("%-7d", value)

    private fun formatPrice(price: Double) = String.format("%-7.4f", price)

    override fun onQuote(quote: Quote) {
        sequenceStateMap[quote.symbol]?.value = formatLong(quote.sequence)
        bidStateMap[quote.symbol]?.value = formatPrice(quote.bidPrice)
        askStateMap[quote.symbol]?.value = formatPrice(quote.askPrice)
    }

    private fun subscribe(subscribeButtonEnabled: MutableState<Boolean>) {
        try {
            val session = client.connect()
            if (session != null && client.connected.get()) {
                client.subscribe(session, handler)
                symbols.forEach { symbol ->
                    client.subscribe(symbol, session)
                    sleep(subscriptionDelayMillis)
                }
            } else {
                subscribeButtonEnabled.value = true
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }

    @Composable
    fun Line(symbol: String, sequenceState: String, bidState: String, askState: String) {
        Row {
            Text(symbol, textAlign = TextAlign.Left, modifier = Modifier.width(100.dp))
            Text(sequenceState, textAlign = TextAlign.Left, modifier = Modifier.width(100.dp))
            Text(bidState, textAlign = TextAlign.Left, modifier = Modifier.width(100.dp))
            Text(askState, textAlign = TextAlign.Left, modifier = Modifier.width(100.dp))
        }
    }

    fun build() {
        application {
            Window(title = "Quotes", onCloseRequest = ::exitApplication) {
                MainPanel()
            }
        }
    }

    @Composable
    fun MainPanel() {
        val subscribeButtonEnabled = remember { mutableStateOf(true) }

        sequenceStateMap.putAll(data.map { e ->
            e.key to remember {
                mutableStateOf(formatLong(e.value.sequence))
            }
        }.toMap())

        bidStateMap.putAll(data.map { e ->
            e.key to remember {
                mutableStateOf(formatPrice(e.value.bidPrice))
            }
        }.toMap())

        askStateMap.putAll(data.map { e ->
            e.key to remember {
                mutableStateOf(formatPrice(e.value.askPrice))
            }
        }.toMap())

        MaterialTheme {
            Column {

                data.forEach { e ->
                    val sequenceState = sequenceStateMap[e.key]
                    val bidState = bidStateMap[e.key]
                    val askState = askStateMap[e.key]
                    if (sequenceState != null && bidState != null && askState != null) {
                        Line(e.key, sequenceState.value, bidState.value, askState.value)
                    }
                }

                Button(onClick = {
                    subscribeButtonEnabled.value = false

                    @Suppress("DeferredResultUnused")
                    GlobalScope.async {
                        subscribe(subscribeButtonEnabled)
                    }

                }, enabled = subscribeButtonEnabled.value) {
                    Text("Subscribe")
                }

            }
        }
    }

}