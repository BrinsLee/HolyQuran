package com.ihyas.soharamkarubar.utils.data

import com.ihyas.soharamkarubar.HiltApplication
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.models.PrayerGuideModel
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.getStringByLocale

object PrayerGuideProvider {

    fun getPrayerGuideMaleData(): ArrayList<PrayerGuideModel> {
        val context = HiltApplication.getInstance()!!
        return arrayListOf(
            PrayerGuideModel(
                getStringByLocale(context, R.string.how_to_pray),
                R.drawable.ic_bismillah,
                getStringByLocale(context, R.string.pray_guide_desc1),
                1
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.make_your_intention),
                R.drawable.prayer_guide_one, getStringByLocale(context, R.string.pray_guide_desc2),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.raise_your_hands_up),
                R.drawable.prayer_guide_two,
                getStringByLocale(context, R.string.pray_guide_desc3),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.place_your_right_hand_over),
                R.drawable.prayer_guide_three,
                getStringByLocale(context, R.string.pray_guide_desc4),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.say_allahu_akbar),
                R.drawable.prayer_guide_four,
                getStringByLocale(context, R.string.pray_guide_desc5),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.stand_back_up_raise),
                R.drawable.prayer_guide_five,
                getStringByLocale(context, R.string.pray_guide_desc6),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.say_allahu_akbar_and_prostrate),
                R.drawable.prayer_guide_six,
                getStringByLocale(context, R.string.pray_guide_desc7),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.rise_from_sajdah),
                R.drawable.prayer_guide_seven,
                getStringByLocale(context, R.string.pray_guide_desc8),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.say_allahu_akbar_and_stand_up),
                R.drawable.prayer_guide_eight,
                getStringByLocale(context, R.string.pray_guide_desc9),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.finish_the_prayer_with_tasleem),
                R.drawable.prayer_guide_nine,
                getStringByLocale(context, R.string.pray_guide_desc10),
                2
            ),

            )
    }

    fun getPrayerGuideFemaleData(): ArrayList<PrayerGuideModel> {
        val context = HiltApplication.getInstance()!!

        return arrayListOf(
            PrayerGuideModel(
                getStringByLocale(context, R.string.how_to_pray),
                R.drawable.ic_bismillah,
                getStringByLocale(context, R.string.pray_guide_desc1_female),
                1
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.make_your_intention_known_in_your_heart),
                R.drawable.prayer_guide_female_one,
                getStringByLocale(context, R.string.pray_guide_desc2_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.raise_your_hands_up_next_to_your_ears_and_shoulders),
                R.drawable.prayer_guide_female_two,
                getStringByLocale(context, R.string.pray_guide_desc3_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.place_your_right_hand_over_your_left_hand),
                R.drawable.prayer_guide_female_three,
                getStringByLocale(context, R.string.pray_guide_desc4_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.say_allahu_akbar_and_bend_down),
                R.drawable.prayer_guide_female_four,
                getStringByLocale(context, R.string.pray_guide_desc5_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.stand_back_up_raise_from_ruku),
                R.drawable.prayer_guide_female_five,
                getStringByLocale(context, R.string.pray_guide_desc6_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.say_allahu_akbar_),
                R.drawable.prayer_guide_female_six,
                getStringByLocale(context, R.string.pray_guide_desc7_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.rise_from_sajdah_and_sit_on_your_knees),
                R.drawable.prayer_guide_female_seven,
                getStringByLocale(context, R.string.pray_guide_desc8_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.say_allahu_akbar_a),
                R.drawable.prayer_guide_female_eight,
                getStringByLocale(context, R.string.pray_guide_desc9_female),
                2
            ),
            PrayerGuideModel(
                getStringByLocale(context, R.string.finish_the_prayer_with_t),
                R.drawable.prayer_guide_female_nine,
                getStringByLocale(context, R.string.pray_guide_desc10_female),
                2
            ),

            )

    }

}