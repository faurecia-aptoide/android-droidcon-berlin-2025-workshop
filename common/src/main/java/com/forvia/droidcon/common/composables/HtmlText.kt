package com.forvia.droidcon.common.composables

import android.text.Spanned
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
    html: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    val spanned = remember(html) {
        HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    val annotated = remember(spanned) { spanned.toAnnotatedStringPreservingLists() }

    Text(
        text = annotated,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = textColor
        ),
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
    )
}

private fun Spanned.toAnnotatedStringPreservingLists(): AnnotatedString {

    val bulletRanges = getSpans(0, length, android.text.style.BulletSpan::class.java)
        .map { getSpanStart(it) until getSpanEnd(it) }
        .sortedBy { it.first }

    fun addedOffsetAt(pos: Int): Int {
        var delta = 0
        for (r in bulletRanges) if (r.first < pos) delta += 2 /* "• " */ else break
        return delta
    }

    val b = AnnotatedString.Builder()
    var i = 0
    while (i < length) {
        val paraEnd = indexOf('\n', i).let { if (it == -1) length else it + 1 }
        val hasBullet =
            bulletRanges.any { it.first <= i && it.last >= (paraEnd - 1).coerceAtLeast(i) }
        if (hasBullet) b.append("• ")
        b.append(subSequence(i, paraEnd))
        i = paraEnd
    }

    // Re-apply inline spans with corrected offsets
    getSpans(0, length, Any::class.java).forEach { span ->
        val start = getSpanStart(span).coerceAtLeast(0)
        val end = getSpanEnd(span).coerceAtLeast(start)
        val newStart = start + addedOffsetAt(start)
        val newEnd = end + addedOffsetAt(end)

        when (span) {
            is android.text.style.StyleSpan -> {
                if (span.style and android.graphics.Typeface.BOLD != 0) {
                    b.addStyle(SpanStyle(fontWeight = FontWeight.Bold), newStart, newEnd)
                }
                if (span.style and android.graphics.Typeface.ITALIC != 0) {
                    b.addStyle(SpanStyle(fontStyle = FontStyle.Italic), newStart, newEnd)
                }
            }

            is android.text.style.UnderlineSpan -> {
                b.addStyle(SpanStyle(textDecoration = TextDecoration.Underline), newStart, newEnd)
            }

            is android.text.style.URLSpan -> {
                b.addStyle(SpanStyle(textDecoration = TextDecoration.Underline), newStart, newEnd)
                b.addStringAnnotation(
                    tag = "URL",
                    annotation = span.url,
                    start = newStart,
                    end = newEnd
                )
            }

            is android.text.style.TypefaceSpan -> {
                if (span.family == "monospace") {
                    b.addStyle(SpanStyle(/* add code style if you want */), newStart, newEnd)
                }
            }

            is android.text.style.ForegroundColorSpan -> {
                b.addStyle(
                    SpanStyle(color = Color(span.foregroundColor)),
                    newStart, newEnd
                )
            }
        }
    }

    return b.toAnnotatedString()
}
