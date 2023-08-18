import com.google.cloud.bigquery.*
import java.util.*


fun main(args: Array<String>) {
    val bigquery = BigQueryOptions.getDefaultInstance().getService()
    val queryConfig = QueryJobConfiguration.newBuilder(
        "SELECT CONCAT('https://stackoverflow.com/questions/', "
                + "CAST(id as STRING)) as url, view_count "
                + "FROM `bigquery-public-data.stackoverflow.posts_questions` "
                + "WHERE tags like '%google-bigquery%' "
                + "ORDER BY view_count DESC "
                + "LIMIT 10"
    ).setUseLegacySql(false).build()
    val jobId = JobId.of(UUID.randomUUID().toString())
    var queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build())

    queryJob = queryJob.waitFor()

    if (queryJob == null) {
        throw RuntimeException("Job no longer exists")
    } else if (queryJob.status.error != null) {
        throw RuntimeException(queryJob.status.error.toString())
    }
    val result: TableResult = queryJob.getQueryResults()
    for (row: FieldValueList in result.iterateAll()) {
        val url = row["url"].getStringValue()
        val viewCount = row["view_count"].getStringValue()
        System.out.printf("%s : %s views\n", url, viewCount)
    }
}
