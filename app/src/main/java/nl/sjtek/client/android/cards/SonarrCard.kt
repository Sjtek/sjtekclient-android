package nl.sjtek.client.android.cards

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import nl.sjtek.client.android.R
import nl.sjtek.control.data.parsers.ResponseHolder

/**
 * Card for displaying Sonarr information.
 */
class SonarrCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCard(context, attrs, defStyleAttr) {

    override fun onShouldInflate(context: Context) {
        inflate(context, R.layout.card_sonarr, this)
    }

    @SuppressLint("DefaultLocale")
    override fun onUpdate(update: ResponseHolder) {
//        if (update.sonarr == null || update.sonarr.diskUsage.isEmpty()) return
//        val episodes = update.sonarr.upcoming
//        val disks = update.sonarr.diskUsage
//
//        val available = episodes.isNotEmpty()
//        val visibility = if (available) View.VISIBLE else View.GONE
//        textViewUpcoming.visibility = visibility
//        textViewShowTitle.visibility = visibility
//        textViewEpisodeName.visibility = visibility
//
//        if (available) {
//            val episode = episodes[0]
//            textViewShowTitle.text = episode.seriesTitle
//            textViewEpisodeName.text = String.format(EPISODE_NAME_TEMPLATE, episode.seasonInt, episode.episodeInt, episode.episodeName)
//        }
//
//        textViewDiskRootPercentage.text = getDiskPercentage(disks["/"])
//        textViewDiskDataPercentage.text = getDiskPercentage(disks["/tv"])
    }

//    @SuppressLint("DefaultLocale")
//    private fun getDiskPercentage(disk: SonarrResponse.Disk?): String {
//        if (disk == null) return "n/a"
//        return "${((disk.total - disk.free) / disk.total * 100).toInt()}%"
//    }

    companion object {

        private val EPISODE_NAME_TEMPLATE = "S%02dE%02d %s"
    }
}
