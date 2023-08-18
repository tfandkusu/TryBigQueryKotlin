import com.google.cloud.Timestamp
import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.InsertAllRequest
import java.util.*

const val PROJECT_ID = "your_project_id"
const val DATASET_NAME = "mobile_develop"
const val TABLE_NAME = "build_time_test"
fun main() {
    val random = Random()
    val bigquery = BigQueryOptions.newBuilder().setProjectId(PROJECT_ID).build().service
    val calendar = GregorianCalendar.getInstance()
    calendar.set(2023, 5, 1, 0, 0)
    calendar.set(GregorianCalendar.MILLISECOND, 0)
    val now = GregorianCalendar.getInstance()

    while (calendar < now) {
        // 平日10時から19時まで1時間1ビルド行われるとする
        if (calendar.get(Calendar.DAY_OF_WEEK) !in listOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
            for (hour in 10..19) {
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                val timestamp = Timestamp.of(calendar.time).toString()
                val duration = 10000 + random.nextInt(3000)
                val content = mapOf<String, Any>(
                    "created_at" to timestamp,
                    "duration" to duration
                )
                val request = InsertAllRequest.newBuilder(
                    DATASET_NAME,
                    TABLE_NAME
                ).addRow(content).build()
                val response = bigquery.insertAll(request)
                if (response.hasErrors()) {
                    throw RuntimeException(response.insertErrors.toString())
                } else {
                    println("Success")
                }
            }
        }
        calendar.add(GregorianCalendar.DAY_OF_MONTH, 1)
    }
}
