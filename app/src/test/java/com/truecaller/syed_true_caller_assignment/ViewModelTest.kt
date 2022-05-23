package com.truecaller.syed_true_caller_assignment

import android.app.Application
import com.truecaller.syed_true_caller_assignment.data.DataModel
import com.truecaller.syed_true_caller_assignment.viewmodel.AssignmentViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ViewModelTest {
    @RelaxedMockK
    private lateinit var assignmentViewModel: AssignmentViewModel

    @RelaxedMockK
    private lateinit var dataModel: DataModel

    @Before
    fun before() {
        MockKAnnotations.init(this)
        mockkObject(AssignmentViewModel::class)
        assignmentViewModel = AssignmentViewModel(application = Application())
    }

    @Test
    fun `given response is success, when fetched website content, then verify if response is correct`() {
        runTest {
            //Given
            val mockedData = "This is mocked data as string"
            dataModel = DataModel()
            assignmentViewModel.fetch10ThChar(dataModel = dataModel)
            dataModel.webPageData = mockedData

            Assert.assertTrue(dataModel.result10thChar.equals('c'))
        }
    }
}