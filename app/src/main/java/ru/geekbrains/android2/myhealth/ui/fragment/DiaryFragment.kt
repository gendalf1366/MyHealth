package ru.geekbrains.android2.myhealth.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.android.ext.android.getKoin
import ru.geekbrains.android2.myhealth.R
import ru.geekbrains.android2.myhealth.data.entity.HealthData
import ru.geekbrains.android2.myhealth.databinding.FragmentDiaryBinding
import ru.geekbrains.android2.myhealth.ui.activity.splash.SplashActivity
import kotlin.coroutines.CoroutineContext

class DiaryFragment : Fragment(), CoroutineScope {

    companion object {
        fun newInstance() = DiaryFragment()
        private const val DIALOG_FRAGMENT_TAG = "ru.geekbrains.android2.myhealth.ui.fragment.enterdatadialogfragment"
    }

    @ObsoleteCoroutinesApi
    val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(this, getKoin().get()).get(DiaryViewModel::class.java)
    }

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + Job()
    }

    lateinit var dataJob: Job
    private lateinit var errorJob: Job

    lateinit var binding: FragmentDiaryBinding
    lateinit var adapter: DiaryRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater, parent, false)
        val v = binding.root
        setHasOptionsMenu(true)
        return v
    }

    @ObsoleteCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvHealthData.layoutManager = LinearLayoutManager(requireContext())
        adapter = DiaryRVAdapter { healthData ->
            diaryViewModel.listItemClicked(healthData)
        }
        binding.rvHealthData.adapter = adapter

        binding.fab.setOnClickListener {
            diaryViewModel.fabClicked()
        }
        diaryViewModel.openDialogFragmentLiveData.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled().let { healthData ->
                println("OPEN dialog fragment")
                openDialogFragment(healthData)
            }
        })
    }

    @ObsoleteCoroutinesApi
    override fun onStart() {
        super.onStart()
        dataJob = launch {
            diaryViewModel.getViewState().consumeEach { renderData(it) }
        }
        errorJob = launch {
            diaryViewModel.getErrorChannel().consumeEach { renderError(it) }
        }
    }

    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    private fun renderError(error: Throwable) {
        error.message?.let { showError(it) }
    }

    protected fun showError(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    private fun renderData(data: List<HealthData>?) {
        data?.let { adapter.data = it } // notifyDataSetChanged will invoke automatically
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_diary_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> showLogoutDialog().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.logout_ok) { dialog, which ->
                logout()
            }
            .setNegativeButton(R.string.logout_cancel) { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                SplashActivity.start(requireContext())
                activity?.finish()
            }
    }

    private fun openDialogFragment(healthData: HealthData?) {
        val dialogFragment = EnterDataDialogFragment.newInstance(healthData)
        dialogFragment.show(childFragmentManager, DIALOG_FRAGMENT_TAG)
    }
}
