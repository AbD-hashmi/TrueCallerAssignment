package com.truecaller.syed_true_caller_assignment.viewmodel

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.syed_true_caller_assignment.R
import com.truecaller.syed_true_caller_assignment.Constants.CHARACTER_INDEX
import com.truecaller.syed_true_caller_assignment.Constants.DIV
import com.truecaller.syed_true_caller_assignment.Constants.URL
import com.truecaller.syed_true_caller_assignment.Constants.regex
import com.truecaller.syed_true_caller_assignment.data.DataModel
import com.truecaller.syed_true_caller_assignment.data.EMPTY_STRING
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import kotlin.system.measureTimeMillis


class AssignmentViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    application: Application,
) :
    AndroidViewModel(application) {

    private val context: Context by lazy { getApplication() }

    fun fetchBlogData(dataModel: DataModel) {
        Thread {
            val fetchDataJob = viewModelScope.launch(dispatcher) {
                val builder: StringBuilder = StringBuilder()
                try {
                    val url: String = URL
                    val doc: Document = Jsoup.connect(url).get()
                    val body = doc.select(DIV)
                    builder.append(body?.text())
                    dataModel.webPageData = builder.toString()
                } catch (e: Exception) {
                    builder.append(context.getString(R.string.error)).append(e.message)
                    dataModel.webPageData = builder.toString()
                }
            }
            runBlocking {
                fetchDataJob.join()
                val time = measureTimeMillis {
                    launch {
                        fetch10ThChar(dataModel)
                    }
                    launch {
                        fetchEvery10ThChar(dataModel)
                    }
                    launch {
                        fetchEvery10ThCharWithoutSpace(dataModel)
                    }
                    launch {
                        fetchWordCount(dataModel)
                    }
                }
                println("${context.getString(R.string.time_taken)} $time ${context.getString(R.string.millis)}")
            }
        }
    }

    @VisibleForTesting
    fun fetch10ThChar(dataModel: DataModel) {
        dataModel.result10thChar =
            "${context.getString(R.string.str_10th_Char)} ${dataModel.webPageData.toCharArray()[CHARACTER_INDEX]}"
    }

    private fun fetchEvery10ThChar(dataModel: DataModel) {
        val strText = dataModel.webPageData.toCharArray()
        var result = EMPTY_STRING
        for (i in strText.indices) {
            if (i != 0 && i % 10 == 0) {
                result = "$result${strText[i + 1]}, "
            }
        }
        dataModel.result10thCharArray =
            "${context.getString(R.string.str_every_10th_Char)} $result"
    }

    private fun fetchEvery10ThCharWithoutSpace(dataModel: DataModel) {
        val strText = dataModel.webPageData.replace(" ", EMPTY_STRING).toCharArray()
        var resultWithoutSpace = ""
        for (i in strText.indices) {
            if (i != 0 && i % 10 == 0 && strText[i + 1] != ' ') {
                resultWithoutSpace = "$resultWithoutSpace${strText[i + 1]},"
            }
        }
        dataModel.result10thCharArrayWithoutSpace =
            "${context.getString(R.string.str_every_10th_Char_without_space)} $resultWithoutSpace"
    }

    private fun fetchWordCount(dataModel: DataModel) {
        val wordCount = countFrequency(regex.replace(dataModel.webPageData, " "))
        var count = EMPTY_STRING
        wordCount.forEach {
            // Loop to iterate over the
            // elements of the map
            //to make it more readable
            count = "$count${it.key} = ${it.value}\n"
        }
        dataModel.resultWordCount = "${context.getString(R.string.str_word_count)}\n$count"
    }
}

// Function to count frequency of
// words in the given string
fun countFrequency(str: String): MutableMap<String, Int> {
    val map: MutableMap<String, Int> = TreeMap()

    // Splitting to find the word
    val arr = str.split(" ").toTypedArray()

    // Loop to iterate over the words
    for (i in arr.indices) {
        // Condition to check if the
        // array element is present
        // the hash-map
        if (arr[i] != "") {
            //condition to remove extra empty strings
            if (map.containsKey(arr[i])) {
                map[arr[i]] = map[arr[i]]!! + 1
            } else {
                map[arr[i]] = 1
            }
        }
    }
    return map
}