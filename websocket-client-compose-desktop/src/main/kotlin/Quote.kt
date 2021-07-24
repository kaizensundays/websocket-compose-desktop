/**
 * Created: Saturday 7/3/2021, 2:18 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
data class Quote(
    val symbol: String, val sequence: Long = 0,
    val bidPrice: Double = 0.0, val askPrice: Double = 0.0
) : JacksonSerializableUntyped