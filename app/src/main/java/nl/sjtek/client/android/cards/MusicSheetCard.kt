package nl.sjtek.client.android.cards

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_music_sheet.view.*
import nl.sjtek.client.android.R
import nl.sjtek.client.android.activities.ActivityMusic
import nl.sjtek.client.android.api.API
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.parsers.ResponseHolder
import nl.sjtek.control.data.response.Music
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Bottom sheet for controlling the music. Can be dragged up.
 */
class MusicSheetCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    var sheetClickListener: SheetClickListener? = null

    init {
        View.inflate(context, R.layout.card_music_sheet, this)
        viewTitle.alpha = 0f
        buttonPrevious.setOnClickListener(this::onPreviousClick)
        buttonNext.setOnClickListener(this::onNextClick)
        buttonTopNext.setOnClickListener(this::onNextClick)
        buttonPlay.setOnClickListener(this::onToggleClick)
        buttonTopPlay.setOnClickListener(this::onToggleClick)
        viewTop.setOnClickListener(this::onTopClick)
        buttonStartPlaylist.setOnClickListener(this::onStartPlaylistClick)
        imageViewAlbumArt.setOnClickListener(this::onStartPlaylistClick)
    }

    fun onSlide(slideOffset: Float) {
        viewTitle.alpha = slideOffset
        viewInfo.alpha = 1 - slideOffset
        val topButtonVisibility = if (slideOffset == 1f) View.GONE else View.VISIBLE
        buttonTopPlay.visibility = topButtonVisibility
        buttonTopNext.visibility = topButtonVisibility
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        EventBus.getDefault().register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdate(responseCollection: ResponseHolder) {
        val music = responseCollection.music
        setMusicInfo(music)
    }

    fun onPreviousClick(view: View? = null) = API.action(context, Actions.music.previous())
    fun onNextClick(view: View? = null) = API.action(context, Actions.music.next())
    fun onToggleClick(view: View? = null) = API.action(context, Actions.music.toggle())
    fun onStartPlaylistClick(view: View? = null) {
        val pair = arrayOf(
                Pair.create<View, String>(imageViewAlbumArt, "albumArt"),
                Pair.create<View, String>(textViewArtist, "trackArtist"),
                Pair.create<View, String>(textViewTitle, "trackTitle"),
                Pair.create<View, String>(buttonPlay, "buttonPlay"),
                Pair.create<View, String>(buttonPrevious, "buttonPrevious"),
                Pair.create<View, String>(buttonNext, "buttonNext"))
        val activity = context as Activity
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pair)
        context.startActivity(Intent(context, ActivityMusic::class.java), options.toBundle())
    }

    fun onTopClick(view: View? = null) {
        sheetClickListener?.onSheetClick()
    }

    private fun setMusicInfo(music: Music) {
        val buttonImage: Int = if (music.state == Music.State.PLAYING) {
            R.drawable.ic_pause_black_24dp
        } else {
            R.drawable.ic_play_arrow_black_24dp
        }

        buttonPlay.setImageResource(buttonImage)
        buttonTopPlay.setImageResource(buttonImage)

        textViewTopTitle.text = music.name
        textViewTopArtist.text = if (TextUtils.isEmpty(music.artist)) "Music stopped" else music.artist

        textViewTitle.text = music.name
        textViewArtist.text = if (TextUtils.isEmpty(music.artist)) "Music stopped" else music.artist

        val image = when {
            music.albumArt.isNotBlank() -> music.albumArt
            music.artistArt.isNotBlank() -> music.artistArt
            else -> ""
        }

        if (TextUtils.isEmpty(image)) {
            imageViewAlbumArt.setImageDrawable(null)
            imageViewArtistArt.setImageDrawable(null)
        } else {
            Picasso.with(context)
                    .load(image)
                    .into(imageViewAlbumArt)
        }
    }

    interface SheetClickListener {
        fun onSheetClick()
    }
}
