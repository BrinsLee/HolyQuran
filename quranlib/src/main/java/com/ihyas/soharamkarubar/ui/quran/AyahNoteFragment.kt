package com.ihyas.soharamkarubar.ui.quran

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkaru.databinding.FragmentAyahNoteBinding
import com.ihyas.soharamkarubar.models.AyahNote
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AyahNoteFragment : Fragment() {
    lateinit var binding: FragmentAyahNoteBinding

    var surahNumber = 0
    var verseNumber = 0
    var surahUrduName: String? = null
    var surahEngName: String? = null
    var executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private var appDatabase: AppDatabase? = null
    var isExist = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAyahNoteBinding.inflate(
            layoutInflater
        )
        binding.include2.tvTitle.text = getString(R.string.text_notes)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {

            initUtils(it)
            args
            btnNoteClickListener(it)
        }
    }

    private fun btnNoteClickListener(context: Context) {
        binding.saveBtn.setSafeOnClickListener {
            executorService.execute {
                if (binding.etNote.text.isNullOrBlank()) {
                    activity?.runOnUiThread {
                        Toast.makeText(
                            activity,
                            getString(R.string.emptytNoteIsnotAllowed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    return@execute
                }
                val noteText = Objects.requireNonNull(binding.etNote.text).toString()

                if (isExist) {
                    //update
                    appDatabase?.ayahNotesDao()
                        ?.updateAyahNote(noteText, surahNumber, verseNumber)
                    activity?.runOnUiThread {
                        Toast.makeText(
                            context,
                            getString(R.string.note_updated_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                } else {
                    //save
                    appDatabase?.ayahNotesDao()?.saveAyahNote(
                        AyahNote(
                            surahEngName,
                            surahUrduName,
                            surahNumber,
                            verseNumber,
                            noteText
                        )
                    )
                    activity?.runOnUiThread {
                        Toast.makeText(
                            context,
                            getString(R.string.note_save_message),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                }

            }
        }
        binding.include2.backBtn.setSafeOnClickListener { findNavController().popBackStack() }
        binding.ivShare.setSafeOnClickListener {
            val noteText = binding.etNote.text.toString()
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, noteText)
            sendIntent.type = "text/plain"
            startActivity(Intent.createChooser(sendIntent, "Share Ayah Note"))
        }
        binding.ivDelete.setSafeOnClickListener {
            executorService.execute {
                appDatabase?.ayahNotesDao()?.deleteAyahNote(surahNumber, verseNumber)

                activity?.runOnUiThread {
                    Toast.makeText(
                        context,
                        getString(R.string.note_delete_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }

            }
        }
    }

    private fun initUtils(context: Context) {

        surahNumber = 0
        verseNumber = 0
        appDatabase = AppDatabase.getAppDatabase(context)
        isExist = false
    }

    private val args: Unit
        get() {
            arguments?.let { arg ->
                surahNumber = AyahNoteFragmentArgs.fromBundle(
                    arg
                ).surahNumber
                verseNumber = AyahNoteFragmentArgs.fromBundle(
                    arg
                ).verseNumber
                surahUrduName = AyahNoteFragmentArgs.fromBundle(
                    arg
                ).surahUrduName
                surahEngName = AyahNoteFragmentArgs.fromBundle(
                    arg
                ).surahEngName
                fetchNoteIfExists(surahNumber, verseNumber)
            }

        }

    private fun fetchNoteIfExists(surahNumber: Int, verseNumber: Int) {
        executorService.execute {
            val ayahNote = appDatabase?.ayahNotesDao()?.isNoteExists(surahNumber, verseNumber)
            if (activity != null) activity?.runOnUiThread { setNoteText(ayahNote) }
        }
    }

    private fun setNoteText(ayahNote: AyahNote?) {
        if (ayahNote != null) {
            isExist = true
            binding.etNote.setText(ayahNote.noteText)
            binding.ivDelete.visible()
            binding.ivShare.visible()
        }
    }


}