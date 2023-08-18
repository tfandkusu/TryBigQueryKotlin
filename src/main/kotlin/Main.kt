import com.google.cloud.Timestamp
import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.InsertAllRequest

const val PROJECT_ID = "your_project_id"
const val DATASET_NAME = "mobile_develop"
const val TABLE_NAME = "build_time_test"
fun main() {
    val bigquery = BigQueryOptions.newBuilder().setProjectId(PROJECT_ID).build().service
    val timestamp = Timestamp.now().toString()
    val duration = 12345L
    val content = mapOf<String, Any>("created_at" to timestamp, "duration" to duration)
    val request = InsertAllRequest.newBuilder(DATASET_NAME, TABLE_NAME).addRow(content).build()
    val response= bigquery.insertAll(request)
    if (response.hasErrors()) {
        throw RuntimeException(response.insertErrors.toString())
    } else {
        println("Success")
    }
}
