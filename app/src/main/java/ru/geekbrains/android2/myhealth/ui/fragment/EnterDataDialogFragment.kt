package ru.geekbrains.android2.myhealth.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.viewmodel.ext.android.getViewModel
import ru.geekbrains.android2.myhealth.data.entity.HealthData
import ru.geekbrains.android2.myhealth.databinding.DialogFragmentEnterDataBinding
import ru.geekbrains.android2.myhealth.utils.livedataevent.Event
import java.text.SimpleDateFormat
import java.util.*

class EnterDataDialogFragment : DialogFragment() {

    lateinit var binding: DialogFragmentEnterDataBinding

    @ObsoleteCoroutinesApi
    private val model by lazy { requireParentFragment().getViewModel<DiaryViewModel>() }
    private val closeDialogObserver = Observer<Event<Int>> { closeDialog() }

    companion object {
        fun newInstance(healthData: HealthData?) =
            EnterDataDialogFragment().apply {
                arguments = Bundle().apply {
                    healthData?.let {
                        putParcelable(HEALTH_DATA, healthData)
                    }
                }
            }

        private const val HEALTH_DATA = "healthData"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setTitle("Enter your blood pressure and pulse")
        binding = DialogFragmentEnterDataBinding.inflate(inflater, container, false)
        val v = binding.root
        return v
    }

    @ObsoleteCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @ObsoleteCoroutinesApi
    private fun init() {
        with(binding) {
            btnYes.setOnClickListener {
                arguments?.getParcelable<HealthData>(HEALTH_DATA)?.let { healthData ->
                    model.dialogFragmentBtnYesClicked(
                        healthData.copy(
                            upperBloodPressure = upperBloodPressureEditText.text.toString(),
                            lowerBloodPressure = lowerBloodPressureEditText.text.toString(),
                            pulse = pulseEditText.text.toString()
                        )
                    )
                } ?: model.dialogFragmentBtnYesClicked(
                    HealthData(
                        Calendar.getInstance().timeInMillis,
                        getCurrentDate(),
                        getCurrentTime(),
                        upperBloodPressureEditText.text.toString(),
                        lowerBloodPressureEditText.text.toString(),
                        pulseEditText.text.toString()
                    )
                )
                closeDialog()
            }
            btnCancel.setOnClickListener {
                model.dialogFragmentBtnCancelClicked()
                closeDialog()
            }

            arguments?.getParcelable<HealthData>(HEALTH_DATA)?.let { data ->
//            upper_blood_pressure_edit_text.setText(data.upperBloodPressure)
//            lower_blood_pressure_edit_text.setText(data.lowerBloodPressure)
//            pulse_edit_text.setText(data.pulse)
                binding.data = data
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM, yyyy")
        return formatter.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(date)
    }

    private fun closeDialog() {
        this.dismiss()
    }
}
