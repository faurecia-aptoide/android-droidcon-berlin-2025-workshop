package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils

object CurlTemplate {
    fun format(
        notification: String?,
        payload: String,
        url: String,
        bodyB64: String,
        hEnc: String,
        hTtl: String,
        hUrg: String,
        hAuth: String
    ) =
        """
        JSON='{
          "notification": $notification,
          "data": {
            "payload": "$payload"
          }
        }'
        
        BODY_B64='$bodyB64'
        URL='${url}'
        curl -v -X POST \
          -H 'Content-Encoding: ${hEnc}' \
          -H 'TTL: ${hTtl}' \
          -H 'Urgency: ${hUrg}' \
          -H 'Authorization: ${hAuth}' \
          --data-binary @<(python3 -c "import sys, base64; sys.stdout.buffer.write(base64.b64decode(sys.stdin.read()))" <<< "${'$'}BODY_B64") \
        "${'$'}URL"
        """.trimIndent()
}
