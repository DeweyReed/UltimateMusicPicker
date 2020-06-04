package xyz.aprildown.ultimateringtonepicker

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment

class RingtonePickerDialog : DialogFragment(), UltimateRingtonePicker.RingtonePickerListener {

    private var directListener: UltimateRingtonePicker.RingtonePickerListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : AppCompatDialog(requireContext()) {
            init {
                val title = arguments?.getCharSequence(EXTRA_TITLE)
                if (title.isNullOrBlank()) {
                    supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
                } else {
                    setTitle(title)
                }
            }

            override fun onBackPressed() {
                // World miracle: requireActivity().onBackPressedDispatcher doesn't work here.
                handleBack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.urp_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment =
                requireArguments().getParcelable<UltimateRingtonePicker.Settings>(EXTRA_SETTINGS)!!
                    .createFragment()
            childFragmentManager.beginTransaction()
                .add(R.id.urpFrameDialog, fragment, TAG_RINGTONE_PICKER)
                .setPrimaryNavigationFragment(fragment)
                .commit()
        }

        view.findViewById<View>(R.id.urpBtnDialogCancel).setOnClickListener {
            handleBack()
        }
        view.findViewById<View>(R.id.urpBtnDialogSelect).setOnClickListener {
            getRingtonePickerFragment().onSelectClick()
        }
    }

    override fun onPause() {
        super.onPause()
        if (directListener != null) {
            dismiss()
        }
    }

    override fun onRingtonePicked(ringtones: List<UltimateRingtonePicker.RingtoneEntry>) {
        (directListener ?: requireRingtonePickerListener()).onRingtonePicked(ringtones)
        dismiss()
    }

    private fun handleBack() {
        if (!getRingtonePickerFragment().onBackClick()) {
            dismiss()
        }
    }

    private fun getRingtonePickerFragment(): RingtonePickerFragment {
        return childFragmentManager.findFragmentByTag(TAG_RINGTONE_PICKER) as RingtonePickerFragment
    }

    companion object {
        private const val EXTRA_TITLE = "title"

        @JvmStatic
        fun createInstance(
            settings: UltimateRingtonePicker.Settings,
            dialogTitle: CharSequence?
        ): RingtonePickerDialog = RingtonePickerDialog().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_SETTINGS, settings)
                putCharSequence(EXTRA_TITLE, dialogTitle)
            }
        }

        /**
         * The dialog will be dismissed in onPause but give you the result directly in the [listener].
         */
        @JvmStatic
        fun createEphemeralInstance(
            settings: UltimateRingtonePicker.Settings,
            dialogTitle: CharSequence?,
            listener: UltimateRingtonePicker.RingtonePickerListener
        ): RingtonePickerDialog = RingtonePickerDialog().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_SETTINGS, settings)
                putCharSequence(EXTRA_TITLE, dialogTitle)
            }
            directListener = listener
        }
    }
}
