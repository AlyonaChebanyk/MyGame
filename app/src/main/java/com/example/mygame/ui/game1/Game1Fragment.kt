package com.example.mygame.ui.game1

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.viewbinding.library.fragment.viewBinding
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygame.R
import com.example.mygame.adapters.Orientation
import com.example.mygame.databinding.FragmentGame1Binding
import com.example.mygame.adapters.SlotAdapter
import com.example.mygame.adapters.SlotItem
import com.example.mygame.custom_ui.MyLinearLayoutManager
import com.example.mygame.extensions.getCurrentPosition
import com.example.mygame.ui.MainActivity
import com.example.mygame.ui.bonusgame.BonusGamePresenter
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get
import timber.log.Timber
import kotlin.random.Random
import com.example.mygame.adapters.OrientationListener


class Game1Fragment : MvpAppCompatFragment(), Game1View, OrientationListener {

    private lateinit var binding: FragmentGame1Binding

    @InjectPresenter
    lateinit var presenter: Game1Presenter

    @ProvidePresenter
    fun provideGame1Presenter() = get<Game1Presenter>()

    private lateinit var adapter1: SlotAdapter
    private lateinit var adapter2: SlotAdapter
    private lateinit var adapter3: SlotAdapter
    private lateinit var adapter4: SlotAdapter

    private lateinit var item1: SlotItem
    private lateinit var item2: SlotItem
    private lateinit var item3: SlotItem
    private lateinit var item4: SlotItem

    private var scrollCount: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        adapter1 = SlotAdapter(this, getGameItems(1))
        adapter2 = SlotAdapter(this, getGameItems(2))
        adapter3 = SlotAdapter(this, getGameItems(3))
        adapter4 = SlotAdapter(this, getGameItems(4))

        binding = FragmentGame1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            displayBalance(presenter.balance)
            displayBid(presenter.bid)
            displayWin(presenter.win)

            plusButton.setOnClickListener {
                if (presenter.bid != presenter.balance) {
                    presenter.playOnClickSound()
                    if (presenter.musicVolume == 0) {
                        (activity as MainActivity).vibrate(context)
                    }
                    presenter.addBid()
                    bidTextView.text = presenter.bid.toString()
                }
            }

            minusButton.setOnClickListener {
                if (presenter.bid != 0) {
                    presenter.playOnClickSound()
                    if (presenter.musicVolume == 0) {
                        (activity as MainActivity).vibrate(context)
                    }
                    presenter.minusBid()
                    bidTextView.text = presenter.bid.toString()
                }
            }

            playButton.setOnClickListener {
                (activity as MainActivity).vibrate(context)
                if (presenter.bid != 0) {
                    activity?.requestedOrientation = getCurrentScreenOrientation(requireContext())
                    playButton.isClickable = false
                    presenter.minusBidFromBalance()
                    balanceTextView.text = presenter.balance.toString()
                    scrollCount = 0
                    scrollSlot(slot1RecyclerView)
                    scrollSlot(slot2RecyclerView)
                    scrollSlot(slot3RecyclerView)
                    scrollSlot(slot4RecyclerView)
                }
            }
            backButton.setOnClickListener {
                presenter.playOnClickSound()
                if (presenter.musicVolume == 0) {
                    (activity as MainActivity).vibrate(context)
                }
                findNavController().navigate(R.id.action_game1Fragment_to_chooseGameFragment)
            }
        }
    }

    private fun scrollSlot(recyclerView: RecyclerView) {
        var randomPosition = (0..50).random()
        while (randomPosition == recyclerView.getCurrentPosition()
            || randomPosition == recyclerView.getCurrentPosition() + 1
            || randomPosition == recyclerView.getCurrentPosition() - 1
        ) {
            randomPosition = (0..50).random()
        }
        recyclerView.smoothScrollToPosition(randomPosition)
    }

    private fun onAllScrollComplete() {
        if (scrollCount == 4) {

            val currentGameItems = mutableListOf(item1, item2, item3, item4)

            presenter.calculateWin(currentGameItems)

            binding.playButton.isClickable = true

            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        }
    }

    private fun setAdapter(recyclerView: RecyclerView, adapter: SlotAdapter) {
        recyclerView.apply {
            layoutManager = MyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }
    }

    private fun setOnScrollListener(
        recyclerView: RecyclerView,
        adapter: SlotAdapter,
        slotIndex: Int
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    when (slotIndex) {
                        1 -> item1 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                        2 -> item2 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                        3 -> item3 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                        4 -> item4 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                    }
                    scrollCount += 1
                    onAllScrollComplete()
                }
            }
        })
    }

    private fun getGameItems(seed: Int): List<SlotItem> {
        val slotItems = mutableListOf<SlotItem>()
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img1, requireContext().theme
                ), "x16"
            )
        )
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img2, requireContext().theme
                ), "7"
            )
        )
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img3, requireContext().theme
                ), "bell"
            )
        )
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img4, requireContext().theme
                ), "cherry"
            )
        )
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img5, requireContext().theme
                ), "watermelon"
            )
        )
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img6, requireContext().theme
                ), "grape"
            )
        )
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img7, requireContext().theme
                ), "lemon"
            )
        )
        slotItems.add(
            SlotItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.game1_img8, requireContext().theme
                ), "star"
            )
        )
        return slotItems.shuffled(Random(seed))
    }

    private fun getCurrentScreenOrientation(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rotation = windowManager.defaultDisplay.rotation
        return when (rotation) {
            // Check rotation and return corresponding orientation constants
            Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    override fun getOrientation(): Orientation {
        val resources: Resources = requireContext().resources
        val configuration: Configuration = resources.configuration
        return if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            Orientation.PORTRAIT
        else
            Orientation.LANDSCAPE
    }

    override fun displayBalance(balance: Int) {
        binding.balanceTextView.text = balance.toString()
    }

    override fun setAdapters() {
        with(binding) {
            setAdapter(slot1RecyclerView, adapter1)
            setAdapter(slot2RecyclerView, adapter2)
            setAdapter(slot3RecyclerView, adapter3)
            setAdapter(slot4RecyclerView, adapter4)
        }
    }

    override fun setOnScrollListenerOnAdapters() {
        with(binding) {
            setOnScrollListener(slot1RecyclerView, adapter1, 1)
            setOnScrollListener(slot2RecyclerView, adapter2, 2)
            setOnScrollListener(slot3RecyclerView, adapter3, 3)
            setOnScrollListener(slot4RecyclerView, adapter4, 4)
        }
    }

    override fun disableOnTouchRecyclerView() {
        with(binding) {
            slot1RecyclerView.setOnTouchListener(OnTouchListener { v, event -> true })
            slot2RecyclerView.setOnTouchListener(OnTouchListener { v, event -> true })
            slot3RecyclerView.setOnTouchListener(OnTouchListener { v, event -> true })
            slot4RecyclerView.setOnTouchListener(OnTouchListener { v, event -> true })
        }
    }

    override fun displayBid(bid: Int) {
        binding.bidTextView.text = bid.toString()
    }

    override fun displayWin(win: Int) {
        binding.winTextView.text = win.toString()
    }
}