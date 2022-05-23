package com.truecaller.syed_true_caller_assignment.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truecaller.syed_true_caller_assignment.Constants.CHARACTER_INDEX
import com.truecaller.syed_true_caller_assignment.Constants.DIV
import com.truecaller.syed_true_caller_assignment.Constants.URL
import com.truecaller.syed_true_caller_assignment.Constants.regex
import com.example.syed_true_caller_assignment.R
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


class AssignmentViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    ViewModel() {

    fun fetchBlogData(mainActivity: Context, dataModel: DataModel) {
        val fetchDataJob = viewModelScope.launch(dispatcher) {
            val builder: StringBuilder = StringBuilder()
            try {
                val url: String = URL
                val doc: Document = Jsoup.connect(url).get()
                val body = doc.select(DIV)
                builder.append(body?.text())
                dataModel.webPageData = builder.toString()
            } catch (e: Exception) {
                builder.append(mainActivity.getString(R.string.error)).append(e.message)
                dataModel.webPageData = builder.toString()
            }
        }
        runBlocking {
            fetchDataJob.join()
            val time = measureTimeMillis {
                launch {
                    fetch10ThChar(dataModel, mainActivity)
                }
                launch {
                    fetchEvery10ThChar(dataModel, mainActivity)
                }
                launch {
                    fetchEvery10ThCharWithoutSpace(dataModel, mainActivity)
                }
                launch {
                    fetchWordCount(dataModel, mainActivity)
                }
            }
            println("${mainActivity.getString(R.string.time_taken)} $time ${mainActivity.getString(R.string.millis)}")
        }
    }

    private fun fetch10ThChar(dataModel: DataModel, mainActivity: Context) {
        dataModel.result10thChar =
            "${mainActivity.getString(R.string.str_10th_Char)} ${dataModel.webPageData.toCharArray()[CHARACTER_INDEX]}"
    }

    private fun fetchEvery10ThChar(dataModel: DataModel, mainActivity: Context) {
        val strText = dataModel.webPageData.toCharArray()
        var result = EMPTY_STRING
        for (i in strText.indices) {
            if (i != 0 && i % 10 == 0) {
                result = "$result${strText[i + 1]}, "
            }
        }
        dataModel.result10thCharArray =
            "${mainActivity.getString(R.string.str_every_10th_Char)} $result"
    }

    private fun fetchEvery10ThCharWithoutSpace(dataModel: DataModel, mainActivity: Context) {
        val strText = dataModel.webPageData.replace(" ", EMPTY_STRING).toCharArray()
        var resultWithoutSpace = ""
        for (i in strText.indices) {
            if (i != 0 && i % 10 == 0 && strText[i + 1] != ' ') {
                resultWithoutSpace = "$resultWithoutSpace${strText[i + 1]},"
            }
        }
        dataModel.result10thCharArrayWithoutSpace =
            "${mainActivity.getString(R.string.str_every_10th_Char_without_space)} $resultWithoutSpace"
    }

    private fun fetchWordCount(dataModel: DataModel, mainActivity: Context) {
        val wordCount = countFrequency(regex.replace(dataModel.webPageData, " "))
        var count = EMPTY_STRING
        wordCount.forEach {
            // Loop to iterate over the
            // elements of the map
            //to make it more readable
            count = "$count${it.key} = ${it.value}\n"
        }
        dataModel.resultWordCount = "${mainActivity.getString(R.string.str_word_count)}\n$count"
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